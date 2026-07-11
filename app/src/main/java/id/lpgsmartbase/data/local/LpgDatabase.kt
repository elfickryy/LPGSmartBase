package id.lpgsmartbase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.lpgsmartbase.data.local.dao.CustomerDao
import id.lpgsmartbase.data.local.dao.StockMovementDao
import id.lpgsmartbase.data.local.entity.CustomerEntity
import id.lpgsmartbase.data.local.entity.StockMovementEntity

@Database(entities = [CustomerEntity::class, StockMovementEntity::class], version = 1, exportSchema = true)
abstract class LpgDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun stockMovementDao(): StockMovementDao
}
