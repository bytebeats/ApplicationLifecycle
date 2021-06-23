package me.bytebeats.base

import android.app.Application
import android.content.Context
import me.bytebeats.applifecycle.api.ApplicationLifecycleManager

/**
 * Created by bytebeats on 2021/6/1 : 15:28
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */

/**
 * An Application who is able to provide lifecycle to descent modules
 */
class LiveApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ApplicationLifecycleManager.init()
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationLifecycleManager.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        ApplicationLifecycleManager.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ApplicationLifecycleManager.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ApplicationLifecycleManager.onTrimMemory(level)
    }
}

