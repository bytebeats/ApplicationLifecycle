package me.bytebeats.applifecycle.agp.util

/**
 * Created by bytebeats on 2021/6/24 : 11:24
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class Configs {
     static final  PROXY_CLASS_PREFIX = "AppLifecycle\$\$"
     static final  PROXY_CLASS_SUFFIX = "\$\$Proxy.class"
     static final  APP_LIFE_CYCLE_MANAGER_FILE_NAME = "me/bytebeats/applifecycle/api/ApplicationLifecycleManager.class"
     static final  APP_LIFE_CYCLE_MANAGER_CLASS_NAME = "me/bytebeats/applifecycle/api/ApplicationLifecycleManager"
     static final  INJECT_ENTRY_METHOD_NAME = "addApplicationLifecycleCallback"
     static final  INJECT_MANAGER_METHOD_NAME = "init"
     static final  INJECT_ENTRY_METHOD_PARAM = "(Ljava/lang/String;)V"
}
