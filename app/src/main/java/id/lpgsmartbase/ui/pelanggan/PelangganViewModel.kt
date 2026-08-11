package id.lpgsmartbase.ui.pelanggan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.lpgsmartbase.domain.model.Pelanggan
import id.lpgsmartbase.domain.repository.PelangganRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PelangganViewModel(private val repository: PelangganRepository) : ViewModel() {
    val pelanggan = repository.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun simpan(kode: String, nama: String, alamat: String, telepon: String) = viewModelScope.launch { repository.tambah(Pelanggan(kode = kode, nama = nama, alamat = alamat, telepon = telepon)) }
    fun nonaktifkan(id: Long) = viewModelScope.launch { repository.nonaktifkan(id) }
}
class PelangganViewModelFactory(private val repository: PelangganRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = PelangganViewModel(repository) as T
}
