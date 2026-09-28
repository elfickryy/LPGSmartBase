package id.lpgsmartbase.ui.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.lpgsmartbase.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import id.lpgsmartbase.data.local.entity.SettingEntity
import kotlinx.coroutines.launch
class SettingsViewModel(private val repo:SettingsRepository):ViewModel(){
 fun save(k:String,v:String)=viewModelScope.launch{repo.save(k,v)}
 fun observe(k:String): Flow<SettingEntity?> = repo.observe(k)
}
class SettingsViewModelFactory(private val repo:SettingsRepository):ViewModelProvider.Factory{@Suppress("UNCHECKED_CAST") override fun<T:ViewModel> create(c:Class<T>):T=SettingsViewModel(repo) as T}
