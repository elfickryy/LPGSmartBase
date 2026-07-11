package id.lpgsmartbase.presentation.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {
    private val _title = MutableStateFlow("Operasional LPG hari ini")
    val title = _title.asStateFlow()
}
