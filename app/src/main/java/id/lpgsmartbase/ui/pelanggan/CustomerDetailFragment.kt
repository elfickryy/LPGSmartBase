package id.lpgsmartbase.ui.pelanggan

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.local.entity.JatahEntity
import id.lpgsmartbase.data.repository.OperasionalRepository
import id.lpgsmartbase.databinding.FragmentCustomerDetailBinding
import id.lpgsmartbase.databinding.DialogPelangganBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerDetailFragment : Fragment(R.layout.fragment_customer_detail) {
    private var binding: FragmentCustomerDetailBinding? = null
    private val period get() = SimpleDateFormat("yyyy-MM", Locale.US).format(Date())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCustomerDetailBinding.bind(view)
        val id = requireArguments().getLong("id")
        binding?.edit?.setOnClickListener { edit(id) }
        binding?.jatah?.setOnClickListener { aturJatah(id) }
        binding?.titip?.setOnClickListener { catatTitip(id) }
        binding?.nonaktif?.setOnClickListener { viewLifecycleOwner.lifecycleScope.launch { app.database.pelangganDao().nonaktifkan(id); parentFragmentManager.popBackStack() } }
        muat(id)
    }
    private fun muat(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        val customer = app.database.pelangganDao().get(id) ?: run { binding?.detail?.text = "Pelanggan tidak ditemukan"; return@launch }
        val jatah = app.database.operasionalDao().jatah(id, period)
        val titip = app.database.operasionalDao().saldoTitipan(id)?.saldo ?: 0
        val totalPembelian = app.database.transaksiDao().observePelanggan(id).first().sumOf { it.total }
        binding?.detail?.text = "${customer.nama}\n${customer.telepon}\n${customer.alamat}\n\nPeriode: $period\nJatah: ${jatah?.jumlah ?: 0}\nSisa jatah: ${(jatah?.jumlah ?: 0) - (jatah?.realisasi ?: 0)}\nTitipan tersisa: $titip\nTotal pembelian: $totalPembelian"
    }
    private fun edit(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        val c = app.database.pelangganDao().get(id) ?: return@launch
        val form = DialogPelangganBinding.inflate(layoutInflater).apply { kode.setText(c.kode); nama.setText(c.nama); alamat.setText(c.alamat); telepon.setText(c.telepon) }
        AlertDialog.Builder(requireContext()).setTitle("Edit pelanggan").setView(form.root).setNegativeButton("Batal", null).setPositiveButton("Simpan") { _, _ -> viewLifecycleOwner.lifecycleScope.launch { app.database.pelangganDao().update(c.copy(kode = form.kode.text.toString().trim(), nama = form.nama.text.toString().trim(), alamat = form.alamat.text.toString().trim(), telepon = form.telepon.text.toString().trim())); muat(id) } }.show()
    }
    private fun aturJatah(id: Long) {
        val input = EditText(requireContext()).apply { inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        AlertDialog.Builder(requireContext()).setTitle("Jatah $period").setView(input).setNegativeButton("Batal", null).setPositiveButton("Simpan") { _, _ -> viewLifecycleOwner.lifecycleScope.launch { val jumlah = input.text.toString().toIntOrNull() ?: 0; val lama = app.database.jatahDao().get(id, period); if (jumlah >= (lama?.realisasi ?: 0)) { app.database.jatahDao().save(lama?.copy(jumlah = jumlah) ?: JatahEntity(pelangganId = id, periode = period, jumlah = jumlah)); muat(id) } else binding?.detail?.text = "Jatah tidak boleh lebih kecil dari penggunaan" } }.show()
    }
    private fun catatTitip(id: Long) {
        val input = EditText(requireContext()).apply { inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        AlertDialog.Builder(requireContext()).setTitle("Jumlah tabung titipan").setView(input).setNegativeButton("Batal", null).setPositiveButton("Simpan") { _, _ -> viewLifecycleOwner.lifecycleScope.launch { val jumlah = input.text.toString().toIntOrNull() ?: 0; if (jumlah > 0) { OperasionalRepository(app.database).catatTitipan(id, period, jumlah, jumlah, 0, "Titipan pelanggan"); muat(id) } } }.show()
    }
    private val app get() = requireActivity().application as LpgSmartBaseApp
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
