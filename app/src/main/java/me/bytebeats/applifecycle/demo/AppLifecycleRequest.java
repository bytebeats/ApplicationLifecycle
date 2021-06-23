package me.bytebeats.applifecycle.demo;

import android.content.Context;
import android.util.Log;

import me.bytebeats.applifecycle.api.ApplicationLifecycleCallback;
import me.bytebeats.applifecycle.annotation.ApplicationLifecycle;

/**
 * Created by bytebeats on 2021/6/23 : 19:26
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */


/**
 * Test ApplicationLifecycle & ApplicationLifecycleCallback
 */
@ApplicationLifecycle
public class AppLifecycleRequest implements ApplicationLifecycleCallback {
    private static final String TAG = "BusinessAppLifecycle";

    @Override
    public void onCreate(Context context) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory(" + level + ")");
    }

    @Override
    public int getPriority() {
        Log.d(TAG, "getPriority");
        return ApplicationLifecycleCallback.DEFAULT_PRIORITY;
    }
}
