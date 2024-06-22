package com.example.bismillahbisaintermediate.View.ListStory.withmaps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import com.example.bismillahbisaintermediate.databinding.CardviewstoryBinding

class StoryPagingAdapter : PagingDataAdapter<ListStoryItem, StoryPagingAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private var listener: ((ListStoryItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ListStoryItem) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = CardviewstoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val binding: CardviewstoryBinding, listener: ((ListStoryItem) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.let { clickListener ->
                        val item = getItem(position)
                        if (item != null) {
                            clickListener(item)
                        }
                    }
                }
            }
        }

        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            binding.storyDescription.text = story.description
            if(story.photoUrl != null){
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(binding.ivItemPhoto)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
