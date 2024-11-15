package com.fhanafi.storyapp.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    // Error and loading states
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Refresh trigger
    private val refreshTrigger = MutableStateFlow(Unit)

    // Stories flow, recreated each time `refreshTrigger` emits
    @OptIn(ExperimentalCoroutinesApi::class)
    val stories: Flow<PagingData<ListStoryItem>> = refreshTrigger
        .flatMapLatest {
            storyRepository.getPagedStories().cachedIn(viewModelScope)
        }

    // Function to trigger a refresh
    fun refreshStories() {
        refreshTrigger.value = Unit

    }

    // Logout function
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
