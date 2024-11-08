package com.fhanafi.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fhanafi.storyapp.R
import com.fhanafi.storyapp.data.remote.response.ListStoryItem
import com.fhanafi.storyapp.ui.detail.DetailStoryActivity

class StoryAdapter(
    private val context: Context,
    private val token: String,
    private val storyList: List<ListStoryItem>
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView: ImageView = itemView.findViewById(R.id.iv_item_photo)
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_item_name)

        fun bind(story: ListStoryItem) {
            nameTextView.text = story.name
            story.photoUrl?.let {
                Glide.with(itemView.context).load(it).into(photoImageView)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, DetailStoryActivity::class.java).apply {
                    putExtra("token", token)
                    putExtra("storyId", story.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(storyList[position])
    }

    override fun getItemCount(): Int = storyList.size
}
