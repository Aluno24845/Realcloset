package pt.ipt.dam.realcloset.model

data class RegisterRequest(
    val nome: String,
    val apelido: String,
    val dataNascimento: String,
    val tipoDePerfil: String,
    val email: String,
    val password: String
)