package com.harissabil.damome.data.local.entity

import com.harissabil.damome.data.local.object_box.TimestampConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity
data class ChatMessageEntity(
    @Id
    override var id: Long? = null,
    override var chatGroupId: Long = 0L,
    override var isUser: Boolean = false,
    override var order: Int = 0,
    override var message: String = "",
    @Convert(converter = TimestampConverter::class, dbType = Long::class)
    override var timestamp: Instant = Clock.System.now(),
) : IChatMessageEntity {
    lateinit var chatGroup: ToOne<ChatGroupEntity>
    lateinit var transactions: ToMany<TransactionEntity>
}
