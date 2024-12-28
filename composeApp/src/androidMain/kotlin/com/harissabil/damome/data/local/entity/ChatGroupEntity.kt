package com.harissabil.damome.data.local.entity

import com.harissabil.damome.data.local.object_box.TimestampConverter
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import kotlinx.datetime.Instant

@Entity
data class ChatGroupEntity(
    @Id(assignable = false)
    override var id: Long? = null,
    override var name: String,
    @Convert(converter = TimestampConverter::class, dbType = Long::class)
    override var timestamp: Instant
): IChatGroupEntity {
    @Backlink(to = "chatGroup")
    lateinit var chatMessages: ToMany<ChatMessageEntity>
}
