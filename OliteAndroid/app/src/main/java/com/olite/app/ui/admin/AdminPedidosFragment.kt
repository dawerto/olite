package com.olite.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.olite.app.databinding.FragmentAdminPedidosBinding
import com.olite.app.model.dto.PedidoDTO
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.PedidoViewModel

class AdminPedidosFragment : Fragment() {

    private var _binding: FragmentAdminPedidosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PedidoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: AdminPedidoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        configurarRecyclerView()
        configurarBotones()
        observarViewModel()
    }

    override fun onResume() {
        super.onResume()
        cargarPedidos()
    }

    private fun configurarRecyclerView() {
        adapter = AdminPedidoAdapter(emptyList()) { pedido ->
            mostrarDialogoEstado(pedido)
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPedidos.adapter = adapter
    }

    private fun configurarBotones() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun cargarPedidos() {
        val token = sessionManager.getToken() ?: return
        viewModel.getTodosPedidos(token)
    }

    private fun mostrarDialogoEstado(pedido: PedidoDTO) {
        val opciones = arrayOf("PENDIENTE", "ENVIADO", "FINALIZADO")
        val estadoActual = pedido.estadoPedido.uppercase()
        val indexActual = opciones.indexOf(estadoActual).coerceAtLeast(0)

        AlertDialog.Builder(requireContext())
            .setTitle("Estado del pedido #${pedido.idPedido}")
            .setSingleChoiceItems(opciones, indexActual) { dialog, which ->
                val nuevoEstado = opciones[which]
                if (nuevoEstado != estadoActual) {
                    cambiarEstado(pedido.idPedido, nuevoEstado)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cambiarEstado(idPedido: Int, nuevoEstado: String) {
        val token = sessionManager.getToken() ?: return
        viewModel.cambiarEstadoPedido(idPedido, nuevoEstado, token)
    }

    private fun observarViewModel() {
        viewModel.pedidos.observe(viewLifecycleOwner) { lista ->
            val pedidos = lista.orEmpty()
            adapter.actualizarLista(pedidos)
            binding.tvContador.text = "${pedidos.size} pedido(s) totales"
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}