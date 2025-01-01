package com.harissabil.damome.data.local.object_box

import android.content.Context
import android.util.Log
import com.harissabil.damome.BuildConfig
import com.harissabil.damome.data.local.entity.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.Admin

object ObjectBox {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context)
            .build()
        if (BuildConfig.DEBUG) {
            val started = Admin(store).start(context)
            Log.i("ObjectBoxAdmin", "Started: $started")
        }
    }
}