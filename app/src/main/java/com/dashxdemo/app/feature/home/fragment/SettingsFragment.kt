package com.dashxdemo.app.feature.home.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dashx.sdk.DashXClient
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.StoredPreferenceResponse
import com.dashxdemo.app.databinding.FragmentSettingsBinding
import com.google.gson.Gson


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var preferenceData: StoredPreferenceResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
        getStoredPreferences()
    }

    private fun getStoredPreferences() {
        DashXClient.getInstance().identify("19")

        DashXClient.getInstance().fetchStoredPreferences(onSuccess = {
            Log.d("dsa","ad")
            preferenceData = Gson().fromJson(it,StoredPreferenceResponse::class.java)

            Handler(Looper.getMainLooper()).post {
                binding.saveButton.isEnabled = true
                binding.bookmarkPostToggle.isChecked = preferenceData.newBookmark.enabled
                binding.createPostToggle.isChecked = preferenceData.newPost.enabled
            }

        },
        onError = {
            Toast.makeText(requireContext(),getString(R.string.something_went_wrong),Toast.LENGTH_LONG).show()
        })
    }

    private fun setUpUi() {
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            if(::preferenceData.isInitialized && (preferenceData.newPost.enabled != binding.createPostToggle.isChecked || preferenceData.newBookmark.enabled != binding.bookmarkPostToggle.isChecked)){
                preferenceData.newBookmark.enabled = binding.bookmarkPostToggle.isChecked
                preferenceData.newPost.enabled = binding.createPostToggle.isChecked
                DashXClient.getInstance().saveStoredPreferences(Gson().toJsonTree(preferenceData,StoredPreferenceResponse::class.java), {
                    Log.d("n","d")
                    Toast.makeText(requireContext(),getString(R.string.first_name_required_text),Toast.LENGTH_LONG).show()
                }, {
                    Log.d("n","d")
                    Toast.makeText(requireContext(),getString(R.string.something_went_wrong),Toast.LENGTH_LONG).show()
                })
            }
        }
    }
}
