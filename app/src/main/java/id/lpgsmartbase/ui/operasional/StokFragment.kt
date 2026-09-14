package id.lpgsmartbase.ui.operasional

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.databinding.FragmentStokBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StokFragment : Fragment(R.layout.fragment_stok) {
    private var binding: FragmentStokBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentStokBinding.bind(view)
        binding?.daftar?.layoutManager = LinearLayoutManager(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            (requireActivity().application as LpgSmartBaseApp).database.stokDao().observeAll().collectLatest { stok ->
                binding?.isi?.text = stok.sumOf { it.isi }.toString()
                binding?.kosong?.text = stok.sumOf { it.kosong }.toString()
            }
        }
    }
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
