package com.example.apprinconsur.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MicroserviceRetrofitInstance {
    val api: AccesoMicroserviceApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccesoMicroserviceApi::class.java)
    }
}