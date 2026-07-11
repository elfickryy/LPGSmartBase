package id.lpgsmartbase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_movements")
data class StockMovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cylinderType: String,
    val quantity: Int,
    val movementType: String,
    val createdAt: Long = System.currentTimeMillis(),
    val note: String = ""
)
