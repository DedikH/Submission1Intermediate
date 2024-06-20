package com.example.bismillahbisaintermediate.View.ListStory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import com.example.bismillahbisaintermediate.databinding.CardviewstoryBinding

class storyAdapter : ListAdapter<ListStoryItem, storyAdapter.myviewHolder>(DIFF_CALLBACK) {
    var listener : RVonclick? = null
    class myviewHolder (val binding : CardviewstoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(item : ListStoryItem){
            binding.tvItemName.text = "${item.name}"
            binding.storyDescription.text = "${item.description}"
            if(item.photoUrl != null){
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(binding.ivItemPhoto)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onBindViewHolder(holder: myviewHolder, position: Int) {
        val rvitem = getItem(position)
        if(rvitem != null){
            holder.onBind(rvitem)
        }

        holder.binding.cvListStory.setOnClickListener{
            listener?.onItemClicked(it, getItem(position))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewHolder {
        val binding = CardviewstoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myviewHolder(binding)
    }


}