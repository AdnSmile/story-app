package com.vvwxx.bangkit.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch
class HomeViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    val listStories: LiveData<List<ListStoryItem>> = storyRepository.listStories
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

    fun getAllStories(token: String) {
        viewModelScope.launch {
            storyRepository.getAllStories(token)
        }
    }
}