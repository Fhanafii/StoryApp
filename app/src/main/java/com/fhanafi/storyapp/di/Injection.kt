package com.fhanafi.storyapp.di

import android.content.Context
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserPreference
import com.fhanafi.storyapp.data.pref.dataStore
import com.fhanafi.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)

        val user = runBlocking { pref.getSession().first() }
        val token = user.token

        val apiService = ApiConfig().getApiService(token)

        return UserRepository.getInstance(pref, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig().getApiService(runBlocking { pref.getSession().first().token })
        return StoryRepository.getInstance(apiService, pref)  // Pass `pref` as userPreference

    }

}