package com.harissabil.damome.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ChatMessageWithTransactions(
    @Embedded val chatMessage: ChatMessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ChatMessageTransactionCrossRef::class,
            parentColumn = "chatMessageId",
            entityColumn = "transactionId"
        )
    )
    val transactions: List<TransactionEntity>,
)
