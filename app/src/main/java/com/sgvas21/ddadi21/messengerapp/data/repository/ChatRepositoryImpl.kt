package com.sgvas21.ddadi21.messengerapp.data.repository

import com.sgvas21.ddadi21.messengerapp.data.model.Chat
import com.sgvas21.ddadi21.messengerapp.data.model.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {

    private fun createChatId(userA: String, userB: String): String =
        listOf(userA, userB).sorted().joinToString("_")

    override suspend fun sendMessage(sender: String, receiver: String, message: String) {
        val chatId = createChatId(sender, receiver)
        val chatRef = firestore.collection("chats").document(chatId)
        val messagesRef = chatRef.collection("messages")

        val chatMessage = ChatMessage(
            senderUsername = sender,
            message = message,
            timestamp = System.currentTimeMillis()
        )

        messagesRef.add(chatMessage).await()

        val chatData = mapOf(
            "participants" to listOf(sender, receiver),
            "lastMessage" to message,
            "lastMessageTimestamp" to chatMessage.timestamp
        )
        chatRef.set(chatData, SetOptions.merge()).await()
    }

    override fun getMessages(chatId: String): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { it.toObject(ChatMessage::class.java) } ?: emptyList()
                trySend(messages).isSuccess
            }
        awaitClose { subscription.remove() }
    }

    override fun getChatsForUser(username: String): Flow<List<Chat>> = callbackFlow {
        val subscription = firestore.collection("chats")
            .whereArrayContains("participants", username)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val chats = snapshot?.documents?.mapNotNull { it.toObject(Chat::class.java) } ?: emptyList()
                trySend(chats).isSuccess
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getChatById(chatId: String): Chat? {
        return try {
            val snapshot = firestore.collection("chats").document(chatId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(Chat::class.java)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun createChat(chatId: String, participants: List<String>) {
        val chatRef = firestore.collection("chats").document(chatId)

        val chatData = mapOf(
            "chatId" to chatId,
            "participants" to participants,
            "lastMessage" to "",
            "lastMessageTimestamp" to System.currentTimeMillis()
        )

        chatRef.set(chatData, SetOptions.merge()).await()
    }
}