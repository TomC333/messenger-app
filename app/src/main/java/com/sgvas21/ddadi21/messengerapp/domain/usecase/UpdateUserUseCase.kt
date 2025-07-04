package com.sgvas21.ddadi21.messengerapp.domain.usecase

import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validateUsernameUseCase: ValidateUsernameUseCase
) {
    /**
     * Updates an existing user profile.
     *
     * @param user The updated user profile.
     * @param currentUsername The current username of the user.
     * @throws IllegalArgumentException If username is blank or validation fails.
     * @throws Exception if the update operation fails or the user doesn't exist.
     */
    suspend operator fun invoke(user: User, currentUsername: String) {
        if (user.username.isBlank()) {
            throw IllegalArgumentException("Username cannot be blank.")
        }

        validateUsernameUseCase(user.username, currentUsername)

        if (user.username != currentUsername) {
            val currentUser = userRepository.getUser(currentUsername)
                ?: throw IllegalArgumentException("Current user not found.")

            val updatedUser = user.copy(
                salt = currentUser.salt,
                password = currentUser.password
            )

            userRepository.addUser(updatedUser)

            userRepository.deleteUser(currentUsername)
        } else {
            userRepository.updateUser(user)
        }
    }
}
