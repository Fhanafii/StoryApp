package com.fhanafi.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.remote.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userRepository.register(name, email, password)
                if (!response.error!!) {
                    onResult(true, "Registrasi Berhasil")
                } else {
                    onResult(false, response.message ?: "Registrasi Gagal")
                }
            }catch (e: Exception){
                val errorMessage = if (e is HttpException) {
                    val errorJson = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
                    errorResponse?.message ?: "Registrasi Gagal"
                } else {
                    e.message ?: "Registrasi Gagal"
                }
                onResult(false, errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }
}