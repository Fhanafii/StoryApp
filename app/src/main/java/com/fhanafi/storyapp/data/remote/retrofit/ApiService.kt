package com.fhanafi.storyapp.data.remote.retrofit

import com.fhanafi.storyapp.data.remote.response.LoginResponse
import com.fhanafi.storyapp.data.remote.response.RegisterResponse
<<<<<<< HEAD
import com.fhanafi.storyapp.data.remote.response.StoryResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
=======
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
<<<<<<< HEAD

    @GET("stories")
    suspend fun getStories(): StoryResponse
=======
>>>>>>> 877769ecef66d56bdc771dea6dd7068e33e4d328
}