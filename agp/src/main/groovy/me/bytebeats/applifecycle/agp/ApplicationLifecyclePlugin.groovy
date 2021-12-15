package me.bytebeats.applifecycle.agp

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import me.bytebeats.applifecycle.agp.util.Log
import me.bytebeats.applifecycle.agp.util.PluginExtensionInitializer
import org.gradle.api.Plugin
import org.gradle.api.Project

class ApplicationLifecyclePlugin implements Plugin<Project> {
    private static final String TAG = "AppLifecycleAgp"

    @Override
    void apply(Project project) {
        println("------" + TAG + " started------")
        Log.init(project.logger)
        PluginExtensionInitializer.init(project)
        if (project.plugins.hasPlugin(AppPlugin.class)) {
            AppExtension android = project.getExtensions().getByType(AppExtension.class)
            android.registerTransform(new ApplicationLifecycleTransform(project))
        }
    }
}