package com.dashxdemo.app.feature.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.dashxdemo.app.R
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.requests.RegisterRequest
import com.dashxdemo.app.api.responses.RegisterResponse
import com.dashxdemo.app.databinding.FragmentRegisterBinding
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.isValidEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
    }

    private fun setUpUi() {

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
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.code() < 300) {
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG)
                            .show()
                        findNavController().navigateUp()
                    }
                    else{
                        Toast.makeText(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun validateFields(): Boolean {
        return validateEmail(binding.emailEditText.text.toString()) && validatePassword() && validateNameFields()
    }

    private fun validatePassword(): Boolean {
        if(binding.passwordEditText.text.isNullOrEmpty()) {
            binding.passwordTextInput.isErrorEnabled = true
            binding.passwordTextInput.error = "Password is required"
        }
        return !binding.passwordEditText.text.isNullOrEmpty()
    }

    private fun validateNameFields(): Boolean {
        if (binding.firstNameEditText.text.isNullOrEmpty()) {
            binding.firstNameTextInput.isErrorEnabled = true
            binding.firstNameTextInput.error = "First name is required"
            return false
        }

        if (binding.lastNameEditText.text.isNullOrEmpty()) {
            binding.lastNameTextInput.isErrorEnabled = true
            binding.lastNameTextInput.error = "Last name is required"
            return false
        }
        return true
    }

    private fun validateEmail(emailId: String): Boolean {
        if (emailId.isEmpty()) {
            binding.emailTextInput.isErrorEnabled = true
            binding.emailTextInput.error = "Email is required"
        } else if (!isValidEmail(emailId)) {
            binding.emailTextInput.isErrorEnabled = true
            binding.emailTextInput.error = "Please enter valid Email Id."
        } else {
            binding.emailTextInput.isErrorEnabled = false
            binding.emailTextInput.error = null
            return true
        }
        return false
    }
}