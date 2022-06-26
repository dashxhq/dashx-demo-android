package com.dashxdemo.app.feature.login.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.dashxdemo.app.utils.Utils.Companion.isValidString
import com.dashxdemo.app.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater)
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

        binding.forgotPasswordButton.setOnClickListener {
            validateEmail()
        }

    }

    private fun validateEmail() {

        val emailId = binding.emailEditText.text.toString().trim()

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
}