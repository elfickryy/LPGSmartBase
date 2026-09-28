package id.lpgsmartbase.data.repository

import id.lpgsmartbase.data.local.dao.SettingDao
import id.lpgsmartbase.data.local.entity.SettingEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsRepositoryTest {
    @Test fun `printer settings tersimpan dan dapat dibaca kembali`() = runBlocking {
        val dao = FakeSettingDao()
        val repository = SettingsRepository(dao)
        repository.savePrinterSettings(PrinterSettings("AA:BB", "80", true))
        assertEquals("AA:BB", repository.observe("printer_default").first()?.nilai)
        assertEquals("80", repository.observe("printer_paper_width").first()?.nilai)
        assertEquals("true", repository.observe("printer_auto_print").first()?.nilai)
    }
    private class FakeSettingDao : SettingDao {
        private val values = mutableMapOf<String, MutableStateFlow<SettingEntity?>>()
        override fun observe(kunci: String): Flow<SettingEntity?> = values.getOrPut(kunci) { MutableStateFlow(null) }
        override suspend fun save(data: SettingEntity) { values.getOrPut(data.kunci) { MutableStateFlow(null) }.value = data }
    }
}
