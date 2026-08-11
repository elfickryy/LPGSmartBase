package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.JatahEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JatahDao {
    @Query("SELECT * FROM jatah WHERE periode = :periode") fun observePeriode(periode: String): Flow<List<JatahEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpan(jatah: JatahEntity)
}
