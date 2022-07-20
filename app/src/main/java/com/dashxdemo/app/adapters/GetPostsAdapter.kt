package com.dashxdemo.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.Post
import com.dashxdemo.app.api.responses.PostsResponse
import com.dashxdemo.app.databinding.RecyclerViewItemsBinding
import com.dashxdemo.app.utils.Utils.Companion.convertTimeToText

class GetPostsAdapter(private val postsResponse: PostsResponse) :
    RecyclerView.Adapter<GetPostsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: RecyclerViewItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onBookmarkClick: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            RecyclerViewItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var isBookmarked = false
        holder.binding.nameTextView.text =
            postsResponse.posts[position].user.firstName + " " + postsResponse.posts[position].user.lastName
        holder.binding.contentTextView.text = postsResponse.posts[position].text
        holder.binding.historyTextView.text =
            convertTimeToText(postsResponse.posts[position].createdAt)

        holder.binding.bookmarkImageView.setOnClickListener {
            if (!isBookmarked) {
                isBookmarked = true
                holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_filled)
            } else {
                isBookmarked = false
                holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_outlined)
            }
            onBookmarkClick?.invoke(postsResponse.posts[position])
        }
    }

    override fun getItemCount(): Int {
        return postsResponse.posts.size
    }
}
