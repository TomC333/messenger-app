package com.sgvas21.ddadi21.messengerapp.domain.usecase.user

import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.data.repository.UserRepository
import javax.inject.Inject

/**
 * Use case for searching users by username.
 *
 * This use case provides an interface to search for users
 * based on a given username query.
 */
class SearchUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Executes the search operation for a given username query.
     *
     * @param query The username query to search for.
     * @return A list of [User] objects matching the search query, or an empty list if no matches are found.
     */
    suspend fun execute(query: String): List<User> {
        return try {
            userRepository.searchUsers(query)
        } catch (_: Exception) {
            emptyList()
        }
    }
}