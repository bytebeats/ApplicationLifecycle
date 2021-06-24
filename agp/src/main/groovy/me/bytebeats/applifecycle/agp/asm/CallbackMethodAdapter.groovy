package me.bytebeats.applifecycle.agp.asm

import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter
import me.bytebeats.applifecycle.agp.util.Configs

/**
 * Created by bytebeats on 2021/6/24 : 14:34
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class CallbackMethodAdapter extends AdviceAdapter {
    private List<String> proxyClassFiles

    CallbackMethodAdapter(MethodVisitor mv, int access, String name, String desc, List<String> files) {
        super(ASM6, mv, access, name, desc)
        this.proxyClassFiles = files
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()
        println "-------onMethodEnter------"
        if (proxyClassFiles != null && !proxyClassFiles.isEmpty()) {
            proxyClassFiles.forEach(proxyClassFile -> {
                println("proxy class: ${proxyClassFile}")
                mv.visitLdcInsn(proxyClassFile.substring(0, proxyClassFile.length() - 6))//remove `.class`
                mv.visitMethodInsn(INVOKESTATIC, Configs.APP_LIFE_CYCLE_MANAGER_CLASS_NAME, Configs.INJECT_ENTRY_METHOD_NAME, Configs.INJECT_ENTRY_METHOD_PARAM, false)
            })
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode)
        println "-------onMethodExit------"
    }
}
