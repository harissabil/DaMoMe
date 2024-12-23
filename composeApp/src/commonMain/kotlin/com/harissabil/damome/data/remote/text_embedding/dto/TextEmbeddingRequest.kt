package com.harissabil.damome.data.remote.text_embedding.dto

import kotlinx.serialization.Serializable

@Serializable
data class TextEmbeddingRequest(
    val model: String,
    val content: Content
)

@Serializable
data class Content(
    val parts: List<ContentPart>
)

@Serializable
data class ContentPart(
    val text: String
)