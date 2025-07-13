package com.sgvas21.ddadi21.messengerapp.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgvas21.ddadi21.messengerapp.data.model.ChatMessage
import com.sgvas21.ddadi21.messengerapp.domain.usecase.chat.GetMessagesUseCase
import com.sgvas21.ddadi21.messengerapp.domain.usecase.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun startListening(chatId: String) {
        viewModelScope.launch {
            getMessagesUseCase(chatId).collect { newMessages ->
                _messages.value = newMessages
            }
        }
    }

    fun sendMessage(senderUsername: String, receiverUsername: String, message: String) {
        viewModelScope.launch {
            sendMessageUseCase(senderUsername, receiverUsername, message)
        }
    }
}
