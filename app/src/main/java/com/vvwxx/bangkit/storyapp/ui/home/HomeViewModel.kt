package com.vvwxx.bangkit.storyapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    fun getUserPref() = storyRepository.getUserPref()

}