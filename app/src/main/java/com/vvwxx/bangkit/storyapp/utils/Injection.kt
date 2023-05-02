package com.vvwxx.bangkit.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.vvwxx.bangkit.storyapp.data.api.ApiConfig
import com.vvwxx.bangkit.storyapp.data.db.StoriesDatabase
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import com.vvwxx.bangkit.storyapp.model.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun provideRepository(context: Context) : StoryAppRepository {
        val apiService = ApiConfig.getApiService()
        val preferences = UserPreferences.getInstance(context.dataStore)
        val database = StoriesDatabase.getDatabase(context)
        return StoryAppRepository.getInstance(apiService, preferences, database)
    }
}