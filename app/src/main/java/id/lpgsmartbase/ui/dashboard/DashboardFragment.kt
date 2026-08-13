package id.lpgsmartbase.ui.dashboard
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.lpgsmartbase.R
import id.lpgsmartbase.databinding.FragmentDashboardBinding
class DashboardFragment : Fragment(R.layout.fragment_dashboard) { private var binding: FragmentDashboardBinding? = null; override fun onViewCreated(view: View, savedInstanceState: Bundle?) { binding = FragmentDashboardBinding.bind(view); binding?.pelanggan?.setOnClickListener { findNavController().navigate(R.id.to_pelanggan) } }; override fun onDestroyView() { binding = null; super.onDestroyView() } }
