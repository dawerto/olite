package com.olite.app.ui.productos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.olite.app.databinding.FragmentProductosBinding
import com.olite.app.model.dto.ProductoDTO
import com.olite.app.viewmodel.ProductoViewModel
import androidx.navigation.fragment.findNavController
import com.olite.app.R

class ProductosFragment : Fragment() {

    private var _binding: FragmentProductosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductoViewModel by viewModels()
    private lateinit var adapter: ProductoGridAdapter
    private var listaCompleta: List<ProductoDTO> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRecyclerView()
        configurarBuscador()
        observarViewModel()

        viewModel.getProductos()
    }

    private fun configurarRecyclerView() {
        adapter = ProductoGridAdapter(emptyList()) { producto ->
            val bundle = Bundle().apply {
                putInt("idProducto", producto.idProducto)
            }
            findNavController().navigate(R.id.productoDetalleFragment, bundle)
        }
        binding.rvProductos.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProductos.adapter = adapter
    }

    private fun configurarBuscador() {
        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrarProductos(s?.toString().orEmpty())
            }
        })
    }

    private fun filtrarProductos(query: String) {
        val filtrados = if (query.isBlank()) {
            listaCompleta
        } else {
            listaCompleta.filter {
                it.nombreProducto.contains(query, ignoreCase = true)
            }
        }
        adapter.actualizarLista(filtrados)
        binding.tvSinResultados.visibility =
            if (filtrados.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun observarViewModel() {
        viewModel.productos.observe(viewLifecycleOwner) { productos ->
            if (productos != null) {
                listaCompleta = productos
                adapter.actualizarLista(productos)
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