package com.vvwxx.bangkit.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import com.vvwxx.bangkit.storyapp.model.UserPreferences
import com.vvwxx.bangkit.storyapp.ui.home.HomeViewModel
import com.vvwxx.bangkit.storyapp.ui.login.LoginViewModel
import com.vvwxx.bangkit.storyapp.ui.register.RegisterViewModel
import com.vvwxx.bangkit.storyapp.ui.welcome.MainViewModel

class ViewModelFactory(private val pref: StoryAppRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}