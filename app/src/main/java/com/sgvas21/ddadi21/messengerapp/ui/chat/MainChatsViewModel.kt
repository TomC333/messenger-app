package com.sgvas21.ddadi21.messengerapp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgvas21.ddadi21.messengerapp.data.model.Chat
import com.sgvas21.ddadi21.messengerapp.data.model.ChatPreview
import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepository
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainChatsViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _chatPreviews = MutableLiveData<List<ChatPreview>>()
    val chatPreviews: LiveData<List<ChatPreview>> = _chatPreviews

    private val _filteredChatPreviews = MutableLiveData<List<ChatPreview>>()
    val filteredChatPreviews: LiveData<List<ChatPreview>> = _filteredChatPreviews

    private var currentSearchQuery = ""
    private var allChatPreviews = emptyList<ChatPreview>()

    fun loadChats(username: String) {
        viewModelScope.launch {
            chatRepository.getChatsForUser(username)
                .catch { e ->
                    // Handle error
                }
                .collect { chats ->
                    val previews = chats.mapNotNull { chat ->
                        val otherUsername = chat.participants.firstOrNull { it != username }

                        if (otherUsername.isNullOrEmpty()) {
                            return@mapNotNull null
                        }

                        try {
                            val otherUser = userRepository.getUser(otherUsername)
                            ChatPreview(
                                chat = chat,
                                otherUserName = otherUsername,
                                otherUserImageUrl = otherUser?.profileImageUrl ?: ""
                            )
                        } catch (e: Exception) {
                            ChatPreview(
                                chat = chat,
                                otherUserName = otherUsername,
                                otherUserImageUrl = ""
                            )
                        }
                    }

                    allChatPreviews = previews
                    _chatPreviews.value = previews

                    // Apply current search filter
                    filterChats(currentSearchQuery)
                }
        }
    }

    fun filterChats(query: String) {
        currentSearchQuery = query

        val filteredList = if (query.isEmpty() || query.length < 3) {
            allChatPreviews
        } else {
            allChatPreviews.filter { chatPreview ->
                chatPreview.otherUserName.contains(query, ignoreCase = true)
            }
        }

        _filteredChatPreviews.value = filteredList
    }
}