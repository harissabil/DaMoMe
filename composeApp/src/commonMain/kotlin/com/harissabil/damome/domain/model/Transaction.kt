package com.harissabil.damome.domain.model

import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.data.local.entity.ITransactionEntity
import kotlinx.datetime.Instant

data class Transaction(
    val id: Long? = null,
    val type: TransactionType,
    val timestamp: Instant,
    val amount: Double,
    val currency: Currency,
    val category: String,
    val description: String?,
    val textToEmbed: String?,
    val embedding: FloatArray?,
)

enum class TransactionType(val value: String) {
    INCOME("income"),
    EXPENSE("expense")
}

fun ITransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = id,
        type = TransactionType.entries.find { this.type.lowercase() == it.value }!!,
        timestamp = timestamp,
        amount = amount,
        currency = currency,
        category = category,
        description = description,
        textToEmbed = textToEmbed,
        embedding = embedding
    )
}