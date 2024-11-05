package com.fhanafi.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

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
}