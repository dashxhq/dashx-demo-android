package com.dashxdemo.app.feature.home.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dashxdemo.app.R
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.requests.UpdateProfileRequest
import com.dashxdemo.app.api.responses.UpdateProfileResponse
import com.dashxdemo.app.databinding.FragmentProfileBinding
import com.dashxdemo.app.pref.AppPref
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.validateNameFields
import com.dashxdemo.app.utils.Utils.Companion.validateEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        initProgressDialog(progressDialog, requireContext())

        setupUi()
        setDataToInputFields()
    }

    private fun setDataToInputFields() {
        val dataString = AppPref(requireContext()).getUserData()

        binding.firstNameEditText.setText(dataString.userData.firstName)
        binding.lastNameEditText.setText(dataString.userData.lastName)
        binding.emailEditText.setText(dataString.userData.email)
    }

    private fun setupUi() {
        binding.editButton.setOnClickListener {
            enableEditMode(true)
        }

        binding.firstNameEditText.addTextChangedListener {
            binding.firstNameTextInput.isErrorEnabled = false
        }

        binding.lastNameEditText.addTextChangedListener {
            binding.lastNameTextInput.isErrorEnabled = false
        }

        binding.emailEditText.addTextChangedListener {
            binding.emailTextInput.isErrorEnabled = false
        }

        binding.updateButton.setOnClickListener {
            if (validateFields()) {
                showDialog()
                updateProfile()
            }
        }
    }

    private fun enableEditMode(enabled: Boolean) {
        binding.editButton.isVisible = !enabled

        binding.firstNameEditText.isEnabled = enabled
        binding.firstNameEditText.isFocusable = enabled
        binding.firstNameEditText.isFocusableInTouchMode = enabled

        binding.lastNameEditText.isEnabled = enabled
        binding.lastNameEditText.isFocusable = enabled
        binding.lastNameEditText.isFocusableInTouchMode = enabled

        binding.emailEditText.isEnabled = enabled
        binding.emailEditText.isFocusable = enabled
        binding.emailEditText.isFocusableInTouchMode = enabled

        binding.updateButton.isVisible = enabled
    }

    private fun updateProfile() {
        ApiClient.getInstance(requireContext()).updateProfile(
            UpdateProfileRequest(
                binding.firstNameEditText.text.toString(),
                binding.lastNameEditText.text.toString(),
                binding.emailEditText.text.toString()
            ), object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    hideDialog()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigateUp()
                    } else {
                        try {
                            Toast.makeText(
                                requireContext(),
                                getErrorMessageFromJson(response.errorBody()?.string()),
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (exception: Exception) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    hideDialog()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    )
                }
            }
        )
    }

    private fun validateFields(): Boolean {
        return validateNameFields(
            binding.firstNameTextInput,
            binding.lastNameTextInput,
            requireContext()
        ) && validateEmail(
            binding.emailEditText.text.toString(),
            binding.emailTextInput,
            requireContext()
        )
    }

    private fun showDialog() {
        progressDialog.show()
    }

    private fun hideDialog() {
        progressDialog.dismiss()
    }
}
