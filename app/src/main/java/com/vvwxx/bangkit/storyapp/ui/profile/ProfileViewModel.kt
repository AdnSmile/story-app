package com.vvwxx.bangkit.storyapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}