package com.sgvas21.ddadi21.messengerapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgvas21.ddadi21.messengerapp.domain.usecase.user.SigninUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    private val signinUserUseCase: SigninUserUseCase
): ViewModel() {

    private val _signinState = MutableStateFlow<Result<Unit>?>(null)
    val signinState: StateFlow<Result<Unit>?> = _signinState

    fun signin(username: String, password: String) {
        viewModelScope.launch {
            try{
                signinUserUseCase(username, password)
                _signinState.value = Result.success(Unit)
            }catch (e: Exception) {
                _signinState.value = Result.failure(e)
            }
        }
    }
}