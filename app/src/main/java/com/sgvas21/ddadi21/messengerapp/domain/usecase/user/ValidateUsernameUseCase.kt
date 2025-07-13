package com.sgvas21.ddadi21.messengerapp.domain.usecase.user

import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import javax.inject.Inject

class ValidateUsernameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Validates if a username is available for use.
     *
     * @param newUsername The username to validate.
     * @param currentUsername The current username of the user (to allow keeping the same username).
     * @return true if the username is valid and available, false otherwise.
     * @throws IllegalArgumentException if the username is invalid.
     */
    suspend operator fun invoke(newUsername: String, currentUsername: String) {
        if (newUsername.isBlank()) {
            throw IllegalArgumentException("Username cannot be blank.")
        }

        if (newUsername.length < 3) {
            throw IllegalArgumentException("Username must be at least 3 characters long.")
        }

        if (newUsername.length > 20) {
            throw IllegalArgumentException("Username cannot be longer than 20 characters.")
        }

        val usernamePattern = "^[a-zA-Z0-9_]+$".toRegex()
        if (!newUsername.matches(usernamePattern)) {
            throw IllegalArgumentException("Username can only contain letters, numbers, and underscores.")
        }

        if (newUsername == currentUsername) {
            return
        }

        val exists = userRepository.usernameExists(newUsername)
        if (exists) {
            throw IllegalArgumentException("Username '$newUsername' is already taken.")
        }
    }
}