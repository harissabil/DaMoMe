package com.harissabil.damome.data.local.object_box

import android.content.Context
import com.harissabil.damome.data.local.entity.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context)
            .build()
    }
}