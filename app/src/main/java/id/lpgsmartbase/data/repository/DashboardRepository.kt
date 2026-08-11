package id.lpgsmartbase.data.repository

import id.lpgsmartbase.data.local.LpgDatabase
import id.lpgsmartbase.domain.model.RingkasanDashboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DashboardRepository(database: LpgDatabase) {
    private val stok = database.stokDao().observeSemua()
    private val transaksi = database.transaksiDao().observeRiwayat()
    private val kas = database.kasDao().observeRiwayat()
    fun observeRingkasan(): Flow<RingkasanDashboard> = combine(stok, transaksi, kas) { daftarStok, daftarTransaksi, daftarKas ->
        val awalHari = System.currentTimeMillis() - (System.currentTimeMillis() % 86_400_000L)
        RingkasanDashboard(daftarStok.sumOf { it.stokIsi }, daftarStok.sumOf { it.stokKosong }, daftarTransaksi.count { it.waktu >= awalHari }, daftarKas.filter { it.waktu >= awalHari }.sumOf { if (it.tipe == "MASUK") it.nominal else -it.nominal })
    }
}
