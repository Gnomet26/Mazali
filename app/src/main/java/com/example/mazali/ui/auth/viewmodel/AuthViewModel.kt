package com.example.mazali.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazali.data.model.LoginRequest
import com.example.mazali.data.model.RegisterRequest
import com.example.mazali.data.repository.AuthRepository
import com.example.mazali.utils.PrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import com.example.mazali.data.model.UpdateRequest

// ------------------ Auth holatlari ------------------
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val error: String) : AuthState()
}

class AuthViewModel(
    private val repository: AuthRepository,
    private val context: Context // PrefsManager ishlashi uchun
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // ------------------ REGISTER ------------------
    fun register(name: String, phone: String, password: String) {
        val deviceToken = PrefsManager.getDeviceToken(context) ?: ""
        val request = RegisterRequest(name, phone, password, deviceToken)

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.registerUser(request)
            result.onSuccess {
                PrefsManager.saveAuthToken(context, it.token)
                PrefsManager.saveUserData(context, it.user.name, it.user.phoneNumber)
                _authState.value = AuthState.Success("Ro'yxatdan o'tish muvaffaqiyatli ✅")
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Noma'lum xato")
            }
        }
    }

    // ------------------ LOGIN ------------------
    fun login(phone: String, password: String) {
        val deviceToken = PrefsManager.getDeviceToken(context) ?: ""
        val request = LoginRequest(phone, password, deviceToken)

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.loginUser(request)
            result.onSuccess {
                PrefsManager.saveAuthToken(context, it.token)
                PrefsManager.saveUserData(context, it.user.name, it.user.phoneNumber)
                _authState.value = AuthState.Success("Kirish muvaffaqiyatli ✅")
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Noma'lum xato")
            }
        }
    }

    fun updateUser(name: String, phone: String, password: String) {
        val token = PrefsManager.getAuthToken(context) ?: return

        val request = UpdateRequest(name, phone, password)
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.updateUser(token,request)
            result.onSuccess { userDto ->
                PrefsManager.saveUserData(context, userDto.name, userDto.phoneNumber)
                _authState.value = AuthState.Success("Ma’lumotlar yangilandi ✅")
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Update xatolik")
            }
        }
    }


    // ------------------ LOGOUT ------------------
    fun logout() {
        val token = PrefsManager.getAuthToken(context) ?: return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.logout(token)
            result.onSuccess {
                PrefsManager.clearAuthData(context)
                PrefsManager.clearUserDate(context)
                _authState.value = AuthState.Success("Chiqish amalga oshirildi")
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Logout xatolik")
            }
        }
    }
}
