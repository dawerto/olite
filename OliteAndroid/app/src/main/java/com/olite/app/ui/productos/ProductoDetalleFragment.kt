package com.olite.app.ui.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.olite.app.databinding.FragmentProductoDetalleBinding
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.CarritoViewModel
import com.olite.app.viewmodel.ProductoViewModel
import com.olite.app.R

class ProductoDetalleFragment : Fragment() {

    private var _binding: FragmentProductoDetalleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductoViewModel by viewModels()
    private val carritoViewModel: CarritoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    private var idProducto: Int = -1
    private var cantidad: Int = 1
    private var stockDisponible: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductoDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        // Obtener el ID del producto pasado como argumento
        idProducto = arguments?.getInt("idProducto") ?: -1

        if (idProducto == -1) {
            Toast.makeText(requireContext(), "Producto no válido", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        configurarBotones()
        observarViewModel()

        // Cargar el producto desde el backend
        viewModel.getProductoById(idProducto)
    }

    private fun configurarBotones() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSumar.setOnClickListener {
            if (cantidad < stockDisponible) {
                cantidad++
                binding.tvCantidad.text = cantidad.toString()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No hay más stock disponible",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnRestar.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                binding.tvCantidad.text = cantidad.toString()
            }
        }

        binding.btnAnadirCarrito.setOnClickListener {
            if (!sessionManager.isLoggedIn()) {
                // Invitado: pedirle iniciar sesión
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Inicia sesión")
                    .setMessage("Para añadir productos al carrito necesitas tener una cuenta.")
                    .setPositiveButton("Iniciar sesión") { _, _ ->
                        findNavController().navigate(
                            R.id.loginFragment,
                            null,
                            androidx.navigation.NavOptions.Builder()
                                .setPopUpTo(R.id.productoDetalleFragment, true)
                                .build()
                        )
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
                return@setOnClickListener
            }

            // Usuario logueado: añadir normal
            val token = sessionManager.getToken() ?: return@setOnClickListener
            val idUsuario = sessionManager.getIdUsuario()
            if (idUsuario != -1) {
                carritoViewModel.añadirAlCarrito(idUsuario, idProducto, cantidad, token)
            }
        }
    }

    private fun observarViewModel() {
        // Observar el detalle del producto
        viewModel.productoDetalle.observe(viewLifecycleOwner) { producto ->
            producto?.let {
                binding.tvNombre.text = it.nombreProducto
                binding.tvPrecio.text = String.format("%.2f€", it.precio)
                binding.tvStock.text = "${it.stock} unidades disponibles"
                binding.tvDescripcion.text = it.descripcion ?: "Sin descripción"
                stockDisponible = it.stock

                if (!it.imagen.isNullOrEmpty()) {
                    Glide.with(binding.imgProducto)
                        .load(it.imagen)
                        .into(binding.imgProducto)
                }

                // Si no hay stock, deshabilitar botones
                if (it.stock <= 0) {
                    binding.btnAnadirCarrito.isEnabled = false
                    binding.btnAnadirCarrito.text = "Sin stock"
                    binding.btnSumar.isEnabled = false
                    binding.btnRestar.isEnabled = false
                }
            }
        }

        // Errores del ProductoViewModel
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        // Confirmación de añadir al carrito
        carritoViewModel.operacionExitosa.observe(viewLifecycleOwner) { ok ->
            if (ok == true) {
                Toast.makeText(
                    requireContext(),
                    "✓ Añadido al carrito",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Errores del CarritoViewModel
        carritoViewModel.error.observe(viewLifecycleOwner) { msg ->
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