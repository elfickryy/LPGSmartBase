package id.lpgsmartbase.domain.service

import id.lpgsmartbase.domain.model.StatusPembayaran
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class OperasionalRulesTest {
    @Test fun `ambil titipan mengurangi jatah dan titipan`() { assertEquals(5, OperasionalRules.sisaJatah(10, 5)); assertEquals(4, OperasionalRules.sisaJatah(9, 5)) }
    @Test fun `penjualan mengurangi jatah bukan titipan`() { assertEquals(8, OperasionalRules.sisaJatah(10, 2)); assertEquals(9, OperasionalRules.sisaJatah(9, 0)) }
    @Test fun `ambil lalu beli mempertahankan saldo titipan`() { assertEquals(3, OperasionalRules.sisaJatah(10, 7)); assertEquals(4, OperasionalRules.sisaJatah(9, 5)) }
    @Test fun `dua pengambilan titipan`() { assertEquals(3, OperasionalRules.sisaJatah(10, 7)); assertEquals(2, OperasionalRules.sisaJatah(9, 7)) }
    @Test fun `jatah habis sisa nol`() = assertEquals(0, OperasionalRules.sisaJatah(3, 3))
    @Test fun `penjualan melebihi stok ditolak`() { assertThrows(IllegalArgumentException::class.java) { OperasionalRules.validasiPenjualan(2, 3, 20_000, 15_000, 60_000) } }
    @Test fun `pembayaran sebagian dikenali`() = assertEquals(StatusPembayaran.SEBAGIAN, OperasionalRules.statusPembayaran(60_000, 30_000))
    @Test fun `pelanggan tanpa jatah tidak memengaruhi sisa`() = assertEquals(0, OperasionalRules.sisaJatah(0, 0))
    @Test fun `ambil melebihi titipan ditolak`() { assertThrows(IllegalArgumentException::class.java) { OperasionalRules.validasiAlokasi(10, 4, 5, true) } }
    @Test fun `beli melebihi jatah ditolak`() { assertThrows(IllegalArgumentException::class.java) { OperasionalRules.validasiAlokasi(1, 9, 2, false) } }
    @Test fun `jatah titipan dan stok tidak negatif`() { assertEquals(0, OperasionalRules.sisaJatah(1, 3)); assertThrows(IllegalArgumentException::class.java) { OperasionalRules.validasiPenjualan(1, 2, 1, 0, 2) } }
}
