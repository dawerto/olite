package com.olite.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.olite.app.databinding.FragmentAdminProductoEditorBinding
import com.olite.app.model.dto.ProductoDTO
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.ProductoViewModel

class AdminProductoEditorFragment : Fragment() {

    private var _binding: FragmentAdminProductoEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    private var idProducto: Int = -1
    private var esEdicion: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminProductoEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        idProducto = arguments?.getInt("idProducto", -1) ?: -1
        esEdicion = idProducto != -1

        configurarTitulo()
        configurarBotones()
        observarViewModel()

        // Si es edición, cargar datos del producto
        if (esEdicion) {
            viewModel.getProductoById(idProducto)
        }
    }

    private fun configurarTitulo() {
        binding.tvTitulo.text = if (esEdicion) "Editar producto" else "Nuevo producto"
    }

    private fun configurarBotones() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardar.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val precioStr = binding.etPrecio.text.toString().trim()
        val stockStr = binding.etStock.text.toString().trim()
        val imagen = binding.etImagen.text.toString().trim()

        // Validaciones
        if (nombre.isEmpty()) {
            Toast.makeText(requireContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (precioStr.isEmpty()) {
            Toast.makeText(requireContext(), "El precio es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (stockStr.isEmpty()) {
            Toast.makeText(requireContext(), "El stock es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.replace(",", ".").toDoubleOrNull()
        if (precio == null || precio < 0) {
            Toast.makeText(requireContext(), "Precio no válido", Toast.LENGTH_SHORT).show()
            return
        }

        val stock = stockStr.toIntOrNull()
        if (stock == null || stock < 0) {
            Toast.makeText(requireContext(), "Stock no válido", Toast.LENGTH_SHORT).show()
            return
        }

        val token = sessionManager.getToken() ?: return

        val producto = ProductoDTO(
            idProducto = if (esEdicion) idProducto else 0,
            nombreProducto = nombre,
            descripcion = if (descripcion.isEmpty()) null else descripcion,
            precio = precio,
            stock = stock,
            imagen = if (imagen.isEmpty()) null else imagen
        )

        if (esEdicion) {
            viewModel.actualizarProducto(idProducto, producto, token)
        } else {
            viewModel.crearProducto(producto, token)
        }
    }

    private fun observarViewModel() {
        // Solo se ejecuta en modo edición
        viewModel.productoDetalle.observe(viewLifecycleOwner) { producto ->
            producto?.let {
                binding.etNombre.setText(it.nombreProducto)
                binding.etDescripcion.setText(it.descripcion ?: "")
                binding.etPrecio.setText(it.precio.toString())
                binding.etStock.setText(it.stock.toString())
                binding.etImagen.setText(it.imagen ?: "")
            }
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) { ok ->
            if (ok == true) {
                val msg = if (esEdicion) "Producto actualizado" else "Producto creado"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                viewModel.resetOperacion()
                findNavController().navigateUp()
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