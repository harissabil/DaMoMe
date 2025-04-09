package com.harissabil.damome.ui.screen.damommy_chat

import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.toReadableDateTime
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.TransactionType

data class TransactionChatContext(
    val id: Long? = null,
    val type: TransactionType,
    val timestamp: String,
    val amount: Double,
    val currency: Currency,
    val category: String,
    val description: String?,
)

fun Transaction.toTransactionChatContext(): TransactionChatContext {
    return TransactionChatContext(
        id = this.id,
        type = this.type,
        timestamp = this.timestamp.toReadableDateTime(),
        amount = this.amount,
        currency = this.currency,
        category = this.category,
        description = this.description,
    )
}
