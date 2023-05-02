package com.vvwxx.bangkit.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
class HomeViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    val listStories: LiveData<PagingData<ListStoryItem>> = storyRepository.getAllStories().cachedIn(viewModelScope)
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

}