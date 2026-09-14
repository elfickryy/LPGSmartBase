package id.lpgsmartbase.ui.laporan
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.repository.ReportRepository
import id.lpgsmartbase.databinding.FragmentLaporanBinding
import id.lpgsmartbase.utils.toRupiah
import kotlinx.coroutines.launch
class LaporanFragment : Fragment(R.layout.fragment_laporan) { private var b: FragmentLaporanBinding? = null; private val vm: LaporanViewModel by viewModels { LaporanViewModelFactory(ReportRepository((requireActivity().application as LpgSmartBaseApp).database)) }; override fun onViewCreated(v: View,s: Bundle?) { b=FragmentLaporanBinding.bind(v); b?.filter?.setOnClickListener { vm.pilih(PeriodeLaporan.HARI_INI) }; viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { vm.state.collect { data -> if(data==null) { b?.isi?.text="Memuat laporan..."; return@collect }; val (r,f)=data; val(jual,kas,stok)=r; b?.isi?.text="Periode: $f\n\nRINGKASAN / PENJUALAN\nTransaksi: ${jual.transaksi}\nTabung: ${jual.tabung}\nOmzet: ${jual.omzet.toRupiah()}\nPembayaran: ${jual.dibayar.toRupiah()}\nHUTANG: ${jual.hutang.toRupiah()}\nLABA\nModal: ${jual.modal.toRupiah()}\nLaba: ${jual.laba.toRupiah()}\n\nKAS\nMasuk: ${kas.masuk.toRupiah()}\nKeluar: ${kas.keluar.toRupiah()}\nSaldo: ${kas.saldo.toRupiah()}\n\nSTOK\nIsi: ${stok.sumOf{it.isi}}\nKosong: ${stok.sumOf{it.kosong}}\nTitipan: data pelanggan" } } } }; override fun onDestroyView(){b=null;super.onDestroyView()} }
