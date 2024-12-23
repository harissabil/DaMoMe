package com.harissabil.damome.domain.model

import com.harissabil.damome.data.remote.text_embedding.dto.TextEmbeddingResponse

data class TextEmbedding(
    val values: List<Double>,
)

fun TextEmbeddingResponse.toTextEmbedding() = TextEmbedding(
    values = this.embedding.values
)
