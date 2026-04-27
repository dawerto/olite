package com.olite.app.ui.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.olite.app.R
import com.olite.app.databinding.FragmentCarritoBinding
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.CarritoViewModel
import com.olite.app.viewmodel.PedidoViewModel

class CarritoFragment : Fragment() {

    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarritoViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: CarritoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        // Si no hay login, mostrar pantalla "inicia sesión"
        if (!sessionManager.isLoggedIn()) {
            mostrarNoLogueado()
            return
        }

        configurarRecyclerView()
        observarViewModel()
        configurarBotones()

        cargarCarrito()
    }

    override fun onResume() {
        super.onResume()
        if (sessionManager.isLoggedIn()) {
            cargarCarrito()
        } else {
            mostrarNoLogueado()
        }
    }

    private fun configurarRecyclerView() {
        adapter = CarritoAdapter(emptyList()) { producto ->
            eliminarProducto(producto.idProducto)
        }
        binding.rvCarrito.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCarrito.adapter = adapter
    }

    private fun configurarBotones() {
        binding.btnVaciar.setOnClickListener {
            val token = sessionManager.getToken() ?: return@setOnClickListener
            val idUsuario = sessionManager.getIdUsuario()
            viewModel.vaciarCarrito(idUsuario, token)
        }

        binding.btnRealizarPedido.setOnClickListener {
            val token = sessionManager.getToken() ?: return@setOnClickListener
            val idUsuario = sessionManager.getIdUsuario()
            if (idUsuario != -1) {
                pedidoViewModel.realizarPedido(idUsuario, "Tarjeta", token)
            }
        }
    }

    private fun cargarCarrito() {
        val token = sessionManager.getToken() ?: return
        val idUsuario = sessionManager.getIdUsuario()
        if (idUsuario != -1) {
            viewModel.getCarrito(idUsuario, token)
        }
    }

    private fun eliminarProducto(idProducto: Int) {
        val token = sessionManager.getToken() ?: return
        val idUsuario = sessionManager.getIdUsuario()
        viewModel.eliminarDelCarrito(idUsuario, idProducto, token)
    }

    private fun observarViewModel() {
        viewModel.carrito.observe(viewLifecycleOwner) { carrito ->
            val productos = carrito?.productos.orEmpty()
            adapter.actualizarLista(productos)

            if (productos.isEmpty()) {
                binding.layoutVacio.visibility = View.VISIBLE
                binding.rvCarrito.visibility = View.GONE
                binding.cardFooter.visibility = View.GONE
                binding.tvNumProductos.text = "0 productos"
            } else {
                binding.layoutVacio.visibility = View.GONE
                binding.rvCarrito.visibility = View.VISIBLE
                binding.cardFooter.visibility = View.VISIBLE

                val totalUnidades = productos.sumOf { it.cantidad }
                binding.tvNumProductos.text = "$totalUnidades producto(s)"

                val total = productos.sumOf { it.cantidad * it.precioUnidad }
                binding.tvTotal.text = String.format("%.2f€", total)
            }
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) { ok ->
            if (ok == true) {
                Toast.makeText(requireContext(), "Carrito vaciado", Toast.LENGTH_SHORT).show()
                cargarCarrito()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        pedidoViewModel.pedidoRealizado.observe(viewLifecycleOwner) { pedido ->
            if (pedido != null) {
                Toast.makeText(
                    requireContext(),
                    "✓ Pedido #${pedido.idPedido} realizado correctamente",
                    Toast.LENGTH_LONG
                ).show()
                // Navegar a Pedidos limpiando el back stack para que BottomNav funcione correctamente
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.carritoFragment, true)
                    .build()
                findNavController().navigate(R.id.pedidosFragment, null, navOptions)

                // Resetear el LiveData para que no se vuelva a disparar al rotar pantalla
                pedidoViewModel.resetPedidoRealizado()
            }
        }

        pedidoViewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Muestra la pantalla para usuarios no logueados (modo invitado).
     * Oculta la lista y el footer, y reutiliza el layoutVacio para el mensaje.
     */
    private fun mostrarNoLogueado() {
        binding.layoutVacio.visibility = View.VISIBLE
        binding.rvCarrito.visibility = View.GONE
        binding.cardFooter.visibility = View.GONE
        binding.tvNumProductos.text = "Sin sesión iniciada"

        val tvTitulo = binding.layoutVacio.getChildAt(1) as? android.widget.TextView
        val tvSubtitulo = binding.layoutVacio.getChildAt(2) as? android.widget.TextView
        tvTitulo?.text = "Inicia sesión para ver tu carrito"
        tvSubtitulo?.text = "Necesitas tener una cuenta para comprar"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}