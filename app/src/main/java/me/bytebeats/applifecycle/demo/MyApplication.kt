package me.bytebeats.applifecycle.demo

import android.app.Application
import android.content.Context

/**
 * Created by bytebeats on 2021/6/1 : 15:28
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }
}