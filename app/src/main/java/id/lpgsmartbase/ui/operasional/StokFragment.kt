package id.lpgsmartbase.ui.operasional

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.lpgsmartbase.LpgSmartBaseApp
import id.lpgsmartbase.R
import id.lpgsmartbase.data.local.entity.StokEntity
import id.lpgsmartbase.databinding.FragmentStokBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StokFragment : Fragment(R.layout.fragment_stok) {
    private var binding: FragmentStokBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentStokBinding.bind(view)
        binding?.tambahStok?.setOnClickListener { formStok() }
        viewLifecycleOwner.lifecycleScope.launch { app.database.stokDao().observeAll().collectLatest { stok -> binding?.isi?.text = stok.sumOf { it.isi }.toString(); binding?.kosong?.text = stok.sumOf { it.kosong }.toString() } }
    }
    private fun formStok() {
        fun field(hint: String) = EditText(requireContext()).apply { this.hint = hint; inputType = android.text.InputType.TYPE_CLASS_TEXT }
        val jenis = field("Jenis tabung, contoh LPG 3 Kg")
        val isi = field("Stok isi").apply { inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        val kosong = field("Stok kosong").apply { inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        val modal = field("Harga modal").apply { inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        val jual = field("Harga jual").apply { inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        val layout = LinearLayout(requireContext()).apply { orientation = LinearLayout.VERTICAL; setPadding(48, 16, 48, 0); listOf(jenis, isi, kosong, modal, jual).forEach(::addView) }
        AlertDialog.Builder(requireContext()).setTitle("Stok LPG").setView(layout).setNegativeButton("Batal", null).setPositiveButton("Simpan") { _, _ ->
            val name = jenis.text.toString().trim(); val filled = isi.text.toString().toIntOrNull(); val empty = kosong.text.toString().toIntOrNull(); val cost = modal.text.toString().toLongOrNull() ?: 0; val price = jual.text.toString().toLongOrNull() ?: 0
            if (name.isNotBlank() && filled != null && empty != null && filled >= 0 && empty >= 0 && cost >= 0 && price >= 0) viewLifecycleOwner.lifecycleScope.launch { app.database.stokDao().save(StokEntity(name, filled, empty, cost, price)) }
        }.show()
    }
    private val app get() = requireActivity().application as LpgSmartBaseApp
    override fun onDestroyView() { binding = null; super.onDestroyView() }
}
