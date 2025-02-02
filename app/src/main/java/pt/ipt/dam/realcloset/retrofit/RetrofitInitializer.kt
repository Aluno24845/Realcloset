package pt.ipt.dam.realcloset.retrofit


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.ipt.dam.realcloset.retrofit.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    val host = "http://192.168.1.29:5000/"  // URL base da API
    val gson: Gson = GsonBuilder().setLenient().create() // Gson para fazer parse flexível de JSON

    // Cria uma instância do Retrofit configurada com o host e conversor Gson
    fun createRetrofit(): Retrofit {
        val client = OkHttpClient.Builder() // OkHttpClient para gerir as requisições HTTP
            .build()

        return Retrofit.Builder()
            .baseUrl(host)
            .client(client)  // Configura o Retrofit com o OkHttpClient personalizado
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    // Função para criar uma instância do serviço da API
    fun apiService(): ApiService = createRetrofit().create(ApiService::class.java)
}


