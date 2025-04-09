package com.harissabil.damome.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harissabil.damome.core.utils.Currency
import kotlinx.datetime.Instant

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = 0,
    override var type: String,
    override var timestamp: Instant,
    override var amount: Double,
    override var currency: Currency,
    override var category: String,
    override var description: String? = null,
    @ColumnInfo(name = "text_to_embed")
    override var textToEmbed: String? = null,
    override var embedding: FloatArray? = null,
) : ITransactionEntity