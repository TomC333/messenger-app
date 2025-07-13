package com.sgvas21.ddadi21.messengerapp.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgvas21.ddadi21.messengerapp.adapters.ChatAdapter
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    private lateinit var chatId: String
    private lateinit var currentUser: String
    private lateinit var otherUser: String
    private var otherUserProfileUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatId = it.getString("chatId") ?: error("Chat ID missing")
            currentUser = it.getString("currentUser") ?: error("Current user missing")
            otherUser = it.getString("otherUser") ?: error("Other user missing")
            otherUserProfileUrl = it.getString("otherUserProfileUrl")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupRecyclerView()
        setupMessageSending()
        observeMessages()

        viewModel.startListening(chatId)
    }

    private fun setupToolbar() {
        binding.tvChatTitle.text = otherUser
//        otherUserProfileUrl?.let {
//            Glide.with(requireContext())
//                .load(it)
//                .placeholder(R.drawable.avatar_image_placeholder)
//                .error(R.drawable.avatar_image_placeholder)
//                .circleCrop()
//                .into(binding.ivBack)
//        }

        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(currentUser)
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupMessageSending() {
        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(currentUser, otherUser, messageText)
                binding.etMessage.text.clear()
            } else {
                Toast.makeText(requireContext(), "Enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                chatAdapter.submitList(messages)
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            chatId: String,
            currentUser: String,
            otherUser: String,
            otherUserProfileUrl: String? = null
        ): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(
                    "chatId" to chatId,
                    "currentUser" to currentUser,
                    "otherUser" to otherUser,
                    "otherUserProfileUrl" to otherUserProfileUrl
                )
            }
        }
    }
}