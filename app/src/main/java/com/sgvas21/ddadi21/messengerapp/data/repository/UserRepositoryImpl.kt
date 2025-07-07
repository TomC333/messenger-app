package com.sgvas21.ddadi21.messengerapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sgvas21.ddadi21.messengerapp.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    val firestore: FirebaseFirestore
): UserRepository {

    private val userCollection = firestore.collection("users")
    private val usernameCollection = firestore.collection("usernames")

    override suspend fun addUser(user: User) {
        val usernameDoc = usernameCollection.document(user.username)
        val userDoc = userCollection.document(user.username)

        firestore.runTransaction { transaction ->
            val usernameSnapshot = transaction.get(usernameDoc)
            if (usernameSnapshot.exists()) {
                throw Exception("Username '${user.username}' is already taken.")
            }

            transaction.set(usernameDoc, mapOf("reserved" to true))
            transaction.set(userDoc, user)
        }.await()
    }

    override suspend fun getUser(username: String): User? {
        val snapshot = userCollection.document(username).get().await()
        return snapshot.toObject(User::class.java)
    }

    override suspend fun updateUser(user: User)  {
        val userDoc = userCollection.document(user.username)
        val snapshot = userDoc.get().await()

        if(!snapshot.exists()) {
            throw Exception("User with username '${user.username}' does not exists")
        }

        userDoc.set(user).await()
    }

    override suspend fun usernameExists(username: String): Boolean {
        return try {
            val user = getUser(username)
            user != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteUser(username: String) {
        val usernameDoc = usernameCollection.document(username)
        val userDoc = userCollection.document(username)

        firestore.runTransaction { transaction ->
            val userSnapshot = transaction.get(userDoc)
            if (!userSnapshot.exists()) {
                throw Exception("User with username '$username' does not exist.")
            }

            transaction.delete(userDoc)
            transaction.delete(usernameDoc)
        }.await()
    }

    /**
     * Retrieves all user profiles from the database.
     * Fetches all documents from the "users" collection.
     *
     * @return A list of all user objects.
     * @throws Exception on failure to retrieve data.
     */
    override suspend fun getAllUsers(): List<User> {
        return try {
            val querySnapshot = userCollection.get().await()
            querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Searches for user profiles whose usernames contain the given query string.
     *
     * IMPORTANT: Firestore does not support "contains" queries directly.
     * This implementation fetches ALL users and filters them in memory.
     * For large datasets, consider a dedicated search service (e.g., Algolia).
     *
     * @param query The search string.
     * @return A list of [User] objects matching the search query.
     * @throws Exception on failure to retrieve data.
     */
    override suspend fun searchUsers(query: String): List<User> {
        return try {
            if (query.isBlank()) {
                return getAllUsers()
            }

            val lowerCaseQuery = query.lowercase()
            val allUsers = getAllUsers()

            allUsers.filter { it.username.lowercase().contains(lowerCaseQuery) }
        } catch (e: Exception) {
            emptyList()
        }
    }

}