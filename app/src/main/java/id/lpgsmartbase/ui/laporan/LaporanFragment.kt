package id.lpgsmartbase.ui.laporan

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.repository.ReportData
import id.lpgsmartbase.data.repository.ReportRepository
import id.lpgsmartbase.databinding.FragmentLaporanBinding
import id.lpgsmartbase.utils.toRupiah
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar

class LaporanFragment : Fragment(R.layout.fragment_laporan) {
    private var binding: FragmentLaporanBinding? = null
    private val vm: LaporanViewModel by viewModels { LaporanViewModelFactory(ReportRepository((requireActivity().application as LpgSmartBaseApp).database)) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLaporanBinding.bind(view)
        binding?.filter?.setOnClickListener { pilihFilter() }
        binding?.kategori?.setOnClickListener { pilihKategori() }
        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { combine(vm.state, vm.kategori) { data, kategori -> data to kategori }.collect { (data, kategori) -> if (data == null) binding?.isi?.text = "Memuat laporan..." else tampilkan(data, kategori) } } }
    }
    private fun pilihFilter() {
        val options = arrayOf("Hari ini", "Kemarin", "Minggu ini", "Bulan ini", "Bulan sebelumnya", "Custom")
        AlertDialog.Builder(requireContext()).setTitle("Filter periode").setItems(options) { _, i ->
            val value = PeriodeLaporan.entries[i]
            if (value == PeriodeLaporan.CUSTOM) pilihTanggalMulai() else { vm.pilih(value); binding?.filter?.text = options[i] }
        }.show()
    }
    private fun pilihTanggalMulai() { val c = Calendar.getInstance(); DatePickerDialog(requireContext(), { _, y, m, d -> val start = Calendar.getInstance().apply { set(y,m,d,0,0,0); set(Calendar.MILLISECOND,0) }.timeInMillis; pilihTanggalAkhir(start) }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show() }
    private fun pilihTanggalAkhir(start: Long) { val c = Calendar.getInstance(); DatePickerDialog(requireContext(), { _, y, m, d -> val end = Calendar.getInstance().apply { set(y,m,d,23,59,59); set(Calendar.MILLISECOND,999) }.timeInMillis; if (end >= start) { vm.custom(start,end); binding?.filter?.text = "Custom" } else binding?.isi?.text = "Tanggal akhir harus setelah tanggal mulai" }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show() }
    private fun pilihKategori() { val values = KategoriLaporan.entries; AlertDialog.Builder(requireContext()).setTitle("Kategori laporan").setItems(values.map { it.name.lowercase().replaceFirstChar(Char::titlecase) }.toTypedArray()) { _, i -> vm.kategori.value = values[i]; binding?.kategori?.text = "Kategori: ${values[i].name.lowercase().replaceFirstChar(Char::titlecase)}" }.show() }
    private fun tampilkan(data: ReportData, kategori: KategoriLaporan) {
        val p = data.penjualan; val k = data.kas
        binding?.isi?.text = when (kategori) {
            KategoriLaporan.RINGKASAN -> "Transaksi: ${p.transaksi}\nTabung: ${p.tabung}\nOmzet: ${p.omzet.toRupiah()}\nPembayaran: ${p.dibayar.toRupiah()}\nBelum dibayar: ${p.hutang.toRupiah()}\nModal: ${p.modal.toRupiah()}\nLaba: ${p.laba.toRupiah()}"
            KategoriLaporan.PENJUALAN -> "Jumlah transaksi: ${p.transaksi}\nTabung terjual: ${p.tabung}\nOmzet: ${p.omzet.toRupiah()}\nSudah dibayar: ${p.dibayar.toRupiah()}\nBelum dibayar: ${p.hutang.toRupiah()}"
            KategoriLaporan.KAS -> "Kas masuk: ${k.masuk.toRupiah()}\nKas keluar: ${k.keluar.toRupiah()}\nSaldo: ${k.saldo.toRupiah()}"
            KategoriLaporan.STOK -> "LPG isi: ${data.stok.sumOf { it.isi }}\nTabung kosong: ${data.stok.sumOf { it.kosong }}"
            KategoriLaporan.PELANGGAN -> if (data.pelanggan.isEmpty()) "Belum ada pelanggan" else data.pelanggan.joinToString("\n") { "${it.nama} (${it.kode})" }
            KategoriLaporan.HUTANG -> "Total belum dibayar: ${p.hutang.toRupiah()}"
            KategoriLaporan.LABA -> "Total modal: ${p.modal.toRupiah()}\nTotal penjualan: ${p.omzet.toRupiah()}\nEstimasi laba: ${p.laba.toRupiah()}"
        }
    }
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
