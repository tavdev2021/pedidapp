package com.tavdev2022.pedidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.tavdev2022.pedidapp.databinding.ActivityRegisterBinding
import com.tavdev2022.pedidapp.models.ResponseHttp
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    var userProvider = UsersProvider()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToLogin.setOnClickListener {
            goToLogin()
            finish()
        }

        binding.btnregister.setOnClickListener {
            register()
        }

    }

    private fun register() {
        val name = binding.edtnamegister.text.toString()
        val lastname = binding.edtlastnameregister.text.toString()
        val email = binding.edtemailregister.text.toString()
        val phone = binding.edtphoneregister.text.toString()
        val password = binding.edtpasswordregister.text.toString()
        val confirmpassword = binding.edtconfirmpassword.text.toString()

        if (isValidform(name, lastname, email, phone, password, confirmpassword)) {
            val user = User(
                name = name,
                lastname = lastname,
                email = email,
                phone = phone,
                password = password
            )

            userProvider.register(user)?.enqueue(object: Callback<ResponseHttp> {

                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    binding.edtnamegister.text = null
                    binding.edtlastnameregister.text = null
                    binding.edtemailregister.text = null
                    binding.edtphoneregister.text = null
                    binding.edtpasswordregister.text = null
                    binding.edtconfirmpassword.text = null

                    Log.d("RegisterActivity","Response: $response")
                    Log.d("RegisterActivity","Body: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()

                    Log.d("RegisterActivity","Se produjo un error ${t.message}")
                }

            })
        }

    }

    private fun String.isEmailValid(): Boolean {

        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidform(
        name: String,
        lastname: String,
        email: String,
        phone: String,
        password: String,
        confirmpassword: String
    ): Boolean {

        if (name.isBlank()){
            Toast.makeText(this, "Debes escribir el Nombre", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastname.isBlank()){
            Toast.makeText(this, "Debes escribir el Apellido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isBlank()){
            Toast.makeText(this, "Debes escribir un correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.isBlank()){
            Toast.makeText(this, "Debes escribir un numero de telefono", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isBlank()){
            Toast.makeText(this, "Debes escribir una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmpassword.isBlank()){
            Toast.makeText(this, "Debes volver a escribir contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.isEmailValid()) {
            Toast.makeText(this, "El Email ingresado no es valido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmpassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}