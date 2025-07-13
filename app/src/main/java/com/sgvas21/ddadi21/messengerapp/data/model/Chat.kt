package com.sgvas21.ddadi21.messengerapp.data.model


/**
 * Represents a single chat between two users.
 *
 * @property chatId Unique identifier for the chat (for simplicity we will use {user1}-{user2})
 * @property participants List of usernames participate in chat (hmm we can use Id for that, but I think it's not a best practice :D)
 * @property lastMessage The last message for the preview.
 * @property lastMessageTimestamp The last message receiving time, again for the preview.
 */
data class Chat(
    val chatId: String = "",
    val participants: List<String> = listOf(),
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = 0L
)