package com.harissabil.damome.data.local.object_box

interface CurrencyLocalStore<T> {
    fun <E> put(entity: CurrencyObject<E>)
    fun get(): T
}