package pt.ipt.dam.realcloset.model

data class LoginRequest(
    val email: String,
    val password: String
)