package com.harissabil.damome.data.local.object_box

import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.ITransactionEntity
import kotlinx.coroutines.flow.Flow

interface ChatLocalStore<T, U, W> {
    fun <E : IChatGroupEntity> putChatGroup(chatGroup: ChatGroupObject<E>): Long

    fun <V : IChatMessageEntity, X : ITransactionEntity> putChatMessageAndTransactionToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
        transactions: List<TransactionObject<X>>,
    )

    fun <V : IChatMessageEntity> putChatMessageToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
    )

    fun <E : IChatGroupEntity, V : IChatMessageEntity> putChatGroupAndMessage(
        chatGroup: ChatGroupObject<E>,
        chatMessages: List<ChatMessageObject<V>>,
    )

    fun getTransactionByChatMessageId(chatMessageId: Long): List<W>

    fun allChatGroup(): Flow<List<T>>

    fun allChatMessage(chatGroupId: Long): List<U>

    fun deleteChatGroupAndMessage(chatGroupId: Long)
}