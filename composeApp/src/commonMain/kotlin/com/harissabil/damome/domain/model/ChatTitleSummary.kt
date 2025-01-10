package com.harissabil.damome.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatTitleSummary(
    val summary: String?,
    val error: Boolean,
)
