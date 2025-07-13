package com.sgvas21.ddadi21.messengerapp.domain.usecase.chat

import com.sgvas21.ddadi21.messengerapp.data.model.Chat
import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepository
import javax.inject.Inject

/**
 * Use case for retrieving a specific chat by its ID.
 *
 * This use case is used when you want to check if a conversation already exists
 * between two users based on the known chat ID format.
 *
 * @param chatRepository Repository that handles chat data operations.
 */
class GetChatByIdUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    /**
     * Retrieves a chat by its unique chat ID.
     *
     * @param chatId The unique identifier of the chat (e.g., "user1_user2").
     * @return The [Chat] object if found, or null if the chat does not exist.
     *
     * @throws IllegalArgumentException if the chatId is blank.
     */
    suspend operator fun invoke(chatId: String): Chat? {
        if (chatId.isBlank()) {
            throw IllegalArgumentException("Chat ID cannot be blank.")
        }

        return chatRepository.getChatById(chatId)
    }
}