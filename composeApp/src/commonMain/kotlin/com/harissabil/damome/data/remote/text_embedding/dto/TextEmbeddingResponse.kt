package com.harissabil.damome.data.remote.text_embedding.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TextEmbeddingResponse(

	@SerialName("embedding")
	val embedding: Embedding
)

@Serializable
data class Embedding(

	@SerialName("values")
	val values: FloatArray
)
