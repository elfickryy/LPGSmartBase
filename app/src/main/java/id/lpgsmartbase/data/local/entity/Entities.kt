package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pelanggan") data class PelangganEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val kode: String, val nama: String, val alamat: String, val telepon: String, val aktif: Boolean = true)
@Entity(tableName = "stok") data class StokEntity(@PrimaryKey val jenis: String, val isi: Int = 0, val kosong: Int = 0, val diperbaruiPada: Long = System.currentTimeMillis())
@Entity(tableName = "transaksi") data class TransaksiEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val nomorNota: String, val pelangganId: Long, val jumlah: Int, val total: Long, val dibayar: Long, val waktu: Long = System.currentTimeMillis())
@Entity(tableName = "jatah") data class JatahEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val pelangganId: Long, val periode: String, val jumlah: Int, val realisasi: Int = 0)
@Entity(tableName = "kas") data class KasEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val tipe: String, val nominal: Long, val keterangan: String, val waktu: Long = System.currentTimeMillis())
@Entity(tableName = "setting") data class SettingEntity(@PrimaryKey val kunci: String, val nilai: String)
