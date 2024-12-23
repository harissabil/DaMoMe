package com.harissabil.damome.data.remote.text_embedding.repository

import com.harissabil.damome.core.secret.AppSecret
import com.harissabil.damome.domain.model.TextEmbedding
import com.harissabil.damome.domain.model.toTextEmbedding
import com.harissabil.damome.domain.repository.TextEmbeddingRepository
import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.data.remote.text_embedding.api.TextEmbeddingClient

class TextEmbeddingRepositoryImpl(
    private val textEmbeddingClient: TextEmbeddingClient,
) : TextEmbeddingRepository {
    override suspend fun getEmbedding(text: String): Result<TextEmbedding> {
        return when (val response = textEmbeddingClient.fetchTextEmbedding(
            apiKey = AppSecret.GEMINI_API_KEY,
            text
        )) {
            is Result.Success -> Result.Success(response.data.toTextEmbedding())
            is Result.Error -> Result.Error(response.statusCode, response.message)
        }
    }
}