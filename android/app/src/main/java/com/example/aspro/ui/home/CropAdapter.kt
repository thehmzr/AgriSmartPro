package com.example.aspro.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.databinding.ItemCropBinding
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale

class CropAdapter(
    private var crops: MutableList<Crop>,
    private val onUpdateClicked: (Crop) -> Unit,
    private val onDeleteClicked: (Crop) -> Unit
) : RecyclerView.Adapter<CropAdapter.CropViewHolder>(), Filterable {

    private var cropsFull: List<Crop> = ArrayList(crops)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val binding = ItemCropBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CropViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        val crop = crops[position]
        holder.bind(crop)
        holder.binding.updateButton.setOnClickListener { onUpdateClicked(crop) }
        holder.binding.deleteButton.setOnClickListener { onDeleteClicked(crop) }
    }

    override fun getItemCount(): Int {
        return crops.size
    }

    fun updateData(newCrops: List<Crop>) {
        crops.clear()
        crops.addAll(newCrops)
        cropsFull = ArrayList(newCrops)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return cropFilter
    }

    private val cropFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Crop>()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(cropsFull)
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()

                for (item in cropsFull) {
                    if (item.plotNumber.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.landInPlot.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.landUnit.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.sowingDate.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.seed.lowercase(Locale.ROOT).contains(filterPattern) ||
                        item.seedQuantity.toString().contains(filterPattern) ||
                        item.landPreparationExpenses.toString().contains(filterPattern)) {
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
            crops.clear()
            crops.addAll(results?.values as List<Crop>)
            notifyDataSetChanged()
        }
    }

    inner class CropViewHolder(val binding: ItemCropBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crop: Crop) {
            binding.plotNumber.text = crop.plotNumber
            binding.landInPlot.text = "Land in Plot: ${crop.landInPlot} ${crop.landUnit}"
            binding.sowingDate.text = "Sowing Date: ${crop.sowingDate}"
            binding.seed.text = "Seed: ${crop.seed}"
            binding.seedQuantity.text = "Seed Quantity: ${crop.seedQuantity}"
            binding.landPreparationExpenses.text = "Land Preparation Expenses: ${crop.landPreparationExpenses}"
        }
    }
}
