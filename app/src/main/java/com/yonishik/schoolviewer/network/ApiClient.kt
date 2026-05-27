package com.yonishik.schoolviewer.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // private const val BASE_URL = "http://10.0.2.2:8080/"
    private const val BASE_URL = "https://helidon-school-app.onrender.com/"

    val schoolApi: SchoolApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SchoolApi::class.java)
    }
}
