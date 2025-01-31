package com.fhanafi.storyapp.ui.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.mycamera.reduceFileImage
import com.fhanafi.mycamera.uriToFile
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.remote.response.FileUploadResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _uploadResponse = MutableLiveData<FileUploadResponse>()
    val uploadResponse: LiveData<FileUploadResponse> = _uploadResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(uri: Uri, description: String, lat: Double?, lon: Double?, context: Context) {
        val imageFile = uriToFile(uri, context).reduceFileImage()

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = storyRepository.uploadStory(imagePart, descriptionBody, lat, lon)
                _uploadResponse.value = response
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }
}