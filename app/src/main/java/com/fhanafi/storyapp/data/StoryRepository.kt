package com.fhanafi.storyapp.data

import com.fhanafi.storyapp.data.remote.response.DetailStoryResponse
import com.fhanafi.storyapp.data.remote.response.StoryResponse
import com.fhanafi.storyapp.data.remote.retrofit.ApiConfig
import com.fhanafi.storyapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService
) {

    suspend fun getStories(token: String): StoryResponse {
        val apiService = ApiConfig().getApiService(token)
        return apiService.getStories()
    }

    suspend fun getDetailStory(token: String, storyId: String): DetailStoryResponse {
        val apiService = ApiConfig().getApiService(token)
        return apiService.getDetailStory(storyId)
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
