package com.example.smartreturn.viewmodel



import androidx.lifecycle.ViewModel
import com.example.smartreturn.data.repository.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val authManager: AuthManager) : ViewModel() {

    // Status apakah user sudah terdaftar atau belum (untuk menentukan layar awal)
    private val _isRegistered = MutableStateFlow(authManager.isUserRegistered())
    val isRegistered: StateFlow<Boolean> = _isRegistered

    // Status login (berhasil/gagal)
    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult

    // Pesan error untuk ditampilkan di UI
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // REQ-02 & REQ-03: Logika Registrasi
    fun register(username: String, passwordRaw: String) {
        if (username.length < 4) {
            _errorMessage.value = "Username minimal 4 karakter"
            return
        }
        if (passwordRaw.length < 6) {
            _errorMessage.value = "Password minimal 6 karakter"
            return
        }

        authManager.registerUser(username, passwordRaw)
        _isRegistered.value = true
        _loginResult.value = true
        _errorMessage.value = null
    }

    // REQ-06: Logika Login
    fun login(username: String, passwordRaw: String) {
        val success = authManager.login(username, passwordRaw)
        if (success) {
            _loginResult.value = true
            _errorMessage.value = null
        } else {
            _loginResult.value = false
            _errorMessage.value = "Username atau Password salah"
        }
    }

    fun resetLoginStatus() {
        _loginResult.value = null
    }
}