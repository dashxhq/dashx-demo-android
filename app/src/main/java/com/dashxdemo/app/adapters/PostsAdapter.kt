package com.dashxdemo.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.Post
import com.dashxdemo.app.api.responses.PostsResponse
import com.dashxdemo.app.databinding.RecyclerViewItemsBinding
import com.dashxdemo.app.utils.Utils.Companion.timestampToText

class PostsAdapter(private val postsResponse: PostsResponse) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: RecyclerViewItemsBinding) : RecyclerView.ViewHolder(binding.root)

    var onBookmarkClick: ((Post, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            RecyclerViewItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = postsResponse.posts[position]

        holder.binding.nameTextView.text = "${item.user.firstName} ${item.user.lastName}"
        holder.binding.contentTextView.text = item.text
        holder.binding.historyTextView.text = timestampToText(item.createdAt)

        if (item.bookmarkedAt != null) {
            holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_outlined)
        }

        holder.binding.bookmarkImageView.setOnClickListener {
            if (item.bookmarkedAt == null) {
                holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_filled)
            } else {
                holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_outlined)
            }
            onBookmarkClick?.invoke(item, position)
        }
    }

    override fun getItemCount(): Int {
        return postsResponse.posts.size
    }
}
