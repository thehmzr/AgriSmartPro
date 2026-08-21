package com.example.aspro.ui.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// Import fragments from their specific folders
import com.example.aspro.ui.dashboard.seed.SeedFragment
import com.example.aspro.ui.dashboard.fertilizer.FertilizerFragment
import com.example.aspro.ui.dashboard.pesticide.PesticideFragment

class DashboardPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SeedFragment()
            1 -> FertilizerFragment()
            2 -> PesticideFragment()
            else -> SeedFragment()
        }
    }
}
