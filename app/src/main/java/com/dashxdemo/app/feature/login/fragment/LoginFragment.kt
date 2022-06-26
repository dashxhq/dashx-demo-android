package com.dashxdemo.app.feature.login.fragment

import android.content.Intent
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
import com.dashxdemo.app.api.requests.LoginRequest
import com.dashxdemo.app.api.responses.LoginResponse
import com.dashxdemo.app.api.responses.UserData
import com.dashxdemo.app.databinding.FragmentLoginBinding
import com.dashxdemo.app.feature.home.HomeActivity
import com.dashxdemo.app.pref.AppPref
import com.dashxdemo.app.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
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
            if(validateFields()){
                loginUser()
            }
        }

        binding.editTextEmail.addTextChangedListener {
            binding.emailField.isErrorEnabled = false
        }
    }

    private fun loginUser() {
        ApiClient.getInstance(requireContext()).login(LoginRequest(binding.editTextEmail.text.toString(),binding.editTextPassword.text.toString()),object :Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                AppPref(requireContext()).setUserData(UserData(binding.editTextEmail.text.toString(),response.body()?.token))
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(requireContext(),"login error",Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun validateFields(): Boolean{
        return validateEmail(binding.editTextEmail.text.toString())
    }

    private fun validateEmail(emailId: String): Boolean {
        if (emailId.isEmpty()) {
            binding.emailField.isErrorEnabled = true
            binding.emailField.error = "Email is required"

        } else if (!Utils.isValidEmail(emailId)) {
            binding.emailField.isErrorEnabled = true
            binding.emailField.error = "Please enter valid email id"
        } else {
            binding.emailField.isErrorEnabled = false
            binding.emailField.error = null
            return true
        }
        return false
    }
}