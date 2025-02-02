package pt.ipt.dam.realcloset.retrofit.service


import okhttp3.MultipartBody
import okhttp3.RequestBody
import pt.ipt.dam.realcloset.model.Peca
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiServiceUpload {
    @Multipart
    @POST("pecas")
    suspend fun adicionarPeca(
        @Header("Authorization") token: String,
        @Part("peca") peca: RequestBody, // Dados do objeto Peca como parte do body
        @Part file: MultipartBody.Part // Arquivo como parte do body
    )

}