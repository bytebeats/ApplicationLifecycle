package me.bytebeats.applifecycle.api;

import android.content.Context;

/**
 * Created by bytebeats on 2021/6/1 : 15:53
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
public interface ApplicationLifecycleCallback {
    void onCreate(Context context);

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);

    int getPriority();

    int MIN_PRIORITY = 1;
    int MAX_PRIORITY = 10;
    int DEFAULT_PRIORITY = 5;
}
