package id.lpgsmartbase.ui.pelanggan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.lpgsmartbase.databinding.ItemPelangganBinding
import id.lpgsmartbase.domain.model.Pelanggan

class PelangganAdapter(private val onLongClick: (Pelanggan) -> Unit) : ListAdapter<Pelanggan, PelangganAdapter.ViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemPelangganBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
    inner class ViewHolder(private val binding: ItemPelangganBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pelanggan) { binding.kode.text = item.kode; binding.nama.text = item.nama; binding.detail.text = "${item.telepon} • ${item.alamat}"; binding.root.setOnLongClickListener { onLongClick(item); true } }
    }
    private object Diff : DiffUtil.ItemCallback<Pelanggan>() { override fun areItemsTheSame(a: Pelanggan, b: Pelanggan) = a.id == b.id; override fun areContentsTheSame(a: Pelanggan, b: Pelanggan) = a == b }
}
