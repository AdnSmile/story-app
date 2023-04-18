package com.vvwxx.bangkit.storyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.databinding.ItemRowStoriesBinding

class StoriesAdapter(
    private val listStories: List<ListStoryItem>,
    private val onClick: (ListStoryItem) -> Unit
) : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listStories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position])
    }

    inner class ViewHolder(binding: ItemRowStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imgPhoto: ImageView = binding.imgItemPhoto
        private val tvName: TextView = binding.tvItemName
        private val tvDescription: TextView = binding.tvItemDescription

        fun bind(item: ListStoryItem) {
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(imgPhoto)

            tvName.text = item.name
            tvDescription.text = item.description

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

}