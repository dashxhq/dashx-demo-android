package com.dashxdemo.app.feature.settings

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dashx.sdk.DashX
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.StoredPreferences
import com.dashxdemo.app.api.responses.StoredPreferencesResponse
import com.dashxdemo.app.databinding.FragmentSettingsBinding
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.runOnUiThread
import com.dashxdemo.app.utils.Utils.Companion.showToast
import com.google.gson.Gson

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var preferenceData: StoredPreferences
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        initProgressDialog(progressDialog, requireContext())

        setUpUi()
        showProgressBar()
        getStoredPreferences()
    }

    private fun getStoredPreferences() {
        DashX.fetchStoredPreferences(onSuccess = {
            hideProgressBar()
            val preferenceDataJson = Gson().fromJson(it, StoredPreferencesResponse::class.java).preferenceData
            preferenceData = Gson().fromJson(preferenceDataJson, StoredPreferences::class.java)
            runOnUiThread {
                binding.saveButton.isEnabled = true
                if (::preferenceData.isInitialized && (preferenceData.newPost.enabled != binding.newPostToggle.isChecked || preferenceData.newBookmark.enabled != binding.bookmarkPostToggle.isChecked)) {
                    setToggles(preferenceData.newBookmark.enabled, preferenceData.newPost.enabled)
                }
            }
        }, onError = {
            runOnUiThread {
                hideProgressBar()
                showToast(requireContext(), getString(R.string.something_went_wrong))
            }
        })
    }

    private fun setUpUi() {
        binding.saveButton.setOnClickListener {
            if (::preferenceData.isInitialized && (preferenceData.newPost.enabled != binding.newPostToggle.isChecked || preferenceData.newBookmark.enabled != binding.bookmarkPostToggle.isChecked)) {
                showProgressBar()
                preferenceData.newBookmark.enabled = binding.bookmarkPostToggle.isChecked
                preferenceData.newPost.enabled = binding.newPostToggle.isChecked
                DashX.saveStoredPreferences(Gson().toJson(preferenceData, StoredPreferences::class.java), onSuccess = {
                    runOnUiThread {
                        hideProgressBar()
                        showToast(requireContext(), getString(R.string.preferences_saved))
                    }
                }, onError = {
                    runOnUiThread {
                        hideProgressBar()
                        showToast(requireContext(), getString(R.string.something_went_wrong))
                    }
                })
            }
        }

        binding.cancelButton.setOnClickListener {
            if (::preferenceData.isInitialized && (preferenceData.newPost.enabled != binding.newPostToggle.isChecked || preferenceData.newBookmark.enabled != binding.bookmarkPostToggle.isChecked)) {
                setToggles(preferenceData.newBookmark.enabled, preferenceData.newPost.enabled)
            }
        }
    }

    private fun showProgressBar() {
        progressDialog.show()
    }

    private fun hideProgressBar() {
        progressDialog.dismiss()
    }

    private fun setToggles(isNewBookmarkToggleEnabled: Boolean, isNewPostToggleEnabled: Boolean) {
        binding.bookmarkPostToggle.isChecked = isNewBookmarkToggleEnabled
        binding.newPostToggle.isChecked = isNewPostToggleEnabled
    }
}
