package com.example.smartreturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartreturn.data.model.SimulasiEntity
import com.example.smartreturn.data.repository.SimulasiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RiwayatViewModel(private val repository: SimulasiRepository) : ViewModel() {

    // State untuk menyimpan jenis filter (semua, emas, deposito)
    private val _filterType = MutableStateFlow("semua")
    val filterType: StateFlow<String> = _filterType

    /**
     * REQ-18 & REQ-21: Flow data riwayat yang otomatis terupdate berdasarkan filter.
     * Menggunakan flatMapLatest untuk beralih sumber data saat filter berubah.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val listRiwayat: StateFlow<List<SimulasiEntity>> = _filterType
        .flatMapLatest { type ->
            if (type == "semua") {
                repository.allSimulasi
            } else {
                repository.getSimulasiByType(type)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Menjaga aliran tetap hidup selama 5 detik setelah UI tidak aktif
            initialValue = emptyList()
        )

    // REQ-21: Mengubah filter jenis investasi
    fun setFilter(jenis: String) {
        _filterType.value = jenis
    }

    // REQ-20: Menghapus satu item riwayat
    fun hapusRiwayat(simulasi: SimulasiEntity) {
        viewModelScope.launch {
            repository.delete(simulasi)
        }
    }
    // Tambahkan ini di RiwayatViewModel.kt
    fun perbaruiRiwayat(simulasi: SimulasiEntity) {
        viewModelScope.launch {
            repository.update(simulasi)
        }
    }
}