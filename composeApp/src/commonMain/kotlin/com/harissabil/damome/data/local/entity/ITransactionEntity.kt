package com.harissabil.damome.data.local.entity

import com.harissabil.damome.core.utils.Currency
import kotlinx.datetime.Instant

interface ITransactionEntity {
    var id: Long?
    var type: String
    var timestamp: Instant
    var amount: Double
    var currency: Currency
    var category: String
    var description: String?
    var textToEmbed: String?
    var embedding: FloatArray?
}