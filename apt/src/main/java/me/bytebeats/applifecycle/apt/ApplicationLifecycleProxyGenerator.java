package me.bytebeats.applifecycle.apt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by bytebeats on 2021/6/23 : 14:08
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
public class ApplicationLifecycleProxyGenerator {
    private static final String METHOD_ON_CREATE = "onCreate";
    private static final String METHOD_ON_TERMINATE = "onTerminate";
    private static final String METHOD_ON_LOW_MEMORY = "onLowMemory";
    private static final String METHOD_ON_TRIM_MEMORY = "onTrimMemory";
    private static final String METHOD_GET_PRIORITY = "getPriority";
    private static final String FIELD_APPLICATION_LIFE_CYCLE_CALLBACK = "mApplicationLifecycleCallback";

    private Filer mFiler;

    private ApplicationLifecycleProxyGenerator(Filer filer) {
        mFiler = filer;
    }

    private static volatile ApplicationLifecycleProxyGenerator INSTANCE;

    public static synchronized void init(Filer filer) {
        if (INSTANCE == null) {
            synchronized (ApplicationLifecycleProxyGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApplicationLifecycleProxyGenerator(filer);
                }
            }
        }
    }

    public static synchronized ApplicationLifecycleProxyGenerator getInstance() {
        return INSTANCE;
    }

    public boolean generate(TypeElement typeElement, String pkg, TypeMirror context, TypeMirror callback) {
        TypeSpec proxy = generateProxyClass(typeElement, context, callback);
        JavaFile file = JavaFile.builder(pkg, proxy).build();
        try {
            file.writeTo(mFiler);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private TypeSpec generateProxyClass(TypeElement typeElement, TypeMirror context, TypeMirror callback) {
        return TypeSpec.classBuilder(generateProxyClassName(typeElement))
                .addSuperinterface(callback)
                .addModifiers(Modifier.PUBLIC)
                .addField(generateCallbackField(callback))
                .addMethod(generateConstructorMethod(typeElement))
                .addMethod(generateOnCreateMethod(context))
                .addMethod(generateOnTerminateMethod())
                .addMethod(generateOnLowMemoryMethod())
                .addMethod(generateOnTrimMemoryMethod())
                .addMethod(generateGetPriorityMethod())
                .build();
    }

    private String generateProxyClassName(TypeElement typeElement) {
        return String.format("%1s%2s$3s", Configs.PROXY_CLASS_PREFIX, typeElement.getSimpleName().toString(), Configs.PROXY_CLASS_SUFFIX);
    }

    private FieldSpec generateCallbackField(TypeMirror callback) {
        return FieldSpec.builder(TypeName.get(callback), FIELD_APPLICATION_LIFE_CYCLE_CALLBACK)
                .addModifiers(Modifier.PRIVATE)
                .addModifiers(Modifier.FINAL)
                .build();
    }

    private MethodSpec generateConstructorMethod(TypeElement typeElement) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N = new $T()", FIELD_APPLICATION_LIFE_CYCLE_CALLBACK, ClassName.get(typeElement))
                .build();
    }

    private MethodSpec generateOnCreateMethod(TypeMirror contextType) {
        return MethodSpec.methodBuilder(METHOD_ON_CREATE)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addParameter(TypeName.get(contextType), "context")
                .addStatement("this.$N.$N($N)", FIELD_APPLICATION_LIFE_CYCLE_CALLBACK, METHOD_ON_CREATE, "context")
                .build();
    }

    private MethodSpec generateOnTerminateMethod() {
        return MethodSpec.methodBuilder(METHOD_ON_TERMINATE)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addStatement("this.$N.$N()", FIELD_APPLICATION_LIFE_CYCLE_CALLBACK, METHOD_ON_TERMINATE)
                .build();
    }

    private MethodSpec generateOnLowMemoryMethod() {
        return MethodSpec.methodBuilder(METHOD_ON_LOW_MEMORY)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addStatement("this.$N.$N()", FIELD_APPLICATION_LIFE_CYCLE_CALLBACK, METHOD_ON_LOW_MEMORY)
                .build();
    }

    private MethodSpec generateOnTrimMemoryMethod() {
        return MethodSpec.methodBuilder(METHOD_ON_TRIM_MEMORY)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addParameter(int.class, "level")
                .addStatement("this.$N.$N($N)", FIELD_APPLICATION_LIFE_CYCLE_CALLBACK, METHOD_ON_TRIM_MEMORY, "level")
                .build();
    }

    private MethodSpec generateGetPriorityMethod() {
        return MethodSpec.methodBuilder(METHOD_GET_PRIORITY)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addStatement("return this.$N.$N()", FIELD_APPLICATION_LIFE_CYCLE_CALLBACK, METHOD_GET_PRIORITY)
                .build();
    }

}
