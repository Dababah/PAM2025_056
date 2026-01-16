package com.example.smartreturn.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartreturn.data.repository.AuthManager
import com.example.smartreturn.data.repository.SimulasiRepository
import com.example.smartreturn.viewmodel.AuthViewModel
import com.example.smartreturn.viewmodel.RiwayatViewModel
import com.example.smartreturn.viewmodel.SimulasiViewModel

class ViewModelFactory(
    private val repository: SimulasiRepository,
    private val authManager: AuthManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authManager) as T
            }
            modelClass.isAssignableFrom(SimulasiViewModel::class.java) -> {
                SimulasiViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RiwayatViewModel::class.java) -> {
                RiwayatViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}