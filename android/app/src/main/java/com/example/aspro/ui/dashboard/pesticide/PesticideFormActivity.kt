package com.example.aspro.ui.dashboard.pesticide

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aspro.DatabaseHelper
import com.example.aspro.databinding.ActivityPesticideFormBinding

class PesticideFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPesticideFormBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var pesticideId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesticideFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        pesticideId = intent.getIntExtra("EXTRA_PESTICIDE_ID", -1)
        if (pesticideId != -1) {
            loadPesticideData(pesticideId)
        }

        binding.btnSave.setOnClickListener {
            savePesticide()
        }

        // Set the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadPesticideData(id: Int) {
        val pesticide = databaseHelper.getPesticideById(id)
        binding.pesticideName.setText(pesticide.name)
        binding.company.setText(pesticide.company)
        binding.literKilogram.setText(pesticide.literKilogram.toString())
        binding.numberOfPackets.setText(pesticide.numberOfPackets.toString())
        binding.pricePerPacking.setText(pesticide.pricePerPacking.toString())
    }

    private fun savePesticide() {
        val name = binding.pesticideName.text.toString()
        val company = binding.company.text.toString()
        val literKilogram = binding.literKilogram.text.toString().toDoubleOrNull()
        val numberOfPackets = binding.numberOfPackets.text.toString().toIntOrNull()
        val pricePerPacking = binding.pricePerPacking.text.toString().toDoubleOrNull()

        if (name.isNotEmpty() && company.isNotEmpty() && literKilogram != null && numberOfPackets != null && pricePerPacking != null) {
            val id: Long = if (pesticideId != -1) {
                databaseHelper.updatePesticide(pesticideId, name, company, literKilogram, numberOfPackets, pricePerPacking)
                pesticideId.toLong() // Ensuring compatibility in type
            } else {
                databaseHelper.insertPesticide(name, company, literKilogram, numberOfPackets, pricePerPacking)
            }

            if (id > -1) {
                Toast.makeText(this, "Pesticide saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error saving pesticide", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
