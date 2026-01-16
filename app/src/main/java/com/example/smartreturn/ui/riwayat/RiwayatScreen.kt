package com.example.smartreturn.ui.riwayat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartreturn.R
import com.example.smartreturn.data.model.SimulasiEntity
import com.example.smartreturn.utils.FormatUtils
import com.example.smartreturn.viewmodel.RiwayatViewModel
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(navController: NavController, viewModel: RiwayatViewModel) {
    val listRiwayat by viewModel.listRiwayat.collectAsState()
    val currentFilter by viewModel.filterType.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<SimulasiEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Tekstur Premium
        Image(
            painter = painterResource(id = R.drawable.bg_dark_texture),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Riwayat Investasi", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.7f),
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                // 2. Tab Filter
                FilterTabs(
                    selectedFilter = currentFilter,
                    onFilterSelected = { viewModel.setFilter(it) }
                )

                if (listRiwayat.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada riwayat simulasi.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(listRiwayat) { item ->
                            RiwayatItem(
                                simulasi = item,
                                onDelete = { viewModel.hapusRiwayat(item) },
                                onEdit = {
                                    selectedItem = item
                                    showEditDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }

        // 3. Logic Dialog Edit
        if (showEditDialog && selectedItem != null) {
            EditSimulasiDialog(
                simulasi = selectedItem!!,
                onDismiss = { showEditDialog = false },
                onConfirm = { updatedItem ->
                    viewModel.perbaruiRiwayat(updatedItem)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun RiwayatItem(simulasi: SimulasiEntity, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = simulasi.jenis_investasi.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = FormatUtils.formatRupiah(simulasi.nilai_akhir),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Modal: ${FormatUtils.formatRupiah(simulasi.modal_awal)} | ${simulasi.durasi_tahun} Thn",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun EditSimulasiDialog(
    simulasi: SimulasiEntity,
    onDismiss: () -> Unit,
    onConfirm: (SimulasiEntity) -> Unit
) {
    var modalStr by remember { mutableStateOf(simulasi.modal_awal.toString()) }
    var durasiStr by remember { mutableStateOf(simulasi.durasi_tahun.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Koreksi Data ${simulasi.jenis_investasi}", color = MaterialTheme.colorScheme.primary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = modalStr,
                    onValueChange = { modalStr = it },
                    label = { Text("Modal Awal (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = durasiStr,
                    onValueChange = { durasiStr = it },
                    label = { Text("Durasi (Tahun)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val m = modalStr.toDoubleOrNull() ?: simulasi.modal_awal
                    val t = durasiStr.toIntOrNull() ?: simulasi.durasi_tahun
                    val r = simulasi.asumsi_pertumbuhan / 100

                    // Hitung Ulang Nilai Akhir agar hasil di list berubah
                    val nilaiBaru = if (simulasi.jenis_investasi.equals("Emas", ignoreCase = true)) {
                        m * (1.0 + r).pow(t.toDouble())
                    } else {
                        m * (1.0 + (r / 12)).pow((12 * t).toDouble())
                    }

                    // Update Keuntungan
                    val keuntunganBaru = nilaiBaru - m

                    val updated = simulasi.copy(
                        modal_awal = m,
                        durasi_tahun = t,
                        nilai_akhir = nilaiBaru,
                        keuntungan = keuntunganBaru
                    )
                    onConfirm(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("Simpan Perubahan", color = Color.Black) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = Color.Gray) }
        }
    )
}

@Composable
fun FilterTabs(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("semua", "Emas", "Deposito")
    ScrollableTabRow(
        selectedTabIndex = filters.indexOf(selectedFilter),
        edgePadding = 16.dp,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        divider = {}
    ) {
        filters.forEach { filter ->
            Tab(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                text = {
                    Text(
                        text = filter.replaceFirstChar { it.uppercase() },
                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}