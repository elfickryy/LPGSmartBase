package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.KasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KasDao {
    @Query("SELECT * FROM kas ORDER BY waktu DESC") fun observeRiwayat(): Flow<List<KasEntity>>
    @Insert suspend fun insert(kas: KasEntity): Long
}
