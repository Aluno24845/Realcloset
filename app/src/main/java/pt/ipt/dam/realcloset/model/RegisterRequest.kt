package pt.ipt.dam.realcloset.model

import java.util.Date

data class RegisterRequest(
    val nome: String,
    val apelido: String,
    val dataNascimento: Date,
    val tipoDePerfil: String,
    val email: String,
    val password: String
)