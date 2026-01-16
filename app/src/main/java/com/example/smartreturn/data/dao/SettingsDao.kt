package com.example.smartreturn.data.dao



import androidx.room.*
import com.example.smartreturn.data.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    // REQ-25: Simpan atau update pengaturan secara lokal
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)

    // REQ-24: Ambil nilai default untuk isian awal simulasi
    @Query("SELECT * FROM settings WHERE user_id = :userId LIMIT 1")
    fun getSettings(userId: String): Flow<SettingsEntity?>
}