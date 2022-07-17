package com.tavdev2022.pedidapp.activities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.tavdev2022.pedidapp.activities.client.home.ClientHomeActivity
import com.tavdev2022.pedidapp.databinding.ActivitySaveImageBinding
import com.tavdev2022.pedidapp.models.ResponseHttp
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.providers.UsersProvider
import com.tavdev2022.pedidapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySaveImageBinding

    private var imageFile: File? = null

    private var usersProvider = UsersProvider()
    private var user: User? = null
    private var sharedPref: SharedPref? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)
        getUserFromSession()
        
        binding.circleImageUser.setOnClickListener { 
            selectImage()
        }

        binding.btnNext.setOnClickListener {
            goToClientHome()
        }

        binding.btnConfirm.setOnClickListener {
            saveImage()
        }
    }

    private fun saveImage() {

        if (imageFile != null && user != null) {
            usersProvider.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(
                        this@SaveImageActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
        else {
            Toast.makeText(this, "La imagen y el usuario no pueden ser nulos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserInSession(data: String) {

        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
        goToClientHome()
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //Ruta del archivo que vamos a guardar en el Storage
                binding.circleImageUser.setImageURI(fileUri)
            }
            else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "La tarea se cancelo", Toast.LENGTH_SHORT).show()
            }
    }
    
    private fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
               startImageForResult.launch(intent)
            }
    }

    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}