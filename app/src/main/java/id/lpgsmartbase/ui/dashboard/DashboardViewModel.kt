package id.lpgsmartbase.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.lpgsmartbase.data.repository.DashboardRepository

class DashboardViewModel(repository: DashboardRepository) : ViewModel() {
    val ringkasan = repository.observeRingkasan()
}

class DashboardViewModelFactory(private val repository: DashboardRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DashboardViewModel(repository) as T
}
