package com.example.superherosquadmaker.data.api


import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object APIClient {
    private const val BASE_URL = "https://gateway.marvel.com:443"


    fun getRetrofit(context: Context): APIInterface {
        val client = OkHttpClient.Builder()
            .addInterceptor(ConnectivityInterceptor(context))
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

}
