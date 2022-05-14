package com.tavdev2022.pedidapp.providers

import com.tavdev2022.pedidapp.api.ApiRoutes
import com.tavdev2022.pedidapp.models.ResponseHttp
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.routes.UsersRoutes
import retrofit2.Call

class UsersProvider {

    private  var usersRoutes: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {
        return usersRoutes?.register(user)
    }

    fun login(email: String, password: String): Call<ResponseHttp>? {
        return usersRoutes?.login(email, password)
    }

}