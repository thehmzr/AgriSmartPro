package com.example.aspro.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.aspro.DatabaseHelper
import com.example.aspro.databinding.ActivityCropFormBinding
import java.text.SimpleDateFormat
import java.util.*

class CropFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropFormBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var cropId: Int = -1
    private var availableStock: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        setupSpinners()

        cropId = intent.getIntExtra("EXTRA_CROP_ID", -1)
        if (cropId != -1) {
            loadCropData(cropId)
        }

        binding.sowingDate.setOnClickListener {
            showDatePicker()
        }

        binding.seedQuantity.addTextChangedListener {
            validateSeedQuantity()
        }

        binding.btnSave.setOnClickListener {
            saveCrop()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        val landUnits = arrayOf("Select", "Canals", "Acres", "Hectares")
        val landUnitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, landUnits)
        landUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.landUnitSpinner.adapter = landUnitAdapter

        val seeds = arrayListOf("Select") + databaseHelper.getAllSeeds().map { it.name }
        val seedAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, seeds)
        seedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.seedSpinner.adapter = seedAdapter

        binding.seedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                updateAvailableStock()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                return i
            }
        }
        return 0
    }

    private fun loadCropData(id: Int) {
        val crop = databaseHelper.getCropById(id)
        binding.plotNumber.setText(crop.plotNumber)
        binding.landInPlot.setText(crop.landInPlot)
        binding.sowingDate.setText(crop.sowingDate)
        binding.seedQuantity.setText(crop.seedQuantity.toString())
        binding.landPreparationExpenses.setText(crop.landPreparationExpenses.toString())

        // Set the spinner selections after the data is loaded
        binding.landUnitSpinner.setSelection(getIndex(binding.landUnitSpinner, crop.landUnit))
        binding.seedSpinner.setSelection(getIndex(binding.seedSpinner, crop.seed))
        updateAvailableStock()
    }

    private fun updateAvailableStock() {
        val selectedSeed = binding.seedSpinner.selectedItem.toString()
        if (selectedSeed != "Select") {
            val seed = databaseHelper.getSeedByName(selectedSeed)
            seed?.let {
                availableStock = it.packingWeight
                binding.availableStock.text = "Available In Stock: $availableStock"
                validateSeedQuantity()  // Ensure validation is updated whenever the seed is changed
            }
        } else {
            binding.availableStock.text = "Available In Stock: 0"
            binding.errorMessage.visibility = View.GONE
        }
    }

    private fun validateSeedQuantity() {
        val seedQuantity = binding.seedQuantity.text.toString().toDoubleOrNull()
        if (seedQuantity != null && seedQuantity > availableStock) {
            binding.errorMessage.visibility = View.VISIBLE
        } else {
            binding.errorMessage.visibility = View.GONE
        }

        // Update the displayed available stock dynamically
        val remainingStock = availableStock - (seedQuantity ?: 0.0)
        binding.availableStock.text = "Available In Stock: ${if (remainingStock < 0) 0 else remainingStock}"
    }

    private fun saveCrop() {
        val plotNumber = binding.plotNumber.text.toString()
        val landInPlot = binding.landInPlot.text.toString()
        val landUnit = binding.landUnitSpinner.selectedItem.toString()
        val sowingDate = binding.sowingDate.text.toString()
        val seed = binding.seedSpinner.selectedItem.toString()
        val seedQuantity = binding.seedQuantity.text.toString().toDoubleOrNull()
        val landPreparationExpenses = binding.landPreparationExpenses.text.toString().toDoubleOrNull()

        if (plotNumber.isNotEmpty() && landInPlot.isNotEmpty() && sowingDate.isNotEmpty() && seedQuantity != null && landPreparationExpenses != null) {
            if (landUnit == "Select") {
                showToast("Please select a valid land unit")
                return
            }
            if (seed == "Select") {
                showToast("Please select a valid seed")
                return
            }
            if (seedQuantity > availableStock) {
                showToast("Seed quantity exceeds available stock")
                return
            }

            val id: Long = if (cropId != -1) {
                databaseHelper.updateCrop(cropId, plotNumber, landInPlot, landUnit, sowingDate, seed, seedQuantity, landPreparationExpenses)
                cropId.toLong()
            } else {
                databaseHelper.insertCrop(plotNumber, landInPlot, landUnit, sowingDate, seed, seedQuantity, landPreparationExpenses)
            }

            if (id > -1) {
                updateSeedQuantity(seed, seedQuantity)
                showToast("Crop saved successfully")
                finish()
            } else {
                showToast("Error saving crop")
            }
        } else {
            showToast("Please fill all fields")
        }
    }

    private fun updateSeedQuantity(seedName: String, quantityUsed: Double) {
        val seed = databaseHelper.getSeedByName(seedName)
        seed?.let {
            val newQuantity = it.packingWeight - quantityUsed
            databaseHelper.updateSeedQuantity(it.id, newQuantity)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                binding.sowingDate.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.show()
    }
}
