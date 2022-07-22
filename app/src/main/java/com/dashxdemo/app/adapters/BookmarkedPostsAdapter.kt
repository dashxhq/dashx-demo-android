package com.dashxdemo.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.BookmarkedPostResponse
import com.dashxdemo.app.api.responses.Bookmarks
import com.dashxdemo.app.databinding.RecyclerViewItemsBinding
import com.dashxdemo.app.utils.Utils.Companion.timestampToText

class BookmarkedPostsAdapter(private val bookmarkedPosts: BookmarkedPostResponse) : RecyclerView.Adapter<BookmarkedPostsAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: RecyclerViewItemsBinding) : RecyclerView.ViewHolder(binding.root)

    var onBookmarkClick: ((Bookmarks, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            RecyclerViewItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = bookmarkedPosts.bookmarks[position]
        holder.binding.nameTextView.text = "${item.user.firstName} ${item.user.lastName}"
        holder.binding.contentTextView.text = item.text
        holder.binding.historyTextView.text = timestampToText(item.createdAt)
        holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_filled)

        holder.binding.bookmarkImageView.setOnClickListener {
            onBookmarkClick?.invoke(item, position)
        }
    }

    override fun getItemCount(): Int {
        return bookmarkedPosts.bookmarks.size
    }

    fun addElementAtPosition(position: Int = 0, bookmark: Bookmarks) {
        bookmarkedPosts.bookmarks.add(position, bookmark)
    }

    fun removeElementAtPosition(position: Int = 0) {
        bookmarkedPosts.bookmarks.removeAt(position)
    }
}
