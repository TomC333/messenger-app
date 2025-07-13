package com.sgvas21.ddadi21.messengerapp.ui.chat

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgvas21.ddadi21.messengerapp.data.model.ChatPreview
import com.sgvas21.ddadi21.messengerapp.domain.usecase.chat.GetChatsForUserUseCase
import com.sgvas21.ddadi21.messengerapp.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainChatsViewModel @Inject constructor(
    private val getChatsForUserUseCase: GetChatsForUserUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _chatPreviews = MutableLiveData<List<ChatPreview>>()
    val chatPreviews: LiveData<List<ChatPreview>> = _chatPreviews

    fun loadChats(currentUser: String) {
        viewModelScope.launch {
            getChatsForUserUseCase(currentUser).collect { chats ->
                val otherUsernames = chats.mapNotNull { chat ->
                    val participants = chat.chatId.split('_')
                    participants.firstOrNull { it != currentUser && it.isNotBlank() }
                }.distinct()

                if (otherUsernames.isEmpty()) {
                    _chatPreviews.value = emptyList()
                    return@collect
                }

                val userProfiles = otherUsernames.mapNotNull { username ->
                    try {
                        getUserUseCase(username)
                    } catch (_: Exception) {
                        null
                    }
                }

                val userImagesMap = userProfiles.associateBy({ it.username }, { it.profileImageUrl })

                val previews = chats.mapNotNull { chat ->
                    val otherUser = chat.chatId.split('_').firstOrNull { it != currentUser }
                    if (otherUser != null && otherUser.isNotBlank()) {
                        ChatPreview(
                            chat = chat,
                            otherUserName = otherUser,
                            otherUserImageUrl = userImagesMap[otherUser].orEmpty()
                        )
                    } else null
                }

                _chatPreviews.value = previews
            }
        }
    }
}
