package com.fhanafi.storyapp.data

import com.fhanafi.storyapp.data.pref.UserPreference
import com.fhanafi.storyapp.data.remote.response.DetailStoryResponse
import com.fhanafi.storyapp.data.remote.response.FileUploadResponse
import com.fhanafi.storyapp.data.remote.response.StoryResponse
import com.fhanafi.storyapp.data.remote.retrofit.ApiConfig
import com.fhanafi.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    private suspend fun getToken(): String {
        return userPreference.getSession().first().token
    }

    private suspend fun getAuthorizedApiService(): ApiService {
        val token = getToken()
        return ApiConfig().getApiService(token)  // Create an instance of ApiService with the token
    }

    suspend fun getStories(): StoryResponse {
        return getAuthorizedApiService().getStories()  // Call without passing the token explicitly
    }

    suspend fun getDetailStory(storyId: String): DetailStoryResponse {
        return getAuthorizedApiService().getDetailStory(storyId)  // Call without passing the token
    }

    suspend fun uploadStory(imageFile: MultipartBody.Part, description: RequestBody, lat: Double? = null, lon: Double? = null): FileUploadResponse {
        return getAuthorizedApiService().uploadStory(imageFile, description, lat, lon)  // Call without passing the token
    }

    suspend fun getStoryWithLocation(): StoryResponse {
        return getAuthorizedApiService().getStoriesWithLocation()  // Call without passing the token
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
