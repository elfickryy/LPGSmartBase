package id.lpgsmartbase.data.repository
import id.lpgsmartbase.data.local.LpgDatabase
import id.lpgsmartbase.domain.service.ReportCalculator
import kotlinx.coroutines.flow.combine
class ReportRepository(private val db: LpgDatabase) { fun dashboard() = combine(db.transaksiDao().observeAll(), db.kasDao().observeAll(), db.stokDao().observeAll()) { transaksi, kas, stok -> Triple(ReportCalculator.penjualan(transaksi), ReportCalculator.kas(kas), stok) } }
