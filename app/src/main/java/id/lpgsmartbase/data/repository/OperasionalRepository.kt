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
        if (input.dibayar > 0) database.kasDao().insert(KasEntity(tipe = "MASUK", nominal = input.dibayar, keterangan = "Pembayaran ${input.nomorNota}"))
        database.stokDao().save(stok.copy(isi = stok.isi - input.jumlah, kosong = stok.kosong + input.jumlah, diperbaruiPada = System.currentTimeMillis()))
        database.operasionalDao().mutasi(MutasiStokEntity(jenis = input.jenisTabung, perubahanIsi = -input.jumlah, perubahanKosong = input.jumlah, tipe = "PENJUALAN", referensi = input.nomorNota, catatan = input.catatan))
        if (input.pelangganId != null && input.periodeJatah != null) {
            val jatah = database.operasionalDao().jatah(input.pelangganId, input.periodeJatah)
            requireNotNull(jatah) { "Jatah pelanggan belum ditetapkan" }
            OperasionalRules.validasiAlokasi(jatah.jumlah - jatah.realisasi, Int.MAX_VALUE, input.jumlah, false)
            database.operasionalDao().simpanJatah(jatah.copy(realisasi = jatah.realisasi + input.jumlah)); database.operasionalDao().historiJatah(HistoriJatahEntity(pelangganId = input.pelangganId, periode = input.periodeJatah, tipe = "PENJUALAN", jumlah = input.jumlah, referensi = input.nomorNota))
        }
        HasilPenjualan(transaksiId, status)
    }
    suspend fun catatTitipan(pelangganId: Long, periode: String, titip: Int, kosongMilikPelanggan: Int, isiDiambil: Int, catatan: String) = database.withTransaction {
        require(titip >= 0 && kosongMilikPelanggan >= 0 && isiDiambil >= 0) { "Jumlah tabung tidak valid" }
        val saldo = database.operasionalDao().saldoTitipan(pelangganId)?.saldo ?: 0
        database.operasionalDao().simpanSaldoTitipan(SaldoTitipanEntity(pelangganId, saldo + titip))
        database.operasionalDao().titip(TitipTabungEntity(pelangganId = pelangganId, jumlahTitipan = titip, jumlahKosongMilikPelanggan = kosongMilikPelanggan, jumlahIsiDiambil = isiDiambil, catatan = catatan))
        database.operasionalDao().historiJatah(HistoriJatahEntity(pelangganId = pelangganId, periode = periode, tipe = "TITIP", jumlah = titip, referensi = "TITIPAN"))
    }
    suspend fun ambilDariTitipan(input: InputPenjualan): HasilPenjualan = database.withTransaction {
        requireNotNull(input.pelangganId) { "Pelanggan wajib untuk ambil titipan" }
        requireNotNull(input.periodeJatah) { "Periode jatah wajib diisi" }
        val stok = requireNotNull(database.stokDao().get(input.jenisTabung)) { "Stok tabung tidak ditemukan" }
        val jatah = requireNotNull(database.operasionalDao().jatah(input.pelangganId, input.periodeJatah)) { "Jatah pelanggan belum ditetapkan" }
        val titipan = database.operasionalDao().saldoTitipan(input.pelangganId)?.saldo ?: 0
        OperasionalRules.validasiPenjualan(stok.isi, input.jumlah, input.hargaJual, input.hargaModal, input.dibayar)
        OperasionalRules.validasiAlokasi(jatah.jumlah - jatah.realisasi, titipan, input.jumlah, true)
        val total = input.jumlah * input.hargaJual; val status = OperasionalRules.statusPembayaran(total, input.dibayar)
        val id = database.transaksiDao().insert(TransaksiEntity(nomorNota=input.nomorNota, pelangganId=input.pelangganId, jumlah=input.jumlah, hargaJual=input.hargaJual, hargaModal=input.hargaModal, total=total, dibayar=input.dibayar, statusPembayaran=status.name, jenisTransaksi="AMBIL_TITIPAN", sumberTabung="TITIPAN", catatan=input.catatan))
        if (input.dibayar > 0) database.kasDao().insert(KasEntity(tipe = "MASUK", nominal = input.dibayar, keterangan = "Pembayaran ${input.nomorNota}"))
        database.stokDao().save(stok.copy(isi=stok.isi-input.jumlah, kosong=stok.kosong+input.jumlah, diperbaruiPada=System.currentTimeMillis()))
        database.operasionalDao().simpanJatah(jatah.copy(realisasi=jatah.realisasi+input.jumlah))
        database.operasionalDao().simpanSaldoTitipan(SaldoTitipanEntity(input.pelangganId, titipan-input.jumlah))
        database.operasionalDao().mutasi(MutasiStokEntity(jenis=input.jenisTabung, perubahanIsi=-input.jumlah, perubahanKosong=input.jumlah, tipe="AMBIL_TITIPAN", referensi=input.nomorNota, catatan=input.catatan))
        database.operasionalDao().historiJatah(HistoriJatahEntity(pelangganId=input.pelangganId, periode=input.periodeJatah, tipe="AMBIL_TITIPAN", jumlah=input.jumlah, referensi=input.nomorNota))
        HasilPenjualan(id, status)
    }
}
