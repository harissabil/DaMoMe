package com.harissabil.damome.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionOcrData(
    val amount: Double?,
    val dateTime: String?,
    val category: String?,
    val description: String?,
    val type: String?,
    val error: Boolean,
)
