package com.vvwxx.bangkit.storyapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.vvwxx.bangkit.storyapp.data.api.ApiService
import com.vvwxx.bangkit.storyapp.data.db.StoriesDatabase
import com.vvwxx.bangkit.storyapp.data.paging.StoriesRemoteMediator
import com.vvwxx.bangkit.storyapp.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class StoryAppRepository(
    private val apiService: ApiService,
    private val pref: UserPreferences,
    private val db: StoriesDatabase
) {


    private val _loginRespon = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginRespon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isAddLoading = MutableLiveData<Boolean>()
    val isAddLoading: LiveData<Boolean> = _isAddLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _registerRespon = MutableLiveData<RegisterResponse> ()
    val registerResponse: LiveData<RegisterResponse> = _registerRespon

    private val _listMap = MutableLiveData<List<ListStoryItem>> ()
    val listMap: LiveData<List<ListStoryItem>> = _listMap

    private val _storiesData = MutableLiveData<DetailResponse> ()
    val storiesData : LiveData<DetailResponse> = _storiesData

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

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoriesRemoteMediator(db, pref, apiService),
            pagingSourceFactory = {
                db.storiesDao().getAllStories()
            }
        ).liveData
    }

    fun getDetailStories(token: String, id: String) {
        _isAddLoading.value = true
        val client = apiService.getDetailStories("Bearer $token", id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isAddLoading.value = false
                if (response.isSuccessful){
                    _storiesData.value = response.body()
                    _message.value = response.message()
                    Log.e(TAG, response.message())
                } else {
                    _message.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isAddLoading.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun uploadStories(photo: MultipartBody.Part, token: String, desc: RequestBody, lat: Float, lon: Float) {
        _isLoading.value = true
        val client = apiService.postStories(photo, "Bearer $token", desc, lat, lon)
        client.enqueue(object : Callback<UploadStoriesResponse> {
            override fun onResponse(
                call: Call<UploadStoriesResponse>,
                response: Response<UploadStoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _message.value = "Uploading stories ${responseBody.message}"
                    }
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<UploadStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getListMap(token: String) {
        _isLoading.value = true
        val client = apiService.getMapStories("Bearer $token")
        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, response.message())
                    _listMap.value = response.body()?.listStory
                    _message.value = response.message()
                } else {
                    _message.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    suspend fun saveUserPref(user: UserModel) {
        pref.saveUser(user)
    }

    suspend fun setLocation(lat: Float, lon: Float){
        pref.setLocation(lat, lon)
    }

    suspend fun logout() {
        _loginRespon.value = null
        pref.logout()
    }

    fun getUserPref(): LiveData<UserModel> {
        _message.value = ""
        return pref.getUser().asLiveData()
    }

    companion object {
        private const val  TAG = "StoryAppRepository"

        @Volatile
        private var instance: StoryAppRepository? = null

        fun getInstance(
            apiService: ApiService,
            pref: UserPreferences,
            db: StoriesDatabase
        ): StoryAppRepository =
            instance ?: synchronized(this) {
                instance ?: StoryAppRepository(apiService, pref, db)
            }.also { instance = it }
    }
}