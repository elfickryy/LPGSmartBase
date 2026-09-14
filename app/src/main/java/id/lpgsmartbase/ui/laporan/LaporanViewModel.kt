package id.lpgsmartbase.ui.laporan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.lpgsmartbase.data.repository.ReportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import id.lpgsmartbase.domain.service.ReportCalculator

enum class PeriodeLaporan { HARI_INI, KEMARIN, MINGGU_INI, BULAN_INI, BULAN_SEBELUMNYA, CUSTOM }

class LaporanViewModel(private val repository: ReportRepository) : ViewModel() { val periode = MutableStateFlow(PeriodeLaporan.HARI_INI); val state = combine(repository.dashboard(), periode) { report, filter -> report to filter }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null); fun pilih(filter: PeriodeLaporan) { periode.value = filter } }
class LaporanViewModelFactory(private val repository: ReportRepository) : ViewModelProvider.Factory { @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = LaporanViewModel(repository) as T }
