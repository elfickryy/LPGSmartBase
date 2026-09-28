package id.lpgsmartbase.ui.operasional

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.local.entity.TransaksiEntity
import id.lpgsmartbase.databinding.FragmentRiwayatBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RiwayatFragment : Fragment(R.layout.fragment_riwayat) {
    private var binding: FragmentRiwayatBinding? = null
    private var transaksi: List<TransaksiEntity> = emptyList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRiwayatBinding.bind(view)
        binding?.pilihTransaksi?.setOnClickListener { pilihTransaksi() }
        viewLifecycleOwner.lifecycleScope.launch {
            (requireActivity().application as LpgSmartBaseApp).database.transaksiDao().observeAll().collectLatest { daftar ->
                transaksi = daftar
                binding?.riwayat?.text = if (daftar.isEmpty()) "Belum ada transaksi" else daftar.joinToString("\n") { "${it.nomorNota} • ${it.jumlah} tabung • ${it.total} • ${it.statusPembayaran}" }
            }
        }
    }
    private fun pilihTransaksi() {
        if (transaksi.isEmpty()) { binding?.riwayat?.text = "Belum ada transaksi untuk dipilih"; return }
        AlertDialog.Builder(requireContext()).setTitle("Pilih transaksi")
            .setItems(transaksi.map { "${it.nomorNota} • ${it.jumlah} tabung • ${it.total}" }.toTypedArray()) { _, index ->
                findNavController().navigate(R.id.to_transaction_detail, bundleOf("id" to transaksi[index].id))
            }.show()
    }
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
