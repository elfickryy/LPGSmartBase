package id.lpgsmartbase.data.repository
import id.lpgsmartbase.data.local.dao.SettingDao
import id.lpgsmartbase.data.local.entity.SettingEntity
import kotlinx.coroutines.flow.Flow
class SettingsRepository(private val dao: SettingDao) { fun observe(kunci:String): Flow<SettingEntity?> = dao.observe(kunci); suspend fun save(kunci:String,nilai:String)=dao.save(SettingEntity(kunci,nilai)) }
