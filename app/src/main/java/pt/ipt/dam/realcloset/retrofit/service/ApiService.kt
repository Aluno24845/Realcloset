package pt.ipt.dam.realcloset.retrofit.service

import pt.ipt.dam.realcloset.model.LoginRequest
import pt.ipt.dam.realcloset.model.LoginResponse
import pt.ipt.dam.realcloset.model.Peca
import pt.ipt.dam.realcloset.model.RegisterRequest
import pt.ipt.dam.realcloset.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    // Endpoint para remover peças dos favoritos
    @DELETE("favoritos/{id}")
    suspend fun removerFavorito(
        @Path("id") pecaId: Int,
        @Header("Authorization") token: String
    )

    // Endpoint para ir buscar os favoritos
    @GET("favoritos")
    suspend fun getFavoritos(
        @Header("Authorization") token: String
    ): List<Peca>

    // Endpoint para ir buscar os favoritos
    @GET("pecas")
    suspend fun getPecas(
        @Header("Authorization") token: String
    ): List<Peca>
}


