package com.sgvas21.ddadi21.messengerapp.domain.usecase.chat

import com.sgvas21.ddadi21.messengerapp.data.model.ChatMessage
import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Returns a flow of [ChatMessage]s for a given chat ID.
     *
     * @param chatId The unique identifier of the chat.
     * @return Flow of message list that updates in real-time.
     *
     * @throws Exception if chatId is blank or repository fails.
     */
    operator fun invoke(chatId: String): Flow<List<ChatMessage>> {
        if (chatId.isBlank()) {
            throw IllegalArgumentException("Chat ID cannot be blank.")
        }

        return chatRepository.getMessages(chatId)
    }
}