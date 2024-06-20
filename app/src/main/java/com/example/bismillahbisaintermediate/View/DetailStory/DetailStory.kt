package com.example.bismillahbisaintermediate.View.DetailStory

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import com.example.bismillahbisaintermediate.Response.ListStoryResponse
import com.example.bismillahbisaintermediate.View.ListStory.storyAdapter
import com.example.bismillahbisaintermediate.databinding.ActivityDetailStoryBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class DetailStory : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailintent()
    }

    private fun detailintent(){
        val bundle = intent.extras
        var title = intent.getStringExtra("title").toString()
        var desc = intent.getStringExtra("description").toString()
        val image_url = bundle!!.getString("imageUrl")

        binding.detailTitle.setText(title)
        binding.detailDescription.setText(desc)
        val img = binding.detailImage
        Glide.with(this)
            .load(image_url)
            .into(img)
    }
}