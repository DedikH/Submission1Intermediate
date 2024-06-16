package com.example.bismillahbisaintermediate.View.ListStory

import android.view.View
import com.example.bismillahbisaintermediate.Response.ListStoryItem

interface RVonclick {
    fun onItemClicked(view: View, nama : ListStoryItem)
}