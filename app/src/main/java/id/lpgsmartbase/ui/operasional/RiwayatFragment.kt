package id.lpgsmartbase.ui.operasional

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.databinding.FragmentRiwayatBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RiwayatFragment : Fragment(R.layout.fragment_riwayat) {
    private var binding: FragmentRiwayatBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRiwayatBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch {
            (requireActivity().application as LpgSmartBaseApp).database.transaksiDao().observeAll().collectLatest { daftar ->
                binding?.riwayat?.text = daftar.joinToString("\n") { "${it.nomorNota} - ${it.jumlah} tabung - ${it.total} - ${it.statusPembayaran}" }
            }
        }
    }
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
