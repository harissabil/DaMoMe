package com.harissabil.damome.data.local.local_store

import com.harissabil.damome.data.local.object_box.ChatMessageObject

class ChatMessageObjectDesktop<E>(box: E) : ChatMessageObject<E> {
    val entity: E = box
}