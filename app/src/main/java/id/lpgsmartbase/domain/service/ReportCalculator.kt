package id.lpgsmartbase.domain.service
import id.lpgsmartbase.data.local.entity.KasEntity
import id.lpgsmartbase.data.local.entity.TransaksiEntity
import id.lpgsmartbase.domain.model.RekapKas
import id.lpgsmartbase.domain.model.RekapPenjualan
object ReportCalculator { fun penjualan(data: List<TransaksiEntity>) = RekapPenjualan(data.size, data.sumOf { it.jumlah }, data.sumOf { it.total }, data.sumOf { it.dibayar }, data.sumOf { it.total - it.dibayar }, data.sumOf { it.hargaModal * it.jumlah }); fun kas(data: List<KasEntity>) = RekapKas(data.filter { it.tipe == "MASUK" }.sumOf { it.nominal }, data.filter { it.tipe == "KELUAR" }.sumOf { it.nominal }) }
