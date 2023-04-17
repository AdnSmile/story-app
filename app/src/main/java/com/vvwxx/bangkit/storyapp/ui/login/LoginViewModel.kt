package com.vvwxx.bangkit.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.data.response.LoginResponse
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import com.vvwxx.bangkit.storyapp.model.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LoginViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    val loginResponse: LiveData<LoginResponse> = storyRepository.loginResponse
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

    fun userLogin(email: String, password: String) {
        viewModelScope.launch {
            storyRepository.userLogin(email, password)
        }
    }

    fun loginPref() {
        viewModelScope.launch {
            storyRepository.loginPref()
        }
    }

    fun saveUserPref(user: UserModel) {
        viewModelScope.launch {
            storyRepository.saveUserPref(user)
        }
    }
}