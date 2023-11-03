package com.dashxdemo.app.feature.location

import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dashxdemo.app.databinding.FragmentFetchLocationBinding
import com.dashxdemo.app.R
import com.dashxdemo.app.utils.Utils.Companion.showToast

class FetchLocation : Fragment() {

    private var location: Location? = null

    private val coarseLocationRequestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            location = getLocation()
            binding.coarseLatitudeTextView.text = location?.latitude.toString()
            binding.coarseLongitudeTextView.text = location?.longitude.toString()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 811)
        }
    }

    private val fineLocationRequestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            location = getLocation()
            binding.fineLatitudeTextView.text = location?.latitude.toString()
            binding.fineLongitudeTextView.text = location?.longitude.toString()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 812)
        }
    }

    private lateinit var binding: FragmentFetchLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFetchLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        binding.coarseLocationButton.setOnClickListener {
            coarseLocationRequestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        binding.fineLocationButton.setOnClickListener {
            fineLocationRequestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            811 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    location = getLocation()
                    binding.coarseLatitudeTextView.text = location?.latitude.toString()
                    binding.coarseLongitudeTextView.text = location?.longitude.toString()
                } else {
                    showToast(requireContext(), getString(R.string.location_permission_denied))
                }
            }

            812 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    location = getLocation()
                    binding.fineLatitudeTextView.text = location?.latitude.toString()
                    binding.fineLongitudeTextView.text = location?.longitude.toString()
                } else {
                    showToast(requireContext(), getString(R.string.location_permission_denied))
                }
            }
        }
    }

    private fun getLocation(): Location? {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager?
        var location: Location? = null

        return locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }
}
