package com.dashxdemo.app.feature.home.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dashxdemo.app.R
import com.dashxdemo.app.databinding.FragmentHomeBinding
import com.dashxdemo.app.databinding.FragmentLoginBinding
import com.dashxdemo.app.feature.home.HomeActivity
import com.dashxdemo.app.feature.login.LoginActivity
import com.dashxdemo.app.pref.AppPref

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        binding.logoutButton.setOnClickListener {
            AppPref(requireContext()).clearPref()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
