package com.example.aspro.ui.dashboard.seed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.databinding.ItemSeedBinding
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale

class SeedAdapter(
    private var seeds: MutableList<Seed>,
    private val onUpdateClicked: (Seed) -> Unit,
    private val onDeleteClicked: (Seed) -> Unit
) : RecyclerView.Adapter<SeedAdapter.SeedViewHolder>(), Filterable {

    private var seedsFull: List<Seed> = ArrayList(seeds)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeedViewHolder {
        val binding = ItemSeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeedViewHolder, position: Int) {
        val seed = seeds[position]
        holder.bind(seed)
        holder.binding.updateButton.setOnClickListener { onUpdateClicked(seed) }
        holder.binding.deleteButton.setOnClickListener { onDeleteClicked(seed) }
    }

    override fun getItemCount(): Int {
        return seeds.size
    }

    fun updateData(newSeeds: List<Seed>) {
        seeds.clear()
        seeds.addAll(newSeeds)
        seedsFull = ArrayList(newSeeds)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return seedFilter
    }

    private val seedFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Seed>()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(seedsFull)
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()

                for (item in seedsFull) {
                    if (item.name.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.variety.lowercase(Locale.ROOT).contains(filterPattern) ||
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
            seeds.clear()
            seeds.addAll(results?.values as List<Seed>)
            notifyDataSetChanged()
        }
    }

    inner class SeedViewHolder(val binding: ItemSeedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seed: Seed) {
            binding.seedName.text = seed.name
            binding.variety.text = "Variety: ${seed.variety}"
            binding.packingWeight.text = "Packing Weight: ${seed.packingWeight} Kgs"
            binding.numberOfPackets.text = "Number of Packets: ${seed.numberOfPackets}"
            binding.pricePerPacking.text = "Price per Packing: $${seed.pricePerPacking}"
        }
    }
}
