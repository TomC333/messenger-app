package com.sgvas21.ddadi21.messengerapp.ui.mainScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sgvas21.ddadi21.messengerapp.adapters.UserAdapter
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchFunctionality()
        observeViewModel()

        binding.backButton.setOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
    }

    private fun setupSearchFunctionality() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setSearchQuery(text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults
            .onEach { users ->
                userAdapter.submitList(users)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
