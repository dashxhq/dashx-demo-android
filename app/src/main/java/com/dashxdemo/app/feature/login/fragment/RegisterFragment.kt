package com.dashxdemo.app.feature.login.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dashxdemo.app.R
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.requests.RegisterRequest
import com.dashxdemo.app.api.responses.RegisterResponse
import com.dashxdemo.app.databinding.FragmentRegisterBinding
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.validateEmail
import com.dashxdemo.app.utils.Utils.Companion.validateNameFields
import com.dashxdemo.app.utils.Utils.Companion.validatePassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        initProgressDialog(progressDialog, requireContext())
        setupUi()
    }

    private fun setupUi() {

        binding.emailEditText.addTextChangedListener {
            binding.emailTextInput.isErrorEnabled = false
        }

        binding.passwordEditText.addTextChangedListener {
            binding.passwordTextInput.isErrorEnabled = false
        }

        binding.firstNameEditText.addTextChangedListener {
            binding.firstNameTextInput.isErrorEnabled = false
        }

        binding.lastNameEditText.addTextChangedListener {
            binding.lastNameTextInput.isErrorEnabled = false
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.registerButton.setOnClickListener {
            if (validateFields()) {
                showDialog()
                registerUser()
            }
        }

    }

    private fun registerUser() {
        ApiClient.getInstance(requireContext()).register(
            RegisterRequest(
                email = binding.emailEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                firstName = binding.firstNameEditText.text.toString(),
                lastName = binding.lastNameEditText.text.toString()
            ), object :
                Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    hideDialog()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getErrorMessageFromJson(response.errorBody()?.string()),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    t.printStackTrace()
                    hideDialog()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
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
        ) && validatePassword(
            binding.passwordEditText.text.toString(),
            binding.passwordTextInput,
            requireContext()
        )
    }

    private fun showDialog() {
        initProgressDialog(progressDialog, requireContext())
        progressDialog.show()
    }

    private fun hideDialog() {
        progressDialog.dismiss()
    }
}
