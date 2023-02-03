package com.tavdev2022.pedidapp.api

import com.tavdev2022.pedidapp.routes.UsersRoutes

class ApiRoutes {

    private val API_URL = "http://192.168.43.215:3000/api/"
    private val retrofit = RetrofitClient()

    fun getUsersRoutes(): UsersRoutes {
        return  retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }

}