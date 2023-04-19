package com.vvwxx.bangkit.storyapp.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvwxx.bangkit.storyapp.data.response.UploadStoriesResponse
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class CreateStoriesViewModel(private val storyRepository: StoryAppRepository) : ViewModel() {

    val uploadResponse: LiveData<UploadStoriesResponse> = storyRepository.uploadStoriesResponse
    val isLoading: LiveData<Boolean> = storyRepository.isLoading
    val message: LiveData<String> = storyRepository.message

    val getUser = storyRepository.getUserPref()

    fun uploadStories(photo: MultipartBody.Part, token: String, desc: String) {
        viewModelScope.launch {
            storyRepository.uploadStories(photo, token, desc)
        }
    }
}