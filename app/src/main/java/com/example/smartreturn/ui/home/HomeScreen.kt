package com.example.smartreturn.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreturn.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Tekstur yang sama dengan layar lainnya
        Image(
            painter = painterResource(id = R.drawable.bg_dark_texture),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_logo_smartreturn),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SmartReturn", fontWeight = FontWeight.ExtraBold)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.5f),
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Halo, Selamat Datang!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Pilih instrumen investasi Anda hari ini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // 2. Menu Cards dengan gaya Premium
                item {
                    MenuCard(
                        "Simulasi Emas",
                        "Prediksi pertumbuhan aset emas fisik",
                        Icons.Default.TrendingUp,
                        MaterialTheme.colorScheme.primary
                    ) {
                        navController.navigate("simulasi_emas")
                    }
                }
                item {
                    MenuCard(
                        "Simulasi Deposito",
                        "Hitung bunga majemuk perbankan",
                        Icons.Default.Savings,
                        MaterialTheme.colorScheme.primary
                    ) {
                        navController.navigate("simulasi_deposito")
                    }
                }
                item {
                    MenuCard(
                        "Riwayat",
                        "Kelola data simulasi sebelumnya",
                        Icons.Default.History,
                        Color.LightGray
                    ) {
                        navController.navigate("riwayat")
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCard(title: String, subtitle: String, icon: ImageVector, accentColor: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .clip(RoundedCornerShape(20.dp)),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(accentColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}