package com.dashxdemo.app.feature.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dashx.sdk.DashXClient
import com.dashxdemo.app.R
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.requests.UpdateProfileRequest
import com.dashxdemo.app.api.responses.ProfileResponse
import com.dashxdemo.app.api.responses.UpdateProfileResponse
import com.dashxdemo.app.databinding.DialogToSelectImageBinding
import com.dashxdemo.app.databinding.FragmentProfileBinding
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.validateEmail
import com.dashxdemo.app.utils.Utils.Companion.validateNameFields
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private const val ALL_PERMISSIONS = 1
private const val CAMERA_REQUEST_CODE = 100
private const val GALLERY_REQUEST_CODE = 200

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var dialogBinding: DialogToSelectImageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        initProgressDialog(progressDialog, requireContext())

        setupUi()
        showDialog()
        getProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = data?.data
                DashXClient.getInstance().uploadExternalAsset(File(selectedImage?.path), "e8b7b42f-1f23-431c-b739-9de0fba3dadf", onSuccess = {
                    Log.d("ds", it.toString())
                }, onError = {
                    Log.d("sda", it)
                })
            }
            1 -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = data?.data
                DashXClient.getInstance().uploadExternalAsset(File(selectedImage?.path), "e8b7b42f-1f23-431c-b739-9de0fba3dadf", onSuccess = {
                    Log.d("asd", it.toString())
                }, onError = {
                    Log.d("dsa", it)
                })
                binding.imageView.setImageURI(selectedImage)
            }
        }
    }

    private fun setDataToInputFields(firstName: String, lastName: String, email: String, avatar: String) {
        binding.firstNameEditText.setText(firstName)
        binding.lastNameEditText.setText(lastName)
        binding.emailEditText.setText(email)

        if (avatar.isNullOrEmpty()) {
            binding.imageView.setImageResource(R.drawable.icon_profile)
        } else {
            Glide.with(requireContext()).load(avatar).into(binding.imageView)
        }
    }

    private fun setupUi() {
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

        binding.addImageButton.setOnClickListener {
            dialogBinding = DialogToSelectImageBinding.inflate(layoutInflater)
            val dialogBoxBuilder = AlertDialog.Builder(activity).setView(dialogBinding.root)
            val dialogBoxInstance = dialogBoxBuilder.show()

            dialogBinding.iconCamera.setOnClickListener {
                if (checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
                    dialogBoxInstance.dismiss()
                } else {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                    dialogBoxInstance.dismiss()
                }
            }
            
            dialogBinding.iconGallery.setOnClickListener {
                if (checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE)
                    dialogBoxInstance.dismiss()
                } else {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                    dialogBoxInstance.dismiss()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                val permission: HashMap<String, Int> = HashMap()
                permission[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                permission[permissions[0]] = grantResults[0]
                if (permission[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, 0)
                } else {
                    
                }

            }
        }




        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 0)
            } else {
                Toast.makeText(requireContext(), "camera permission denied", Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Gallery permission granted", Toast.LENGTH_LONG).show()
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 1)
            } else {
                Toast.makeText(requireContext(), "Gallery permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun getProfile() {
        ApiClient.getInstance(requireContext()).getProfile(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                hideDialog()
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    setDataToInputFields(responseBody.user.firstName, responseBody.user.lastName, responseBody.user.email, responseBody.user.avatar)
                } else {
                    try {
                        Toast.makeText(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG).show()
                    } catch (exception: Exception) {
                        Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                hideDialog()
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
            }
        })
    }

    private fun updateProfile() {
        ApiClient.getInstance(requireContext()).updateProfile(UpdateProfileRequest(binding.firstNameEditText.text.toString(), binding.lastNameEditText.text.toString(), binding.emailEditText.text.toString()),
                object : Callback<UpdateProfileResponse> {
                    override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                        hideDialog()
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                            findNavController().navigateUp()
                        } else {
                            try {
                                Toast.makeText(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG).show()
                            } catch (exception: Exception) {
                                Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                        hideDialog()
                        Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
                    }
                })
    }

    private fun validateFields(): Boolean {
        return validateNameFields(binding.firstNameTextInput, binding.lastNameTextInput, requireContext()) && validateEmail(binding.emailEditText.text.toString(),
            binding.emailTextInput,
            requireContext())
    }

    private fun showDialog() {
        progressDialog.show()
    }

    private fun hideDialog() {
        progressDialog.dismiss()
    }
}
