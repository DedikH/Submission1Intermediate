package com.example.bismillahbisaintermediate.View.ListStory.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bismillahbisaintermediate.Response.ListStoryResponse
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import com.example.submission1intermediate.API.APIServices
import retrofit2.Call

class PagingSource(
    private val apiService: APIServices
) : PagingSource<Int, ListStoryItem>(){

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesAll(page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}

