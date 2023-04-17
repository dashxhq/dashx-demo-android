package com.dashxdemo.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.Post
import com.dashxdemo.app.databinding.DialogImagePreviewBinding
import com.dashxdemo.app.databinding.ViewPostItemBinding
import com.dashxdemo.app.utils.Utils.Companion.getInitials
import com.dashxdemo.app.utils.Utils.Companion.timeAgo
import com.dashxdemo.app.utils.data.VideoPlayerData

class PostsAdapter(private val posts: MutableList<Post>, private val context: Context, private val layoutInflater: LayoutInflater) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ViewPostItemBinding) : RecyclerView.ViewHolder(binding.root)

    var onBookmarkClick: ((Post, Int) -> Unit)? = null

    var onVideoPlayClick: ((VideoPlayerData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ViewPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = posts[position]

        holder.binding.nameTextView.text = "${item.user?.firstName} ${item.user?.lastName}"
        holder.binding.contentTextView.text = item.text
        holder.binding.historyTextView.text = timeAgo(item.createdAt)

        holder.binding.postImageView.isVisible = !item.image?.url.isNullOrEmpty()
        if (!item.image?.url.isNullOrEmpty()) {
            Glide.with(context).load(item.image?.url).into(holder.binding.postImageView)
        }

        if (item.user?.avatar?.url.isNullOrEmpty()) {
            holder.binding.imageView.visibility = View.GONE
            holder.binding.imageTextView.text = getInitials("${item.user?.firstName} ${item.user?.lastName}")
        } else {
            holder.binding.imageTextView.visibility = View.GONE
            Glide.with(context).load(item.user?.avatar?.url).into(holder.binding.imageView)
        }

        holder.binding.videoContainer.isVisible = !item.video?.url.isNullOrEmpty()
        if (!item.video?.url.isNullOrEmpty()) {
            Glide.with(context).load(item.video?.url).into(holder.binding.videoThumbnail)
            holder.binding.videoContainer.setOnClickListener {
                onVideoPlayClick?.invoke(VideoPlayerData(item.video?.url.toString()))
            }
        }

        if (item.bookmarkedAt != null) {
            holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            holder.binding.bookmarkImageView.setImageResource(R.drawable.ic_bookmark_outlined)
        }

        holder.binding.postImageView.setOnClickListener {
            item.image?.url?.let { url -> showImagePreview(url) }
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
        return posts.size
    }

    fun addElementAtPosition(position: Int = 0, post: Post) {
        posts.add(position, post)
    }

    fun removeElementAtPosition(position: Int = 0) {
        posts.removeAt(position)
    }

    private fun showImagePreview(url: String) {
        val imagePreviewBinding = DialogImagePreviewBinding.inflate(layoutInflater)
        val createPostDialogBuilder = AlertDialog.Builder(context).setView(imagePreviewBinding.root)

        val createPostDialog = createPostDialogBuilder.create()

        Glide.with(context).load(url).into(imagePreviewBinding.imagePreview)
        imagePreviewBinding.closeIcon.setOnClickListener {
            createPostDialog.dismiss()
        }
        createPostDialog.show()
    }
}
