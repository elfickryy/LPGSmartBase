package id.lpgsmartbase.domain.service
import id.lpgsmartbase.data.local.entity.KasEntity
import id.lpgsmartbase.data.local.entity.TransaksiEntity
import org.junit.Assert.assertEquals
import org.junit.Test
class ReportCalculatorTest { @Test fun `rekap omzet pembayaran hutang dan laba`() { val r = ReportCalculator.penjualan(listOf(TransaksiEntity(nomorNota="1", pelangganId=null, jumlah=2, hargaJual=20, hargaModal=15, total=40, dibayar=10, statusPembayaran="SEBAGIAN"))); assertEquals(40,r.omzet); assertEquals(10,r.dibayar); assertEquals(30,r.hutang); assertEquals(10,r.laba) }; @Test fun `saldo kas`() { assertEquals(70, ReportCalculator.kas(listOf(KasEntity(tipe="MASUK", nominal=100,keterangan="x"),KasEntity(tipe="KELUAR", nominal=30,keterangan="x"))).saldo) } }
