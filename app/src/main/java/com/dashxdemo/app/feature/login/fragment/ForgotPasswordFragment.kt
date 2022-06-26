package com.dashxdemo.app.feature.login.fragment

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
import com.dashxdemo.app.api.requests.ForgotPasswordRequest
import com.dashxdemo.app.api.responses.ForgotPasswordResponse
import com.dashxdemo.app.databinding.FragmentForgotPasswordBinding
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.isValidEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            if (validateEmail()) {
                forgotPassword()
            }
        }

    }

    private fun forgotPassword() {
        ApiClient.getInstance(requireContext()).forgotPassword(
            ForgotPasswordRequest(
                binding.emailEditText.text.toString()
            ), object : Callback<ForgotPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    if (response.code() < 300) {
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(requireContext(), Utils.getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun validateEmail(): Boolean {

        val emailId = binding.emailEditText.text.toString().trim()

        if (emailId.isEmpty()) {
            binding.emailTextInput.isErrorEnabled = true
            binding.emailTextInput.error = getString(R.string.email_required_text)
        } else if (!isValidEmail(emailId)) {
            binding.emailTextInput.isErrorEnabled = true
            binding.emailTextInput.error = getString(R.string.valid_email_text)
        } else {
            binding.emailTextInput.isErrorEnabled = false
            binding.emailTextInput.error = null
            return true
        }
        return false
    }
}