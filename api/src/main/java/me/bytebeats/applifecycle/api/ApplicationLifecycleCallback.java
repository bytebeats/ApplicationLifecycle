package me.bytebeats.applifecycle.api;

import android.app.Application;

/**
 * Created by bytebeats on 2021/6/1 : 15:53
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
public interface ApplicationLifecycleCallback {
    public void onCreate(Application application);

    public void onTerminate();

    public void onLowMemory();

    public void onTrimMemory(int level);
}
