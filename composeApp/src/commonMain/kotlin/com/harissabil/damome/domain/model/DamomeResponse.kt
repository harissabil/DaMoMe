package com.harissabil.damome.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DamomeResponse(
    val showRelatedTransaction: Boolean,
    val relatedTransactionIds: List<Long>? = null,
    val message: String,
)
