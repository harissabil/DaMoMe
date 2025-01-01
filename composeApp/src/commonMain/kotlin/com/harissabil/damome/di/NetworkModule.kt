package com.harissabil.damome.di

import com.harissabil.damome.data.remote.gemini.GeminiClient
import com.harissabil.damome.data.remote.text_embedding.api.TextEmbeddingClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val httpClientModule = module {
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
    single { TextEmbeddingClient(get()) }
    single { GeminiClient() }
}