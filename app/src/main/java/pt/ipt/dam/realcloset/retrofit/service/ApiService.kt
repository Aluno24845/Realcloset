package pt.ipt.dam.realcloset.retrofit.service

import pt.ipt.dam.realcloset.model.LoginRequest
import pt.ipt.dam.realcloset.model.LoginResponse
import pt.ipt.dam.realcloset.model.RegisterRequest
import pt.ipt.dam.realcloset.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Interface onde se indicam os endpoints da API
interface ApiService {

    // Endpoint para registo
    @POST("autenticacao/registo")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    // Endpoint para login
    @POST("autenticacao/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
}



