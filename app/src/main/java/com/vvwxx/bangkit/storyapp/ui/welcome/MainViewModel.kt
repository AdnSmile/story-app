package com.vvwxx.bangkit.storyapp.ui.welcome

import androidx.lifecycle.ViewModel
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository

class MainViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {
    fun getUserPref() = storyRepository.getUserPref()
}