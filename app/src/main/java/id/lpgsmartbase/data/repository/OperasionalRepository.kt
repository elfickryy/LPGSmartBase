package id.lpgsmartbase.data.repository

import androidx.room.withTransaction
import id.lpgsmartbase.data.local.LpgDatabase
import id.lpgsmartbase.data.local.entity.*
import id.lpgsmartbase.domain.model.HasilPenjualan
import id.lpgsmartbase.domain.model.InputPenjualan
import id.lpgsmartbase.domain.service.OperasionalRules

class OperasionalRepository(private val database: LpgDatabase) {
    suspend fun prosesPenjualan(input: InputPenjualan): HasilPenjualan = database.withTransaction {
        val stok = requireNotNull(database.stokDao().get(input.jenisTabung)) { "Stok tabung tidak ditemukan" }
        OperasionalRules.validasiPenjualan(stok.isi, input.jumlah, input.hargaJual, input.hargaModal, input.dibayar)
        val total = input.jumlah * input.hargaJual
        val status = OperasionalRules.statusPembayaran(total, input.dibayar)
        val transaksiId = database.transaksiDao().insert(TransaksiEntity(nomorNota = input.nomorNota, pelangganId = input.pelangganId, jumlah = input.jumlah, hargaJual = input.hargaJual, hargaModal = input.hargaModal, total = total, dibayar = input.dibayar, statusPembayaran = status.name, catatan = input.catatan))
        database.stokDao().save(stok.copy(isi = stok.isi - input.jumlah, kosong = stok.kosong + input.jumlah, diperbaruiPada = System.currentTimeMillis()))
        database.operasionalDao().mutasi(MutasiStokEntity(jenis = input.jenisTabung, perubahanIsi = -input.jumlah, perubahanKosong = input.jumlah, tipe = "PENJUALAN", referensi = input.nomorNota, catatan = input.catatan))
        if (input.pelangganId != null && input.periodeJatah != null) {
            val jatah = database.operasionalDao().jatah(input.pelangganId, input.periodeJatah)
            if (jatah != null) { database.operasionalDao().simpanJatah(jatah.copy(realisasi = jatah.realisasi + input.jumlah)); database.operasionalDao().historiJatah(HistoriJatahEntity(pelangganId = input.pelangganId, periode = input.periodeJatah, tipe = "PENGAMBILAN", jumlah = input.jumlah, referensi = input.nomorNota)) }
        }
        HasilPenjualan(transaksiId, status)
    }
    suspend fun catatTitipan(pelangganId: Long, periode: String, titip: Int, kosongMilikPelanggan: Int, isiDiambil: Int, catatan: String) = database.withTransaction {
        require(titip >= 0 && kosongMilikPelanggan >= 0 && isiDiambil >= 0) { "Jumlah tabung tidak valid" }
        database.operasionalDao().titip(TitipTabungEntity(pelangganId = pelangganId, jumlahTitipan = titip, jumlahKosongMilikPelanggan = kosongMilikPelanggan, jumlahIsiDiambil = isiDiambil, catatan = catatan))
        database.operasionalDao().historiJatah(HistoriJatahEntity(pelangganId = pelangganId, periode = periode, tipe = "TITIP", jumlah = titip, referensi = "TITIPAN"))
    }
}
