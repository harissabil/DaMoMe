package com.harissabil.damome.data.local.local_store

import com.harissabil.damome.data.local.object_box.ChatGroupObject

class ChatGroupObjectDesktop<E>(box: E) : ChatGroupObject<E> {
    val entity: E = box
}