package com.harissabil.damome.data.remote.text_embedding.api

import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.data.remote.text_embedding.dto.Content
import com.harissabil.damome.data.remote.text_embedding.dto.ContentPart
import com.harissabil.damome.data.remote.text_embedding.dto.TextEmbeddingRequest
import com.harissabil.damome.data.remote.text_embedding.dto.TextEmbeddingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class TextEmbeddingClient(
    private val httpClient: HttpClient,
) {
    suspend fun fetchTextEmbedding(
        apiKey: String,
        text: String,
    ): Result<TextEmbeddingResponse> {
        val response = try {
            httpClient.post(
                urlString = "https://generativelanguage.googleapis.com/v1beta/models/text-embedding-004:embedContent"
            ) {
                parameter("key", apiKey)
                contentType(ContentType.Application.Json)
                setBody(
                    TextEmbeddingRequest(
                        model = "models/text-embedding-004",
                        content = Content(
                            parts = listOf(
                                ContentPart(text = text)
                            )
                        )
                    )
                )
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(503, "No internet connection!")
        } catch (e: SerializationException) {
            return Result.Error(500, "Serialization error!")
        } catch (e: ClientRequestException) { // 4xx errors
            val statusCode = e.response.status.value
            val errorMessage = e.response.bodyAsText() // read error message from response
            return Result.Error(statusCode, errorMessage)

        } catch (e: ServerResponseException) { // 5xx errors
            val statusCode = e.response.status.value
            val errorMessage = e.response.bodyAsText()
            return Result.Error(statusCode, errorMessage)
        } catch (e: Exception) { // General errors like timeout
            return Result.Error(-1, e.message ?: "Something went wrong!")
        }

        val textEmbedded = response.body<TextEmbeddingResponse>()
        return Result.Success(textEmbedded)
    }
}