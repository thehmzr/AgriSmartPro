package com.example.aspro.ui.dashboard.fertilizer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aspro.DatabaseHelper
import com.example.aspro.databinding.ActivityFertilizerFormBinding

class FertilizerFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFertilizerFormBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var fertilizerId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFertilizerFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        fertilizerId = intent.getIntExtra("EXTRA_FERTILIZER_ID", -1)
        if (fertilizerId != -1) {
            loadFertilizerData(fertilizerId!!)
        }

        binding.btnSave.setOnClickListener {
            saveFertilizer()
        }
    }

    private fun loadFertilizerData(id: Int) {
        val fertilizer = databaseHelper.getFertilizerById(id)
        binding.fertilizerName.setText(fertilizer.name)
        binding.company.setText(fertilizer.company)
        binding.packingWeight.setText(fertilizer.packingWeight.toString())
        binding.numberOfPackets.setText(fertilizer.numberOfPackets.toString())
        binding.pricePerPacking.setText(fertilizer.pricePerPacking.toString())
    }

    private fun saveFertilizer() {
        val name = binding.fertilizerName.text.toString()
        val company = binding.company.text.toString()
        val packingWeight = binding.packingWeight.text.toString().toDoubleOrNull()
        val numberOfPackets = binding.numberOfPackets.text.toString().toIntOrNull()
        val pricePerPacking = binding.pricePerPacking.text.toString().toDoubleOrNull()

        if (name.isNotEmpty() && company.isNotEmpty() && packingWeight != null && numberOfPackets != null && pricePerPacking != null) {
            val id: Long = if (fertilizerId != null && fertilizerId != -1) {
                databaseHelper.updateFertilizer(fertilizerId!!, name, company, packingWeight, numberOfPackets, pricePerPacking)
                fertilizerId!!.toLong() // Ensuring compatibility in type
            } else {
                databaseHelper.insertFertilizer(name, company, packingWeight, numberOfPackets, pricePerPacking)
            }


            if (id > -1) {
                Toast.makeText(this, "Fertilizer saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error saving fertilizer", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

}
