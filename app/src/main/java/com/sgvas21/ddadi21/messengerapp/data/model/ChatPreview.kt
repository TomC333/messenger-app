package com.sgvas21.ddadi21.messengerapp.data.model

/**
 * Represents a chat preview model
 *
 * @property chat single chat object
 * @property otherUserName The senders username
 * @property otherUserImageUrl The senders image url
 */
data class ChatPreview(
    val chat: Chat,
    val otherUserName: String,
    val otherUserImageUrl: String
)