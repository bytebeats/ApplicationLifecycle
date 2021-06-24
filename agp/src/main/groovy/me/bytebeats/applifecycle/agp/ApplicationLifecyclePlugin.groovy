package me.bytebeats.applifecycle.agp

import com.android.build.gradle.AppExtension

import org.gradle.api.Plugin
import org.gradle.api.Project

class ApplicationLifecyclePlugin implements Plugin<Project> {
    private static final String TAG = "applifecycle-agp"

    @Override
    void apply(Project project) {
        println("------" + TAG + " started------")
        AppExtension android = project.getExtensions().getByType(AppExtension.class)
        android.registerTransform(new ApplicationLifecycleTransform(project))
    }
}