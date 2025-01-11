package com.harissabil.damome.domain.model

import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import dev.shreyaspatil.ai.client.generativeai.type.Content
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.datetime.Instant

data class ChatGroup(
    val id: Long? = null,
    val name: String,
    val timestamp: Instant,
)

data class ChatMessage(
    val id: Long? = null,
    var chatGroupId: Long,
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
        chatGroupId = chatGroupId,
        isUser = isUser,
        order = order,
        message = message,
        timestamp = timestamp,
    )
}

fun ChatMessage.toContent(): Content {
    return content(
        role = if (this.isUser) "user" else "model",
    ) {
        text(this@toContent.message)
    }
}