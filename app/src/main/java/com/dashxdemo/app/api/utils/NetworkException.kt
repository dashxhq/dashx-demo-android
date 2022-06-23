package com.dashxdemo.app.api.utils

open class NetworkException(message: String? = null, cause: Exception? = null) :
    Exception(message, cause) {

    data class NotConnectedException(override val cause: Exception? = null) :
        NetworkException(cause = cause)

    data class ServiceUnavailableException(
        override val message: String? = null,
        override val cause: Exception? = null
    ) : NetworkException(message = message, cause = cause)

    data class ServiceInternalException(
        val responseCode: Int? = null,
        override val message: String? = null,
        override val cause: Exception? = null
    ) : NetworkException(message = message, cause = cause)

    data class AuthenticationInvalidException(override val message: String? = null) :
        NetworkException(message = message)

    data class UnknownNetworkException(
        val description: String? = null,
        val responseCode: Int? = null,
        val url: String? = null,
        override val cause: Exception? = null
    ) : NetworkException(message = "[$responseCode] - $description", cause = cause)

}