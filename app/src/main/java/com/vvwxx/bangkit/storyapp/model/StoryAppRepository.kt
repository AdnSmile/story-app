package com.vvwxx.bangkit.storyapp.model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.vvwxx.bangkit.storyapp.data.api.ApiConfig
import com.vvwxx.bangkit.storyapp.data.api.ApiService
import com.vvwxx.bangkit.storyapp.data.response.LoginResponse
import com.vvwxx.bangkit.storyapp.ui.login.LoginViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.security.AccessController.getContext

class StoryAppRepository(
    private val apiService: ApiService,
    private val pref: UserPreferences
) {

//    suspend fun login(email: String, password: String) = apiService.postLogin(email, password)

    private val _loginRespon = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginRespon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun userLogin(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginRespon.value = response.body()
                } else {
                    try {
                        val jObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        if (jObject != null) {
                            _message.value = jObject.getString("message")
                        }
                    } catch (e : HttpException) {
                        Log.e(TAG, "onFailure: $e catc")

                    }

                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
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