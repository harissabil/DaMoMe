package com.harissabil.damome.core.utils

import com.harissabil.damome.domain.model.Category
import com.harissabil.damome.domain.model.TransactionType

fun formatToTextToEmbed(
    transactionType: TransactionType,
    amount: Double,
    currency: Currency,
    category: Category,
    description: String,
): String {
    return "${transactionType.value} ${
        parseFormattedAmount(
            amount.toString(),
            currency
        )
    } ${currency.symbol} for ${category.value}: ${description.lowercase()}"
}