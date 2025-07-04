package com.sgvas21.ddadi21.messengerapp.data.repository

import com.sgvas21.ddadi21.messengerapp.data.model.User

/**
 * Repository interface for the users.
 */
interface UserRepository {
    /**
     * Adds a new user profile, ensuring the username is unique.
     *
     * This method atomically checks for username uniqueness and creates
     * the user profile in Firestore.
     *
     * Handles race conditions for concurrent requests trying to create
     * new database objects with same username.
     *
     * @param user The user to add.
     * @throws Exception if the username is already taken or operation fails.
     */
    suspend fun addUser(user: User)

    /**
     * Retrieves a user profile by username.
     *
     * @param username The username of the profile to retrieve.
     * @return The user object if found, or null.
     * @throws Exception on failure to retrieve data.
     */
    suspend fun getUser(username: String): User?

    /**
     * Updates an existing user profile.
     *
     * @param user The updated user profile.
     * @throws Exception if the update operation fails or the user doesn't exists.
     */
    suspend fun updateUser(user: User)

    /**
     * Checks if a username already exists in the database.
     *
     * This method is used to determine whether a user with the given username is
     * already registered in the system. It helps ensure username uniqueness during
     * user creation or updates.
     *
     * @param username The username to check for existence.
     * @return True if the username exists, false otherwise.
     * @throws Exception if the operation fails due to network or database errors.
     */
    suspend fun usernameExists(username: String): Boolean

    /**
     * Deletes a user profile by username.
     *
     * @param username The username of the user to delete.
     * @throws Exception if the user does not exist or deletion fails.
     */
    suspend fun deleteUser(username: String)
}
