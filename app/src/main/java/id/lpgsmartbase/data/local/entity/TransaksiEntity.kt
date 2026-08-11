package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi", foreignKeys = [ForeignKey(entity = PelangganEntity::class, parentColumns = ["id"], childColumns = ["pelangganId"], onDelete = ForeignKey.RESTRICT)], indices = [Index("pelangganId"), Index(value = ["nomorNota"], unique = true)])
data class TransaksiEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nomorNota: String,
    val pelangganId: Long,
    val jenisTabung: String,
    val jumlah: Int,
    val hargaSatuan: Long,
    val total: Long,
    val dibayar: Long,
    val waktu: Long = System.currentTimeMillis(),
    val keterangan: String = ""
)
