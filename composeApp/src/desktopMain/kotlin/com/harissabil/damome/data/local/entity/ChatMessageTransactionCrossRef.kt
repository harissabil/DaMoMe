package com.harissabil.damome.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["chatMessageId", "transactionId"],
    foreignKeys = [
        ForeignKey(
            entity = ChatMessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatMessageId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["chatMessageId"]), Index(value = ["transactionId"])]
)
data class ChatMessageTransactionCrossRef(
    val chatMessageId: Long,
    val transactionId: Long,
)
