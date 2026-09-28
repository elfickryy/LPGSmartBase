package id.lpgsmartbase.data.repository
import id.lpgsmartbase.data.local.dao.SettingDao
import id.lpgsmartbase.data.local.entity.SettingEntity
import kotlinx.coroutines.flow.Flow
data class PrinterSettings(val defaultAddress: String = "", val paperWidth: String = "58", val autoPrint: Boolean = false)
class SettingsRepository(private val dao: SettingDao) {
    fun observe(kunci:String): Flow<SettingEntity?> = dao.observe(kunci)
    suspend fun save(kunci:String,nilai:String)=dao.save(SettingEntity(kunci,nilai))
    suspend fun savePrinterSettings(settings: PrinterSettings) {
        save("printer_default", settings.defaultAddress)
        save("printer_paper_width", if (settings.paperWidth == "80") "80" else "58")
        save("printer_auto_print", settings.autoPrint.toString())
    }
}
