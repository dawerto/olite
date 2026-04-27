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
import com.olite.app.R
import com.olite.app.databinding.FragmentAdminProductosBinding
import com.olite.app.model.dto.ProductoDTO
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.ProductoViewModel

class AdminProductosFragment : Fragment() {

    private var _binding: FragmentAdminProductosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: AdminProductoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminProductosBinding.inflate(inflater, container, false)
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
        // Recargamos la lista cada vez que volvemos a la pantalla
        viewModel.getProductos()
    }

    private fun configurarRecyclerView() {
        adapter = AdminProductoAdapter(
            emptyList(),
            onEditar = { producto -> abrirEditor(producto.idProducto) },
            onEliminar = { producto -> confirmarEliminar(producto) }
        )
        binding.rvAdminProductos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAdminProductos.adapter = adapter
    }

    private fun configurarBotones() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNuevoProducto.setOnClickListener {
            // -1 indica "crear nuevo"
            abrirEditor(-1)
        }
    }

    private fun abrirEditor(idProducto: Int) {
        val bundle = Bundle().apply {
            putInt("idProducto", idProducto)
        }
        findNavController().navigate(R.id.adminProductoEditorFragment, bundle)
    }

    private fun confirmarEliminar(producto: ProductoDTO) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar producto")
            .setMessage("¿Seguro que quieres eliminar \"${producto.nombreProducto}\"?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                eliminarProducto(producto.idProducto)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarProducto(id: Int) {
        val token = sessionManager.getToken() ?: return
        viewModel.eliminarProducto(id, token)
    }

    private fun observarViewModel() {
        viewModel.productos.observe(viewLifecycleOwner) { productos ->
            val lista = productos.orEmpty()
            adapter.actualizarLista(lista)
            binding.tvNumProductos.text = "${lista.size} producto(s) en el catálogo"
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) { ok ->
            if (ok == true) {
                Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show()
                viewModel.resetOperacion()
                viewModel.getProductos()
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