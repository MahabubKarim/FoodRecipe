package com.mmk.foodrecipe.data.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object KtorClient {
    private const val BASE_URL = "https://api.spoonacular.com/"

    fun create(apiKey: String): HttpClient {
        return HttpClient(CIO) {
            /*defaultRequest {
                url(BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                parameter("apiKey", apiKey)
            }*/
            defaultRequest {
                url(BASE_URL)
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                url {
                    parameters.append("apiKey", apiKey)
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Important for APIs with many fields
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT // Or a custom logger
                level = LogLevel.ALL // Log everything, adjust for production
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }
        }
    }
}