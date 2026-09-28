package id.lpgsmartbase.ui.settings
import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.repository.SettingsRepository
import id.lpgsmartbase.databinding.FragmentSettingsBinding
import id.lpgsmartbase.data.backup.BackupManager
class SettingsFragment:Fragment(R.layout.fragment_settings){private var b:FragmentSettingsBinding?=null;private val vm:SettingsViewModel by viewModels{SettingsViewModelFactory(SettingsRepository((requireActivity().application as LpgSmartBaseApp).database.settingDao()))};private val buatBackup=registerForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")){uri->uri?:return@registerForActivityResult;runCatching{requireContext().contentResolver.openOutputStream(uri)?.use{BackupManager.backup(requireContext(),(requireActivity().application as LpgSmartBaseApp).database,it)}?:error("File tidak dapat dibuka")}.onSuccess{b?.status?.text="Backup berhasil dibuat"}.onFailure{b?.status?.text="Backup gagal: ${it.message}"}};private val pilihRestore=registerForActivityResult(ActivityResultContracts.OpenDocument()){uri->uri?:return@registerForActivityResult;val valid=runCatching{requireContext().contentResolver.openInputStream(uri)?.use{BackupManager.isValid(it)}?:false}.getOrDefault(false);if(!valid){b?.status?.text="File backup tidak valid";return@registerForActivityResult};AlertDialog.Builder(requireContext()).setMessage("Restore akan mengganti data aplikasi saat ini. Lanjutkan?").setNegativeButton("Batal",null).setPositiveButton("Restore"){_,_->runCatching{requireContext().contentResolver.openInputStream(uri)?.use{BackupManager.restore(requireContext(),(requireActivity().application as LpgSmartBaseApp).database,it)}?:error("File tidak dapat dibuka")}.onSuccess{b?.status?.text="Restore berhasil. Buka ulang aplikasi."}.onFailure{b?.status?.text="Restore gagal: ${it.message}"}}.show()};override fun onViewCreated(v:View,s:Bundle?){b=FragmentSettingsBinding.bind(v);b?.simpan?.setOnClickListener{vm.save("nama_pangkalan",b?.nama?.text?.toString().orEmpty());vm.save("alamat_pangkalan",b?.alamat?.text?.toString().orEmpty());vm.save("telepon_pangkalan",b?.telepon?.text?.toString().orEmpty());vm.save("nama_pemilik",b?.pemilik?.text?.toString().orEmpty())};b?.backup?.setOnClickListener{buatBackup.launch("lpg-smartbase.backup")};b?.restore?.setOnClickListener{pilihRestore.launch(arrayOf("application/octet-stream","application/*"))}};override fun onDestroyView(){b=null;super.onDestroyView()}}
