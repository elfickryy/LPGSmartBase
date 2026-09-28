package id.lpgsmartbase.data.repository
import id.lpgsmartbase.data.local.LpgDatabase
import id.lpgsmartbase.domain.service.ReportCalculator
import kotlinx.coroutines.flow.combine
import id.lpgsmartbase.data.local.entity.PelangganEntity
import id.lpgsmartbase.data.local.entity.StokEntity
import id.lpgsmartbase.domain.model.RekapKas
import id.lpgsmartbase.domain.model.RekapPenjualan
data class ReportData(val penjualan: RekapPenjualan, val kas: RekapKas, val stok: List<StokEntity>, val pelanggan: List<PelangganEntity>)
class ReportRepository(private val db: LpgDatabase) {
    fun laporan(mulai: Long, selesai: Long) = combine(db.transaksiDao().observeRentang(mulai, selesai), db.kasDao().observeAll(), db.stokDao().observeAll()) { transaksi, kas, stok -> Triple(ReportCalculator.penjualan(transaksi), ReportCalculator.kas(kas.filter { it.waktu in mulai..selesai }), stok) }
    fun dashboard(hariMulai: Long, hariSelesai: Long) = laporan(hariMulai, hariSelesai)
    fun laporanDetail(mulai: Long, selesai: Long) = combine(db.transaksiDao().observeRentang(mulai, selesai), db.kasDao().observeAll(), db.stokDao().observeAll(), db.pelangganDao().observeAll()) { transaksi, kas, stok, pelanggan -> ReportData(ReportCalculator.penjualan(transaksi), ReportCalculator.kas(kas.filter { it.waktu in mulai..selesai }), stok, pelanggan) }
}
