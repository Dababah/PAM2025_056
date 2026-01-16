package com.example.smartreturn.ui.simulasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreturn.R
import com.example.smartreturn.viewmodel.SimulasiViewModel
import com.example.smartreturn.utils.FormatUtils

// --- KOMPONEN EMAS ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulasiEmasScreen(navController: NavController, viewModel: SimulasiViewModel) {
    var modalInitial by remember { mutableStateOf("") }
    var pertumbuhanTahunan by remember { mutableStateOf("") }
    var jangkaWaktu by remember { mutableStateOf("") }
    val hasilSimulasi by viewModel.hasilEmas.collectAsState()

    BaseSimulasiLayout(
        title = "Simulasi Emas",
        navController = navController,
        onCalculate = {
            val modal = modalInitial.toDoubleOrNull() ?: 0.0
            val persen = pertumbuhanTahunan.toDoubleOrNull() ?: 0.0
            val tahun = jangkaWaktu.toIntOrNull() ?: 0
            viewModel.hitungEmas(modal, persen, tahun)
            viewModel.simpanSimulasi("user_1", "Emas", modal, persen, tahun)
        },
        hasil = hasilSimulasi
    ) {
        CustomOutlinedTextField(modalInitial, { modalInitial = it }, "Modal Awal (Rp)")
        CustomOutlinedTextField(pertumbuhanTahunan, { pertumbuhanTahunan = it }, "Asumsi Pertumbuhan (%)")
        CustomOutlinedTextField(jangkaWaktu, { jangkaWaktu = it }, "Jangka Waktu (Tahun)")
    }
}

// --- KOMPONEN DEPOSITO ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulasiDepositoScreen(navController: NavController, viewModel: SimulasiViewModel) {
    var modalInitial by remember { mutableStateOf("") }
    var bungaTahunan by remember { mutableStateOf("") }
    var jangkaWaktu by remember { mutableStateOf("") }
    val hasilSimulasi by viewModel.hasilDeposito.collectAsState()

    BaseSimulasiLayout(
        title = "Simulasi Deposito",
        navController = navController,
        onCalculate = {
            val modal = modalInitial.toDoubleOrNull() ?: 0.0
            val bunga = bungaTahunan.toDoubleOrNull() ?: 0.0
            val tahun = jangkaWaktu.toIntOrNull() ?: 0
            viewModel.hitungDeposito(modal, bunga, tahun, 12)
            viewModel.simpanSimulasi("user_1", "Deposito", modal, bunga, tahun, "Bulanan")
        },
        hasil = hasilSimulasi
    ) {
        CustomOutlinedTextField(modalInitial, { modalInitial = it }, "Setoran Awal (Rp)")
        CustomOutlinedTextField(bungaTahunan, { bungaTahunan = it }, "Bunga per Tahun (%)")
        CustomOutlinedTextField(jangkaWaktu, { jangkaWaktu = it }, "Durasi (Tahun)")
    }
}

// --- REUSABLE BASE LAYOUT (Agar Tampilan Konsisten) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseSimulasiLayout(
    title: String,
    navController: NavController,
    onCalculate: () -> Unit,
    hasil: Double?,
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
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)))))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(title, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent, titleContentColor = MaterialTheme.colorScheme.primary)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_logo_smartreturn), contentDescription = "Logo", modifier = Modifier.size(80.dp).padding(top = 16.dp))

                content()

                Button(
                    onClick = onCalculate,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("HITUNG & SIMPAN", fontWeight = FontWeight.ExtraBold, color = Color.Black)
                }

                hasil?.let {
                    Surface(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Hasil Optimal", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                            Text(text = FormatUtils.formatRupiah(it), style = MaterialTheme.typography.displayLarge, textAlign = TextAlign.Center, color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}