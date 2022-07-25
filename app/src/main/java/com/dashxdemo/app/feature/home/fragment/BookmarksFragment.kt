package com.dashxdemo.app.feature.home.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dashxdemo.app.R
import com.dashxdemo.app.adapters.BookmarkedPostsAdapter
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.responses.BookmarkedPostResponse
import com.dashxdemo.app.api.responses.Bookmarks
import com.dashxdemo.app.api.responses.BookmarksResponse
import com.dashxdemo.app.databinding.FragmentBookmarksBinding
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarksFragment : Fragment() {
    private lateinit var binding: FragmentBookmarksBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bookmarkedPostsAdapter: BookmarkedPostsAdapter

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

        bookmarkedPosts()
        showDialog()
    }

    private fun bookmarkedPosts() {
        ApiClient.getInstance(requireContext())
            .bookmarkedPosts(object : Callback<BookmarkedPostResponse> {
                override fun onResponse(
                    call: Call<BookmarkedPostResponse>,
                    response: Response<BookmarkedPostResponse>,
                ) {
                    hideDialog()
                    if (response.isSuccessful) {
                        if (response.body()?.bookmarks?.isEmpty()!!) {
                            Toast.makeText(requireContext(),
                                           getString(R.string.no_bookmarks_found),
                                           Toast.LENGTH_LONG).show()
                        }
                        binding.bookmarkedPostsRecyclerView.setHasFixedSize(true)
                        bookmarkedPostsAdapter = BookmarkedPostsAdapter(response.body()?.bookmarks ?: mutableListOf()).apply {
                            onBookmarkClick = { bookmarks, position ->
                                unBookmarkPosts(bookmarks, position)
                                removeElementAtPosition(position)
                                notifyDataSetChanged()
                            }
                        }
                        binding.bookmarkedPostsRecyclerView.adapter = bookmarkedPostsAdapter
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

                override fun onFailure(call: Call<BookmarkedPostResponse>, t: Throwable) {
                    hideDialog()
                    Toast.makeText(requireContext(),
                                   getString(R.string.something_went_wrong),
                                   Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun unBookmarkPosts(bookmarks: Bookmarks, itemPosition: Int) {
        ApiClient.getInstance(requireContext())
            .bookmarks(bookmarks.id, object : Callback<BookmarksResponse> {
                override fun onResponse(
                    call: Call<BookmarksResponse>,
                    response: Response<BookmarksResponse>,
                ) {
                }

                override fun onFailure(call: Call<BookmarksResponse>, t: Throwable) {
                    Toast.makeText(requireContext(),
                                   getString(R.string.something_went_wrong),
                                   Toast.LENGTH_LONG).show()
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
