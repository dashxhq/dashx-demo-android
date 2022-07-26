package com.dashxdemo.app.feature.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dashx.sdk.DashXClient
import com.dashxdemo.app.databinding.FragmentHomeBinding
import com.dashxdemo.app.pref.AppPref

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val appPref by lazy { AppPref(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DashXClient.getInstance().identify(appPref.getUserData().userData.id.toString())
    }
}
