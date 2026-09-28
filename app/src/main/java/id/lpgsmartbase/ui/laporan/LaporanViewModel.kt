package id.lpgsmartbase.ui.laporan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.lpgsmartbase.data.repository.ReportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.util.Calendar
import id.lpgsmartbase.domain.service.ReportCalculator

enum class PeriodeLaporan { HARI_INI, KEMARIN, MINGGU_INI, BULAN_INI, BULAN_SEBELUMNYA, CUSTOM }

class LaporanViewModel(private val repository: ReportRepository) : ViewModel() {
    val periode = MutableStateFlow(PeriodeLaporan.HARI_INI)
    val state = periode.flatMapLatest { filter ->
        val kalender = Calendar.getInstance(); kalender.set(Calendar.HOUR_OF_DAY, 0); kalender.set(Calendar.MINUTE, 0); kalender.set(Calendar.SECOND, 0); kalender.set(Calendar.MILLISECOND, 0)
        val selesai: Long; val mulai: Long
        when (filter) {
            PeriodeLaporan.KEMARIN -> { kalender.add(Calendar.DAY_OF_YEAR, -1); mulai = kalender.timeInMillis; selesai = mulai + 86_399_999L }
            PeriodeLaporan.MINGGU_INI -> { kalender.set(Calendar.DAY_OF_WEEK, kalender.firstDayOfWeek); mulai = kalender.timeInMillis; selesai = System.currentTimeMillis() }
            PeriodeLaporan.BULAN_INI -> { kalender.set(Calendar.DAY_OF_MONTH, 1); mulai = kalender.timeInMillis; selesai = System.currentTimeMillis() }
            PeriodeLaporan.BULAN_SEBELUMNYA -> { kalender.set(Calendar.DAY_OF_MONTH, 1); kalender.add(Calendar.MONTH, -1); mulai = kalender.timeInMillis; kalender.add(Calendar.MONTH, 1); selesai = kalender.timeInMillis - 1 }
            else -> { mulai = kalender.timeInMillis; selesai = if (filter == PeriodeLaporan.HARI_INI) System.currentTimeMillis() else Long.MAX_VALUE }
        }
        repository.laporan(mulai, selesai)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    fun pilih(filter: PeriodeLaporan) { periode.value = filter }
}
class LaporanViewModelFactory(private val repository: ReportRepository) : ViewModelProvider.Factory { @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = LaporanViewModel(repository) as T }
