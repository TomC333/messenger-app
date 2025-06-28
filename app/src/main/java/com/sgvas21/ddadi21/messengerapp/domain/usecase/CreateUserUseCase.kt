package com.sgvas21.ddadi21.messengerapp.domain.usecase

import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import com.sgvas21.ddadi21.messengerapp.utils.PasswordUtils
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Creates a new user with a salted and hashed password.
     *
     * @param username Unique username.
     * @param plainPassword User's plain-text password.
     * @param profession User's profession or role.
     *
     * @throws IllegalArgumentException If any of the required field are blank.
     * @throws Exception If user creating fails due to repository.
     */
    suspend operator fun invoke(username: String, plainPassword: String, profession: String) {
        if(username.isBlank() || plainPassword.isBlank() || profession.isBlank()) {
            throw IllegalArgumentException("All fields are required.")
        }

        val salt = PasswordUtils.generateSalt()
        val hashedPassword = PasswordUtils.generateHash(plainPassword, salt)

        val user = User(
            username = username,
            salt = salt,
            password = hashedPassword,
            profession = profession,
            profileImageUrl = "",
            timestamp = System.currentTimeMillis(),
        )

        userRepository.addUser(user)
    }
}