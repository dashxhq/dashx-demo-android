package com.dashxdemo.app.api

import android.util.Log
import com.dashxdemo.app.BuildConfig
import com.dashxdemo.app.api.utils.Outcome
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

val httpClient = HttpClient(OkHttp) {

    expectSuccess = false // to avoid exception on non 200 responses

    install(JsonFeature) {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(Auth) {
        bearer {
            //TODO: Add tokens here
        }
    }

    if (BuildConfig.DEBUG) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("Message",message)
                }
            }
            level = LogLevel.BODY
        }
    }

    defaultRequest {
        contentType(ContentType.Application.Json)
    }
}

suspend fun runInParallel(vararg blocks: suspend () -> Outcome<*>): Outcome<Unit> {
    return coroutineScope {
        val outcomes = blocks.map {
            async { it.invoke() }
        }.awaitAll()

        outcomes.firstOrNull { it is Outcome.Error }?.let {
            it as Outcome.Error
        } ?: Outcome.Success
    }
}