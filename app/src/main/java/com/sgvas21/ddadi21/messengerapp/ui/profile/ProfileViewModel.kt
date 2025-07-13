package com.sgvas21.ddadi21.messengerapp.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.domain.usecase.user.GetUserUseCase
import com.sgvas21.ddadi21.messengerapp.domain.usecase.user.UpdateUserUseCase
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()

    private val _usernameExists = MutableSharedFlow<Boolean>()
    val usernameExists: SharedFlow<Boolean> = _usernameExists

    init {
        getUserDocs()
    }

    fun getUserDocs() {
        if (SessionManager.isSignedIn(context)) {
            val username = SessionManager.getUsername(context)
            if (username != null) {
                viewModelScope.launch {
                    _isLoading.value = true
                    _error.value = null

                    try {
                        val user = getUserUseCase(username)
                        _userState.value = user
                    } catch (e: Exception) {
                        _error.value = e.message ?: "Failed to fetch user information"
                    } finally {
                        _isLoading.value = false
                    }
                }
            } else {
                _error.value = "No username found in session"
            }
        } else {
            _error.value = "User not signed in"
        }
    }

    fun updateUserInfo(updatedUser: User) {
        val currentUsernameInSession = SessionManager.getUsername(context)
        if (currentUsernameInSession == null) {
            _error.value = "No current username found in session"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _updateSuccess.value = false

            try {
                updateUserUseCase(updatedUser, currentUsernameInSession)
                _userState.value = updatedUser
                _updateSuccess.value = true

                if (updatedUser.username != currentUsernameInSession) {
                    SessionManager.saveUsername(context, updatedUser.username)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update user information"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkUsernameAndAttemptUpdate(currentUser: User, newUsername: String, newProfession: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _usernameExists.emit(false)

            try {
                val existingUserWithNewUsername = getUserUseCase(newUsername)

               if (existingUserWithNewUsername != null && existingUserWithNewUsername.username != currentUser.username) {
                    _usernameExists.emit(true)
                } else {
                    val updatedUser = currentUser.copy(
                        username = newUsername,
                        profession = newProfession
                    )
                    updateUserInfo(updatedUser)
                }
            } catch (e: Exception) {
                _error.value = "Error checking username: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun uploadProfileImage(uri: Uri, user: User) {
        val currentUsername = SessionManager.getUsername(context)
        if (currentUsername == null) {
            _error.value = "No current username found"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val storageRef = firebaseStorage.reference
                    .child("profileImages/${currentUsername}.jpg")

                val uploadTask = storageRef.putFile(uri)

                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val updatedUser = user.copy(profileImageUrl = downloadUrl.toString())
                        updateUserInfo(updatedUser)
                    }.addOnFailureListener { exception ->
                        _error.value = exception.message ?: "Failed to get download URL"
                        _isLoading.value = false
                    }
                }.addOnFailureListener { exception ->
                    _error.value = exception.message ?: "Failed to upload profile image"
                    _isLoading.value = false
                }.addOnProgressListener { taskSnapshot ->
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to upload profile image"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearUpdateSuccess() {
        _updateSuccess.value = false
    }

    fun clearUsernameExists() {
        viewModelScope.launch {
            _usernameExists.emit(false)
        }
    }
}