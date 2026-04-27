package com.olite.app.ui.carrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olite.app.databinding.ItemCarritoBinding
import com.olite.app.model.dto.CarritoProductoDTO

class CarritoAdapter(
    private var productos: List<CarritoProductoDTO>,
    private val onEliminar: (CarritoProductoDTO) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoVH>() {

    inner class CarritoVH(val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoVH {
        val binding = ItemCarritoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CarritoVH(binding)
    }

    override fun onBindViewHolder(holder: CarritoVH, position: Int) {
        val item = productos[position]
        holder.binding.tvNombre.text = item.nombreProducto
        holder.binding.tvCantidad.text = "Cantidad: ${item.cantidad}"
        val subtotal = item.cantidad * item.precioUnidad
        holder.binding.tvSubtotal.text = String.format("%.2f€", subtotal)

        if (!item.imagen.isNullOrEmpty()) {
            Glide.with(holder.binding.imgProducto)
                .load(item.imagen)
                .into(holder.binding.imgProducto)
        }

        holder.binding.btnEliminar.setOnClickListener { onEliminar(item) }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<CarritoProductoDTO>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }
}