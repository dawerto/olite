package com.olite.app.ui.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olite.app.databinding.ItemAdminUsuarioBinding
import com.olite.app.model.dto.UsuarioDTO

class AdminUsuarioAdapter(
    private var usuarios: List<UsuarioDTO>,
    private val onEliminar: (UsuarioDTO) -> Unit
) : RecyclerView.Adapter<AdminUsuarioAdapter.UsuarioVH>() {

    inner class UsuarioVH(val binding: ItemAdminUsuarioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioVH {
        val binding = ItemAdminUsuarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UsuarioVH(binding)
    }

    override fun onBindViewHolder(holder: UsuarioVH, position: Int) {
        val u = usuarios[position]

        val nombreCompleto = "${u.nombre ?: ""} ${u.apellidos ?: ""}".trim()
        holder.binding.tvNombre.text = if (nombreCompleto.isEmpty()) "Sin nombre" else nombreCompleto
        holder.binding.tvEmail.text = u.email ?: "Sin email"

        // Inicial del nombre (primera letra)
        holder.binding.tvInicial.text = (u.nombre?.firstOrNull()?.toString() ?: "?").uppercase()

        // Rol: mostrar corto (USER/ADMIN)
        val rolCorto = when (u.rol) {
            "ROLE_ADMIN" -> "ADMIN"
            "ROLE_USER" -> "USER"
            else -> u.rol ?: "---"
        }
        holder.binding.tvRol.text = rolCorto

        // Color badge y avatar según rol
        if (u.rol == "ROLE_ADMIN") {
            holder.binding.tvRol.setBackgroundColor(Color.parseColor("#E0BBBB")) // rosa
            holder.binding.tvInicial.setBackgroundColor(Color.parseColor("#E0BBBB"))
        } else {
            holder.binding.tvRol.setBackgroundColor(Color.parseColor("#D4E5D0")) // verde oliva
            holder.binding.tvInicial.setBackgroundColor(Color.parseColor("#D4E5D0"))
        }

        holder.binding.btnEliminar.setOnClickListener { onEliminar(u) }
    }

    override fun getItemCount(): Int = usuarios.size

    fun actualizarLista(nueva: List<UsuarioDTO>) {
        usuarios = nueva
        notifyDataSetChanged()
    }
}