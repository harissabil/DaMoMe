package com.harissabil.damome.domain.repository

import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun saveChatGroup(chatGroup: ChatGroup): Long

    suspend fun saveChatMessage(chatGroupId: Long, messages: ChatMessage)

    fun getChatHistory(): Flow<List<ChatGroup>>

    suspend fun getChatMessage(chatGroupId: Long): List<ChatMessage>

    suspend fun deleteChatGroup(chatGroupId: Long)
}