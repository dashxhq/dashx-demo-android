package com.dashxdemo.app.api

import android.content.Context
import com.dashxdemo.app.api.requests.*
import com.dashxdemo.app.api.responses.*
import com.dashxdemo.app.pref.AppPref
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit


class ApiClient private constructor(private val applicationContext: Context) {
    private val service: ApiService

    companion object {
        const val BASE_URL = "https://node.dashxdemo.com/"

        private var INSTANCE: ApiClient? = null

        fun getInstance(applicationContext: Context): ApiClient {
            if (INSTANCE == null) {
                INSTANCE = ApiClient(applicationContext)
            }
            return INSTANCE!!
        }
    }

    init {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(Interceptor {
                val builder = it.request().newBuilder()
                val token = AppPref(applicationContext).getUserToken()
                if (!token.isNullOrEmpty()) {
                    builder.addHeader("Authorization", "Bearer $token")
                }
                return@Interceptor it.proceed(builder.build())
            })
        }.build()

        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)).build()

        service = retrofit.create(ApiService::class.java)

    }

    fun login(loginRequest: LoginRequest, callback: Callback<LoginResponse>) {
        val call = service.login(loginRequest)
        call.enqueue(callback)
    }

    fun register(registerRequest: RegisterRequest, callback: Callback<RegisterResponse>) {
        val call = service.register(registerRequest)
        call.enqueue(callback)
    }

    fun forgotPassword(
        forgotPasswordRequest: ForgotPasswordRequest,
        callback: Callback<ForgotPasswordResponse>,
    ) {
        val call = service.forgotPassword(forgotPasswordRequest)
        call.enqueue(callback)
    }

    fun updateProfile(
        updateProfileRequest: UpdateProfileRequest,
        callback: Callback<UpdateProfileResponse>,
    ) {
        val call = service.updateProfile(updateProfileRequest)
        call.enqueue(callback)
    }

    fun getPosts(callback: Callback<PostsResponse>) {
        val call = service.getPosts()
        call.enqueue(callback)
    }

    fun createPost(
        createPostRequest: CreatePostRequest,
        callback: Callback<CreatePostResponse>,
    ) {
        val call = service.createPost(createPostRequest)
        call.enqueue(callback)
    }

    fun toggleBookmark(id: Int, callback: Callback<ToggleBookmarkResponse>) {
        val call = service.toggleBookmark(id)
        call.enqueue(callback)
    }

    fun getBookmarkedPosts(callback: Callback<PostsResponse>) {
        val call = service.getBookmarkedPosts()
        call.enqueue(callback)
    }

    fun contact(contactRequest: ContactRequest, callback: Callback<ContactResponse>) {
        val call = service.contact(contactRequest)
        call.enqueue(callback)
    }

    fun getProfile(callback: Callback<ProfileResponse>) {
        val call = service.getProfile()
        call.enqueue(callback)
    }
}
