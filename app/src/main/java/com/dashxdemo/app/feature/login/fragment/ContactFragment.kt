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
import com.dashxdemo.app.api.requests.ContactRequest
import com.dashxdemo.app.api.responses.ContactResponse
import com.dashxdemo.app.databinding.FragmentContactBinding
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.validateFeedbackEditFields
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactFragment : Fragment() {
    private lateinit var binding: FragmentContactBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        Utils.initProgressDialog(progressDialog, requireContext())
        initListeners()
    }

    private fun initListeners() {
        binding.nameEditText.addTextChangedListener {
            binding.nameTextInput.isErrorEnabled = false
        }

        binding.emailEditText.addTextChangedListener {
            binding.emailTextInput.isErrorEnabled = false
        }

        binding.feedbackEditText.addTextChangedListener {
            binding.feedbackTextInput.isErrorEnabled = false
        }

        binding.goBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.submitButton.setOnClickListener {
            if (validateFields()) {
                showDialog()
                sendFeedback()
            }
        }
    }

    private fun validateFields(): Boolean {
        return validateFeedbackEditFields(
            binding.nameTextInput,
            binding.feedbackTextInput,
            binding.emailTextInput,
            requireContext()
        )
    }

    private fun sendFeedback() {
        ApiClient.getInstance(requireContext()).contact(
            ContactRequest(
                binding.nameEditText.text.toString(),
                binding.emailEditText.text.toString(),
                binding.feedbackEditText.text.toString()
            ), object :
                Callback<ContactResponse> {
                override fun onResponse(
                    call: Call<ContactResponse>,
                    response: Response<ContactResponse>
                ) {
                    hideDialog()
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(requireContext(), Utils.getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    hideDialog()
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun showDialog() {
        Utils.initProgressDialog(progressDialog, requireContext())
        progressDialog.show()
    }

    private fun hideDialog() {
        progressDialog.dismiss()
    }
}
