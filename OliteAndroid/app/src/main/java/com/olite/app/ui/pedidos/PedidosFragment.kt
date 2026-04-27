package com.olite.app.ui.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.olite.app.databinding.FragmentPedidosBinding
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.PedidoViewModel

class PedidosFragment : Fragment() {

    private var _binding: FragmentPedidosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PedidoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: PedidoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        configurarRecyclerView()
        observarViewModel()

        cargarPedidos()
    }

    override fun onResume() {
        super.onResume()
        // Recargamos cada vez que se entra a la pantalla
        cargarPedidos()
    }

    private fun configurarRecyclerView() {
        adapter = PedidoAdapter(emptyList())
        binding.rvPedidos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPedidos.adapter = adapter
    }

    private fun cargarPedidos() {
        val token = sessionManager.getToken() ?: return
        val idUsuario = sessionManager.getIdUsuario()
        if (idUsuario != -1) {
            viewModel.getPedidosUsuario(idUsuario, token)
        }
    }

    private fun observarViewModel() {
        viewModel.pedidos.observe(viewLifecycleOwner) { pedidos ->
            val lista = pedidos.orEmpty()
            adapter.actualizarLista(lista)

            if (lista.isEmpty()) {
                binding.layoutVacio.visibility = View.VISIBLE
                binding.rvPedidos.visibility = View.GONE
                binding.tvSubtitulo.text = "Tu historial de compras"
            } else {
                binding.layoutVacio.visibility = View.GONE
                binding.rvPedidos.visibility = View.VISIBLE
                binding.tvSubtitulo.text = "${lista.size} pedido(s) realizados"
            }
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