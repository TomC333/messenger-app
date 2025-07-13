package com.sgvas21.ddadi21.messengerapp.data.model

/**
 * Represents a single message in the chat.
 *
 * @property senderUsername Simply the sender :D
 * @property message Simply the message again :D
 * @property timestamp Time when message was sent
 *
 */
data class ChatMessage(
    val senderUsername: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
)