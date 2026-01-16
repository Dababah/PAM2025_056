package com.example.smartreturn.data.model



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val user_id: String, // Menghubungkan ke user (sesuai SRS)
    val default_kenaikan_emas: Double = 8.0,
    val default_suku_bunga: Double = 6.5
)