package com.sgvas21.ddadi21.messengerapp.ui.auth

import androidx.lifecycle.ViewModel
import com.sgvas21.ddadi21.messengerapp.domain.usecase.user.CreateUserUseCase
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _signupState = MutableStateFlow<Result<Unit>?>(null)
    val signupState: StateFlow<Result<Unit>?> = _signupState

    fun signup(username: String, password: String, profession: String) {
        viewModelScope.launch {
            try {
                createUserUseCase(username, password, profession)
                _signupState.value = Result.success(Unit)
            }catch (e: Exception) {
                _signupState.value = Result.failure(e)
            }
        }
    }
}