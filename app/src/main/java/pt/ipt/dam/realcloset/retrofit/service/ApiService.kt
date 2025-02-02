package pt.ipt.dam.realcloset.retrofit.service

import pt.ipt.dam.realcloset.model.LoginRequest
import pt.ipt.dam.realcloset.model.LoginResponse
import pt.ipt.dam.realcloset.model.Peca
import pt.ipt.dam.realcloset.model.RegisterRequest
import pt.ipt.dam.realcloset.model.RegisterResponse
import pt.ipt.dam.realcloset.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Interface onde se indicam os endpoints da API
interface ApiService {

    // Endpoint para registo
    @POST("autenticacao/registo")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    // Endpoint para login
    @POST("autenticacao/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // Endpoint para buscar todas as peças
    @GET("pecas/public")
    suspend fun getPecasPublic(): List<Peca>

    // Endpoint para adicionar peças aos favoritos
    @POST("favoritos/{id}")
    suspend fun adicionarFavorito(
        @Path("id") pecaId: Int,
        @Header("Authorization") token: String
    )

    // Endpoint para buscar informações de um utilizador consoante o seu id
    @GET("utilizador/{id}")
    suspend fun getUtilizador(
        @Header("Authorization") token: String,
        @Path("id") utilizadorId: Int
    ): Response<User>

    // Endpoint para buscar informações do utilizador logado
    @GET("utilizador/me")
    suspend fun getLoggedUser(@Header("Authorization") token: String): Response<User>

    // Endpoint para atualizar as informações de um utilizador específico, dado o seu id
    @PUT("utilizador/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
        @Body data: Map<String, String>
    ): Response<Void>
}



