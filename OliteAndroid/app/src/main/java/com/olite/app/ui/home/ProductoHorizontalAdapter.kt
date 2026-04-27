package com.olite.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olite.app.R
import com.olite.app.databinding.ItemProductoHorizontalBinding
import com.olite.app.model.dto.ProductoDTO

class ProductoHorizontalAdapter(
    private val productos: List<ProductoDTO>,
    private val onClick: (ProductoDTO) -> Unit
) : RecyclerView.Adapter<ProductoHorizontalAdapter.ProductoVH>() {

    inner class ProductoVH(val binding: ItemProductoHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoVH {
        val binding = ItemProductoHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductoVH(binding)
    }

    override fun onBindViewHolder(holder: ProductoVH, position: Int) {
        val producto = productos[position]
        holder.binding.tvNombreProducto.text = producto.nombreProducto
        holder.binding.tvPrecioProducto.text = String.format("%.2f€", producto.precio)

        if (!producto.imagen.isNullOrEmpty()) {
            android.util.Log.d("OLITE_DEBUG", "Cargando imagen: ${producto.imagen}")
            Glide.with(holder.binding.imgProducto)
                .load(producto.imagen)
                .into(holder.binding.imgProducto)
        }

        holder.itemView.setOnClickListener { onClick(producto) }
    }

    override fun getItemCount(): Int = productos.size
}