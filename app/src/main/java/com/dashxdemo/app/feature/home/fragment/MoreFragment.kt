package com.dashxdemo.app.feature.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dashx.sdk.DashXClient
import com.dashxdemo.app.R
import com.dashxdemo.app.databinding.FragmentMoreBinding
import com.dashxdemo.app.feature.login.LoginActivity
import com.dashxdemo.app.pref.AppPref

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding

    private val DashX = DashXClient.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        binding.navBilling.setOnClickListener {
            findNavController().navigate(R.id.action_nav_more_to_nav_billing)
        }

        binding.navSettings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_more_to_nav_settings)
        }

        binding.navProfile.setOnClickListener {
            findNavController().navigate(R.id.action_nav_more_to_nav_profile)
        }

        binding.navLocation.setOnClickListener {
            findNavController().navigate(R.id.action_nav_more_to_nav_fetch_location)
        }

        binding.navLogout.setOnClickListener {
            DashX.reset()
            logout()
        }
    }

    private fun logout() {
        AppPref(requireContext()).clearPref()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
