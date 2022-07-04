package com.tavdev2022.pedidapp.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.tavdev2022.pedidapp.activities.client.home.ClientHomeActivity
import com.tavdev2022.pedidapp.activities.delivery.home.DeliveryHomeActivity
import com.tavdev2022.pedidapp.activities.restaurant.home.RestaurantHomeActivity
import com.tavdev2022.pedidapp.databinding.ActivityMainBinding
import com.tavdev2022.pedidapp.models.ResponseHttp
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.providers.UsersProvider
import com.tavdev2022.pedidapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var usersProvider = UsersProvider()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToRegister.setOnClickListener {
            goToRegister()
        }

        binding.btnlogin.setOnClickListener {
            login()
        }

        getUserFromSession()
    }

    private fun login() {
        val email = binding.edtemaillogin.text.toString()
        val password = binding.edtpasswordlogin.text.toString()

        if (isValidform(email, password)) {

            usersProvider.login(email, password)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d("MainActivity", "Response: ${response.body()}")

                    if (response.body()?.isSuccess == true) {

                        saveUserInSession(response.body()?.data.toString())
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        binding.edtemaillogin.text = null
                        binding.edtpasswordlogin.text = null
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                    Log.d("MainActivity", "Hubo un error ${t.message}")
                }

            })
        }
    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun goToRestaurantHome() {
        val i = Intent(this, RestaurantHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun goToDeliveryHome() {
        val i = Intent(this, DeliveryHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun goToSelectRol() {
        val i = Intent(this, SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun saveUserInSession(data: String) {

        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)

        if (user.roles?.size!! > 1) { //TIENE MAS DE UN ROL
            goToSelectRol()
        }
        else{ //SOLO TIENE UN ROL (CLIENTE)
            goToClientHome()
        }
    }

    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        //val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION
            //val user = gson.fromJson(sharedPref.getData("user"), User::class.java)

            if (!sharedPref.getData("rol").isNullOrBlank()) {
                //SI EL USUARIO SELECCIONO EL ROL

                when (sharedPref.getData("rol")?.replace("\"","")) {
                    "RESTAURANTE" -> {
                        goToRestaurantHome()
                    }
                    "CLIENTE" -> {
                        goToClientHome()
                    }
                    "REPARTIDOR" -> {
                        goToDeliveryHome()
                    }
                }
            }
            else {
                goToClientHome()
            }
        }
    }

    private fun String.isEmailValid(): Boolean {

        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidform(email: String, password: String): Boolean {

      if (email.isBlank()){
          Toast.makeText(this, "Debes escribir un correo electronico", Toast.LENGTH_SHORT).show()
          return false
      }

        if (password.isBlank()){
            Toast.makeText(this, "Debes escribir una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.isEmailValid()) {
            Toast.makeText(this, "El Email ingresado no es valido", Toast.LENGTH_SHORT).show()
           return false
        }

        return true
    }
}