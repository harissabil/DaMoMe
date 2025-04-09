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
) {
    fun toTransactionChatContext() : String {
        val transactionType = if (type == TransactionType.INCOME) "income" else "expense"
        val transactionAmount = "$amount ${currency.symbol}"
        val transactionCategory = category
        val transactionDescription = description ?: ""
        val transactionDate = timestamp.toString()

        return """
            $transactionType: $transactionAmount
            Category: $transactionCategory
            Description: $transactionDescription
            Date: $transactionDate
        """.trimIndent()
    }
}

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