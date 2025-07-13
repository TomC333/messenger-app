package com.sgvas21.ddadi21.messengerapp.data.repository

import com.sgvas21.ddadi21.messengerapp.data.model.Chat
import com.sgvas21.ddadi21.messengerapp.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    /**
     * Sends a message from sender to receiver
     *
     * @param sender The username of the sender.
     * @param receiver The username of the receiver.
     * @throws Exception if operation fails due to network issues or database errors.
     */
    suspend fun sendMessage(sender: String, receiver: String, message: String)

    /**
     * Gets all the messages under received chatId
     *
     * @param chatId The unique chatId
     * @return A cold flow of chat messages for the given chatId, ordered by timestamp ascending, Emits new lists every time messages update.
     */
    fun getMessages(chatId: String): Flow<List<ChatMessage>>

    /**
     * Gets all the chats for specific user
     *
     * @param username the unique username.
     * @return a cold flow of chat messages for the give username, ordered by timestamp (of the last message) ascending, Emits new list every time new chat creates.
     */
    fun getChatsForUser(username: String): Flow<List<Chat>>

    /**
     * Gets a single chat document by its ID.
     *
     * @param chatId The unique chat identifier (e.g., "user1_user2").
     * @return The Chat object if found, or null if it doesn't exist.
     */
    suspend fun getChatById(chatId: String): Chat?

    /**
     * Creates a new chat
     *
     * @param chatId The unique identifier for the chat
     * @param participants The list of participants (currently it should contain 2 usernames)
     */
    suspend fun createChat(chatId: String, participants: List<String>)
}