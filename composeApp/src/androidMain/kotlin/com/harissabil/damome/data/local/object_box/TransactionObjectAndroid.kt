package com.harissabil.damome.data.local.object_box

class TransactionObjectAndroid<E>(box: E): TransactionObject<E> {
    val entity: E = box
}