package com.sgvas21.ddadi21.messengerapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.adapters.MainPagerAdapter
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentMainBinding
import com.sgvas21.ddadi21.messengerapp.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupBottomNavigation()
        setupFabAdd()
    }

    private fun setupFabAdd() {
        binding.fabAdd.setOnClickListener {
            val searchFragment = SearchFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, searchFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun setupViewPager() {
        pagerAdapter = MainPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavigationView.selectedItemId = R.id.nav_home
                    1 -> binding.bottomNavigationView.selectedItemId = R.id.nav_profile
                }
            }
        })
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    binding.viewPager.currentItem = 0
                    true
                }
                R.id.nav_profile -> {
                    binding.viewPager.currentItem = 1
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
