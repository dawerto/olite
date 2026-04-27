package com.olite.app.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olite.app.databinding.ItemAdminProductoBinding
import com.olite.app.model.dto.ProductoDTO

class AdminProductoAdapter(
    private var productos: List<ProductoDTO>,
    private val onEditar: (ProductoDTO) -> Unit,
    private val onEliminar: (ProductoDTO) -> Unit
) : RecyclerView.Adapter<AdminProductoAdapter.AdminProductoVH>() {

    inner class AdminProductoVH(val binding: ItemAdminProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductoVH {
        val binding = ItemAdminProductoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AdminProductoVH(binding)
    }

    override fun onBindViewHolder(holder: AdminProductoVH, position: Int) {
        val producto = productos[position]
        holder.binding.tvNombre.text = producto.nombreProducto
        holder.binding.tvPrecio.text = String.format("%.2f€", producto.precio)
        holder.binding.tvStock.text = "Stock: ${producto.stock}"

        if (!producto.imagen.isNullOrEmpty()) {
            Glide.with(holder.binding.imgProducto)
                .load(producto.imagen)
                .into(holder.binding.imgProducto)
        }

        holder.binding.btnEditar.setOnClickListener { onEditar(producto) }
        holder.binding.btnEliminar.setOnClickListener { onEliminar(producto) }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<ProductoDTO>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }
}