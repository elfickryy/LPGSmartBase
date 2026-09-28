package id.lpgsmartbase.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.backup.BackupManager
import id.lpgsmartbase.data.printer.BluetoothPrinterManager
import id.lpgsmartbase.data.repository.SettingsRepository
import id.lpgsmartbase.databinding.FragmentSettingsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var binding: FragmentSettingsBinding? = null
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingsRepository(app.database.settingDao()))
    }
    private val bluetoothPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) tampilkanPrinterPaired() else binding?.status?.text = "Izin Bluetooth ditolak"
    }
    private val buatBackup = registerForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
        uri ?: return@registerForActivityResult
        runCatching { requireContext().contentResolver.openOutputStream(uri)?.use { BackupManager.backup(requireContext(), app.database, it) } ?: error("File tidak dapat dibuka") }
            .onSuccess { binding?.status?.text = "Backup berhasil dibuat" }
            .onFailure { binding?.status?.text = "Backup gagal: ${it.message}" }
    }
    private val pilihRestore = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri ?: return@registerForActivityResult
        val valid = runCatching { requireContext().contentResolver.openInputStream(uri)?.use(BackupManager::isValid) ?: false }.getOrDefault(false)
        if (!valid) { binding?.status?.text = "File backup tidak valid"; return@registerForActivityResult }
        AlertDialog.Builder(requireContext()).setMessage("Restore akan mengganti data aplikasi saat ini. Pastikan Anda sudah memiliki backup terbaru. Lanjutkan?")
            .setNegativeButton("Batal", null).setPositiveButton("Restore") { _, _ ->
                runCatching { requireContext().contentResolver.openInputStream(uri)?.use { BackupManager.restore(requireContext(), app.database, it) } ?: error("File tidak dapat dibuka") }
                    .onSuccess { binding?.status?.text = "Restore berhasil. Buka ulang aplikasi." }
                    .onFailure { binding?.status?.text = "Restore gagal: ${it.message}" }
            }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSettingsBinding.bind(view)
        observeSettings()
        binding?.simpan?.setOnClickListener {
            viewModel.save("nama_pangkalan", binding?.nama?.text?.toString().orEmpty())
            viewModel.save("alamat_pangkalan", binding?.alamat?.text?.toString().orEmpty())
            viewModel.save("telepon_pangkalan", binding?.telepon?.text?.toString().orEmpty())
            viewModel.save("nama_pemilik", binding?.pemilik?.text?.toString().orEmpty())
            viewModel.save("printer_auto_print", binding?.autoPrint?.isChecked.toString())
            binding?.status?.text = "Pengaturan tersimpan"
        }
        binding?.pilihPrinter?.setOnClickListener { mintaIzinLaluTampilkanPrinter() }
        binding?.hubungkanPrinter?.setOnClickListener { hubungkanPrinter() }
        binding?.putuskanPrinter?.setOnClickListener {
            BluetoothPrinterManager.disconnect()
            binding?.status?.text = "Printer diputuskan"
        }
        binding?.ukuranKertas?.setOnClickListener { pilihUkuranKertas() }
        binding?.autoPrint?.setOnCheckedChangeListener { _, checked -> viewModel.save("printer_auto_print", checked.toString()) }
        binding?.backup?.setOnClickListener { buatBackup.launch("lpg-smartbase.backup") }
        binding?.restore?.setOnClickListener { pilihRestore.launch(arrayOf("application/octet-stream", "application/*")) }
    }

    private fun observeSettings() {
        fun observe(key: String, update: (String) -> Unit) = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.observe(key).collectLatest { update(it?.nilai.orEmpty()) }
        }
        observe("nama_pangkalan") { binding?.nama?.setText(it) }
        observe("alamat_pangkalan") { binding?.alamat?.setText(it) }
        observe("telepon_pangkalan") { binding?.telepon?.setText(it) }
        observe("nama_pemilik") { binding?.pemilik?.setText(it) }
        observe("printer_default") { binding?.printer?.text = if (it.isBlank()) "Printer default belum dipilih" else "Printer default: $it" }
        observe("printer_paper_width") { binding?.ukuranKertas?.text = "Ukuran kertas: ${if (it == "80") "80mm" else "58mm"}" }
        observe("printer_auto_print") { binding?.autoPrint?.isChecked = it == "true" }
    }

    private fun mintaIzinLaluTampilkanPrinter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) bluetoothPermission.launch(Manifest.permission.BLUETOOTH_CONNECT) else tampilkanPrinterPaired()
    }

    private fun tampilkanPrinterPaired() {
        BluetoothPrinterManager.paired(requireContext()).onSuccess { devices ->
            if (devices.isEmpty()) { binding?.status?.text = "Tidak ada perangkat Bluetooth yang sudah dipasangkan"; return@onSuccess }
            val daftar = devices.sortedBy { it.name ?: it.address }.toList()
            AlertDialog.Builder(requireContext()).setTitle("Pilih printer default")
                .setItems(daftar.map { "${it.name ?: "Tanpa nama"}\n${it.address}" }.toTypedArray()) { _, index ->
                    val device = daftar[index]
                    viewModel.save("printer_default", device.address)
                    viewModel.save("printer_default_name", device.name ?: device.address)
                    binding?.status?.text = "Printer default dipilih"
                }.show()
        }.onFailure { binding?.status?.text = it.message ?: "Bluetooth tidak tersedia" }
    }

    private fun pilihUkuranKertas() {
        AlertDialog.Builder(requireContext()).setTitle("Ukuran kertas")
            .setItems(arrayOf("58mm", "80mm")) { _, which -> viewModel.save("printer_paper_width", if (which == 1) "80" else "58") }.show()
    }

    private fun hubungkanPrinter() {
        viewLifecycleOwner.lifecycleScope.launch {
            val address = viewModel.observe("printer_default").first()?.nilai
            if (address.isNullOrBlank()) { binding?.status?.text = "Pilih printer default terlebih dahulu"; return@launch }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) { bluetoothPermission.launch(Manifest.permission.BLUETOOTH_CONNECT); return@launch }
            binding?.status?.text = "Menghubungkan printer..."
            val result = withContext(Dispatchers.IO) { BluetoothPrinterManager.connect(requireContext(), address) }
            binding?.status?.text = result.fold({ "Printer terhubung" }, { it.message ?: "Koneksi printer gagal" })
        }
    }

    private val app get() = requireActivity().application as LpgSmartBaseApp
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
