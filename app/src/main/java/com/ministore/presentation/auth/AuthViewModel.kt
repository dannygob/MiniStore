package com.ministore.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ministore.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.isUserAuthenticated.collect { isAuthenticated ->
                if (isAuthenticated) {
                    _authState.value = AuthState.Authenticated(repository.currentUser?.email ?: "")
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signIn(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.Authenticated(user.email ?: "")
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Unknown error")
                }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signUp(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.Authenticated(user.email ?: "")
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Unknown error")
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _authState.value = AuthState.Unauthenticated
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Authenticated(val email: String) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
} 