package com.example.myproject

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

//Api Server Connection with interface classes


interface ApiInterface {

    //Login
    @POST("api/auth/login")
    fun loginData(
        @Body info: ModelLogin
    ): retrofit2.Call<ResponseLogin>

    //Dashoboard
    @POST("api/auth/dashboard")
    fun dashboardData(
        @Body info: ModelDashboard
    ): retrofit2.Call<ResponseDashboard>

}


class RetrofitInstance(applicationContext: Context, BASE_URL: String) {

    private val sharedPrefName = "login"
    //  lateinit var context: Context

val BASE_URL = BASE_URL


    val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    var gson = GsonBuilder()
        .setLenient()
        .create()


    fun getRetrofitInstance(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

}
