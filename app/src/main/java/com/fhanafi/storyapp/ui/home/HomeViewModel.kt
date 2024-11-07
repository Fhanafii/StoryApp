package com.fhanafi.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

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
        _isLoading.postValue(true)
        try {
            val response = storyRepository.getStories(token)
            emit(response.listStory)
        } catch (e: Exception) {
            _errorMessage.postValue("Failed to load data: ${e.message}")
        }finally {
            _isLoading.postValue(false)
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}