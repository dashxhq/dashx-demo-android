package com.dashxdemo.app.feature.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
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
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
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
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.getPath
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.validateEmail
import com.dashxdemo.app.utils.Utils.Companion.validateNameFields
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


private const val CAMERA_REQUEST_CODE = 100
private const val GALLERY_REQUEST_CODE = 200

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var dialogBinding: DialogToSelectImageBinding

    private val PICK_IMAGE_CAMERA = 0
    val PICK_IMAGE_GALLERY = 1

    private var avatar: String? = null

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
            0 -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = data?.data
                DashXClient.getInstance().uploadExternalAsset(File(getPath(requireContext(), selectedImage)!!), "e8b7b42f-1f23-431c-b739-9de0fba3dadf", onSuccess = {
                    avatar = it.data.asset.url
                }, onError = {
                    Utils.showToast(requireContext(), it)
                })
            }
            1 -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = data?.data
                //File(getPath(requireContext(), selectedImage)!!
                DashXClient.getInstance().uploadExternalAsset(File(getPath(requireContext(), selectedImage)), "e8b7b42f-1f23-431c-b739-9de0fba3dadf", onSuccess = {
                    avatar = it.data.asset.url
                }, onError = {
                    Utils.showToast(requireContext(), it)
                })
                binding.imageView.setImageURI(selectedImage)
            }
        }
    }

    private fun setDataToInputFields(firstName: String, lastName: String, email: String, avatar: String?) {
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
                showProgressDialog()
                updateProfile()
            }
        }

        binding.addImageButton.setOnClickListener {
            //selectImage()
            dialogBinding = DialogToSelectImageBinding.inflate(layoutInflater)
            val dialogBoxBuilder = AlertDialog.Builder(activity).setView(dialogBinding.root)
            val dialogBoxInstance = dialogBoxBuilder.show()

            dialogBinding.iconCamera.setOnClickListener {
                if (checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
                    dialogBoxInstance.dismiss()
                } else {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                    dialogBoxInstance.dismiss()
                }
            }
            
            dialogBinding.iconGallery.setOnClickListener {
                if (checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE)
                    dialogBoxInstance.dismiss()
                } else {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                    dialogBoxInstance.dismiss()
                }
            }
            /*showFilePicker(
                limitItemSelection = 1,
                fileType = FileType.VIDEO,
                listDirection = ListDirection.RTL,
                accentColor = ContextCompat.getColor(requireContext(), R.color.purple_700),
                titleTextColor = ContextCompat.getColor(requireContext(), R.color.purple_700),
                onSubmitClickListener = object : OnSubmitClickListener {
                    override fun onClick(files: List<Media>) {
                        // Do something here with selected files
                        Log.d("ds",files.toString())
                        DashXClient.getInstance().uploadExternalAsset(files[0].file,"e8b7b42f-1f23-431c-b739-9de0fba3dadf", onSuccess = {
                            Log.d("dsaas",it.data.externalAssetData.asset.url)
                        }, onError = {
                            Log.d("dsfssdfs",it)
                        })
                    }
                },
                onItemClickListener = object : OnItemClickListener {
                    override fun onClick(media: Media, position: Int, adapter: FilePickerAdapter) {
                        if (!media.file.isDirectory) {
                            adapter.setSelected(position)
                        }
                    }
                }
            )*/
        }
    }

    private fun selectImage() {
        try {
            val pm: PackageManager = requireActivity().getPackageManager()
            val hasPerm = pm.checkPermission(Manifest.permission.CAMERA, requireActivity().getPackageName())
//            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Select Option")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                if (options[item] == "Take Photo") {
                    dialog.dismiss()
                    if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, PICK_IMAGE_CAMERA)
                    } else {
                        requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
                    }
                } else if (options[item] == "Choose From Gallery") {
                    dialog.dismiss()
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            })
            builder.show()
            /*} else {

            }*/
        } catch (e: java.lang.Exception) {
            Toast.makeText(requireContext(), "Camera Permission error", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                val permission: HashMap<String, Int> = HashMap()
                permission[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                permission[permissions[0]] = grantResults[0]
                val pm: PackageManager = requireActivity().getPackageManager()
                val hasPerm = pm.checkPermission(Manifest.permission.CAMERA, requireActivity().getPackageName())
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
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
                hideProgressDialog()
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    setDataToInputFields(responseBody.user.firstName, responseBody.user.lastName, responseBody.user.email, responseBody.user.avatar)
                } else {
                    try {
                        Utils.showToast(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()))
                    } catch (exception: Exception) {
                        Utils.showToast(requireContext(), getString(R.string.something_went_wrong))
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                hideProgressDialog()
                Utils.showToast(requireContext(), getString(R.string.something_went_wrong))
            }
        })
    }

    private fun updateProfile() {
        ApiClient.getInstance(requireContext())
            .updateProfile(UpdateProfileRequest(binding.firstNameEditText.text.toString(), binding.lastNameEditText.text.toString(), binding.emailEditText.text.toString(), avatar),
                           object : Callback<UpdateProfileResponse> {
                               override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                                   hideProgressDialog()
                                   if (response.isSuccessful) {
                                       Utils.showToast(requireContext(), response.body()?.message.toString())
                                       findNavController().navigateUp()
                                   } else {
                                       try {
                                           Utils.showToast(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()))
                                       } catch (exception: Exception) {
                                           Utils.showToast(requireContext(), getString(R.string.something_went_wrong))
                                       }
                                   }
                               }

                               override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                                   hideProgressDialog()
                                   Utils.showToast(requireContext(), getString(R.string.something_went_wrong))
                               }
                           })
    }

    private fun validateFields(): Boolean {
        return validateNameFields(binding.firstNameTextInput, binding.lastNameTextInput, requireContext())
            && validateEmail(binding.emailEditText.text.toString(), binding.emailTextInput, requireContext())
    }

    private fun showProgressDialog() {
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}
