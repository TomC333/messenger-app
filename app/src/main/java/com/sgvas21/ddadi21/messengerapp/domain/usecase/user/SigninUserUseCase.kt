package com.sgvas21.ddadi21.messengerapp.domain.usecase.user

import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import com.sgvas21.ddadi21.messengerapp.utils.PasswordUtils
import javax.inject.Inject

class SigninUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    /**
     * Use case for authenticating a user with their credentials.
     *
     * @param username Unique username.
     * @param plainPassword User's plain-text password.
     *
     * @throws IllegalArgumentException If any of the required field are blank.
     * @throws Exception If user with 'username' doesn't exist, or if the password is incorrect.
     */
    suspend operator fun invoke(username: String, plainPassword: String) {
        if(username.isBlank() || plainPassword.isBlank()) {
            throw IllegalArgumentException("All fields are required.")
        }

        val user = userRepository.getUser(username)
            ?: throw Exception("User not found.")

        val hashedEnteredPassword = PasswordUtils.generateHash(plainPassword, user.salt)
        if(hashedEnteredPassword != user.password) {
            throw Exception("Incorrect password.")
        }
    }
}