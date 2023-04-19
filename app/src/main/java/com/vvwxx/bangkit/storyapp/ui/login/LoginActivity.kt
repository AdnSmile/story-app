package com.vvwxx.bangkit.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.vvwxx.bangkit.storyapp.databinding.ActivityLoginBinding
import com.vvwxx.bangkit.storyapp.model.UserModel
import com.vvwxx.bangkit.storyapp.ui.home.HomeActivity
import com.vvwxx.bangkit.storyapp.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    private lateinit var user: UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        loginViewModel.getUser.observe(this) { user ->
            this.user = user
            if (user.isLogin) {
                saveUserPrefData()
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.message.observe(this) {
            if (it.isNotEmpty() && !user.isLogin) {
                showToast(it)
            }
        }

        playAnimation()
        setupView()
        setupAction()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, message, tvEmail, etEmail, tvPassword, etPassword, login)
            startDelay = 500
        }.start()
    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                email.isEmpty() -> binding.emailEditTextLayout.error = getString(R.string.empty_email)
                password.isEmpty() || password.length<8 -> binding.passwordEditTextLayout.error = getString(R.string.empty_password)
                else -> {
                    loginViewModel.userLogin(email, password)
                    loginViewModel.loginResponse.observe(this) {
                        if (it != null && !it.error) {
                            loginViewModel.loginPref()
                        }
                    }
                }
            }
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

    private fun saveUserPrefData() {
        loginViewModel.loginResponse.observe(this) {response ->
            val result = response.loginResult
            user = UserModel(result.name, result.token, true)
            Log.d(TAG, "nama : ${user.token}")
            loginViewModel.saveUserPref(user)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}