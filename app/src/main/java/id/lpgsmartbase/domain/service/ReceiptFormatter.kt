package id.lpgsmartbase.domain.service

import id.lpgsmartbase.data.local.entity.TransaksiEntity
import java.text.DateFormat
import java.util.Date

enum class PaperWidth(val chars: Int) { MM58(32), MM80(48) }
data class ReceiptProfile(val nama: String, val alamat: String, val telepon: String, val paper: PaperWidth = PaperWidth.MM58)
object ReceiptFormatter {
    fun format(profile: ReceiptProfile, transaksi: TransaksiEntity, pelanggan: String? = null): String {
        fun line(text: String) = text.take(profile.paper.chars)
        return listOf(line(profile.nama), line(profile.alamat), line(profile.telepon), "-".repeat(profile.paper.chars), "Nota: ${transaksi.nomorNota}", "Tanggal: ${DateFormat.getDateTimeInstance().format(Date(transaksi.waktu))}", "Jenis: ${transaksi.jenisTransaksi}", pelanggan?.let { "Pelanggan: $it" }, "Jumlah: ${transaksi.jumlah}", "Harga: ${transaksi.hargaJual}", "Total: ${transaksi.total}", "Bayar: ${transaksi.dibayar}", "Status: ${transaksi.statusPembayaran}", transaksi.catatan.takeIf { it.isNotBlank() }?.let { "Catatan: $it" }).filterNotNull().joinToString("\n")
    }
}
