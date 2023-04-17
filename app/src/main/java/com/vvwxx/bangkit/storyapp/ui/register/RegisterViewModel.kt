package com.vvwxx.bangkit.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.data.response.RegisterResponse
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    val registerResponse: LiveData<RegisterResponse> = storyRepository.registerResponse
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    fun userRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            storyRepository.userRegister(name, email, password)
        }
    }
}