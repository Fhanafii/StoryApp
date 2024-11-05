package com.fhanafi.storyapp.data

import com.fhanafi.storyapp.data.pref.UserModel
import com.fhanafi.storyapp.data.pref.UserPreference
<<<<<<< HEAD
import com.fhanafi.storyapp.data.remote.response.StoryResponse
import com.fhanafi.storyapp.data.remote.retrofit.ApiConfig
=======
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
import com.fhanafi.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user.copy(isLogin = true))
    }

<<<<<<< HEAD
    suspend fun getStories(token: String): StoryResponse {
        val apiService = ApiConfig().getApiService(token)
        return apiService.getStories()
    }


=======
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}