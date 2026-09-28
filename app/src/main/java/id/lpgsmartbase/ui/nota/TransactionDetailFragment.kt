package id.lpgsmartbase.ui.nota

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.databinding.FragmentTransactionDetailBinding
import kotlinx.coroutines.launch

class TransactionDetailFragment : Fragment(R.layout.fragment_transaction_detail) {
    private var binding: FragmentTransactionDetailBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTransactionDetailBinding.bind(view)
        val id = requireArguments().getLong("id")
        binding?.previewNota?.setOnClickListener { findNavController().navigate(R.id.to_receipt, bundleOf("id" to id)) }
        viewLifecycleOwner.lifecycleScope.launch {
            val transaksi = (requireActivity().application as LpgSmartBaseApp).database.transaksiDao().get(id)
            binding?.detail?.text = transaksi?.let { "Nomor: ${it.nomorNota}\nTanggal: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(it.waktu))}\nJenis: ${it.jenisTransaksi}\nJumlah: ${it.jumlah}\nHarga: ${it.hargaJual}\nTotal: ${it.total}\nPembayaran: ${it.statusPembayaran}\nCatatan: ${it.catatan}" } ?: "Transaksi tidak ditemukan"
            binding?.previewNota?.isEnabled = transaksi != null
        }
    }
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
