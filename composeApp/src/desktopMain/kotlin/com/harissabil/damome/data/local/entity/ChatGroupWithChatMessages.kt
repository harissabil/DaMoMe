package com.harissabil.damome.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ChatGroupWithChatMessages(
    @Embedded val chatGroupEntity: ChatGroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chatGroupId",
    )
    val chatMessages: List<ChatMessageEntity>,
)