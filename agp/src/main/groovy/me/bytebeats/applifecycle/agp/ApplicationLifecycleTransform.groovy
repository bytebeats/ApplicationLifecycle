package me.bytebeats.applifecycle.agp

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import me.bytebeats.applifecycle.agp.asm.ApplicationLifecycleCallbackInjector
import me.bytebeats.applifecycle.agp.util.ClassScanner
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

/**
 * Created by bytebeats on 2021/6/23 : 21:19
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class ApplicationLifecycleTransform extends Transform {
    private static final String TAG = "applifecycle-agp-transform"
    private Project project

    ApplicationLifecycleTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return TAG
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        Set<QualifiedContent.ContentType> types = new HashSet<>()
        types.add(QualifiedContent.DefaultContentType.CLASSES)
        return types
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        Set<? super QualifiedContent.Scope> types = new HashSet<>()
        types.add(QualifiedContent.Scope.PROJECT)
        types.add(QualifiedContent.Scope.SUB_PROJECTS)
        types.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        return types;
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        println("<<<<<------${TAG} transforming started------>>>>>")

        def appLifecycleProxyClasses = []

        println("Scanning inputs")
        transformInvocation.inputs.each { input ->

            input.directoryInputs.each { directoryInput ->
                if (directoryInput.file.isDirectory()) {
                    directoryInput.file.eachFileRecurse { file ->
                        if (ClassScanner.isTargetProxyClass(file)) {
                            println("target directory input: ${file.path}")
                            appLifecycleProxyClasses.add("${file.path}")
                        }
                    }
                }
                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.each { jarInput ->
                def jarName = jarInput.name
                if (jarName.endsWith(".jar")) {//remove .jar
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def absolutePath = jarInput.file.absolutePath
                def md5 = DigestUtils.md2Hex(absolutePath)
                def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                if (absolutePath.endsWith(".jar")) {
                    def src = jarInput.file
                    if (ClassScanner.shouldProcessPreDexJar(absolutePath)) {
                        def proxyClassFiles = ClassScanner.scanProxyClassesFromJar(src, dest)
                        if (proxyClassFiles != null && !proxyClassFiles.isEmpty()) {
                            appLifecycleProxyClasses.addAll(proxyClassFiles)
                        }
                    }
                }
                FileUtils.copyFile(jarInput.file, dest)
            }
        }

        if (appLifecycleProxyClasses.isEmpty()) {
            println("ApplicationLifecycleCallbacks is empty")
        } else {
            ApplicationLifecycleCallbackInjector.getInstance().inject(appLifecycleProxyClasses)
        }
        println("<<<<<------${TAG} transforming finished------>>>>>");
    }
}