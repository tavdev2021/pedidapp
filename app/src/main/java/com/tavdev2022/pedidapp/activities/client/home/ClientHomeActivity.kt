package com.tavdev2022.pedidapp.activities.client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.tavdev2022.pedidapp.activities.MainActivity
import com.tavdev2022.pedidapp.databinding.ActivityClientHomeBinding
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.utils.SharedPref

class ClientHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientHomeBinding
    private var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        binding.btnLogout.setOnClickListener {
            logout()
        }
        getUserFromSession()

    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d("ClientHomeActivity", "Usuario $user")
        }
    }
}