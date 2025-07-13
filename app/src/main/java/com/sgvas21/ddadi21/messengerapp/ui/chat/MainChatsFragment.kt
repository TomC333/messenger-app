package com.sgvas21.ddadi21.messengerapp.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.adapters.ChatPreviewAdapter
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentMainChatsBinding
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainChatsFragment : Fragment() {

    private var _binding: FragmentMainChatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainChatsViewModel by viewModels()
    private lateinit var chatPreviewAdapter: ChatPreviewAdapter

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

        val username = SessionManager.getUsername(requireContext()) ?: throw IllegalStateException("User was not logged in")

        viewModel.loadChats(username)
    }

    private fun setupRecyclerView() {
        val currentUsername = SessionManager.getUsername(requireContext())
            ?: return Toast.makeText(requireContext(), "You must be signed in.", Toast.LENGTH_SHORT).show()

        chatPreviewAdapter = ChatPreviewAdapter(
            onChatClick = { chatPreview ->
                val chatFragment = ChatFragment.newInstance(
                    chatId = chatPreview.chat.chatId,
                    currentUser = currentUsername,
                    otherUser = chatPreview.otherUserName,
                    otherUserProfileUrl = chatPreview.otherUserImageUrl
                )

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, chatFragment)
                    .addToBackStack(null)
                    .commit()
            }
        )

        binding.usersInfo.layoutManager = LinearLayoutManager(requireContext())
        binding.usersInfo.adapter = chatPreviewAdapter
    }

    private fun observeViewModel() {
        viewModel.chatPreviews.observe(viewLifecycleOwner) { previews ->
            chatPreviewAdapter.submitList(previews)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}