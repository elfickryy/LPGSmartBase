package id.lpgsmartbase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.lpgsmartbase.data.local.entity.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Query("SELECT * FROM setting WHERE kunci = :kunci") fun observe(kunci: String): Flow<SettingEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpan(setting: SettingEntity)
}
