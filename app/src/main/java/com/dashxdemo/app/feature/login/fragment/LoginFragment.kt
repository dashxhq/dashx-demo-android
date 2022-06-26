package com.dashxdemo.app.feature.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.dashxdemo.app.R
import com.dashxdemo.app.databinding.FragmentLoginBinding
import com.dashxdemo.app.utils.Utils

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

        binding.editTextPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val emailId = binding.editTextEmail.text.toString().trim()
                validateEmail(emailId)
            }
        }

        binding.editTextEmail.addTextChangedListener {
            binding.emailField.isErrorEnabled = false
        }
    }

    private fun validateEmail(emailId: String) {

        if (emailId.isEmpty()) {
            binding.emailField.isErrorEnabled = true
            binding.emailField.error = "Email is required"

        } else if (!Utils.isValidString(emailId)) {
            binding.emailField.isErrorEnabled = true
            binding.emailField.error = "Please enter valid email id"

        } else {
            binding.emailField.isErrorEnabled = false
            binding.emailField.error = null

        }
    }
}