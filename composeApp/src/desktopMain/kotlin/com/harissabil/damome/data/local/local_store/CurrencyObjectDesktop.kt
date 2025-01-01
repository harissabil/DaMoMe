package com.harissabil.damome.data.local.local_store

import com.harissabil.damome.data.local.object_box.CurrencyObject

class CurrencyObjectDesktop<E>(box: E): CurrencyObject<E> {
    val entity: E = box
}