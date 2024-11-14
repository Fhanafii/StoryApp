package com.fhanafi.storyapp.di

import android.content.Context
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserPreference
import com.fhanafi.storyapp.data.pref.dataStore
import com.fhanafi.storyapp.data.remote.retrofit.ApiConfig


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig(pref).getApiService()
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig(pref).getApiService()
        return StoryRepository.getInstance(apiService)
    }
}
