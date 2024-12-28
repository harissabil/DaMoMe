package com.harissabil.damome.domain.model

import com.harissabil.damome.data.local.entity.ICurrencyEntity

data class Currency(
    val id: Long? = null,
    val currency: String,
)

fun ICurrencyEntity.toCurrency(): Currency {
    return Currency(
        id = this.id,
        currency = this.currency,
    )
}
