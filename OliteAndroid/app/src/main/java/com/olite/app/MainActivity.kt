package com.olite.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.olite.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el NavController desde el NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Enganchar el BottomNavigationView al NavController.
        binding.bottomNavigationView.setupWithNavController(navController)

        // Listener: ocultar BottomNav en pantallas donde no tiene sentido
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility = when (destination.id) {
                R.id.productoDetalleFragment,
                R.id.adminProductosFragment,
                R.id.adminProductoEditorFragment,
                R.id.adminUsuariosFragment,
                R.id.adminPedidosFragment -> View.GONE
                else -> View.VISIBLE
            }
        }
    }
}