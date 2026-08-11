package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.lpgsmartbase.data.local.entity.PelangganEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PelangganDao {
    @Query("SELECT * FROM pelanggan WHERE aktif = 1 ORDER BY nama") fun observeAktif(): Flow<List<PelangganEntity>>
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(pelanggan: PelangganEntity): Long
    @Update suspend fun update(pelanggan: PelangganEntity)
    @Query("UPDATE pelanggan SET aktif = 0 WHERE id = :id") suspend fun nonaktifkan(id: Long)
}
