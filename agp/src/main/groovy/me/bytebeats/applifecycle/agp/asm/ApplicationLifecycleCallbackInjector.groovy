package me.bytebeats.applifecycle.agp.asm

import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.ClassWriter
import me.bytebeats.applifecycle.agp.util.ClassScanner
import me.bytebeats.applifecycle.agp.util.Configs
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * Created by bytebeats on 2021/6/24 : 14:21
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */

/**
 * Inject every proxy class file.
 * Invoke {@link #addApplicationLifecycleCallback(String)} in {@link ApplicationLifecycleCallbackManager#init()} for every file.
 */
class ApplicationLifecycleCallbackInjector {
    private static volatile ApplicationLifecycleCallbackInjector instance;

    private ApplicationLifecycleCallbackInjector() {

    }

    synchronized static ApplicationLifecycleCallbackInjector getInstance() {
        if (instance == null) {
            synchronized (ApplicationLifecycleCallbackInjector.class) {
                if (instance == null) {
                    instance = new ApplicationLifecycleCallbackInjector()
                }
            }
        }
        return instance
    }

    void inject(List<String> proxyClassFiles) {
        println("ASM started")
        File callbackManagerFile = ClassScanner.getAppLifecycleCallbackManagerFile()
        //temp file for storing optimized application lifecycle callback manager file.
        File optJar = new File(callbackManagerFile.parent, "${callbackManagerFile.name}.opt")
        if (optJar.exists()) optJar.delete()
        JarFile file = new JarFile(callbackManagerFile)
        Enumeration<JarEntry> entryEnumeration = file.entries()
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(optJar))
        while (entryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = entryEnumeration.nextElement()
            String jarEntryName = jarEntry.name
            ZipEntry zipEntry = new ZipEntry(jarEntryName)
            InputStream is = file.getInputStream(jarEntry)
            jos.putNextEntry(zipEntry)

            //Find ApplicationLifecycleManager.class and execute inserting application lifecycle callback instances.
            if (jarEntryName == Configs.APP_LIFE_CYCLE_MANAGER_FILE_NAME) {
                println("find manager class: $jarEntryName")
                ClassReader reader = new ClassReader(is)
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)
                ClassVisitor visitor = new AppLifecycleCallbackManagerClassVisitor(writer, proxyClassFiles)
                reader.accept(visitor, ClassReader.EXPAND_FRAMES)

                /**
                 * write inserted class into temp file.
                 */
                byte[] bytes = writer.toByteArray()
                jos.write(bytes)
                println("inject finished")
            } else {
                //class not to be insert, keep same
                jos.write(IOUtils.toByteArray(is))
            }
            is.close()
            jos.closeEntry()
        }
        jos.close()
        file.close()
        if (callbackManagerFile.exists()) callbackManagerFile.delete()
        optJar.renameTo(callbackManagerFile)
        println("ASM finished")
    }
}
