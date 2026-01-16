package com.example.smartreturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartreturn.data.model.SimulasiEntity
import com.example.smartreturn.data.repository.SimulasiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow

class SimulasiViewModel(private val repository: SimulasiRepository) : ViewModel() {

    // Gunakan dua state berbeda agar hasil Emas dan Deposito tidak tertukar di layar
    private val _hasilEmas = MutableStateFlow<Double?>(null)
    val hasilEmas: StateFlow<Double?> = _hasilEmas.asStateFlow()

    private val _hasilDeposito = MutableStateFlow<Double?>(null)
    val hasilDeposito: StateFlow<Double?> = _hasilDeposito.asStateFlow()

    // REQ-10: Menghitung simulasi Emas
    fun hitungEmas(modal: Double, pertumbuhan: Double, tahun: Int) {
        val r = pertumbuhan / 100
        val hasil = modal * (1 + r).pow(tahun.toDouble())
        _hasilEmas.value = hasil
    }

    // REQ-13: Perbaikan parameter 'frekuensi' menjadi Int agar cocok dengan UI
    fun hitungDeposito(modal: Double, bunga: Double, tahun: Int, frekuensi: Int) {
        val r = bunga / 100
        val n = frekuensi.toDouble()
        val hasil = modal * (1 + r / n).pow(n * tahun)
        _hasilDeposito.value = hasil
    }

    fun simpanSimulasi(
        userId: String,
        jenis: String,
        modal: Double,
        pertumbuhan: Double,
        tahun: Int,
        frekuensi: String? = null
    ) {
        viewModelScope.launch {
            // Ambil nilai akhir berdasarkan jenis investasi
            val nilaiAkhir = if (jenis == "Emas") _hasilEmas.value ?: 0.0 else _hasilDeposito.value ?: 0.0
            val untung = nilaiAkhir - modal

            val entity = SimulasiEntity(
                user_id = userId,
                jenis_investasi = jenis,
                modal_awal = modal,
                asumsi_pertumbuhan = pertumbuhan,
                durasi_tahun = tahun,
                frekuensi_bunga = frekuensi,
                nilai_akhir = nilaiAkhir,
                keuntungan = untung
            )
            repository.insert(entity)
        }
    }
}