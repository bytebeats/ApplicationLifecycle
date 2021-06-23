package me.bytebeats.applifecycle.apt;

import com.google.auto.service.AutoService;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes(Configs.OPTION_ANNOTATION)
@SupportedOptions(value = {Configs.OPTION_PACKAGE, Configs.OPTION_VERBOSE})
public class ApplicationLifecycleProcessor extends AbstractProcessor {
    private static final String TAG = ApplicationLifecycleProcessor.class.getSimpleName();

    private boolean verbose = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ApplicationLifecycleProxyGenerator.init(processingEnv.getFiler());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            String pkg = processingEnv.getOptions().get(Configs.OPTION_PACKAGE);
            if (pkg == null || pkg.isEmpty()) {
                printMessage(Diagnostic.Kind.ERROR, "No option " + Configs.OPTION_PACKAGE +
                        " passed to annotation processor");
                return false;
            }
            if (roundEnvironment.processingOver()) {
                if (!set.isEmpty()) {
                    printMessage(Diagnostic.Kind.ERROR,
                            "Unexpected processing state: annotations still available after processing over");
                    return false;
                }
            }
            if (set.isEmpty()) {
                return false;
            }
            verbose = Boolean.parseBoolean(processingEnv.getOptions().get(Configs.OPTION_VERBOSE));

            for (TypeElement annotation : set) {
                Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
                TypeMirror context = processingEnv.getElementUtils().getTypeElement(Configs.CONTEXT).asType();
                for (Element element : elements) {
                    if (element.getKind().isClass()) {
                        TypeElement typeElement = (TypeElement) element;
                        String qualifiedName = typeElement.getQualifiedName().toString();
                        printMessage(Diagnostic.Kind.NOTE, "Checking class: " + qualifiedName);
                        //A Class who want to follow ApplicationLifecycle has to be annotated ApplicationLifecycle and implement ApplicationLifecycleCallback
                        List<? extends TypeMirror> mirrors = typeElement.getInterfaces();
                        TypeMirror callbackTypeMirror = null;
                        if (mirrors.isEmpty()) {
                            printMessage(Diagnostic.Kind.ERROR, qualifiedName + " has to implement interface " + Configs.APPLICATION_LIFE_CYCLE_CALLBACK_CANONICAL_NAME);
                            return false;
                        } else {
                            boolean hasAppLifecycleCallbackInterface = false;
                            for (TypeMirror mirror : mirrors) {
                                if (Configs.APPLICATION_LIFE_CYCLE_CALLBACK_CANONICAL_NAME.equals(mirror.toString())) {
                                    callbackTypeMirror = mirror;
                                    hasAppLifecycleCallbackInterface = true;
                                    break;
                                }
                            }
                            if (!hasAppLifecycleCallbackInterface) {
                                printMessage(Diagnostic.Kind.ERROR, qualifiedName + " has to implement interface " + Configs.APPLICATION_LIFE_CYCLE_CALLBACK_CANONICAL_NAME);
                                return false;
                            }
                        }
                        printMessage(Diagnostic.Kind.NOTE, "start generating proxy class for " + qualifiedName);
                        ApplicationLifecycleProxyGenerator.getInstance().generate(typeElement, pkg, context, callbackTypeMirror);
                    } else {
                        printMessage(Diagnostic.Kind.ERROR, "@ApplicationLifecycle is only valid for Class", element);
                        return false;
                    }
                }
            }
        } catch (RuntimeException e) {
            printMessage(Diagnostic.Kind.ERROR, "Unexpected error in " + TAG + ": " + e);
        }
        return true;
    }

    private void printMessage(Diagnostic.Kind kind, CharSequence message) {
        if (verbose) {
            processingEnv.getMessager().printMessage(kind, message);
        }
    }

    private void printMessage(Diagnostic.Kind kind, CharSequence message, Element element) {
        if (verbose) {
            processingEnv.getMessager().printMessage(kind, message, element);
        }
    }
}