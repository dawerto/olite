package com.olite.app.ui.pedidos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olite.app.databinding.ItemPedidoBinding
import com.olite.app.model.dto.PedidoDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PedidoAdapter(
    private var pedidos: List<PedidoDTO>
) : RecyclerView.Adapter<PedidoAdapter.PedidoVH>() {

    inner class PedidoVH(val binding: ItemPedidoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoVH {
        val binding = ItemPedidoBinding.inflate(
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

        // Color del badge del estado según el estado
        holder.binding.tvEstado.text = pedido.estadoPedido
        val color = when (pedido.estadoPedido.uppercase()) {
            "PENDIENTE" -> Color.parseColor("#E0BBBB")  // rosa
            "ENVIADO" -> Color.parseColor("#D4E5D0")    // verde
            "FINALIZADO", "PAGADO" -> Color.parseColor("#C4E0F5") // azul claro
            else -> Color.parseColor("#E8E6E1")         // gris
        }
        holder.binding.tvEstado.setBackgroundColor(color)
    }

    override fun getItemCount(): Int = pedidos.size

    fun actualizarLista(nuevaLista: List<PedidoDTO>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }

    /**
     * Formatea fecha del backend (LocalDateTime ISO) a formato "dd/MM/yyyy HH:mm".
     * Si la fecha llega malformada, devuelve el original.
     */
    private fun formatearFecha(fechaIso: String): String {
        return try {
            val ldt = LocalDateTime.parse(fechaIso)
            ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        } catch (e: Exception) {
            fechaIso
        }
    }
}