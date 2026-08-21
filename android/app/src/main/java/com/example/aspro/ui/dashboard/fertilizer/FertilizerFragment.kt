package com.example.aspro.ui.dashboard.fertilizer

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
import com.example.aspro.databinding.FragmentFertilizerBinding

class FertilizerFragment : Fragment() {

    private var _binding: FragmentFertilizerBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var fertilizerAdapter: FertilizerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFertilizerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fertilizerAdapter = FertilizerAdapter(databaseHelper.getAllFertilizers().toMutableList(), ::onUpdateClicked, ::onDeleteClicked)
        binding.recyclerView.adapter = fertilizerAdapter

        // Add Fertilizer
        binding.btnAddFertilizer.setOnClickListener {
            val intent = Intent(activity, FertilizerFormActivity::class.java)
            startActivity(intent)
        }

        // Load fertilizer data
        loadFertilizerData()

        setHasOptionsMenu(true) // Enable menu

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
                fertilizerAdapter.filter.filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun loadFertilizerData() {
        val fertilizerList = databaseHelper.getAllFertilizers()
        fertilizerAdapter.updateData(fertilizerList)
    }

    private fun onUpdateClicked(fertilizer: Fertilizer) {
        val intent = Intent(activity, FertilizerFormActivity::class.java)
        intent.putExtra("EXTRA_FERTILIZER_ID", fertilizer.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(fertilizer: Fertilizer) {
        // Show confirmation dialog
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this fertilizer?")
            .setPositiveButton("Yes") { dialog, which ->
                databaseHelper.deleteFertilizer(fertilizer.id)
                Toast.makeText(requireContext(), "Fertilizer deleted successfully", Toast.LENGTH_SHORT).show()
                loadFertilizerData()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadFertilizerData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
