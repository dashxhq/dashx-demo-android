package com.dashxdemo.app.feature.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dashx.sdk.DashXClient
import com.dashxdemo.app.R
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.requests.UpdateProfileRequest
import com.dashxdemo.app.api.responses.AssetData
import com.dashxdemo.app.api.responses.ProfileResponse
import com.dashxdemo.app.api.responses.UpdateProfileResponse
import com.dashxdemo.app.databinding.DialogToSelectImageBinding
import com.dashxdemo.app.databinding.FragmentProfileBinding
import com.dashxdemo.app.utils.Utils.Companion.PERM_CAMERA
import com.dashxdemo.app.utils.Utils.Companion.PERM_READ_EXT_STORAGE
import com.dashxdemo.app.utils.Utils.Companion.PICK_GALLERY_IMAGE
import com.dashxdemo.app.utils.Utils.Companion.TAKE_CAMERA_IMAGE
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.getFileFromBitmap
import com.dashxdemo.app.utils.Utils.Companion.getPath
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.showToast
import com.dashxdemo.app.utils.Utils.Companion.validateEmail
import com.dashxdemo.app.utils.Utils.Companion.validateNameFields
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var dialogBinding: DialogToSelectImageBinding

    private var avatar: com.dashx.sdk.data.AssetData? = null

    val cameraRequestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            cameraIntent(MediaStore.ACTION_IMAGE_CAPTURE, TAKE_CAMERA_IMAGE)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }
    }

    val galleryRequestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            galleryIntent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PICK_GALLERY_IMAGE)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERM_READ_EXT_STORAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        showProgressDialog()
        getProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            TAKE_CAMERA_IMAGE -> if (resultCode == RESULT_OK) {
                val bitmap: Bitmap = data?.extras?.get("data") as Bitmap
                val file = getFileFromBitmap(bitmap, requireContext())
                DashXClient.getInstance().uploadExternalAsset(File(getPath(requireContext(), file)!!), "e8b7b42f-1f23-431c-b739-9de0fba3dadf", onSuccess = {
                    avatar = it.data.asset
                }, onError = {
                    showToast(requireContext(), it)
                })
                binding.imageView.setImageURI(file)
            }

            PICK_GALLERY_IMAGE -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = data?.data
                DashXClient.getInstance().uploadExternalAsset(File(getPath(requireContext(), selectedImage)!!), "e8b7b42f-1f23-431c-b739-9de0fba3dadf", onSuccess = {
                    avatar = it.data.asset
                }, onError = {
                    showToast(requireContext(), it)
                })
                binding.imageView.setImageURI(selectedImage)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray,
    ) {
        when (requestCode) {

            PERM_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cameraIntent(requireActivity(), MediaStore.ACTION_IMAGE_CAPTURE, 811)
                } else {
                    Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            PERM_READ_EXT_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    galleryIntent(requireActivity(), Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 812)
                } else {
                    Toast.makeText(requireContext(), "Gallery permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {
            }
        }
    }

    private fun setDataToInputFields(firstName: String, lastName: String, email: String, avatar: AssetData?) {
        binding.firstNameEditText.setText(firstName)
        binding.lastNameEditText.setText(lastName)
        binding.emailEditText.setText(email)

        if (avatar?.url.isNullOrEmpty()) {
            binding.imageView.setImageResource(R.drawable.icon_profile)
        } else {
            Glide.with(requireContext()).load(avatar?.url).into(binding.imageView)
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
                showProgressDialog()
                updateProfile()
            }
        }

        binding.addImageIcon.setOnClickListener {
            dialogBinding = DialogToSelectImageBinding.inflate(layoutInflater)
            val dialogBoxBuilder = AlertDialog.Builder(activity).setView(dialogBinding.root)
            val dialogBoxInstance = dialogBoxBuilder.show()

            dialogBinding.iconCamera.setOnClickListener {
                dialogBoxInstance.dismiss()
                cameraRequestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            dialogBinding.iconGallery.setOnClickListener {
                dialogBoxInstance.dismiss()
                galleryRequestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

            }
        }
    }

    private fun getProfile() {
        ApiClient.getInstance(requireContext()).getProfile(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                hideProgressDialog()
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    setDataToInputFields(responseBody.user.firstName, responseBody.user.lastName, responseBody.user.email, responseBody.user.avatar)
                } else {
                    try {
                        showToast(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()))
                    } catch (exception: Exception) {
                        showToast(requireContext(), getString(R.string.something_went_wrong))
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                hideProgressDialog()
                showToast(requireContext(), getString(R.string.something_went_wrong))
            }
        })
    }

    private fun updateProfile() {
        ApiClient.getInstance(requireContext()).updateProfile(UpdateProfileRequest(binding.firstNameEditText.text.toString(),
            binding.lastNameEditText.text.toString(),
            binding.emailEditText.text.toString(),
            AssetData(avatar?.status, avatar?.url)), object : Callback<UpdateProfileResponse> {
            override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                hideProgressDialog()
                if (response.isSuccessful) {
                    showToast(requireContext(), response.body()?.message.toString())
                    findNavController().navigateUp()
                } else {
                    try {
                        showToast(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()))
                    } catch (exception: Exception) {
                        showToast(requireContext(), getString(R.string.something_went_wrong))
                    }
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                hideProgressDialog()
                showToast(requireContext(), getString(R.string.something_went_wrong))
            }
        })
    }

    private fun cameraIntent(permission: String, requestCode: Int) {
        val pictureIntent = Intent(permission)
        activity?.startActivityForResult(pictureIntent, requestCode)
    }

    private fun galleryIntent(intentAction: String, externalContentUri: Uri, requestCode: Int) {
        val pickPhoto = Intent(intentAction, externalContentUri)
        activity?.startActivityForResult(pickPhoto, requestCode)
    }

    private fun validateFields(): Boolean {
        return validateNameFields(binding.firstNameTextInput, binding.lastNameTextInput, requireContext()) && validateEmail(binding.emailEditText.text.toString(),
            binding.emailTextInput,
            requireContext())
    }

    private fun showProgressDialog() {
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}
