package com.example.aspro.ui.dashboard.fertilizer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.databinding.ItemFertilizerBinding
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale

class FertilizerAdapter(
    private var fertilizers: MutableList<Fertilizer>,
    private val onUpdateClicked: (Fertilizer) -> Unit,
    private val onDeleteClicked: (Fertilizer) -> Unit
) : RecyclerView.Adapter<FertilizerAdapter.FertilizerViewHolder>(), Filterable {

    private var fertilizersFull: List<Fertilizer> = ArrayList(fertilizers)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FertilizerViewHolder {
        val binding = ItemFertilizerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FertilizerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FertilizerViewHolder, position: Int) {
        val fertilizer = fertilizers[position]
        holder.bind(fertilizer)
        holder.binding.updateButton.setOnClickListener { onUpdateClicked(fertilizer) }
        holder.binding.deleteButton.setOnClickListener { onDeleteClicked(fertilizer) }
    }

    override fun getItemCount(): Int {
        return fertilizers.size
    }

    fun updateData(newFertilizers: List<Fertilizer>) {
        fertilizers.clear()
        fertilizers.addAll(newFertilizers)
        fertilizersFull = ArrayList(newFertilizers)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return fertilizerFilter
    }

    private val fertilizerFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Fertilizer>()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(fertilizersFull)
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()

                for (item in fertilizersFull) {
                    if (item.name.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.company.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.packingWeight.toString().contains(filterPattern) ||
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
            fertilizers.clear()
            fertilizers.addAll(results?.values as List<Fertilizer>)
            notifyDataSetChanged()
        }
    }

    inner class FertilizerViewHolder(val binding: ItemFertilizerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fertilizer: Fertilizer) {
            binding.fertilizerName.text = fertilizer.name
            binding.company.text = "Company: ${fertilizer.company}"
            binding.packingWeight.text = "Weight: ${fertilizer.packingWeight} Kgs"
            binding.numberOfPackets.text = "Packets: ${fertilizer.numberOfPackets}"
            binding.pricePerPacking.text = "Price: $${fertilizer.pricePerPacking}"
        }
    }
}
