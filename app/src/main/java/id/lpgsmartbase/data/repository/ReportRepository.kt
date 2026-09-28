package id.lpgsmartbase.data.repository
import id.lpgsmartbase.data.local.LpgDatabase
import id.lpgsmartbase.domain.service.ReportCalculator
import kotlinx.coroutines.flow.combine
class ReportRepository(private val db: LpgDatabase) {
    fun laporan(mulai: Long, selesai: Long) = combine(db.transaksiDao().observeRentang(mulai, selesai), db.kasDao().observeAll(), db.stokDao().observeAll()) { transaksi, kas, stok -> Triple(ReportCalculator.penjualan(transaksi), ReportCalculator.kas(kas.filter { it.waktu in mulai..selesai }), stok) }
    fun dashboard(hariMulai: Long, hariSelesai: Long) = laporan(hariMulai, hariSelesai)
}
