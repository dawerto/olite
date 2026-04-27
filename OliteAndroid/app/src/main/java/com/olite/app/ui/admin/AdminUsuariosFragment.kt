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
import com.olite.app.databinding.FragmentAdminUsuariosBinding
import com.olite.app.model.dto.UsuarioDTO
import com.olite.app.utils.SessionManager
import com.olite.app.viewmodel.UsuarioViewModel

class AdminUsuariosFragment : Fragment() {

    private var _binding: FragmentAdminUsuariosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsuarioViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: AdminUsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUsuariosBinding.inflate(inflater, container, false)
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
        cargarUsuarios()
    }

    private fun configurarRecyclerView() {
        adapter = AdminUsuarioAdapter(emptyList()) { usuario ->
            confirmarEliminar(usuario)
        }
        binding.rvUsuarios.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsuarios.adapter = adapter
    }

    private fun configurarBotones() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun cargarUsuarios() {
        val token = sessionManager.getToken() ?: return
        viewModel.getUsuarios(token)
    }

    private fun confirmarEliminar(usuario: UsuarioDTO) {
        // Protección: el admin no puede eliminarse a sí mismo
        if (usuario.idUsuario == sessionManager.getIdUsuario()) {
            Toast.makeText(
                requireContext(),
                "No puedes eliminar tu propio usuario",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val nombreCompleto = "${usuario.nombre ?: ""} ${usuario.apellidos ?: ""}".trim()
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar usuario")
            .setMessage("¿Seguro que quieres eliminar a \"$nombreCompleto\"?\n\nEsta acción también eliminará su carrito y pedidos.")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                eliminarUsuario(usuario.idUsuario ?: return@setPositiveButton)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarUsuario(id: Int) {
        val token = sessionManager.getToken() ?: return
        viewModel.eliminarUsuario(id, token)
    }

    private fun observarViewModel() {
        viewModel.usuarios.observe(viewLifecycleOwner) { lista ->
            val usuarios = lista.orEmpty()
            adapter.actualizarLista(usuarios)
            binding.tvContador.text = "${usuarios.size} usuario(s) registrados"
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) { ok ->
            if (ok == true) {
                Toast.makeText(requireContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show()
                cargarUsuarios()
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