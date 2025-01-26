package pt.ipt.dam.realcloset.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pt.ipt.dam.realcloset.retrofit.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    val host = "http://192.168.1.78:5000/"  // URL base da API

    private val gson:Gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    fun apiService(): ApiService = retrofit.create(ApiService::class.java)
}


