package com.dashxdemo.app.api

import com.dashxdemo.app.api.utils.NetworkException
import com.dashxdemo.app.api.utils.Outcome
import io.ktor.client.statement.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class DashXService {

    private suspend inline fun <reified ResponseType> handleCallAsOutcome(
        url: String,
        returnRawResponse: Boolean = false,
        request: (String) -> HttpResponse,
    ): Outcome<ResponseType> =
        try {
            Outcome.Success(handleCall(url, returnRawResponse, request))
        } catch (ex: Exception) {
            Outcome.Error(ex)
        }

    private suspend inline fun <reified ResponseType> handleCall(
        url: String,
        returnRawResponse: Boolean = false,
        request: (String) -> HttpResponse,
    ): ResponseType {
        val response = try {
            request(url)
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            throw NetworkException.UnknownNetworkException(url = url, cause = ex)
        }

        val content = response.readText(Charsets.UTF_8)
        val responseCode = response.status.value

        return try {
            when (responseCode) {
                502 -> {
                    // service under maintenance
                    throw NetworkException.ServiceUnavailableException()
                }
                401 -> {
                    throw NetworkException.AuthenticationInvalidException(content)
                }
                in 400..499 -> {
                    throw NetworkException.ServiceInternalException(responseCode, content)
                }
                in 200..300 -> {
                    if (returnRawResponse) {
                        response as ResponseType
                    } else {
                        val parsed: ResponseType = JSON.decodeFromString(content)
                        parsed
                    }
                }
                else -> {
                    throw NetworkException.UnknownNetworkException(
                        responseCode = responseCode,
                        description = content,
                        url = url,
                    )
                }
            }
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: SerializationException) {
            // parse exception
            throw NetworkException.UnknownNetworkException(
                responseCode = responseCode,
                url = url,
                cause = ex
            )
        }
    }

    companion object{
        const val BASE_URL = "https://node.dashxdemo.com/"

        private val JSON = Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
        }
    }
}