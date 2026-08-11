package id.lpgsmartbase.domain.model

data class Pelanggan(val id: Long = 0, val kode: String, val nama: String, val alamat: String, val telepon: String, val aktif: Boolean = true)
