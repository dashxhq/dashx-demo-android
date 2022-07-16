package com.dashxdemo.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dashxdemo.app.api.responses.PostsResponse
import com.dashxdemo.app.databinding.RecyclerViewItemsBinding
import com.dashxdemo.app.utils.Utils.Companion.convertTimeToText

class GetPostsAdapter(private val postsResponse: PostsResponse) :
    RecyclerView.Adapter<GetPostsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: RecyclerViewItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            RecyclerViewItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.nameTextView.text =
            postsResponse.posts[position].user.firstName + " " + postsResponse.posts[position].user.lastName
        holder.binding.contentTextView.text = postsResponse.posts[position].text
        holder.binding.historyTextView.text =
            convertTimeToText(postsResponse.posts[position].createdAt)
    }

    override fun getItemCount(): Int {
        return postsResponse.posts.size
    }
}
