package com.fhanafi.storyapp.di

import android.content.Context
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
        val token = user.token ?: "" // Default to empty string if token is null

        val apiService = ApiConfig().getApiService(token)

        return UserRepository.getInstance(pref, apiService)
    }
}