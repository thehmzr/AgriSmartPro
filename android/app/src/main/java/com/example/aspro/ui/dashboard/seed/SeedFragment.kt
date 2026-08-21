package com.example.aspro.ui.dashboard.seed

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
import com.example.aspro.databinding.FragmentSeedBinding

class SeedFragment : Fragment() {

    private var _binding: FragmentSeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var seedAdapter: SeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        seedAdapter = SeedAdapter(databaseHelper.getAllSeeds().toMutableList(), ::onUpdateClicked, ::onDeleteClicked)
        binding.recyclerView.adapter = seedAdapter

        binding.btnAddSeed.setOnClickListener {
            val intent = Intent(activity, SeedFormActivity::class.java)
            startActivity(intent)
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onResume() {
        super.onResume()
        loadSeedData()
    }

    private fun loadSeedData() {
        val seedList = databaseHelper.getAllSeeds()
        seedAdapter.updateData(seedList)
    }

    private fun onUpdateClicked(seed: Seed) {
        val intent = Intent(activity, SeedFormActivity::class.java)
        intent.putExtra("EXTRA_SEED_ID", seed.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(seed: Seed) {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this seed?")
            .setPositiveButton("Yes") { _, _ ->
                databaseHelper.deleteSeed(seed.id)
                loadSeedData()
                showToast("Seed deleted")
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.show()
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
                seedAdapter.filter.filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
