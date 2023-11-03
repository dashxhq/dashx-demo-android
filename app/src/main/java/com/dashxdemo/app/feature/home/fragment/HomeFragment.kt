package com.dashxdemo.app.feature.home.fragment

import android.Manifest
import android.app.Activity
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
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dashx.sdk.DashX
import com.dashxdemo.app.R
import com.dashxdemo.app.utils.data.VideoPlayerData
import com.dashxdemo.app.adapters.PostsAdapter
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.requests.CreatePostRequest
import com.dashxdemo.app.api.responses.AssetData
import com.dashxdemo.app.api.responses.CreatePostResponse
import com.dashxdemo.app.api.responses.PostsResponse
import com.dashxdemo.app.api.responses.ToggleBookmarkResponse
import com.dashxdemo.app.databinding.DialogCreatePostBinding
import com.dashxdemo.app.databinding.*
import com.dashxdemo.app.utils.Constants.PERM_CAMERA
import com.dashxdemo.app.utils.Constants.PERM_READ_EXT_STORAGE
import com.dashxdemo.app.utils.Constants.PICK_GALLERY_IMAGE
import com.dashxdemo.app.utils.Constants.PICK_GALLERY_VIDEO
import com.dashxdemo.app.utils.Constants.TAKE_CAMERA_IMAGE
import com.dashxdemo.app.utils.Constants.TAKE_CAMERA_VIDEO
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.getFileFromBitmap
import com.dashxdemo.app.utils.Utils.Companion.getPath
import com.dashxdemo.app.utils.Utils.Companion.getVideoThumbnail
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import com.dashxdemo.app.utils.Utils.Companion.runOnUiThread
import com.dashxdemo.app.utils.Utils.Companion.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var createPostBinding: DialogCreatePostBinding
    private lateinit var pickerBinding: DialogViewPickerBinding

    private var createPostDialogBuilder: AlertDialog.Builder? = null
    private var pickerDialogBuilder: AlertDialog.Builder? = null

    private var createPostDialog: AlertDialog? = null
    private var pickerDialog: AlertDialog? = null

    private lateinit var progressDialog: ProgressDialog
    private lateinit var postsAdapter: PostsAdapter

    private var imageAssetData: com.dashx.sdk.data.UploadData? = null
    private var videoAssetData: com.dashx.sdk.data.UploadData? = null
    private var shouldPickImage = false

    private val cameraRequestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            if (shouldPickImage) {
                cameraImageIntent()
            } else {
                cameraVideoIntent()
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }
    }

    private val galleryRequestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            if (shouldPickImage) {
                galleryImageIntent()
            } else {
                galleryVideoIntent()
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERM_READ_EXT_STORAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(requireContext())
        initProgressDialog(progressDialog, requireContext())

        pickerBinding = DialogViewPickerBinding.inflate(layoutInflater)
        pickerDialogBuilder = AlertDialog.Builder(requireContext()).setView(pickerBinding.root)
        pickerDialog = pickerDialogBuilder?.create()

        getPosts()
        showProgressDialog()
        setUpUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICK_GALLERY_VIDEO -> if (resultCode == Activity.RESULT_OK) {
                val selectedVideo: Uri? = data?.data

                showProgressDialog()

                DashX.uploadAsset(File(getPath(requireContext(), selectedVideo!!)), "post", "video", onSuccess = {
                    hideProgressDialog()
                    videoAssetData = it.data.asset
                }, onError = {
                    hideProgressDialog()
                    runOnUiThread { showToast(requireContext(), it) }
                })
                createPostBinding.videoPathTextView.text = getPath(requireContext(), selectedVideo).toString()
            }

            TAKE_CAMERA_VIDEO -> if (resultCode == Activity.RESULT_OK) {
                val selectedVideo = data?.data
                val bitmap = getVideoThumbnail(requireContext(), selectedVideo)
                val file = getFileFromBitmap(bitmap!!, requireContext())

                showProgressDialog()

                DashX.uploadAsset(file, "post", "video", onSuccess = {
                    hideProgressDialog()
                    videoAssetData = it.data.asset
                }, onError = {
                    hideProgressDialog()
                    runOnUiThread { showToast(requireContext(), it) }
                })
                createPostBinding.videoPathTextView.text = file.path
            }

            PICK_GALLERY_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri? = data?.data

                showProgressDialog()

                DashX.uploadAsset(File(getPath(requireContext(), selectedImage!!)), "post", "image", onSuccess = {
                    hideProgressDialog()
                    imageAssetData = it.data.asset
                }, onError = {
                    hideProgressDialog()
                    runOnUiThread { showToast(requireContext(), it) }
                })
                createPostBinding.imagePathTextView.text = getPath(requireContext(), selectedImage).toString()
            }

            TAKE_CAMERA_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                val bitmap = data?.extras?.get("data") as Bitmap
                val file = getFileFromBitmap(bitmap, requireContext())

                showProgressDialog()

                DashX.uploadAsset(file, "post", "image", onSuccess = {
                    hideProgressDialog()
                    imageAssetData = it.data.asset
                }, onError = {
                    hideProgressDialog()
                    runOnUiThread { showToast(requireContext(), it) }
                })
                createPostBinding.imagePathTextView.text = file.path
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERM_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cameraImageIntent()
                } else {
                    showToast(requireContext(), getString(R.string.camera_permission_denied))
                }
            }

            PERM_READ_EXT_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    galleryImageIntent()
                } else {
                    showToast(requireContext(), getString(R.string.gallery_permission_denied))
                }
            }
        }
    }

    private fun setUpUi() {
        binding.addPostsButton.setOnClickListener {
            showCreatePostDialog()
        }
    }

    private fun showCreatePostDialog() {
        prepareCreatePostDialog()
        createPostBinding.cancelButton.setOnClickListener {
            createPostDialog?.cancel()
        }

        createPostBinding.contentEditText.addTextChangedListener {
            createPostBinding.contentTextInput.isErrorEnabled = false
        }

        createPostBinding.postButton.setOnClickListener {
            if (createPostBinding.contentEditText.text?.isEmpty()!!) {
                createPostBinding.contentTextInput.isErrorEnabled = true
                createPostBinding.contentTextInput.error = getString(R.string.text_required)
            } else {
                showProgressDialog()
                createPost()
                createPostDialog?.dismiss()
            }
        }

        createPostBinding.chooseImageFileButton.setOnClickListener {
            shouldPickImage = true
            showPickerDialog()
        }

        createPostBinding.chooseVideoFileButton.setOnClickListener {
            shouldPickImage = false
            showPickerDialog()
        }
    }

    private fun prepareCreatePostDialog() {
        createPostDialogBuilder = null
        createPostDialog = null
        createPostBinding = DialogCreatePostBinding.inflate(layoutInflater)
        createPostDialogBuilder = AlertDialog.Builder(requireContext()).setView(createPostBinding.root)
        createPostDialog = createPostDialogBuilder?.create()
        createPostDialog?.show()
    }

    private fun showPickerDialog() {

        pickerDialog?.show()

        pickerBinding.iconCamera.setOnClickListener {
            pickerDialog?.dismiss()
            cameraRequestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        pickerBinding.iconGallery.setOnClickListener {
            pickerDialog?.dismiss()
            galleryRequestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun getPosts() {
        ApiClient.getInstance(requireContext()).getPosts(object : Callback<PostsResponse> {
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                hideProgressDialog()
                if (response.isSuccessful) {
                    binding.recyclerView.setHasFixedSize(true)
                    postsAdapter = PostsAdapter(response.body()?.posts ?: mutableListOf(), requireContext(), layoutInflater).apply {
                        onBookmarkClick = { post, position ->
                            toggleBookmark(post.id, position)
                        }

                        onVideoPlayClick = { videoUrl ->
                            navigateToVideoPlayer(videoUrl)
                        }
                    }
                    binding.recyclerView.adapter = postsAdapter
                } else {
                    try {
                        Toast.makeText(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG).show()
                    } catch (exception: Exception) {
                        Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                hideProgressDialog()
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun navigateToVideoPlayer(videoUrl: VideoPlayerData) {
        val action = HomeFragmentDirections.actionNavHomeToNavVideoPlayer(videoUrl)
        findNavController().navigate(action)
    }

    fun toggleBookmark(postId: Int, itemPosition: Int) {
        ApiClient.getInstance(requireContext()).toggleBookmark(postId, object : Callback<ToggleBookmarkResponse> {
            override fun onResponse(
                call: Call<ToggleBookmarkResponse>,
                response: Response<ToggleBookmarkResponse>,
            ) {
            }

            override fun onFailure(call: Call<ToggleBookmarkResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                postsAdapter.notifyItemChanged(itemPosition)
            }
        })
    }

    private fun createPost() {
        ApiClient.getInstance(requireContext()).createPost(CreatePostRequest(createPostBinding.contentEditText.text.toString(),
            AssetData(imageAssetData?.status, imageAssetData?.url),
            AssetData(videoAssetData?.status, videoAssetData?.url)), object : Callback<CreatePostResponse> {
            override fun onResponse(
                call: Call<CreatePostResponse>,
                response: Response<CreatePostResponse>,
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                    getPosts()
                } else {
                    try {
                        Toast.makeText(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG)
                    } catch (exception: Exception) {
                        Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                    }
                }
                createPostDialog?.dismiss()
            }

            override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {
                t.printStackTrace()
                createPostDialog?.dismiss()
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun cameraImageIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(pictureIntent, TAKE_CAMERA_IMAGE)
    }

    private fun galleryImageIntent() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, PICK_GALLERY_IMAGE)
    }

    private fun cameraVideoIntent() {
        val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(videoIntent, TAKE_CAMERA_VIDEO)
    }

    private fun galleryVideoIntent() {
        val pickVideo = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickVideo, PICK_GALLERY_VIDEO)
    }

    private fun showProgressDialog() {
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}
