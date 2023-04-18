package com.vvwxx.bangkit.storyapp.ui.welcome

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout.TabGravity
import com.vvwxx.bangkit.storyapp.R
import com.vvwxx.bangkit.storyapp.databinding.ActivityMainBinding
import com.vvwxx.bangkit.storyapp.ui.home.HomeActivity
import com.vvwxx.bangkit.storyapp.ui.login.LoginActivity
import com.vvwxx.bangkit.storyapp.ui.register.RegisterActivity
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        mainViewModel.getUserPref().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
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

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}