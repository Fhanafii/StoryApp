package com.fhanafi.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.pref.UserModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userRepository.login(email, password)
                if (response.error == false) {
                    // Save session with the token from response
                    response.loginResult?.token?.let { token ->
                        viewModelScope.launch {
                            saveSession(UserModel(email, token))
                        }
                    }
                    onResult(true)
                } else {
                    Log.e("LoginViewModel", "Login failed with message: ${response.message}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login: ${e.message}")
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getSession(onResult: (UserModel?) -> Unit) {
        viewModelScope.launch {
            val userSession = userRepository.getSession().firstOrNull()
            onResult(userSession)
        }
    }
}