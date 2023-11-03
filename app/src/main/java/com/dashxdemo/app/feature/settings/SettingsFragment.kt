package com.dashxdemo.app.feature.settings

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.dashx.sdk.DashX
import com.dashx.sdk.DashXLog
import com.dashx.sdk.utils.PermissionUtils
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.StoredPreferences
import com.dashxdemo.app.databinding.FragmentSettingsBinding
import com.dashxdemo.app.feature.home.HomeActivity
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.runOnUiThread
import com.dashxdemo.app.utils.Utils.Companion.showToast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
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
    }

    override fun onResume() {
        super.onResume()
        setUpUi()
    }


    private fun getStoredPreferences() {
        DashX.fetchStoredPreferences(onSuccess = {
            hideProgressBar()
            val preferenceDataJson = it.preferenceData
            preferenceData =  Json { ignoreUnknownKeys = true }.decodeFromJsonElement<StoredPreferences>(preferenceDataJson)
            runOnUiThread {
                binding.saveButton.isEnabled = true
                if (::preferenceData.isInitialized && (preferenceData.newPost.enabled != binding.newPostToggle.isChecked || preferenceData.newBookmark.enabled != binding.bookmarkPostToggle.isChecked)) {
                    setToggles(preferenceData.newBookmark.enabled, preferenceData.newPost.enabled)
                }
            }
        }, onError = {
            DashXLog.e("Error while running 'DashX.fetchStoredPreferences':", it)

            runOnUiThread {
                hideProgressBar()
                showToast(requireContext(), getString(R.string.something_went_wrong))
            }
        })
    }

    private fun checkLocationPermission(): Boolean {
        return PermissionUtils.hasPermissions(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ||
            PermissionUtils.hasPermissions(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return PermissionUtils.hasPermissions(requireActivity(), android.Manifest.permission.POST_NOTIFICATIONS)
    }

    fun setUpUi() {
        showProgressBar()
        getStoredPreferences()

        val hasLocationPermission = checkLocationPermission()
        binding.locationToggle.isChecked = hasLocationPermission

        val hasNotificationPermission = checkNotificationPermission()
        binding.notificationToggle.isChecked = hasNotificationPermission

        if (!hasNotificationPermission) {
            binding.notificationPreferencesTextView.isVisible = false
            binding.bookmarksPostSettingDescription.isVisible = false
            binding.bookmarkPostToggle.isVisible = false
            binding.bookmarksPostSettingTitle.isVisible = false
            binding.newPostToggle.isVisible = false
            binding.newPostSettingDescription.isVisible = false
            binding.newPostSettingTitle.isVisible = false
            binding.saveButton.isVisible = false
            binding.cancelButton.isVisible = false
        } else {
            binding.notificationPreferencesTextView.isVisible = true
            binding.bookmarksPostSettingDescription.isVisible = true
            binding.bookmarkPostToggle.isVisible = true
            binding.bookmarksPostSettingTitle.isVisible = true
            binding.newPostToggle.isVisible = true
            binding.newPostSettingDescription.isVisible = true
            binding.newPostSettingTitle.isVisible = true
            binding.saveButton.isVisible = true
            binding.cancelButton.isVisible = true

            binding.saveButton.setOnClickListener {
                if (::preferenceData.isInitialized && (preferenceData.newPost.enabled != binding.newPostToggle.isChecked || preferenceData.newBookmark.enabled != binding.bookmarkPostToggle.isChecked)) {
                    showProgressBar()
                    preferenceData.newBookmark.enabled = binding.bookmarkPostToggle.isChecked
                    preferenceData.newPost.enabled = binding.newPostToggle.isChecked
                    DashX.saveStoredPreferences(Json.parseToJsonElement(Json.encodeToString(preferenceData)).jsonObject, onSuccess = {
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
                findNavController().popBackStack()
            }
        }

        binding.locationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                (requireActivity() as HomeActivity).askForLocationPermission()
            } else {
                if (checkLocationPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requireActivity().revokeSelfPermissionsOnKill(listOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION))

                    showToast(requireActivity(), "Permission will be revoked when the app is killed.")
                } else {
                    showToast(requireActivity(), "Before Android 13, runtime permissions can only be revoked from settings.")
                }
            }
        }

        binding.notificationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    (requireActivity() as HomeActivity).askForNotificationPermission()
                }
            } else {
                if (checkNotificationPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requireActivity().revokeSelfPermissionOnKill(android.Manifest.permission.POST_NOTIFICATIONS)

                    showToast(requireActivity(), "Permission will be revoked when the app is killed.")
                }
            }

            setUpUi()
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
