package com.fhanafi.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
<<<<<<< HEAD
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
=======
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserModel
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

<<<<<<< HEAD
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

=======
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
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
<<<<<<< HEAD

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
=======
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
}