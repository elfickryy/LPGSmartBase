package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kas")
data class KasEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tipe: String,
    val nominal: Long,
    val keterangan: String,
    val waktu: Long = System.currentTimeMillis(),
    val referensi: String? = null
)
