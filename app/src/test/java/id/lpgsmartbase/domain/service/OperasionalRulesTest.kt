package id.lpgsmartbase.domain.service

import id.lpgsmartbase.domain.model.StatusPembayaran
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class OperasionalRulesTest {
    @Test fun `jatah tiga titip dua ambil dua sisa satu`() = assertEquals(1, OperasionalRules.sisaJatah(3, 2))
    @Test fun `jatah habis sisa nol`() = assertEquals(0, OperasionalRules.sisaJatah(3, 3))
    @Test fun `penjualan melebihi stok ditolak`() { assertThrows(IllegalArgumentException::class.java) { OperasionalRules.validasiPenjualan(2, 3, 20_000, 15_000, 60_000) } }
    @Test fun `pembayaran sebagian dikenali`() = assertEquals(StatusPembayaran.SEBAGIAN, OperasionalRules.statusPembayaran(60_000, 30_000))
    @Test fun `pelanggan tanpa jatah tidak memengaruhi sisa`() = assertEquals(0, OperasionalRules.sisaJatah(0, 0))
}
