package com.example.aspro.ui.dashboard.pesticide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.databinding.ItemPesticideBinding
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale

class PesticideAdapter(
    private var pesticides: MutableList<Pesticide>,
    private val onUpdateClicked: (Pesticide) -> Unit,
    private val onDeleteClicked: (Pesticide) -> Unit
) : RecyclerView.Adapter<PesticideAdapter.PesticideViewHolder>(), Filterable {

    private var pesticidesFull: List<Pesticide> = ArrayList(pesticides)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesticideViewHolder {
        val binding = ItemPesticideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PesticideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PesticideViewHolder, position: Int) {
        val pesticide = pesticides[position]
        holder.bind(pesticide)
        holder.binding.updateButton.setOnClickListener { onUpdateClicked(pesticide) }
        holder.binding.deleteButton.setOnClickListener { onDeleteClicked(pesticide) }
    }

    override fun getItemCount(): Int {
        return pesticides.size
    }

    fun updateData(newPesticides: List<Pesticide>) {
        pesticides.clear()
        pesticides.addAll(newPesticides)
        pesticidesFull = ArrayList(newPesticides)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return pesticideFilter
    }

    private val pesticideFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Pesticide>()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(pesticidesFull)
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()

                for (item in pesticidesFull) {
                    if (item.name.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.company.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.literKilogram.toString().contains(filterPattern) ||
                        item.numberOfPackets.toString().contains(filterPattern) ||
                        item.pricePerPacking.toString().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            pesticides.clear()
            pesticides.addAll(results?.values as List<Pesticide>)
            notifyDataSetChanged()
        }
    }

    inner class PesticideViewHolder(val binding: ItemPesticideBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pesticide: Pesticide) {
            binding.pesticideName.text = pesticide.name
            binding.company.text = "Company: ${pesticide.company}"
            binding.literKilogram.text = "Liters/Kilograms: ${pesticide.literKilogram}"
            binding.numberOfPackets.text = "Packets: ${pesticide.numberOfPackets}"
            binding.pricePerPacking.text = "Price: $${pesticide.pricePerPacking}"
        }
    }
}
