package com.sgvas21.ddadi21.messengerapp.domain.usecase

import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Retrieves a user profile by username.
     *
     * @param username The username of the profile to retrieve.
     * @return The user object if found, or null if not found.
     * @throws Exception on failure to retrieve data.
     */
    suspend operator fun invoke(username: String): User? {
        if (username.isBlank()) {
            throw IllegalArgumentException("Username cannot be blank.")
        }

        return userRepository.getUser(username)
    }
}
