package com.vvwxx.bangkit.storyapp.ui.home

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.vvwxx.bangkit.storyapp.data.response.ListStoryItem
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import com.vvwxx.bangkit.storyapp.model.UserPreferences
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
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