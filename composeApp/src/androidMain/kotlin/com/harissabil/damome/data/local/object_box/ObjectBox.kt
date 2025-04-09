package com.harissabil.damome.data.local.object_box

import android.content.Context
import android.util.Log
import com.getkeepsafe.relinker.ReLinker
import com.harissabil.damome.data.local.entity.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context)
            .androidReLinker(ReLinker.log { message -> Log.d("ReLinker", message.toString()) })
            .name("damome_db")
            .build()
//        if (BuildConfig.DEBUG) {
//            val started = Admin(store).start(context)
//            Log.i("ObjectBoxAdmin", "Started: $started")
//        }
    }
}