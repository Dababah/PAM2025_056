package com.example.smartreturn.data.database



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartreturn.data.dao.SettingsDao
import com.example.smartreturn.data.dao.SimulasiDao
import com.example.smartreturn.data.model.SettingsEntity
import com.example.smartreturn.data.model.SimulasiEntity

// Mendefinisikan entitas yang masuk ke database dan versi skema
@Database(
    entities = [SimulasiEntity::class, SettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmartReturnDatabase : RoomDatabase() {

    // Menghubungkan DAO ke Database
    abstract fun simulasiDao(): SimulasiDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: SmartReturnDatabase? = null

        // Singleton pattern untuk memastikan hanya ada satu instance database
        fun getDatabase(context: Context): SmartReturnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartReturnDatabase::class.java,
                    "smartreturn_db"
                )
                    // Strategi migrasi sederhana untuk v0.1
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}