package com.harissabil.damome.domain.model

import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import kotlinx.datetime.Instant

data class ChatGroup(
    val id: Long? = null,
    val name: String,
    val timestamp: Instant,
)

data class ChatMessage(
    val id: Long? = null,
    val isUser: Boolean,
    val order: Int,
    val message: String,
    val timestamp: Instant,
    val relatedTransactions: List<Transaction>? = null,
)

fun IChatGroupEntity.toChatGroup(): ChatGroup {
    return ChatGroup(
        id = id,
        name = name,
        timestamp = timestamp,
    )
}

fun IChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        isUser = isUser,
        order = order,
        message = message,
        timestamp = timestamp,
    )
}
