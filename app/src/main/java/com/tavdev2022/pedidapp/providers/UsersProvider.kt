package com.tavdev2022.pedidapp.providers

import com.tavdev2022.pedidapp.api.ApiRoutes
import com.tavdev2022.pedidapp.models.ResponseHttp
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

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

    fun update(file: File, user: User): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJason())

        return usersRoutes?.update(image, requestBody)
    }

}