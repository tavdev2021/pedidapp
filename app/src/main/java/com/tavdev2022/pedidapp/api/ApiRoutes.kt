package com.tavdev2022.pedidapp.api

import com.tavdev2022.pedidapp.routes.UsersRoutes
import retrofit2.Retrofit

class ApiRoutes {

    val API_URL = "http://192.168.137.1:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes {
        return  retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }

}