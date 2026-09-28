package id.lpgsmartbase.ui.operasional

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.local.entity.PelangganEntity
import id.lpgsmartbase.data.repository.OperasionalRepository
import id.lpgsmartbase.databinding.FragmentTransactionBinding
import id.lpgsmartbase.domain.model.InputPenjualan
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionFragment : Fragment(R.layout.fragment_transaction) {
    private var binding: FragmentTransactionBinding? = null
    private var customers: List<PelangganEntity> = emptyList()
    private var selected: PelangganEntity? = null
    private var titipan = false
    private val vm: OperasionalViewModel by viewModels { OperasionalViewModelFactory(OperasionalRepository((requireActivity().application as LpgSmartBaseApp).database)) }
    private val period get() = SimpleDateFormat("yyyy-MM", Locale.US).format(Date())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTransactionBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch { app.database.pelangganDao().observeAll().collectLatest { customers = it } }
        binding?.jenis?.setOnClickListener { titipan = !titipan; binding?.jenis?.text = "Jenis: ${if (titipan) "AMBIL TITIPAN" else "PENJUALAN"}"; binding?.pelanggan?.isEnabled = true; updateInfo() }
        binding?.pelanggan?.setOnClickListener { pilihCustomer() }
        binding?.simpan?.setOnClickListener { simpan() }
    }
    private fun pilihCustomer() {
        val labels = listOf("Pembeli umum") + customers.map { "${it.nama} (${it.kode})" }
        AlertDialog.Builder(requireContext()).setTitle("Pilih pelanggan").setItems(labels.toTypedArray()) { _, index -> selected = customers.getOrNull(index - 1); binding?.pelanggan?.text = "Pelanggan: ${selected?.nama ?: "Pembeli umum"}"; updateInfo() }.show()
    }
    private fun updateInfo() = viewLifecycleOwner.lifecycleScope.launch {
        val c = selected ?: run { binding?.info?.text = "Pembeli umum tidak memakai jatah atau titipan"; return@launch }
        val j = app.database.operasionalDao().jatah(c.id, period)
        val t = app.database.operasionalDao().saldoTitipan(c.id)?.saldo ?: 0
        binding?.info?.text = "Jatah tersisa: ${(j?.jumlah ?: 0) - (j?.realisasi ?: 0)}\nTitipan tersisa: $t"
    }
    private fun simpan() {
        val jumlah = binding?.jumlah?.text?.toString()?.toIntOrNull() ?: 0
        val harga = binding?.harga?.text?.toString()?.toLongOrNull() ?: -1
        val modal = binding?.modal?.text?.toString()?.toLongOrNull() ?: 0
        val bayar = binding?.bayar?.text?.toString()?.toLongOrNull() ?: 0
        val input = InputPenjualan("TRX-${System.currentTimeMillis()}", selected?.id, binding?.tabung?.text?.toString()?.trim().orEmpty(), jumlah, harga, modal, bayar, selected?.let { period }, binding?.catatan?.text?.toString().orEmpty())
        val ok: () -> Unit = { binding?.info?.text = "Transaksi berhasil disimpan"; binding?.jumlah?.setText(""); Unit }
        val fail: (String) -> Unit = { message -> binding?.info?.text = message; Unit }
        if (titipan) vm.ambilTitipan(input, ok, fail) else vm.jual(input, ok, fail)
    }
    private val app get() = requireActivity().application as LpgSmartBaseApp
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
