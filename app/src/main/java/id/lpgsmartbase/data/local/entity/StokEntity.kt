package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stok")
data class StokEntity(
    @PrimaryKey val jenisTabung: String,
    val stokIsi: Int = 0,
    val stokKosong: Int = 0,
    val diperbaruiPada: Long = System.currentTimeMillis()
)
