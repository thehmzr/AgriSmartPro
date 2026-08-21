package com.example.aspro.ui.dashboard.pesticide

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aspro.DatabaseHelper
import com.example.aspro.R
import com.example.aspro.databinding.FragmentPesticideBinding

class PesticideFragment : Fragment() {

    private var _binding: FragmentPesticideBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var pesticideAdapter: PesticideAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPesticideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        pesticideAdapter = PesticideAdapter(databaseHelper.getAllPesticides().toMutableList(), ::onUpdateClicked, ::onDeleteClicked)
        binding.recyclerView.adapter = pesticideAdapter

        // Add Pesticide button
        binding.btnAddPesticide.setOnClickListener {
            val intent = Intent(activity, PesticideFormActivity::class.java)
            startActivity(intent)
        }

        // Load pesticide data
        loadPesticideData()

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pesticideAdapter.filter.filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun loadPesticideData() {
        val pesticideList = databaseHelper.getAllPesticides()
        pesticideAdapter.updateData(pesticideList)
    }

    private fun onUpdateClicked(pesticide: Pesticide) {
        val intent = Intent(activity, PesticideFormActivity::class.java)
        intent.putExtra("EXTRA_PESTICIDE_ID", pesticide.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(pesticide: Pesticide) {

        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this pesticide?")
            .setPositiveButton("Yes") { dialog, which ->
                databaseHelper.deletePesticide(pesticide.id)
                Toast.makeText(requireContext(), "Pesticide deleted successfully", Toast.LENGTH_SHORT).show()
                loadPesticideData()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadPesticideData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
