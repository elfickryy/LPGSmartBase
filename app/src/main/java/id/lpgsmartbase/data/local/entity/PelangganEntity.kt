package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "pelanggan", indices = [Index(value = ["kode"], unique = true)])
data class PelangganEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kode: String,
    val nama: String,
    val alamat: String,
    val telepon: String,
    val aktif: Boolean = true,
    val dibuatPada: Long = System.currentTimeMillis()
)
