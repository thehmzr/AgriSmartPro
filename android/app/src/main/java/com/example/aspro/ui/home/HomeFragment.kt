package com.example.aspro.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aspro.DatabaseHelper
import com.example.aspro.R
import com.example.aspro.SignInActivity
import com.example.aspro.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var cropAdapter: CropAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())
        auth = FirebaseAuth.getInstance()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cropAdapter = CropAdapter(databaseHelper.getAllCrops().toMutableList(), ::onUpdateClicked, ::onDeleteClicked)
        binding.recyclerView.adapter = cropAdapter

        binding.btnAddCrop.setOnClickListener {
            val intent = Intent(activity, CropFormActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        setHasOptionsMenu(false)
        return root
    }

    override fun onResume() {
        super.onResume()
        loadCropData()
        updateUI(auth.currentUser)
    }

    private fun loadCropData() {
        val cropList = databaseHelper.getAllCrops()
        cropAdapter.updateData(cropList)
    }

    private fun onUpdateClicked(crop: Crop) {
        val intent = Intent(activity, CropFormActivity::class.java)
        intent.putExtra("EXTRA_CROP_ID", crop.id)
        startActivity(intent)
    }

    private fun onDeleteClicked(crop: Crop) {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this crop?")
            .setPositiveButton("Yes") { _, _ ->
                databaseHelper.deleteCrop(crop.id)
                loadCropData()
                showToast("Crop deleted")
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.show()
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val greeting = when (currentTime) {
                in 3..11 -> "Good Morning,"
                in 12..16 -> "Good Afternoon,"
                in 17..19 -> "Good Evening,"
                else -> "Good Night,"
            }
            binding.greeting.text = greeting
            binding.username.text = "${user.displayName ?: "User"}"

            // Set current date
            val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            binding.date.text = currentDate

            // Set current time
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val currentTimeString = timeFormat.format(Date())
            binding.time.text = currentTimeString
        }
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(activity, SignInActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
