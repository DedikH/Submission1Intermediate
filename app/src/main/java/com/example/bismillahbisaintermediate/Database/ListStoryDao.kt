package com.example.bismillahbisaintermediate.Database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bismillahbisaintermediate.Response.ListStoryItem

@Dao
interface ListStoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<ListStoryItem>)

    @Query("SELECT * FROM Story")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM Story")
    suspend fun deleteAll()
}
