package com.olite.app.ui.productos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olite.app.databinding.ItemProductoGridBinding
import com.olite.app.model.dto.ProductoDTO

class ProductoGridAdapter(
    private var productos: List<ProductoDTO>,
    private val onClick: (ProductoDTO) -> Unit
) : RecyclerView.Adapter<ProductoGridAdapter.ProductoVH>() {

    inner class ProductoVH(val binding: ItemProductoGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoVH {
        val binding = ItemProductoGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductoVH(binding)
    }

    override fun onBindViewHolder(holder: ProductoVH, position: Int) {
        val producto = productos[position]
        holder.binding.tvNombreProducto.text = producto.nombreProducto
        holder.binding.tvPrecioProducto.text = String.format("%.2f€", producto.precio)
        holder.binding.tvStock.text = "Stock: ${producto.stock}"

        if (!producto.imagen.isNullOrEmpty()) {
            Glide.with(holder.binding.imgProducto)
                .load(producto.imagen)
                .into(holder.binding.imgProducto)
        }

        holder.itemView.setOnClickListener { onClick(producto) }
    }

    override fun getItemCount(): Int = productos.size

    // Para actualizar la lista al filtrar
    fun actualizarLista(nuevaLista: List<ProductoDTO>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }
}