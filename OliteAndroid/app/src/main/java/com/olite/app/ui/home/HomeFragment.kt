package com.olite.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.olite.app.R
import com.olite.app.databinding.FragmentHomeBinding
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.ProductoViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val productoViewModel: ProductoViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupBanner()
        setupDestacados()
        setupListeners()
        observarErrores()

        productoViewModel.getProductos()
    }

    private fun setupBanner() {
        binding.viewPagerBanner.adapter = BannerAdapter(cantidad = 3)
        binding.indicator.setViewPager(binding.viewPagerBanner)
    }

    private fun setupDestacados() {
        binding.rvDestacados.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        productoViewModel.productos.observe(viewLifecycleOwner) { productos ->
            if (!productos.isNullOrEmpty()) {
                val destacados = productos.take(6)
                binding.rvDestacados.adapter = ProductoHorizontalAdapter(destacados) { producto ->
                    val bundle = Bundle().apply {
                        putInt("idProducto", producto.idProducto)
                    }
                    findNavController().navigate(R.id.productoDetalleFragment, bundle)
                }
            }
        }
    }

    private fun setupListeners() {
        // Chips de categoría
        binding.chipHigiene.setOnClickListener {
            Toast.makeText(requireContext(), "Filtro: Higiene", Toast.LENGTH_SHORT).show()
        }
        binding.chipLimpieza.setOnClickListener {
            Toast.makeText(requireContext(), "Filtro: Limpieza", Toast.LENGTH_SHORT).show()
        }
        binding.chipCuidado.setOnClickListener {
            Toast.makeText(requireContext(), "Filtro: Cuidado", Toast.LENGTH_SHORT).show()
        }

        // Botón Ver más (oferta especial)
        binding.btnVerMas.setOnClickListener {
            Toast.makeText(requireContext(), "Oferta especial", Toast.LENGTH_SHORT).show()
        }

        //  Lupa - selecciona el tab Catálogo del BottomNav (en vez de apilar)
        binding.btnBuscar.setOnClickListener {
            val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
            bottomNav.selectedItemId = R.id.productosFragment
        }

        //  Menú - abre PopupMenu
        binding.btnMenu.setOnClickListener { view ->
            mostrarPopupMenu(view)
        }
    }

    /**
     * Muestra un PopupMenu con opciones dinámicas según si el usuario
     * tiene sesión iniciada o es un invitado.
     */
    private fun mostrarPopupMenu(anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.menu_home, popup.menu)

        val logueado = sessionManager.isLoggedIn()

        // Mostrar/ocultar opciones según estado de sesión
        popup.menu.findItem(R.id.menuLogin).isVisible = !logueado
        popup.menu.findItem(R.id.menuLogout).isVisible = logueado
        popup.menu.findItem(R.id.menuPedidos).isVisible = logueado

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuInicio -> {
                    // Ya estamos en Home, no hacer nada
                    true
                }
                R.id.menuCatalogo -> {
                    val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
                    bottomNav.selectedItemId = R.id.productosFragment
                    true
                }
                R.id.menuPedidos -> {
                    findNavController().navigate(
                        R.id.pedidosFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .build()
                    )
                    true
                }
                R.id.menuPerfil -> {
                    val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
                    bottomNav.selectedItemId = R.id.perfilFragment
                    true
                }

                R.id.menuLogin -> {
                    findNavController().navigate(
                        R.id.loginFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .build()
                    )
                    true
                }

                R.id.menuLogout -> {
                    sessionManager.cerrarSesion()
                    Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    // Recargar la pantalla actual
                    findNavController().navigate(
                        R.id.homeFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.nav_graph, true)
                            .build()
                    )
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun observarErrores() {
        productoViewModel.error.observe(viewLifecycleOwner) { msg ->
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