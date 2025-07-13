package com.sgvas21.ddadi21.messengerapp.domain.usecase.chat

import com.sgvas21.ddadi21.messengerapp.data.model.Chat
import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing all chat conversations of a user.
 *
 * This retrieves a live-updating list of chats the user is involved in,
 * including the latest message preview and other participant info.
 *
 * @param chatRepository Repository for chat-related operations.
 */
class GetChatsForUserUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Returns a flow of [Chat] objects for a given user.
     *
     * @param username The username of the user.
     * @return Flow of chat list that updates in real-time.
     *
     * @throws Exception if username is blank or repository fails.
     */
    operator fun invoke(username: String): Flow<List<Chat>> {
        if (username.isBlank()) {
            throw IllegalArgumentException("Username cannot be blank.")
        }

        return chatRepository.getChatsForUser(username)
    }
}