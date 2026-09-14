package id.lpgsmartbase.ui.operasional

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.lpgsmartbase.data.repository.OperasionalRepository
import id.lpgsmartbase.domain.model.InputPenjualan
import kotlinx.coroutines.launch

class OperasionalViewModel(private val repository: OperasionalRepository) : ViewModel() {
    fun jual(input: InputPenjualan, berhasil: () -> Unit, gagal: (String) -> Unit) = viewModelScope.launch { runCatching { repository.prosesPenjualan(input) }.onSuccess { berhasil() }.onFailure { gagal(it.message ?: "Transaksi gagal") } }
}
class OperasionalViewModelFactory(private val repository: OperasionalRepository) : ViewModelProvider.Factory { @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = OperasionalViewModel(repository) as T }
