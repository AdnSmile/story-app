package com.vvwxx.bangkit.storyapp.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoriesViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = storyRepository.isAddLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

    fun uploadStories(photo: MultipartBody.Part, token: String, desc: RequestBody) {
        viewModelScope.launch {
            storyRepository.uploadStories(photo, token, desc)
        }
    }
}