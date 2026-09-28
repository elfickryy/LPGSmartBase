package id.lpgsmartbase.ui.dashboard
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.repository.ReportRepository
import id.lpgsmartbase.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
 private var binding: FragmentDashboardBinding?=null
 override fun onViewCreated(view:View,savedInstanceState:Bundle?){ binding=FragmentDashboardBinding.bind(view); val mulai=System.currentTimeMillis()-System.currentTimeMillis()%86_400_000L; viewLifecycleOwner.lifecycleScope.launch { ReportRepository((requireActivity().application as LpgSmartBaseApp).database).dashboard(mulai,System.currentTimeMillis()).collectLatest { (jual,kas,stok)-> binding?.ringkasan?.text="Omzet: ${jual.omzet}\nTransaksi: ${jual.transaksi}\nTabung: ${jual.tabung}\nKas: ${kas.saldo}\nHutang: ${jual.hutang}\nStok isi: ${stok.sumOf{it.isi}}\nStok kosong: ${stok.sumOf{it.kosong}}" } }; binding?.pelanggan?.setOnClickListener{findNavController().navigate(R.id.to_pelanggan)}; binding?.stok?.setOnClickListener{findNavController().navigate(R.id.to_stok)}; binding?.riwayat?.setOnClickListener{findNavController().navigate(R.id.to_riwayat)}; binding?.laporan?.setOnClickListener{findNavController().navigate(R.id.to_laporan)} }
 override fun onDestroyView(){binding=null;super.onDestroyView()}
}
