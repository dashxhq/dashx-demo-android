package com.dashxdemo.app.feature.home.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dashxdemo.app.R
import com.dashxdemo.app.utils.data.VideoPlayerData
import com.dashxdemo.app.adapters.PostsAdapter
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.responses.Post
import com.dashxdemo.app.api.responses.PostsResponse
import com.dashxdemo.app.api.responses.ToggleBookmarkResponse
import com.dashxdemo.app.databinding.FragmentBookmarksBinding
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarksFragment : Fragment() {
    private lateinit var binding: FragmentBookmarksBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bookmarkedPostsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBookmarksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        Utils.initProgressDialog(progressDialog, requireContext())

        getBookmarkedPosts()
        showDialog()
    }

    private fun getBookmarkedPosts() {
        ApiClient.getInstance(requireContext()).getBookmarkedPosts(object : Callback<PostsResponse> {
                override fun onResponse(
                    call: Call<PostsResponse>,
                    response: Response<PostsResponse>,
                ) {
                    hideDialog()
                    if (response.isSuccessful) {
                        if (response.body()?.posts?.isEmpty()!!) {
                            Toast.makeText(requireContext(), getString(R.string.no_bookmarks_found), Toast.LENGTH_LONG).show()
                        }
                        binding.bookmarkedPostsRecyclerView.setHasFixedSize(true)

                        bookmarkedPostsAdapter = PostsAdapter(response.body()?.posts ?: mutableListOf(), requireContext(), layoutInflater).apply {
                            onBookmarkClick = { bookmarks, position ->
                                toggleBookmark(bookmarks, position)
                                removeElementAtPosition(position)
                                notifyDataSetChanged()
                            }

                            onVideoPlayClick = { videoUrl ->
                                navigateToVideoPlayer(videoUrl)
                            }
                        }
                        binding.bookmarkedPostsRecyclerView.adapter = bookmarkedPostsAdapter
                    } else {
                        try {
                            Toast.makeText(requireContext(), getErrorMessageFromJson(response.errorBody()?.string()), Toast.LENGTH_LONG).show()
                        } catch (exception: Exception) {
                            Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                    hideDialog()
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun navigateToVideoPlayer(videoUrl: VideoPlayerData) {
        val action = BookmarksFragmentDirections.actionNavBookmarksToVideoPlayerFragment(videoUrl)
        findNavController().navigate(action)
    }

    private fun toggleBookmark(bookmarks: Post, itemPosition: Int) {
        ApiClient.getInstance(requireContext()).toggleBookmark(bookmarks.id, object : Callback<ToggleBookmarkResponse> {
                override fun onResponse(
                    call: Call<ToggleBookmarkResponse>,
                    response: Response<ToggleBookmarkResponse>,
                ) {
                }

                override fun onFailure(call: Call<ToggleBookmarkResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                    bookmarkedPostsAdapter.addElementAtPosition(itemPosition, bookmarks)
                    bookmarkedPostsAdapter.notifyItemInserted(itemPosition)
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
