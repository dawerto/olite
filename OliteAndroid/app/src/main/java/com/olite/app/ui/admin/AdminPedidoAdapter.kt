package com.olite.app.ui.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olite.app.databinding.ItemAdminPedidoBinding
import com.olite.app.model.dto.PedidoDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AdminPedidoAdapter(
    private var pedidos: List<PedidoDTO>,
    private val onPedidoClick: (PedidoDTO) -> Unit
) : RecyclerView.Adapter<AdminPedidoAdapter.PedidoVH>() {

    inner class PedidoVH(val binding: ItemAdminPedidoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoVH {
        val binding = ItemAdminPedidoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PedidoVH(binding)
    }

    override fun onBindViewHolder(holder: PedidoVH, position: Int) {
        val pedido = pedidos[position]
        holder.binding.tvNumPedido.text = "Pedido #${pedido.idPedido}"
        holder.binding.tvFecha.text = formatearFecha(pedido.fechaPedido)
        holder.binding.tvTotal.text = String.format("%.2f€", pedido.total)
        holder.binding.tvMetodoPago.text = "Pago: ${pedido.metodoPago}"

        val numProductos = pedido.detalles.sumOf { it.cantidad }
        holder.binding.tvNumProductos.text = "$numProductos producto(s)"

        holder.binding.tvEstado.text = pedido.estadoPedido
        val color = when (pedido.estadoPedido.uppercase()) {
            "PENDIENTE" -> Color.parseColor("#E0BBBB")
            "ENVIADO" -> Color.parseColor("#D4E5D0")
            "FINALIZADO", "PAGADO" -> Color.parseColor("#C4E0F5")
            else -> Color.parseColor("#E8E6E1")
        }
        holder.binding.tvEstado.setBackgroundColor(color)

        holder.binding.root.setOnClickListener { onPedidoClick(pedido) }
    }

    override fun getItemCount(): Int = pedidos.size

    fun actualizarLista(nueva: List<PedidoDTO>) {
        pedidos = nueva
        notifyDataSetChanged()
    }

    private fun formatearFecha(fechaIso: String): String {
        return try {
            val ldt = LocalDateTime.parse(fechaIso)
            ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        } catch (e: Exception) {
            fechaIso
        }
    }
}