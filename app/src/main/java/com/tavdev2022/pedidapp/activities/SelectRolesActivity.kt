package com.tavdev2022.pedidapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tavdev2022.pedidapp.R
import com.tavdev2022.pedidapp.adapters.RolesAdapter
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.utils.SharedPref

class SelectRolesActivity : AppCompatActivity() {

    private var recyclerViewRoles: RecyclerView? = null
    private var user: User? = null
    private var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles = findViewById(R.id.recyclerView_roles)
        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapter(this, user?.roles!!)
        recyclerViewRoles?.adapter = adapter
    }

    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()) {
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}