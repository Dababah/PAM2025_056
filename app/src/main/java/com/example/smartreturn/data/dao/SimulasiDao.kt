package com.example.smartreturn.data.dao



import androidx.room.*
import com.example.smartreturn.data.model.SimulasiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulasiDao {
    // REQ-11 & REQ-14: Simpan hasil simulasi ke riwayat
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimulasi(simulasi: SimulasiEntity)

    // REQ-18: Mengambil daftar semua simulasi diurutkan dari yang terbaru
    @Query("SELECT * FROM simulasi ORDER BY timestamp_simpan DESC")
    fun getAllSimulasi(): Flow<List<SimulasiEntity>>

    // REQ-21: Filter riwayat berdasarkan jenis (emas/deposito)
    @Query("SELECT * FROM simulasi WHERE jenis_investasi = :jenis ORDER BY timestamp_simpan DESC")
    fun getSimulasiByType(jenis: String): Flow<List<SimulasiEntity>>

    // REQ-19: Update parameter simulasi yang sudah ada
    @Update
    suspend fun updateSimulasi(simulasi: SimulasiEntity)

    // REQ-20: Hapus entri tertentu
    @Delete
    suspend fun deleteSimulasi(simulasi: SimulasiEntity)
}