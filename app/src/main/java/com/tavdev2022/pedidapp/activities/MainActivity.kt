package com.tavdev2022.pedidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.tavdev2022.pedidapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToRegister.setOnClickListener {
            goToRegister()
            finish()
        }

        binding.btnlogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.edtemaillogin.text.toString()
        val password = binding.edtpasswordlogin.text.toString()

        if (isValidform(email, password)) {
            Toast.makeText(this, "El formulario es valido", Toast.LENGTH_SHORT).show()
            binding.edtemaillogin.text = null
            binding.edtpasswordlogin.text = null
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

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}