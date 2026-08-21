package com.example.aspro.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.example.aspro.databinding.FragmentDashboardBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up ViewPager2 and TabLayout
        val pagerAdapter = DashboardPagerAdapter(requireActivity())
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Seeds / بیج"
                1 -> "Fertilizers / کھاد"
                2 -> "Pesticides / ادویات"
                else -> "Seeds / بیج"
            }
        }.attach()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
