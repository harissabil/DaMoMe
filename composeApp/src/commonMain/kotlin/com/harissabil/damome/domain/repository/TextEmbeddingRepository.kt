package com.harissabil.damome.domain.repository

import com.harissabil.damome.domain.model.TextEmbedding
import com.harissabil.damome.core.utils.Result

interface TextEmbeddingRepository {

    suspend fun getEmbedding(text: String): Result<TextEmbedding>
}