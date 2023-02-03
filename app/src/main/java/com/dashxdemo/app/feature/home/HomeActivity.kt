package com.dashxdemo.app.feature.home

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dashx.sdk.DashXClient
import com.dashx.sdk.DashXLog
import com.dashx.sdk.utils.PermissionUtils
import com.dashxdemo.app.R
import com.dashxdemo.app.databinding.ActivityHomeBinding
import com.dashxdemo.app.feature.settings.SettingsFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val locationRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationRequestCode) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                DashXClient.getInstance().track("Location Permission Changed")

                val settingsFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentSettings) as SettingsFragment?

                settingsFragment?.binding?.locationToggle?.isChecked = true
            }
        }
    }

    fun askForLocationPermission() {
        PermissionUtils.requestPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION, locationRequestCode)
    }

    private fun setupUi() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentView) as NavHostFragment
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
