package com.vvwxx.bangkit.storyapp.data.api

import com.vvwxx.bangkit.storyapp.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : AllStoriesResponse

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun postStories(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ) : Call<UploadStoriesResponse>

    @GET("stories?location=1")
    fun getMapStories(
        @Header("Authorization") token: String,
    ) : Call<AllStoriesResponse>
}