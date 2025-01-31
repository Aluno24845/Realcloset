package pt.ipt.dam.realcloset.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Verifica se existe token armazenado (utilizador logado)
    fun isUserLoggedIn(): Boolean {
        return prefs.getString("auth_token", null) != null
    }

    // Remove o token ao fazer logout
    fun logout() {
        prefs.edit().remove("auth_token").apply()
    }
}