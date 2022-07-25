package com.dashxdemo.app.api

import com.dashxdemo.app.api.requests.*
import com.dashxdemo.app.api.responses.*
import retrofit2.http.*
import com.dashxdemo.app.api.requests.ForgotPasswordRequest
import com.dashxdemo.app.api.requests.LoginRequest
import com.dashxdemo.app.api.requests.RegisterRequest
import com.dashxdemo.app.api.requests.UpdateProfileRequest
import com.dashxdemo.app.api.responses.ForgotPasswordResponse
import com.dashxdemo.app.api.responses.LoginResponse
import com.dashxdemo.app.api.responses.RegisterResponse
import com.dashxdemo.app.api.responses.UpdateProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @PATCH("update-profile")
    fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): Call<UpdateProfileResponse>

    @GET("posts")
    fun getPosts(): Call<PostsResponse>

    @POST("posts")
    fun createPost(@Body createPostRequest: CreatePostRequest): Call<CreatePostResponse>

    @PUT("posts/{post_id}/toggle-bookmark")
    fun bookmark(@Path("post_id") postId: Int): Call<BookmarksResponse>

    @GET("/posts/bookmarked")
    fun bookmarkedPost(): Call<BookmarkedPostResponse>
}
