package id.lpgsmartbase.domain.model
data class RekapPenjualan(val transaksi: Int, val tabung: Int, val omzet: Long, val dibayar: Long, val hutang: Long, val modal: Long) { val laba get() = omzet - modal }
data class RekapKas(val masuk: Long, val keluar: Long) { val saldo get() = masuk - keluar }
