package com.fhanafi.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.remote.response.DetailStoryResponse
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val repository: StoryRepository): ViewModel() {
    private val _storyDetail = MutableLiveData<DetailStoryResponse>()
    val storyDetail: LiveData<DetailStoryResponse> = _storyDetail

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getDetailStory(token: String, storyId: String) {
        viewModelScope.launch {
            try {
                val detailResponse = repository.getDetailStory(token, storyId)
                _storyDetail.value = detailResponse
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

}