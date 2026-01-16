package com.example.smartreturn.data.repository



import com.example.smartreturn.data.dao.SettingsDao
import com.example.smartreturn.data.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SettingsDao) {

    // REQ-24: Mengambil pengaturan default berdasarkan User ID
    fun getSettings(userId: String): Flow<SettingsEntity?> {
        return settingsDao.getSettings(userId)
    }

    // REQ-23 & REQ-25: Menyimpan atau memperbarui nilai default
    suspend fun saveSettings(settings: SettingsEntity) {
        settingsDao.saveSettings(settings)
    }
}