package com.vvwxx.bangkit.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch

class MapViewModel (private val storyRepository: StoryAppRepository) : ViewModel() {

    val listMap: LiveData<List<ListStoryItem>> = storyRepository.listMap
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

    fun getListMap(token: String) {
        viewModelScope.launch {
            storyRepository.getListMap(token)
        }
    }

}