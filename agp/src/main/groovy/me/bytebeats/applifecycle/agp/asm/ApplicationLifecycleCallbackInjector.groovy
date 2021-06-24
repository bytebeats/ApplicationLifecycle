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

    void inject(List<String> classFiles) {
        println("ASM started")
        File callbackManagerFile = ClassScanner.getAppLifecycleCallbackManagerFile()
        File optJarFile = new File(callbackManagerFile.parent, "${callbackManagerFile.name}.opt")
        if (optJarFile.exists()) optJarFile.delete()
        JarFile file = new JarFile(callbackManagerFile)
        Enumeration<JarEntry> entryEnumeration = file.entries()
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(optJarFile))
        while (entryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = entryEnumeration.nextElement()
            String jarEntryName = jarEntry.name
            ZipEntry zipEntry = new ZipEntry(jarEntryName)
            InputStream is = file.getInputStream(jarEntry)
            jos.putNextEntry(zipEntry)

            if (jarEntryName == Configs.APP_LIFE_CYCLE_MANAGER_FILE_NAME) {
                println("start adding ApplicationLifecycleCallback into $jarEntryName")
                ClassReader reader = new ClassReader(is)
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)
                ClassVisitor visitor = new CallbackManagerClassVisitor(writer, classFiles)
                reader.accept(visitor, ClassReader.EXPAND_FRAMES)
                byte[] bytes = writer.toByteArray()
                jos.write(bytes)
            } else {
                jos.write(IOUtils.toByteArray(is))
            }
            is.close()
            jos.closeEntry()
        }
        jos.close()
        file.close()
        if (callbackManagerFile.exists()) callbackManagerFile.delete()
        optJarFile.renameTo(callbackManagerFile)
        println("ApplicationLifecycleManager located at ${callbackManagerFile.path}")
        println("ASM finished")
    }
}
