package com.vvwxx.bangkit.storyapp.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.vvwxx.bangkit.storyapp.R
import com.vvwxx.bangkit.storyapp.databinding.ActivityRegisterBinding
import com.vvwxx.bangkit.storyapp.ui.login.LoginActivity
import com.vvwxx.bangkit.storyapp.ui.login.LoginViewModel
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        registerViewModel.registerResponse.observe(this) { response->
            Log.d("RegisterActivity", response.message)
            if (response != null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.message.observe(this) {
            if (it.isNotEmpty()) {
                showToast(it)
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

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> binding.nameEditTextLayout.error = getString(R.string.empty_name)
                email.isEmpty() -> binding.emailEditTextLayout.error = getString(R.string.empty_email)
                password.isEmpty() -> binding.passwordEditTextLayout.error = getString(R.string.empty_password)
                password.length >= 8 -> {
                    registerViewModel.userRegister(name, email, password)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}