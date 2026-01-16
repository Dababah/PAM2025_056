package com.example.smartreturn.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartreturn.R
import com.example.smartreturn.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isRegistered by viewModel.isRegistered.collectAsState()

    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            navController.navigate("login") { popUpTo("register") { inclusive = true } }
        }
    }

    AuthLayout(
        title = "Buat Akun",
        subtitle = "Daftar untuk mulai mengelola investasi Anda",
        errorMessage = errorMessage
    ) {
        CustomAuthInput(value = username, onValueChange = { username = it }, label = "Username")
        Spacer(modifier = Modifier.height(16.dp))
        CustomAuthInput(value = password, onValueChange = { password = it }, label = "Password", isPassword = true)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.register(username, password) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("DAFTAR SEKARANG", fontWeight = FontWeight.ExtraBold, color = Color.Black)
        }

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Sudah punya akun? Login di sini", color = Color.LightGray)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginResult by viewModel.loginResult.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(loginResult) {
        if (loginResult == true) {
            navController.navigate("home") { popUpTo("login") { inclusive = true } }
        }
    }

    AuthLayout(
        title = "SmartReturn",
        subtitle = "Silakan login ke akun investasi Anda",
        errorMessage = errorMessage,
        isLogin = true
    ) {
        CustomAuthInput(value = username, onValueChange = { username = it }, label = "Username")
        Spacer(modifier = Modifier.height(16.dp))
        CustomAuthInput(value = password, onValueChange = { password = it }, label = "Password", isPassword = true)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("MASUK KE AKUN", fontWeight = FontWeight.ExtraBold, color = Color.Black)
        }
    }
}

// Komponen Pembungkus (Layout Dasar) agar konsisten
@Composable
fun AuthLayout(
    title: String,
    subtitle: String,
    errorMessage: String?,
    isLogin: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_dark_texture),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_smartreturn),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                style = if(isLogin) MaterialTheme.typography.displayMedium else MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(text = subtitle, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)

            Spacer(modifier = Modifier.height(40.dp))

            content()

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun CustomAuthInput(value: String, onValueChange: (String) -> Unit, label: String, isPassword: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}