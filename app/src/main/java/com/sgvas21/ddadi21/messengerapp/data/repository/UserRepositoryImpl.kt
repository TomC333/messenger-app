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
}