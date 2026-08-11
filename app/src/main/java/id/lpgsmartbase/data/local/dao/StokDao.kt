package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.StokEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StokDao {
    @Query("SELECT * FROM stok ORDER BY jenisTabung") fun observeSemua(): Flow<List<StokEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpan(stok: StokEntity)
}
