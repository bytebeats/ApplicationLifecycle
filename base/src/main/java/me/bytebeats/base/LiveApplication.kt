package me.bytebeats.base

import android.app.Application
import android.content.Context
import android.util.Log
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
        Log.d(TAG, "attachBaseContext")
        ApplicationLifecycleManager.init()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        ApplicationLifecycleManager.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "onTerminate")
        ApplicationLifecycleManager.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d(TAG, "onLowMemory")
        ApplicationLifecycleManager.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(TAG, "onTrimMemory $level")
        ApplicationLifecycleManager.onTrimMemory(level)
    }

    companion object {
        private const val TAG = "LiveApplication"
    }
}

