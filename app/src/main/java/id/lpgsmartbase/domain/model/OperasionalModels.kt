package id.lpgsmartbase.domain.model

enum class StatusPembayaran { SUDAH_DIBAYAR, BELUM_DIBAYAR, SEBAGIAN }

data class InputPenjualan(val nomorNota: String, val pelangganId: Long?, val jenisTabung: String, val jumlah: Int, val hargaJual: Long, val hargaModal: Long, val dibayar: Long, val periodeJatah: String?, val catatan: String = "")
data class RingkasanJatah(val jatah: Int, val titip: Int, val diambil: Int) { val tersedia get() = jatah; val terpakai get() = diambil; val sisa get() = (jatah - diambil).coerceAtLeast(0) }
data class HasilPenjualan(val transaksiId: Long, val status: StatusPembayaran)
