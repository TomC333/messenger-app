package com.sgvas21.ddadi21.messengerapp.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom


object PasswordUtils {

    /**
     * Generates a cryptographically secure random salt.
     *
     * @param length The length of the salt in bytes (default: 16).
     *
     * @return Base64 encoded salt string.
     */
    fun generateSalt(length: Int = 16): String {
        val random = SecureRandom()
        val salt = ByteArray(length)

        random.nextBytes(salt)
        return Base64.encodeToString(salt, Base64.DEFAULT)
    }

    /**
     * Generates a hash from password and salt using SHA-256.
     *
     * @param password The plain text password.
     * @param salt The salt (Base64 encoded string).
     *
     * @return Base64 encoded hash string.
     */
    fun generateHash(password: String, salt: String): String {
        val saltBytes = Base64.decode(salt, Base64.DEFAULT)
        val passwordBytes = password.toByteArray(Charsets.UTF_8)

        val combined = passwordBytes + saltBytes

        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(combined)

        return Base64.encodeToString(hash, Base64.DEFAULT)
    }

    /**
     * Convenience function to verify a password against a store hash.
     *
     * @param password The plain text password to verify.
     * @param salt The salt used during original hashing.
     * @param storedHash The stored hash to compare against.
     *
     * @return true if password matches, false otherwise.
     */
    fun verifyPassword(password: String, salt: String, storedHash: String): Boolean {
        return generateHash(password, salt) == storedHash
    }
}