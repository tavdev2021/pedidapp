package com.tavdev2022.pedidapp.activities.restaurant.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.tavdev2022.pedidapp.R
import com.tavdev2022.pedidapp.activities.MainActivity
import com.tavdev2022.pedidapp.databinding.ActivityRestaurantHomeBinding
import com.tavdev2022.pedidapp.fragments.client.ClientCategoriesFragment
import com.tavdev2022.pedidapp.fragments.client.ClientProfileFragment
import com.tavdev2022.pedidapp.fragments.restaurant.RestaurantOrdersFragment
import com.tavdev2022.pedidapp.fragments.restaurant.RestaurantProductFragment
import com.tavdev2022.pedidapp.fragments.restaurant.RestauratCategoryFragment
import com.tavdev2022.pedidapp.models.User
import com.tavdev2022.pedidapp.utils.SharedPref

class RestaurantHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantHomeBinding
    private var sharedPref: SharedPref? = null
    private var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        //binding.btnLogout.setOnClickListener {
        // logout()
        //}

        openFragment(RestaurantOrdersFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    openFragment(RestaurantOrdersFragment())
                    true
                }

                R.id.item_category -> {
                    openFragment(RestauratCategoryFragment())
                    true
                }

                R.id.item_products -> {
                    openFragment(RestaurantProductFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }

        getUserFromSession()
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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