package com.vvwxx.bangkit.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.data.response.DetailResponse
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {
    val storiesData: LiveData<DetailResponse> = storyRepository.storiesData
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

    fun getDetailStories(token: String, id: String) {
        viewModelScope.launch {
            storyRepository.getDetailStories(token, id)
        }
    }
}