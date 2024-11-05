package com.fhanafi.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage


    // Session management
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    // Logout function
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    // Fetch stories
    fun getStories(token: String) = liveData(Dispatchers.IO) {
        try {
            val response = repository.getStories(token)
            emit(response.listStory)
        } catch (e: Exception) {
            _errorMessage.postValue("Failed to load data: ${e.message}")
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}