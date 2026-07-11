package id.lpgsmartbase.presentation.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import id.lpgsmartbase.R
import id.lpgsmartbase.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: DashboardViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDashboardBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { viewModel.title.collect { binding.dashboardSubtitle.text = it } } }
        binding.menuCustomers.setOnClickListener { findNavController().navigate(R.id.customersFragment) }
        binding.menuAllocations.setOnClickListener { findNavController().navigate(R.id.allocationsFragment) }
        binding.menuDeposits.setOnClickListener { findNavController().navigate(R.id.depositsFragment) }
        binding.menuStock.setOnClickListener { findNavController().navigate(R.id.stockFragment) }
        binding.menuTransactions.setOnClickListener { findNavController().navigate(R.id.transactionsFragment) }
        binding.menuReceipts.setOnClickListener { findNavController().navigate(R.id.receiptsFragment) }
        binding.menuDebts.setOnClickListener { findNavController().navigate(R.id.debtsFragment) }
        binding.menuReports.setOnClickListener { findNavController().navigate(R.id.reportsFragment) }
        binding.menuBackup.setOnClickListener { findNavController().navigate(R.id.backupFragment) }
        binding.menuSettings.setOnClickListener { findNavController().navigate(R.id.settingsFragment) }
        binding.menuPrinter.setOnClickListener { findNavController().navigate(R.id.printerFragment) }
    }
    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}
