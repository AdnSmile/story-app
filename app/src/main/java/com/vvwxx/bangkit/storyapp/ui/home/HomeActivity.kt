package com.vvwxx.bangkit.storyapp.ui.home

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.vvwxx.bangkit.storyapp.databinding.ActivityHomeBinding
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import com.vvwxx.bangkit.storyapp.data.api.ApiService
import com.vvwxx.bangkit.storyapp.model.StoryAppRepository
import com.vvwxx.bangkit.storyapp.model.UserPreferences
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var factory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        setupView()
        loginSession()

        binding.logoutButton.setOnClickListener {

        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun loginSession() {
//        homeViewModel.
    }
}