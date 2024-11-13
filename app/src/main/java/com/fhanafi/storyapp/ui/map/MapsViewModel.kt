package com.fhanafi.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _storiesWithLocation = MutableLiveData<List<ListStoryItem>>()
    val storiesWithLocation: LiveData<List<ListStoryItem>> = _storiesWithLocation

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val response = storyRepository.getStoryWithLocation()
                _storiesWithLocation.value = response.listStory
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

}