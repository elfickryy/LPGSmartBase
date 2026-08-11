package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "jatah", foreignKeys = [ForeignKey(entity = PelangganEntity::class, parentColumns = ["id"], childColumns = ["pelangganId"], onDelete = ForeignKey.CASCADE)], indices = [Index(value = ["pelangganId", "periode"], unique = true)])
data class JatahEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pelangganId: Long,
    val periode: String,
    val jumlahJatah: Int,
    val jumlahTerealisasi: Int = 0
)
