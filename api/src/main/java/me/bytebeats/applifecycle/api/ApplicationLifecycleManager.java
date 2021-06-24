package me.bytebeats.applifecycle.api;

import android.app.Application;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLifecycleManager {
    private static final List<ApplicationLifecycleCallback> mCallbacks = new ArrayList<>();

    public static void init() {

    }

    public static void addApplicationLifecycleCallback(ApplicationLifecycleCallback callback) {
        mCallbacks.add(callback);
    }

    public static void addApplicationLifecycleCallback(String appLifecycleCallbackProxyClassName) {
        if (TextUtils.isEmpty(appLifecycleCallbackProxyClassName)) {
            return;
        }
        try {
            Object object = Class.forName(appLifecycleCallbackProxyClassName).getConstructor().newInstance();
            if (object instanceof ApplicationLifecycleCallback) {
                addApplicationLifecycleCallback((ApplicationLifecycleCallback) object);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void onCreate(Application application) {
        for (ApplicationLifecycleCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onCreate(application);
            }
        }
    }

    public static void onTerminate() {
        for (ApplicationLifecycleCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onTerminate();
            }
        }
    }

    public static void onLowMemory() {
        for (ApplicationLifecycleCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onLowMemory();
            }
        }
    }

    public static void onTrimMemory(int level) {
        for (ApplicationLifecycleCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onTrimMemory(level);
            }
        }
    }
}