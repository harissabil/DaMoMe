package com.harissabil.damome.data.local.entity

import kotlinx.datetime.Instant

interface ITransactionEntity {
    var id: Long?
    var type: String
    var timestamp: Instant
    var amount: Double
    var currency: String
    var category: String
    var description: String?
    var textToEmbed: String?
    var embedding: FloatArray?
}