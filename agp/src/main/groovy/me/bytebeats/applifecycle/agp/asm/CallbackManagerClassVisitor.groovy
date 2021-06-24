package me.bytebeats.applifecycle.agp.asm

import jdk.internal.org.objectweb.asm.ClassVisitor
import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes
import me.bytebeats.applifecycle.agp.util.Configs

import java.lang.reflect.Method

/**
 * Created by bytebeats on 2021/6/24 : 14:43
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class CallbackManagerClassVisitor extends ClassVisitor {
    private List<String> proxyClassFiles;

    CallbackManagerClassVisitor(ClassVisitor cv, List<String> classFiles) {
        super(Opcodes.ASM6, cv)
        this.proxyClassFiles = classFiles
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        println("visitMethod: $name")
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        if (Configs.INJECT_MANAGER_METHOD_NAME == name) {
            methodVisitor = new CallbackMethodAdapter(methodVisitor, access, name, desc, proxyClassFiles)
        }
        return methodVisitor
    }
}
