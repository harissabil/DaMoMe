package com.harissabil.damome

import android.app.Application
import com.harissabil.damome.data.local.object_box.ObjectBox
import com.harissabil.damome.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}