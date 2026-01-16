package com.example.smartreturn.data.repository



import com.example.smartreturn.data.dao.SimulasiDao
import com.example.smartreturn.data.model.SimulasiEntity
import kotlinx.coroutines.flow.Flow

class SimulasiRepository(private val simulasiDao: SimulasiDao) {

    // Mengambil semua riwayat untuk ditampilkan di menu "Riwayat"
    val allSimulasi: Flow<List<SimulasiEntity>> = simulasiDao.getAllSimulasi()

    // REQ-21: Mendapatkan riwayat berdasarkan filter jenis
    fun getSimulasiByType(jenis: String): Flow<List<SimulasiEntity>> {
        return simulasiDao.getSimulasiByType(jenis)
    }

    // REQ-11 & REQ-14: Menyimpan hasil simulasi baru
    suspend fun insert(simulasi: SimulasiEntity) {
        simulasiDao.insertSimulasi(simulasi)
    }

    // REQ-19: Memperbarui data simulasi yang diedit
    suspend fun update(simulasi: SimulasiEntity) {
        simulasiDao.updateSimulasi(simulasi)
    }

    // REQ-20: Menghapus data simulasi
    suspend fun delete(simulasi: SimulasiEntity) {
        simulasiDao.deleteSimulasi(simulasi)
    }
}