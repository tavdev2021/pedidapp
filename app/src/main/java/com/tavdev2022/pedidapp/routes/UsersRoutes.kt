package com.tavdev2022.pedidapp.routes

import com.tavdev2022.pedidapp.models.ResponseHttp
import com.tavdev2022.pedidapp.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersRoutes {

    @POST("users/create")
    fun register(@Body user: User): Call<ResponseHttp>
}