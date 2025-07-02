package com.sgvas21.ddadi21.messengerapp.ui.mainScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentMainChatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainChatsFragment : Fragment() {
    private var _binding: FragmentMainChatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainChatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
    }

    private fun observeViewModel() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
