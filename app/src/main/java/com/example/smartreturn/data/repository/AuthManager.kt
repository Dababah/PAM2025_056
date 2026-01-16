package com.example.smartreturn.data.repository



import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.smartreturn.data.utils.HashUtils

class AuthManager(context: Context) {

    // REQ-05: Inisialisasi EncryptedSharedPreferences untuk keamanan data login
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD_HASH = "password_hash"
        private const val KEY_IS_REGISTERED = "is_registered"
    }

    // REQ-01: Periksa apakah akun sudah pernah dibuat
    fun isUserRegistered(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_REGISTERED, false)
    }

    // REQ-02 & REQ-03: Mendaftarkan pengguna baru
    fun registerUser(username: String, passwordRaw: String) {
        val hash = HashUtils.sha256(passwordRaw)
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME, username)
            putString(KEY_PASSWORD_HASH, hash)
            putBoolean(KEY_IS_REGISTERED, true)
            apply()
        }
    }

    // REQ-06: Memverifikasi login setiap kali aplikasi dibuka
    fun login(username: String, passwordRaw: String): Boolean {
        val savedUsername = sharedPreferences.getString(KEY_USERNAME, "")
        val savedHash = sharedPreferences.getString(KEY_PASSWORD_HASH, "")
        val inputHash = HashUtils.sha256(passwordRaw)

        return username == savedUsername && inputHash == savedHash
    }

    // REQ-07: Tidak ada fitur lupa password, reset hanya via uninstall (otomatis terhapus)
}