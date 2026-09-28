package id.lpgsmartbase.ui.nota

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.printer.BluetoothPrinterManager
import id.lpgsmartbase.data.repository.SettingsRepository
import id.lpgsmartbase.databinding.FragmentReceiptPreviewBinding
import id.lpgsmartbase.domain.service.PaperWidth
import id.lpgsmartbase.domain.service.ReceiptFormatter
import id.lpgsmartbase.domain.service.ReceiptProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReceiptPreviewFragment : Fragment(R.layout.fragment_receipt_preview) {
    private var binding: FragmentReceiptPreviewBinding? = null
    private var receiptText = ""
    private var printerAddress: String? = null
    private val bluetoothPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) cetakKePrinter() else binding?.status?.text = "Izin Bluetooth ditolak"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentReceiptPreviewBinding.bind(view)
        val id = requireArguments().getLong("id")
        viewLifecycleOwner.lifecycleScope.launch {
            val transaksi = app.database.transaksiDao().get(id)
            if (transaksi == null) { binding?.nota?.text = "Transaksi tidak ditemukan"; return@launch }
            val settings = SettingsRepository(app.database.settingDao())
            suspend fun setting(key: String) = settings.observe(key).first()?.nilai.orEmpty()
            val pelanggan = transaksi.pelangganId?.let { app.database.pelangganDao().get(it)?.nama }
            val width = if (setting("printer_paper_width") == "80") PaperWidth.MM80 else PaperWidth.MM58
            receiptText = ReceiptFormatter.format(ReceiptProfile(setting("nama_pangkalan"), setting("alamat_pangkalan"), setting("telepon_pangkalan"), width), transaksi, pelanggan)
            printerAddress = setting("printer_default").ifBlank { null }
            binding?.nota?.text = receiptText
        }
        binding?.bagikan?.setOnClickListener { bagikan() }
        binding?.cetak?.setOnClickListener { mulaiCetak() }
    }
    private fun bagikan() {
        if (receiptText.isBlank()) { binding?.status?.text = "Nota belum siap"; return }
        try { startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, receiptText), "Bagikan nota")) }
        catch (_: ActivityNotFoundException) { binding?.status?.text = "Tidak ada aplikasi untuk membagikan nota" }
    }
    private fun mulaiCetak() {
        if (receiptText.isBlank()) { binding?.status?.text = "Nota belum siap"; return }
        if (printerAddress == null) { binding?.status?.text = "Pilih printer default di Settings terlebih dahulu"; return }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) bluetoothPermission.launch(Manifest.permission.BLUETOOTH_CONNECT) else cetakKePrinter()
    }
    private fun cetakKePrinter() {
        val address = printerAddress ?: return
        binding?.status?.text = "Menghubungkan printer..."
        viewLifecycleOwner.lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) { BluetoothPrinterManager.print(requireContext(), address, receiptText) }
            binding?.status?.text = result.fold(onSuccess = { "Nota berhasil dicetak" }, onFailure = { it.message ?: "Cetak gagal" })
        }
    }
    private val app get() = requireActivity().application as LpgSmartBaseApp
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
