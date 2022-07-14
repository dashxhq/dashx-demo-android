package com.dashxdemo.app.feature.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dashxdemo.app.R
import com.dashxdemo.app.adapters.GetPostsAdapter
import com.dashxdemo.app.api.ApiClient
import com.dashxdemo.app.api.responses.PostsResponse
import com.dashxdemo.app.databinding.FragmentHomeBinding
import com.dashxdemo.app.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPosts()
    }

    private fun getPosts() {
        ApiClient.getInstance(requireContext()).getPosts(object : Callback<PostsResponse> {
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {

                if (response.isSuccessful) {
                    binding.recyclerView.adapter = GetPostsAdapter(response.body()!!)
                } else {
                    try {
                        Toast.makeText(
                            requireContext(),
                            Utils.getErrorMessageFromJson(response.errorBody()?.string()),
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (exception: Exception) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    t.localizedMessage,
                    //getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
