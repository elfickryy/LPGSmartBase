package id.lpgsmartbase.domain.service

import id.lpgsmartbase.domain.model.StatusPembayaran

object OperasionalRules {
    fun validasiPenjualan(stokIsi: Int, jumlah: Int, hargaJual: Long, hargaModal: Long, dibayar: Long) {
        require(jumlah > 0) { "Jumlah tabung harus lebih dari nol" }
        require(hargaJual >= 0 && hargaModal >= 0) { "Harga tidak boleh negatif" }
        require(jumlah <= stokIsi) { "Stok isi tidak mencukupi" }
        require(dibayar >= 0 && dibayar <= jumlah * hargaJual) { "Nominal pembayaran tidak valid" }
    }
    fun statusPembayaran(total: Long, dibayar: Long): StatusPembayaran = when { dibayar == 0L -> StatusPembayaran.BELUM_DIBAYAR; dibayar == total -> StatusPembayaran.SUDAH_DIBAYAR; else -> StatusPembayaran.SEBAGIAN }
    fun sisaJatah(jatah: Int, diambil: Int): Int = (jatah - diambil).coerceAtLeast(0)
}
