package id.lpgsmartbase.domain.service
import id.lpgsmartbase.data.local.entity.TransaksiEntity
import org.junit.Assert.assertTrue
import org.junit.Test
class ReceiptFormatterTest { private val t=TransaksiEntity(nomorNota="N-1",pelangganId=1,jumlah=2,hargaJual=20000,hargaModal=15000,total=40000,dibayar=20000,statusPembayaran="SEBAGIAN"); @Test fun `nota memuat transaksi dan pelanggan`() { val r=ReceiptFormatter.format(ReceiptProfile("Pangkalan","Alamat","0812"),t,"Budi"); assertTrue(r.contains("N-1")&&r.contains("Tanggal:")&&r.contains("Budi")&&r.contains("SEBAGIAN")) }; @Test fun `format 58 dan 80 tersedia`() { assertTrue(ReceiptFormatter.format(ReceiptProfile("P","A","T",PaperWidth.MM58),t).isNotBlank()); assertTrue(ReceiptFormatter.format(ReceiptProfile("P","A","T",PaperWidth.MM80),t).isNotBlank()) } }
