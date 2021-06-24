package me.bytebeats.applifecycle.agp.util

import java.util.jar.JarFile

/**
 * Created by bytebeats on 2021/6/24 : 11:31
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class ClassScanner {
    static final boolean isTargetProxyClass(File file) {
        return isTargetProxyClass(file.name)
    }

    static final boolean isTargetProxyClass(String fileName) {
        return fileName.startsWith(Configs.PROXY_CLASS_PREFIX) && fileName.endsWith(Configs.PROXY_CLASS_SUFFIX)
    }

    static final boolean shouldProcessPreDexJar(String path) {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    private static File APP_LIFE_CYCLE_CALL_BACK_MANAGER_FILE

    static final File getAppLifecycleCallbackManagerFile() {
        return APP_LIFE_CYCLE_CALL_BACK_MANAGER_FILE
    }

    static final List<String> scanProxyClassesFromJar(File jarFile, File destFile) {
        def file = new JarFile(jarFile)
        def enumeration = file.entries()
        def proxyClassFiles = []
        while (enumeration.hasMoreElements()) {
            def jarEntry = enumeration.nextElement()
            def entryName = jarEntry.name
            if (entryName == Configs.APP_LIFE_CYCLE_MANAGER_FILE_NAME) {
                APP_LIFE_CYCLE_CALL_BACK_MANAGER_FILE = destFile
            } else {
                def fileName = entryName.substring(entryName.lastIndexOf('/') + 1)
                if (isTargetProxyClass(fileName)) {
                    println("target jar input : ${entryName.replace('/', '.')}")
                    proxyClassFiles.add(entryName.replace('/', '.'))
                }
            }
        }
        return proxyClassFiles
    }
}
