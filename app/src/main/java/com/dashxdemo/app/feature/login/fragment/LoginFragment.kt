package com.dashxdemo.app.feature.login.fragment

import android.app.ProgressDialog
import android.content.Intent
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
import com.dashxdemo.app.api.requests.LoginRequest
import com.dashxdemo.app.api.responses.LoginResponse
import com.dashxdemo.app.api.responses.UserData
import com.dashxdemo.app.databinding.FragmentLoginBinding
import com.dashxdemo.app.feature.home.HomeActivity
import com.dashxdemo.app.pref.AppPref
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.validateEmail
import com.dashxdemo.app.utils.Utils.Companion.validatePassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        initProgressDialog(progressDialog, requireContext())
        setupUi()
    }

    private fun setupUi() {

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }

        binding.forgotPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_forgot_password)
        }

        binding.loginButton.setOnClickListener {
            if (validateFields()) {
                showDialog()
                loginUser()
            }
        }

        binding.emailEditText.addTextChangedListener {
            binding.emailTextInput.isErrorEnabled = false
        }
    }

    private fun loginUser() {
        ApiClient.getInstance(requireContext()).login(
            LoginRequest(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            ), object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    hideDialog()
                    if (response.isSuccessful) {
                        AppPref(requireContext()).setUserData(
                            UserData(
                                binding.emailEditText.text.toString(),
                                response.body()?.token
                            )
                        )
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            Utils.getErrorMessageFromJson(response.errorBody()?.string()),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
        return validateEmail(
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
        progressDialog.show()
    }

    private fun hideDialog() {
        progressDialog.dismiss()
    }
}
