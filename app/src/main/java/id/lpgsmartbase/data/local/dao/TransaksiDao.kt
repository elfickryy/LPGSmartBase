package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.TransaksiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao {
    @Query("SELECT * FROM transaksi ORDER BY waktu DESC") fun observeRiwayat(): Flow<List<TransaksiEntity>>
    @Insert suspend fun insert(transaksi: TransaksiEntity): Long
}
