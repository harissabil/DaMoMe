package com.harissabil.damome.data.local.local_store

import com.harissabil.damome.data.local.object_box.TransactionObject

class TransactionObjectDesktop<E>(box: E): TransactionObject<E> {
    val entity: E = box
}