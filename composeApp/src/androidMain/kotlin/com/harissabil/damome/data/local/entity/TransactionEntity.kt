package com.harissabil.damome.data.local.entity

import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.data.local.object_box.CurrencyConverter
import com.harissabil.damome.data.local.object_box.TimestampConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.HnswIndex
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.NameInDb
import io.objectbox.annotation.VectorDistanceType
import kotlinx.datetime.Instant

@Entity
data class TransactionEntity(
    @Id(assignable = false)
    override var id: Long? = 0,
    @Index
    override var type: String,
    @Index
    @Convert(converter = TimestampConverter::class, dbType = Long::class)
    override var timestamp: Instant,
    override var amount: Double,
    @Convert(converter = CurrencyConverter::class, dbType = String::class)
    override var currency: Currency,
    override var category: String,
    override var description: String? = null,
    @NameInDb("text_to_embed")
    override var textToEmbed: String? = null,
    @HnswIndex(
        dimensions = 768,
        distanceType = VectorDistanceType.COSINE,
        neighborsPerNode = 30,
        indexingSearchCount = 200
    )
    override var embedding: FloatArray? = null,
) : ITransactionEntity