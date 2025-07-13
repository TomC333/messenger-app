package com.sgvas21.ddadi21.messengerapp.domain.usecase.chat

import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepository
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Creates a new chat document between two users.
     *
     * @param user1 First participant.
     * @param user2 Second participant.
     * @return The generated chatId.
     */
    suspend operator fun invoke(user1: String, user2: String): String {
        val chatId = listOf(user1, user2).sorted().joinToString("_")
        chatRepository.createChat(chatId, listOf(user1, user2))
        return chatId
    }
}