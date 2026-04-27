package com.olite.app.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.olite.app.R
import com.olite.app.databinding.FragmentPerfilBinding
import com.olite.app.model.dto.UsuarioDTO
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.UsuarioViewModel

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsuarioViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        // Si no hay login, mostrar pantalla "inicia sesión" y no continuar
        if (!sessionManager.isLoggedIn()) {
            mostrarNoLogueado()
            return
        }

        cargarPerfil()
        observarViewModel()
        configurarBotones()
    }

    private fun cargarPerfil() {
        val token = sessionManager.getToken()
        val idUsuario = sessionManager.getIdUsuario()

        if (token != null && idUsuario != -1) {
            viewModel.getUsuarioById(idUsuario, token)
        } else {
            Toast.makeText(requireContext(), "Sesión no válida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observarViewModel() {
        // Cuando llegan los datos del usuario, los pintamos en los campos
        viewModel.usuarioActual.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                binding.tvEmail.text = it.email
                binding.etNombre.setText(it.nombre)
                binding.etApellidos.setText(it.apellidos)
                binding.etDireccion.setText(it.direccion ?: "")
                binding.etTelefono.setText(it.telefono ?: "")
            }
        }

        // Cuando se actualiza correctamente
        viewModel.operacionExitosa.observe(viewLifecycleOwner) { ok ->
            if (ok == true) {
                Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
            }
        }

        // Errores
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarBotones() {
        // Botón Guardar cambios
        binding.btnGuardar.setOnClickListener {
            val token = sessionManager.getToken() ?: return@setOnClickListener
            val idUsuario = sessionManager.getIdUsuario()

            val nombre = binding.etNombre.text.toString().trim()
            val apellidos = binding.etApellidos.text.toString().trim()

            if (nombre.isEmpty() || apellidos.isEmpty()) {
                Toast.makeText(requireContext(), "Nombre y apellidos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuarioActualizado = UsuarioDTO(
                idUsuario = idUsuario,
                nombre = nombre,
                apellidos = apellidos,
                direccion = binding.etDireccion.text.toString().trim(),
                telefono = binding.etTelefono.text.toString().trim(),
                email = binding.tvEmail.text.toString(),
                rol = sessionManager.getRol()
            )

            viewModel.actualizarUsuario(idUsuario, usuarioActualizado, token)
        }

        // Botón Cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            sessionManager.cerrarSesion()
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                R.id.loginFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
            )
        }

        // Botones de administración (visibles solo si rol=ADMIN)
        if (sessionManager.getRol() == "ROLE_ADMIN") {
            binding.btnPanelAdmin.visibility = View.VISIBLE
            binding.btnAdminUsuarios.visibility = View.VISIBLE
            binding.btnAdminPedidos.visibility = View.VISIBLE

            binding.btnPanelAdmin.setOnClickListener {
                findNavController().navigate(R.id.adminProductosFragment)
            }
            binding.btnAdminUsuarios.setOnClickListener {
                findNavController().navigate(R.id.adminUsuariosFragment)
            }
            binding.btnAdminPedidos.setOnClickListener {
                findNavController().navigate(R.id.adminPedidosFragment)
            }
        }
    }

    /**
     * Muestra la pantalla para usuarios no logueados (modo invitado).
     * Reutiliza el mismo layout del perfil pero oculta los campos del usuario
     * y transforma el botón "Guardar" en "Iniciar sesión".
     */
    private fun mostrarNoLogueado() {
        binding.tvEmail.text = "No has iniciado sesión"
        binding.etNombre.visibility = View.GONE
        binding.etApellidos.visibility = View.GONE
        binding.etDireccion.visibility = View.GONE
        binding.etTelefono.visibility = View.GONE
        binding.btnGuardar.text = "Iniciar sesión"
        binding.btnCerrarSesion.visibility = View.GONE

        binding.btnGuardar.setOnClickListener {
            findNavController().navigate(
                R.id.loginFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.perfilFragment, true)
                    .build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}