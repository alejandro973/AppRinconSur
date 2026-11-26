package com.example.apprinconsur.data.remote

import retrofit2.http.*

interface AccesoMicroserviceApi {

    @GET("api/registros")
    suspend fun listarRegistros(): List<AccesoRegistroDto>

    @POST("api/registros")
    suspend fun crearRegistro(@Body registro: AccesoRegistroDto): AccesoRegistroDto

    @PUT("api/registros/{id}")
    suspend fun actualizarRegistro(
        @Path("id") id: Long,
        @Body registro: AccesoRegistroDto
    ): AccesoRegistroDto

    @DELETE("api/registros/{id}")
    suspend fun eliminarRegistro(@Path("id") id: Long)
}