package com.dashxdemo.app.feature.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dashxdemo.app.R
import com.dashxdemo.app.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    private fun setupUi() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

        binding.bottomNavView.setOnItemSelectedListener {
            navController.navigate(
                it.itemId,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.nav_home, false)
                    .build()
            )
            true
        }

        setupNavListener(navController)
    }

    private fun setupNavListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home, R.id.nav_store, R.id.nav_bookmarks, R.id.nav_more -> {
                    binding.bottomNavView.isVisible = true
                }
                else -> binding.bottomNavView.isVisible = false
            }
        }
    }
}
