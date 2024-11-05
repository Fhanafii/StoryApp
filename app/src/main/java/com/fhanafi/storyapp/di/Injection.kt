package com.fhanafi.storyapp.di

import android.content.Context
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserPreference
import com.fhanafi.storyapp.data.pref.dataStore
import com.fhanafi.storyapp.data.remote.retrofit.ApiConfig
<<<<<<< HEAD
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
=======
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
<<<<<<< HEAD

        val user = runBlocking { pref.getSession().first() }
        val token = user.token ?: "" // Default to empty string if token is null

        val apiService = ApiConfig().getApiService(token)

=======
        val apiService = ApiConfig().getApiService()
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
        return UserRepository.getInstance(pref, apiService)
    }
}