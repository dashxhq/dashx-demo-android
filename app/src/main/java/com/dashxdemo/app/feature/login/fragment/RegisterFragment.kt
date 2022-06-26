package com.dashxdemo.app.feature.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.dashxdemo.app.databinding.FragmentRegisterBinding
import com.dashxdemo.app.utils.Utils.Companion.isValidString


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

        val emailId = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        binding.emailEditText.addTextChangedListener {
            binding.emailTextInput.isErrorEnabled = false
        }

        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            validateEmail(emailId)
        }

        binding.registerButton.setOnClickListener {
            validatePassword(password)
        }
    }

    private fun validateEmail(emailId: String) {
        if (emailId.isEmpty()) {
            binding.emailTextInput.isErrorEnabled = true
            binding.emailTextInput.error = "Email is required"
        } else if (!isValidString(emailId)) {
            binding.emailTextInput.isErrorEnabled = true
            binding.emailTextInput.error = "Please enter valid Email Id."
        } else {
            binding.emailTextInput.isErrorEnabled = false
            binding.emailTextInput.error = null
        }
    }

    private fun validatePassword(password: String): Boolean {

        when {
            password.isEmpty() -> {
                binding.passwordTextInput.isErrorEnabled = true
                binding.passwordTextInput.error = "Password is required"
                return false
            }
            !password.matches(".*[A-Z].*".toRegex()) -> {
                binding.passwordTextInput.isErrorEnabled = true
                binding.passwordTextInput.error =
                    "Password must contain atleast one uppercase letter"
                return false
            }
            !password.matches(".*[a-z].*".toRegex()) -> {
                binding.passwordTextInput.isErrorEnabled = true
                binding.passwordTextInput.error =
                    "Password must contain atleast one lowercase letter"
                return false
            }
            !password.matches(".*[!@#$%^&*+=/?].*".toRegex()) -> {
                binding.passwordTextInput.isErrorEnabled = true
                binding.passwordTextInput.error =
                    "Password must contain atleast one special character"
                return false
            }
            !password.matches(".*[0-9].*".toRegex()) -> {
                binding.passwordTextInput.isErrorEnabled = true
                binding.passwordTextInput.error = "Password must contain atleast one number"
                return false
            }
            !password.matches("\\S+\$".toRegex()) -> {
                binding.passwordTextInput.isErrorEnabled = true
                binding.passwordTextInput.error = "Password must not contain whitespaces"
                return false
            }
            !password.matches(".{6,12}".toRegex()) -> {
                binding.passwordTextInput.isErrorEnabled = true
                binding.passwordTextInput.error =
                    "password length must be in between 4 to 12 characters"
                return false
            }
            else -> {
                binding.passwordTextInput.isErrorEnabled = false
                binding.passwordTextInput.error = null
                return true
            }
        }
    }
}