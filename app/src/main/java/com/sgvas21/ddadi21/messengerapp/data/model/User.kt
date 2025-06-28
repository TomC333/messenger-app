package com.sgvas21.ddadi21.messengerapp.data.model

/**
 * Represents a registered user in the application.
 *
 * @property username Unique identifier for the user, This replaces a separate user ID and must be globally unique.
 * @property salt A unique, securely generated random string used to salt the password before hashing it.
 * @property password Secure, salted and hashed version of the user's password.
 * @property profession User's profession or role, e.g developer, designer. p.s users can enter anything here.
 * @property profileImageUrl Public URL of the profile picture stored in Firebase Storage.
 * @property timestamp Account creation time in milliseconds.
 *
 */
data class User (
    val username: String = "",
    val salt: String = "",
    val password: String = "",
    val profession: String = "",
    val profileImageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)