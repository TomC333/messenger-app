package com.sgvas21.ddadi21.messengerapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sgvas21.ddadi21.messengerapp.ui.mainScreens.MainChatsFragment
import com.sgvas21.ddadi21.messengerapp.ui.mainScreens.ProfileFragment

class MainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainChatsFragment()
            1 -> ProfileFragment()
            else -> MainChatsFragment()
        }
    }
}