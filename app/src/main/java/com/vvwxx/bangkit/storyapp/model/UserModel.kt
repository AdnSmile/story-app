package com.vvwxx.bangkit.storyapp.model

data class UserModel(
    val name: String,
    val token: String,
    val isLogin: Boolean,
    val lat: Float,
    val lon: Float
)
