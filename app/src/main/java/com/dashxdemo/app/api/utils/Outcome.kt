package com.dashxdemo.app.api.utils

sealed class Outcome<out T> {
    data class Success<out T>(val data: T) : Outcome<T>()
    data class Error(
        val exception: Exception
    ) : Outcome<Nothing>() {
        val errorMessage: String
            get() = exception.message ?: exception.toString()
    }

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[errorMessage=$errorMessage]"
        }
    }

    suspend fun onSuccessOrError(onSuccess: suspend (data: T) -> Unit): Outcome<Unit> =
        when (this) {
            is Success -> {
                onSuccess(data)
                Success
            }
            is Error -> this
        }

    fun doOnError(onError: (error: Error) -> Unit) =
        when (this) {
            is Error -> {
                onError(this)
            }
            is Success -> {
            }
        }

    companion object {
        val Success = Success(Unit)
    }
}