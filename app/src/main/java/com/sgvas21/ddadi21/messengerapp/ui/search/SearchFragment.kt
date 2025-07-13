package com.sgvas21.ddadi21.messengerapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.ui.chat.ChatFragment
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager
import kotlinx.coroutines.launch

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
        userAdapter = UserAdapter { selectedUser ->
            openChatWithUser(selectedUser)
        }

        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
    }

    private fun openChatWithUser(user: User) {
        val currentUsername = SessionManager.getUsername(requireContext())
            ?: return Toast.makeText(requireContext(), "You must be signed in.", Toast.LENGTH_SHORT).show()

        viewLifecycleOwner.lifecycleScope.launch {
            val chatId = viewModel.getOrCreateChatId(currentUsername, user.username)

            val chatFragment = ChatFragment.newInstance(
                chatId = chatId,
                currentUser = currentUsername,
                otherUser = user.username,
                otherUserProfileUrl = user.profileImageUrl
            )

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, chatFragment)
                .addToBackStack(null)
                .commit()
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
