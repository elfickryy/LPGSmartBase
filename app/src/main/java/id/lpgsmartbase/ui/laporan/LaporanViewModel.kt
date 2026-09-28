package id.lpgsmartbase.ui.laporan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.lpgsmartbase.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

enum class PeriodeLaporan { HARI_INI, KEMARIN, MINGGU_INI, BULAN_INI, BULAN_SEBELUMNYA, CUSTOM }
enum class KategoriLaporan { RINGKASAN, PENJUALAN, KAS, STOK, PELANGGAN, HUTANG, LABA }
data class FilterLaporan(val periode: PeriodeLaporan = PeriodeLaporan.HARI_INI, val mulaiCustom: Long? = null, val selesaiCustom: Long? = null)
class LaporanViewModel(private val repository: ReportRepository) : ViewModel() {
    val filter = MutableStateFlow(FilterLaporan())
    val kategori = MutableStateFlow(KategoriLaporan.RINGKASAN)
    val state = filter.flatMapLatest { repository.laporanDetail(it.mulai(), it.selesai()) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    fun pilih(periode: PeriodeLaporan) { filter.value = FilterLaporan(periode) }
    fun custom(mulai: Long, selesai: Long) { filter.value = FilterLaporan(PeriodeLaporan.CUSTOM, mulai, selesai) }
    private fun FilterLaporan.mulai(): Long { if (periode == PeriodeLaporan.CUSTOM) return mulaiCustom ?: 0; val c = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY,0); set(Calendar.MINUTE,0); set(Calendar.SECOND,0); set(Calendar.MILLISECOND,0) }; return when(periode) { PeriodeLaporan.KEMARIN -> { c.add(Calendar.DAY_OF_YEAR,-1); c.timeInMillis }; PeriodeLaporan.MINGGU_INI -> { c.set(Calendar.DAY_OF_WEEK,c.firstDayOfWeek); c.timeInMillis }; PeriodeLaporan.BULAN_INI -> { c.set(Calendar.DAY_OF_MONTH,1); c.timeInMillis }; PeriodeLaporan.BULAN_SEBELUMNYA -> { c.set(Calendar.DAY_OF_MONTH,1); c.add(Calendar.MONTH,-1); c.timeInMillis }; else -> c.timeInMillis } }
    private fun FilterLaporan.selesai(): Long { if (periode == PeriodeLaporan.CUSTOM) return selesaiCustom ?: Long.MAX_VALUE; if (periode == PeriodeLaporan.KEMARIN) return mulai() + 86_399_999L; return System.currentTimeMillis() }
}
class LaporanViewModelFactory(private val repository: ReportRepository) : ViewModelProvider.Factory { @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = LaporanViewModel(repository) as T }
