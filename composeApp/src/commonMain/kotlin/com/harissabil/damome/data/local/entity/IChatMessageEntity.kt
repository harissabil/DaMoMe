package com.harissabil.damome.data.local.entity

import kotlinx.datetime.Instant

interface IChatMessageEntity {
    var id: Long?
    var chatGroupId: Long
    var isUser: Boolean
    var order: Int
    var message: String
    var timestamp: Instant
}