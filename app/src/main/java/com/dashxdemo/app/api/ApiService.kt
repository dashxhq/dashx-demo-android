package com.dashxdemo.app.api

import com.dashxdemo.app.api.requests.*
import com.dashxdemo.app.api.responses.*
import retrofit2.Call
import retrofit2.http.*

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
    fun bookmark(@Path("post_id") postId: Int): Call<BookmarksReponse>

    @GET("/posts/bookmarked")
    fun bookmarkedPost(): Call<BookmarkedPostResponse>
}
