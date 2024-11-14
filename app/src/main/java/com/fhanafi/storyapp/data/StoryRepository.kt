package com.fhanafi.storyapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fhanafi.storyapp.data.paging.StoryPagingSource
import com.fhanafi.storyapp.data.remote.response.DetailStoryResponse
import com.fhanafi.storyapp.data.remote.response.FileUploadResponse
import com.fhanafi.storyapp.data.remote.response.ListStoryItem
import com.fhanafi.storyapp.data.remote.response.StoryResponse
import com.fhanafi.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService
) {

    suspend fun getStories(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun getDetailStory(storyId: String): DetailStoryResponse {
        return apiService.getDetailStory(storyId)
    }

    suspend fun uploadStory(
        imageFile: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ): FileUploadResponse {
        return apiService.uploadStory(imageFile, description, lat, lon)
    }

    suspend fun getStoryWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    fun getPagedStories(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(apiService) }
        ).flow
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}
