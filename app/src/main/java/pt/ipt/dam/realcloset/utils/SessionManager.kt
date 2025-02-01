package pt.ipt.dam.realcloset.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    // Acesso às Preferences da aplicação
    private val prefs: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Verifica se o utilizador está logado através da existência de um token
    fun isUserLoggedIn(): Boolean {
        return prefs.getString("auth_token", null) != null
    }

    // Remove o token armazenado (logout)
    fun logout() {
        prefs.edit().remove("auth_token").apply()
    }

    // Obtém o token de autenticação armazenado; retorna uma string vazia se não existir
    fun getAuthToken(): String {
        return prefs.getString("auth_token","" ) ?: ""
    }

    // Armazena o token de autenticação para futuras sessões
    fun saveAuthToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }
}