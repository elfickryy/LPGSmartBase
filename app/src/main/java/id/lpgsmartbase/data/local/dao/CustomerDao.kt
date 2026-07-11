package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers WHERE active = 1 ORDER BY name") fun observeActive(): Flow<List<CustomerEntity>>
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(customer: CustomerEntity): Long
}
