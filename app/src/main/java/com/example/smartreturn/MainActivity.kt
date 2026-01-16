package com.example.smartreturn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartreturn.data.database.SmartReturnDatabase
import com.example.smartreturn.data.repository.AuthManager
import com.example.smartreturn.data.repository.SimulasiRepository
import com.example.smartreturn.ui.auth.LoginScreen
import com.example.smartreturn.ui.auth.RegisterScreen
import com.example.smartreturn.ui.home.HomeScreen
import com.example.smartreturn.ui.riwayat.RiwayatScreen
import com.example.smartreturn.ui.simulasi.SimulasiDepositoScreen

import com.example.smartreturn.ui.simulasi.SimulasiEmasScreen
import com.example.smartreturn.ui.theme.SmartReturnTheme
import com.example.smartreturn.viewmodel.AuthViewModel
import com.example.smartreturn.viewmodel.RiwayatViewModel
import com.example.smartreturn.viewmodel.SimulasiViewModel
// Pastikan baris import factory ini benar sesuai lokasi file Anda
import com.example.smartreturn.viewmodel.factory.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = SmartReturnDatabase.getDatabase(this)
        val simulasiRepo = SimulasiRepository(database.simulasiDao())
        val authManager = AuthManager(this)

        // Variabel factory didefinisikan di sini
        val customFactory = ViewModelFactory(simulasiRepo, authManager)

        enableEdgeToEdge()

        setContent {
            SmartReturnTheme {
                val navController = rememberNavController()
                val startDest = if (authManager.isUserRegistered()) "login" else "register"

                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDest,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Gunakan 'customFactory' (sesuai nama variabel di atas)
                        composable("register") {
                            val authVm: AuthViewModel = viewModel(factory = customFactory)
                            RegisterScreen(navController, authVm)
                        }
                        composable("login") {
                            val authVm: AuthViewModel = viewModel(factory = customFactory)
                            LoginScreen(navController, authVm)
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("simulasi_emas") {
                            val simulasiVm: SimulasiViewModel = viewModel(factory = customFactory)
                            SimulasiEmasScreen(navController, simulasiVm)
                        }
                        composable("simulasi_deposito") {
                            val simulasiVm: SimulasiViewModel = viewModel(factory = customFactory)
                            SimulasiDepositoScreen(navController, simulasiVm)
                        }
                        composable("riwayat") {
                            val riwayatVm: RiwayatViewModel = viewModel(factory = customFactory)
                            RiwayatScreen(navController, riwayatVm)
                        }
                    }
                }
            }
        }
    }
}