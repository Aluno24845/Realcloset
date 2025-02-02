package pt.ipt.dam.realcloset.model

import com.google.gson.annotations.SerializedName

public class User (
    @SerializedName("UtilizadorID") val id: Int,
    @SerializedName("Nome") val nome: String,
    @SerializedName("Apelido") val apelido: String,
    @SerializedName("DataNascimento") val dataNascimento: String,
    @SerializedName("TipoDePerfil") val tipoDePerfil: String,
    @SerializedName("Email") val email: String
    )