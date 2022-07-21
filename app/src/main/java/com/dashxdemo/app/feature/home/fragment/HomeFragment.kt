package com.dashxdemo.app.feature.home.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.dashxdemo.app.R
import com.dashxdemo.app.adapters.PostsAdapter
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.requests.CreatePostRequest
import com.dashxdemo.app.api.responses.BookmarksReponse
import com.dashxdemo.app.api.responses.CreatePostResponse
import com.dashxdemo.app.api.responses.PostsResponse
import com.dashxdemo.app.databinding.CreatePostDialogBinding
import com.dashxdemo.app.databinding.FragmentHomeBinding
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import com.dashxdemo.app.utils.Utils.Companion.initProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialogBinding: CreatePostDialogBinding
    private lateinit var progressDialog: ProgressDialog

    private lateinit var postsAdapter: PostsAdapter

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

        getPosts()
        showDialog()
        setUpUi()
    }

    private fun setUpUi() {
        binding.addPostsButton.setOnClickListener {
            showCreatePostDialog()
        }
    }

    private fun showCreatePostDialog() {
        dialogBinding = CreatePostDialogBinding.inflate(layoutInflater)
        val dialogBoxBuilder = AlertDialog.Builder(activity).setView(dialogBinding.root)
        val dialogBoxInstance = dialogBoxBuilder.show()

        dialogBinding.cancelButton.setOnClickListener {
            dialogBoxInstance.dismiss()
        }

        dialogBinding.contentEditText.addTextChangedListener {
            dialogBinding.contentTextInput.isErrorEnabled = false
        }

        dialogBinding.postButton.setOnClickListener {
            if (dialogBinding.contentEditText.text?.isEmpty()!!) {
                dialogBinding.contentTextInput.isErrorEnabled = true
                dialogBinding.contentTextInput.error = getString(R.string.text_required)
            } else {
                createPost()
                dialogBoxInstance.dismiss()
            }
        }
    }

    private fun getPosts() {
        ApiClient.getInstance(requireContext()).getPosts(object : Callback<PostsResponse> {
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {

                hideDialog()
                if (response.isSuccessful) {
                    binding.recyclerView.setHasFixedSize(true)
                    postsAdapter = PostsAdapter(response.body()!!, requireContext()).apply {
                        onBookmarkClick = { post, position ->
                            bookmarkPosts(post.id,position)
                        }
                    }
                    binding.recyclerView.adapter = postsAdapter
                } else {
                    try {
                        Toast.makeText(requireContext(),
                            getErrorMessageFromJson(response.errorBody()?.string()),
                            Toast.LENGTH_LONG).show()
                    } catch (exception: Exception) {
                        Toast.makeText(requireContext(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                hideDialog()
                Toast.makeText(requireContext(),
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    fun bookmarkPosts(postId: Int, itemPosition: Int) {
        ApiClient.getInstance(requireContext())
            .bookmarks(postId, object : Callback<BookmarksReponse> {
                override fun onResponse(
                    call: Call<BookmarksReponse>,
                    response: Response<BookmarksReponse>,
                ) {
                }

                override fun onFailure(call: Call<BookmarksReponse>, t: Throwable) {
                    Toast.makeText(requireContext(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG).show()
                    postsAdapter.notifyItemChanged(itemPosition)
                }
            })
    }

    private fun createPost() {
        ApiClient.getInstance(requireContext())
            .createPost(CreatePostRequest(dialogBinding.contentEditText.text.toString()),
                object : Callback<CreatePostResponse> {
                    override fun onResponse(
                        call: Call<CreatePostResponse>,
                        response: Response<CreatePostResponse>,
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(),
                                response.body()?.message,
                                Toast.LENGTH_LONG).show()
                        } else {
                            try {
                                Toast.makeText(requireContext(),
                                    getErrorMessageFromJson(response.errorBody()?.string()),
                                    Toast.LENGTH_LONG)
                            } catch (exception: Exception) {
                                Toast.makeText(requireContext(),
                                    getString(R.string.something_went_wrong),
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {
                        Toast.makeText(requireContext(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun showDialog() {
        progressDialog.show()
    }

    private fun hideDialog() {
        progressDialog.dismiss()
    }
}
