package com.olite.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.olite.app.R
import com.olite.app.databinding.FragmentRegisterBinding
import com.olite.app.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegistrar.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val nombre = binding.etNombre.text.toString().trim()
            val apellidos = binding.etApellidos.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.register(email, password, nombre, apellidos)
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginFragment)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}