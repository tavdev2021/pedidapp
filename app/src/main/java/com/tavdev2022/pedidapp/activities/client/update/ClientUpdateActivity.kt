package com.tavdev2022.pedidapp.activities.client.update

import android.app.Activity
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.tavdev2022.pedidapp.databinding.ActivityClientUpdateBinding
import com.tavdev2022.pedidapp.models.ResponseHttp
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.providers.UsersProvider
import com.tavdev2022.pedidapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientUpdateBinding
    var sharedPref: SharedPref? = null
    var user: User? = null
    private var imageFile: File? = null
    private var usersProvider = UsersProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        getUserFromSession()

        binding.edtnameupdate.setText(user?.name)
        binding.edtlastnameupdate.setText(user?.lastname)
        binding.edtphoneupdate.setText(user?.phone)

        if (!user?.image.isNullOrBlank()){
            Glide.with(this).load(user?.image).into(binding.circleImageUser)
        }

        binding.circleImageUser.setOnClickListener {
            selectImage()
        }
        binding.btnUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun getUserFromSession() {
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data
                    imageFile =
                        fileUri?.path?.let { File(it) } //Ruta del archivo que vamos a guardar en el Storage
                    //imageFile = File(fileUri?.path) //Ruta del archivo que vamos a guardar en el Storage
                    binding.circleImageUser.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "La tarea se cancelo", Toast.LENGTH_SHORT).show()
                }
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

    private fun updateData() {

        val name = binding.edtnameupdate.text.toString()
        val lastname = binding.edtlastnameupdate.text.toString()
        val phone = binding.edtphoneupdate.text.toString()

        user?.name = name
        user?.lastname = lastname
        user?.phone = phone

            if (imageFile != null) {
                usersProvider.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp> {
                    override fun onResponse(
                        call: Call<ResponseHttp>,
                        response: Response<ResponseHttp>
                    ) {
                        Log.d(ContentValues.TAG, "RESPONSE: $response")
                        Log.d(ContentValues.TAG, "BODY: ${response.body()}")

                        Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_SHORT).show()

                        saveUserInSession(response.body()?.data.toString())
                    }

                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(ContentValues.TAG, "Error: ${t.message}")
                        Toast.makeText(
                            this@ClientUpdateActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            }
            else{
                usersProvider.updateWithoutImage(user!!)?.enqueue(object : Callback<ResponseHttp> {
                    override fun onResponse(
                        call: Call<ResponseHttp>,
                        response: Response<ResponseHttp>
                    ) {
                        Log.d(ContentValues.TAG, "RESPONSE: $response")
                        Log.d(ContentValues.TAG, "BODY: ${response.body()}")

                        Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_SHORT).show()

                        saveUserInSession(response.body()?.data.toString())
                    }

                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(ContentValues.TAG, "Error: ${t.message}")
                        Toast.makeText(
                            this@ClientUpdateActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            }
    }

    private fun saveUserInSession(data: String) {

        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
    }
}