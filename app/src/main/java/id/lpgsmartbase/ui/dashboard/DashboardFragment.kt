package id.lpgsmartbase.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.core.util.toRupiah
import id.lpgsmartbase.data.repository.DashboardRepository
import id.lpgsmartbase.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory(DashboardRepository((requireActivity().application as LpgSmartBaseApp).database))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDashboardBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ringkasan.collect { ringkasan ->
                    binding.stokIsiValue.text = ringkasan.stokIsi.toString()
                    binding.stokKosongValue.text = ringkasan.stokKosong.toString()
                    binding.transaksiValue.text = ringkasan.transaksiHariIni.toString()
                    binding.kasValue.text = ringkasan.kasHariIni.toRupiah()
                }
            }
        }
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}
