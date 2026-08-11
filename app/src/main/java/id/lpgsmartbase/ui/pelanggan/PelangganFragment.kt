package id.lpgsmartbase.ui.pelanggan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.repository.PelangganRepositoryImpl
import id.lpgsmartbase.databinding.FragmentPelangganBinding
import id.lpgsmartbase.databinding.DialogPelangganBinding
import kotlinx.coroutines.launch

class PelangganFragment : Fragment(R.layout.fragment_pelanggan) {
    private var _binding: FragmentPelangganBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: PelangganViewModel by viewModels { PelangganViewModelFactory(PelangganRepositoryImpl((requireActivity().application as LpgSmartBaseApp).database.pelangganDao())) }
    private val adapter = PelangganAdapter { pelanggan -> MaterialAlertDialogBuilder(requireContext()).setTitle("Nonaktifkan pelanggan?").setMessage(pelanggan.nama).setNegativeButton("Batal", null).setPositiveButton("Nonaktifkan") { _, _ -> viewModel.nonaktifkan(pelanggan.id) }.show() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPelangganBinding.bind(view); binding.recycler.layoutManager = LinearLayoutManager(requireContext()); binding.recycler.adapter = adapter
        binding.tambah.setOnClickListener { tampilkanForm() }
        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { viewModel.pelanggan.collect { daftar -> adapter.submitList(daftar); binding.kosong.visibility = if (daftar.isEmpty()) View.VISIBLE else View.GONE } } }
    }
    private fun tampilkanForm() { val form = DialogPelangganBinding.inflate(layoutInflater); MaterialAlertDialogBuilder(requireContext()).setTitle("Tambah Pelanggan").setView(form.root).setNegativeButton("Batal", null).setPositiveButton("Simpan", null).create().also { dialog -> dialog.setOnShowListener { dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener { val kode = form.kode.text?.toString()?.trim().orEmpty(); val nama = form.nama.text?.toString()?.trim().orEmpty(); if (kode.isBlank()) { form.kodeLayout.error = "Kode wajib diisi"; return@setOnClickListener }; if (nama.isBlank()) { form.namaLayout.error = "Nama wajib diisi"; return@setOnClickListener }; viewModel.simpan(kode, nama, form.alamat.text?.toString()?.trim().orEmpty(), form.telepon.text?.toString()?.trim().orEmpty()); dialog.dismiss() } }; dialog.show() } }
    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}
