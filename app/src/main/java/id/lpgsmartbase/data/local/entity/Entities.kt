package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pelanggan") data class PelangganEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val kode: String, val nama: String, val alamat: String, val telepon: String, val aktif: Boolean = true)
@Entity(tableName = "stok") data class StokEntity(@PrimaryKey val jenis: String, val isi: Int = 0, val kosong: Int = 0, val hargaModal: Long = 0, val hargaJual: Long = 0, val diperbaruiPada: Long = System.currentTimeMillis())
@Entity(tableName = "mutasi_stok") data class MutasiStokEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val jenis: String, val perubahanIsi: Int, val perubahanKosong: Int, val tipe: String, val referensi: String, val waktu: Long = System.currentTimeMillis(), val catatan: String = "")
@Entity(tableName = "transaksi") data class TransaksiEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val nomorNota: String, val pelangganId: Long?, val jumlah: Int, val hargaJual: Long, val hargaModal: Long, val total: Long, val dibayar: Long, val statusPembayaran: String, val jenisTransaksi: String = "PENJUALAN", val sumberTabung: String = "STOK", val waktu: Long = System.currentTimeMillis(), val catatan: String = "")
@Entity(tableName = "jatah") data class JatahEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val pelangganId: Long, val periode: String, val jumlah: Int, val realisasi: Int = 0)
@Entity(tableName = "histori_jatah") data class HistoriJatahEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val pelangganId: Long, val periode: String, val tipe: String, val jumlah: Int, val referensi: String, val waktu: Long = System.currentTimeMillis())
@Entity(tableName = "titip_tabung") data class TitipTabungEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val pelangganId: Long, val jumlahTitipan: Int, val jumlahKosongMilikPelanggan: Int, val jumlahIsiDiambil: Int, val waktu: Long = System.currentTimeMillis(), val catatan: String = "")
@Entity(tableName = "saldo_titipan") data class SaldoTitipanEntity(@PrimaryKey val pelangganId: Long, val saldo: Int = 0, val diperbaruiPada: Long = System.currentTimeMillis())
@Entity(tableName = "kas") data class KasEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val tipe: String, val nominal: Long, val keterangan: String, val waktu: Long = System.currentTimeMillis())
@Entity(tableName = "setting") data class SettingEntity(@PrimaryKey val kunci: String, val nilai: String)
