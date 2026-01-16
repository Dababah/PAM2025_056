package com.example.smartreturn.utils



import java.text.NumberFormat
import java.util.Locale

object FormatUtils {

    /**
     * REQ-12 & REQ-15: Mengubah angka Double menjadi format Rupiah
     * Contoh: 1000000 -> Rp 1.000.000
     */
    fun formatRupiah(number: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number)
    }

    /**
     * Memformat angka persentase agar tampil dengan dua angka di belakang koma
     * Contoh: 0.125 -> 12.5%
     */
    fun formatPersen(number: Double): String {
        return String.format("%.2f%%", number)
    }
}