package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.StockMovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockMovementDao {
    @Query("SELECT * FROM stock_movements ORDER BY createdAt DESC") fun observeAll(): Flow<List<StockMovementEntity>>
    @Insert suspend fun insert(movement: StockMovementEntity): Long
}
