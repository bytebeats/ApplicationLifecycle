package me.bytebeats.applifecycle.agp.util

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project


/**
 * Created by bytebeats on 2021/12/15 : 11:30
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */

class PluginExtensionInitializer {
    static Project mProject

    static void init(Project project) {
        mProject = project
        boolean hasAppPlugin = mProject.plugins.hasPlugin(AppPlugin.class)
        boolean hasLibraryPlugin = mProject.plugins.hasPlugin(LibraryPlugin.class)
        if (!hasAppPlugin && !hasLibraryPlugin) {
            throw IllegalStateException("ApplicationLifecycle AGP only works for 'com.android.application' or 'com.android.library' plugin.")
        }
        mProject.extensions.create(PluginExtension.EXTENSION_NAME, PluginExtension.class)
    }

    private static void makeSureInitialized() {
        if (mProject == null) {
            throw IllegalStateException("PluginExtensionInitializer#init(Project) is not initialized")
        }
    }
}