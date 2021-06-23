package me.bytebeats.applifecycle.agp;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;

import org.gradle.api.Project;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bytebeats on 2021/6/23 : 21:19
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
public class ApplicationLifecycleTransform extends Transform {
    private static final String TAG = "applifecycle-agp-transform";
    private Project project;

    public ApplicationLifecycleTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        Set<QualifiedContent.ContentType> types = new HashSet<>();
        types.add(QualifiedContent.DefaultContentType.CLASSES);
        return types;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        Set<? super QualifiedContent.Scope> types = new HashSet<>();
        types.add(QualifiedContent.Scope.PROJECT);
        types.add(QualifiedContent.Scope.SUB_PROJECTS);
        types.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
        return types;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        System.out.println("<<<<<------" + TAG + " start transforming------>>>>>");
    }
}
