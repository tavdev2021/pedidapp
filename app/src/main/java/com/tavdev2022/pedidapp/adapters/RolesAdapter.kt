package com.tavdev2022.pedidapp.adapters

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tavdev2022.pedidapp.R
import com.tavdev2022.pedidapp.activities.client.home.ClientHomeActivity
import com.tavdev2022.pedidapp.activities.delivery.home.DeliveryHomeActivity
import com.tavdev2022.pedidapp.activities.restaurant.home.RestaurantHomeActivity
import com.tavdev2022.pedidapp.models.Rol
import com.tavdev2022.pedidapp.utils.SharedPref

class RolesAdapter(val context: Activity, private val roles: ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    private val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)

        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {

        val rol = roles[position] //CADA ROL

        holder.textviewRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageViewRol)

        holder.itemView.setOnClickListener {
            goToRol(rol)
        }
    }

    private fun goToRol(rol: Rol) {
        when (rol.name) {
            "RESTAURANTE" -> {

                sharedPref.save("rol", "RESTAURANTE")

                val i = Intent(context, RestaurantHomeActivity::class.java)
                i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
                context.startActivity(i)
            }
            "CLIENTE" -> {

                sharedPref.save("rol", "CLIENTE")

                val i = Intent(context, ClientHomeActivity::class.java)
                i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
                context.startActivity(i)
            }
            "REPARTIDOR" -> {

                sharedPref.save("rol", "REPARTIDOR")

                val i = Intent(context, DeliveryHomeActivity::class.java)
                i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //Eliminar el historial de pantallas
                context.startActivity(i)
            }
        }
    }

    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textviewRol: TextView
        val imageViewRol: ImageView

        init {
            textviewRol = view.findViewById(R.id.textview_rol)
            imageViewRol = view.findViewById(R.id.imageview_rol)
        }
    }
}