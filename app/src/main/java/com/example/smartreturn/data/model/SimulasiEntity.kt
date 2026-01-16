package com.example.smartreturn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "simulasi")
data class SimulasiEntity(
    @PrimaryKey(autoGenerate = true)
    val simulasi_id: Long = 0,
    val user_id: String,
    val jenis_investasi: String, // "emas" atau "deposito"
    val modal_awal: Double,
    val asumsi_pertumbuhan: Double, // Persentase per tahun
    val durasi_tahun: Int,
    val frekuensi_bunga: String?, // "tahunan" / "bulanan" (null jika emas)
    val nilai_akhir: Double,
    val keuntungan: Double,
    val timestamp_simpan: Long = System.currentTimeMillis()
)