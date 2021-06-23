package me.bytebeats.applifecycle.agp;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ApplicationLifecyclePlugin implements Plugin<Project> {
    private static final String TAG = "applifecycle-agp";

    @Override
    public void apply(Project project) {
        System.out.println("------" + TAG + " started------");
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        android.registerTransform(new ApplicationLifecycleTransform(project));
    }
}