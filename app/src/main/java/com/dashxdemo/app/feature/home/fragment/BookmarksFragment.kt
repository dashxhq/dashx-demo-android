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
import com.dashxdemo.app.api.responses.BookmarksReponse
import com.dashxdemo.app.databinding.FragmentBookmarksBinding
import com.dashxdemo.app.utils.Utils
import com.dashxdemo.app.utils.Utils.Companion.getErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarksFragment : Fragment() {
    private lateinit var binding: FragmentBookmarksBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
                        binding.bookmarkedPostsRecyclerView.adapter =
                            BookmarkedPostsAdapter(response.body()!!).apply {
                                onBookmarkClick = { Bookmarks ->
                                    getBookmarkedPosts()
                                    unBookmarkPosts(Bookmarks.id)
                                }
                            }
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

    private fun unBookmarkPosts(id: Int) {
        ApiClient.getInstance(requireContext()).bookmarks(id, object : Callback<BookmarksReponse> {
            override fun onResponse(
                call: Call<BookmarksReponse>,
                response: Response<BookmarksReponse>,
            ) {
            }

            override fun onFailure(call: Call<BookmarksReponse>, t: Throwable) {
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
