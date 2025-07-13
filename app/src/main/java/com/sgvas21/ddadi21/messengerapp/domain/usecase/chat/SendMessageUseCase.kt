package com.sgvas21.ddadi21.messengerapp.domain.usecase.chat

import com.sgvas21.ddadi21.messengerapp.data.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Sends a message from sender to receiver.
     *
     * @param senderUsername The username of the sender.
     * @param receiverUsername The username of the receiver.
     * @param message The message content.
     *
     * @throws IllegalArgumentException if any field is blank.
     * @throws Exception if the repository operation fails.
     */
    suspend operator fun invoke(senderUsername: String, receiverUsername: String, message: String) {
        if (senderUsername.isBlank() || receiverUsername.isBlank() || message.isBlank()) {
            throw IllegalArgumentException("Sender, receiver, and message cannot be blank.")
        }

        chatRepository.sendMessage(senderUsername, receiverUsername, message)
    }
}
