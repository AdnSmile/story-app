package com.vvwxx.bangkit.storyapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.vvwxx.bangkit.storyapp.data.api.ApiService
import com.vvwxx.bangkit.storyapp.data.response.LoginResponse
import com.vvwxx.bangkit.storyapp.data.response.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class StoryAppRepository(
    private val apiService: ApiService,
    private val pref: UserPreferences
) {


    private val _loginRespon = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginRespon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _registerRespon = MutableLiveData<RegisterResponse> ()
    val registerResponse: LiveData<RegisterResponse> = _registerRespon

    fun userLogin(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                try {
                    if (response.isSuccessful) {
                        _loginRespon.value = response.body()
                    } else {
                        val jObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        _message.value = jObject?.getString("message")
                    }
                } catch (e: HttpException) {
                    Log.e(TAG, "onFailure: $e")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun userRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                try {
                    if (response.isSuccessful) {
                        _registerRespon.value = response.body()
                        _message.value = response.body()?.message
                    } else {
                        val jObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        _message.value = jObject?.getString("message")
                    }
                } catch (e: HttpException) {
                    Log.e(TAG, "onFailure: $e")
                }

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    suspend fun loginPref() {
        pref.login()
    }

    suspend fun saveUserPref(user: UserModel) {
        pref.saveUser(user)
    }

    suspend fun logout() {
        pref.logout()
    }

    fun getUserPref(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    companion object {
        private const val  TAG = "StoryAppRepository"

        @Volatile
        private var instance: StoryAppRepository? = null

        fun getInstance(
            apiService: ApiService,
            pref: UserPreferences
        ): StoryAppRepository =
            instance ?: synchronized(this) {
                instance ?: StoryAppRepository(apiService, pref)
            }.also { instance = it }
    }
}