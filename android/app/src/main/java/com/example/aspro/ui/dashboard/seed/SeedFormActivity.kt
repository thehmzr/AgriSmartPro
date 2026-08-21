package com.example.aspro.ui.dashboard.seed

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aspro.DatabaseHelper
import com.example.aspro.databinding.ActivitySeedFormBinding

class SeedFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeedFormBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var seedId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeedFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Check if the activity was started to update an existing seed
        seedId = intent.getIntExtra("EXTRA_SEED_ID", -1)
        if (seedId != -1) {
            loadSeedData(seedId)
        }

        binding.btnSave.setOnClickListener {
            saveSeed()
        }

        // Set the toolbar as the app bar for the activity
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadSeedData(id: Int) {
        val seed = databaseHelper.getSeedById(id)
        binding.seedName.setText(seed.name)
        binding.seedVariety.setText(seed.variety)
        binding.packingWeight.setText(seed.packingWeight.toString())
        binding.numberOfPackets.setText(seed.numberOfPackets.toString())
        binding.pricePerPacking.setText(seed.pricePerPacking.toString())
    }

    private fun saveSeed() {
        val name = binding.seedName.text.toString()
        val variety = binding.seedVariety.text.toString()
        val packingWeight = binding.packingWeight.text.toString().toDoubleOrNull()
        val numberOfPackets = binding.numberOfPackets.text.toString().toIntOrNull()
        val pricePerPacking = binding.pricePerPacking.text.toString().toDoubleOrNull()

        if (name.isNotEmpty() && variety.isNotEmpty() && packingWeight != null && numberOfPackets != null && pricePerPacking != null) {
            val id: Long = if (seedId != -1) {
                databaseHelper.updateSeed(seedId, name, variety, packingWeight, numberOfPackets, pricePerPacking)
                seedId.toLong() // Ensuring compatibility in type
            } else {
                databaseHelper.insertSeed(name, variety, packingWeight, numberOfPackets, pricePerPacking)
            }

            if (id > -1) {
                showToast("Seed saved successfully")
                finish()
            } else {
                showToast("Error saving seed")
            }
        } else {
            showToast("Please fill all fields")
        }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.show()
    }
}
