package jp.ishikotaknife;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class IshikotaKnifeProcessor extends AbstractProcessor{

    private static final String ISHIKOTA_KNIFE_SUFFIX = "$$IshikotaViewBinder";

    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(IshikotaBind.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // for all class which has IshikotaBind annotation
        for(Element e : roundEnv.getElementsAnnotatedWith(IshikotaBind.class)) {
            String packageName = processingEnv.getElementUtils().getPackageOf(e).toString();
            String hostActivityName = e.getEnclosingElement().getSimpleName().toString();
            TypeElement te = findEnclosingTypeElement(e);
            TypeSpec.Builder result =
                    TypeSpec.classBuilder(te.getSimpleName()+ISHIKOTA_KNIFE_SUFFIX)
                    .addModifiers(Modifier.PUBLIC);
            MethodSpec.Builder method = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(packageName, hostActivityName), "target");

            // for all variable in annotated file
            for(VariableElement ve : ElementFilter.fieldsIn(te.getEnclosedElements())) {
                for(AnnotationMirror mirror : ve.getAnnotationMirrors()) {
                    // filter to pass only annotated variable
                    if(mirror.getAnnotationType().asElement().getSimpleName().toString().equals(IshikotaBind.class.getSimpleName())) {
                        String annotated_val_class = ve.asType().toString();
                        String annotated_val_name  = ve.toString();
                        for(ExecutableElement key : mirror.getElementValues().keySet()) {
                            AnnotationValue value = mirror.getElementValues().get(key);
                            ClassName hostActivityClass = ClassName.bestGuess(hostActivityName);
                            ClassName variableClass     = ClassName.bestGuess(annotated_val_class);
                            method.addStatement("(($T)target).$L = ($T)target.findViewById($L)",
                                    hostActivityClass, annotated_val_name, variableClass, value);
                        }
                    }
                }
            }
            // add bind method which includes statement to bind all annotated variables
            result.addMethod(method.build());

            JavaFile javaFile = JavaFile.builder(packageName, result.build()).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException error) {
                System.err.println(error.getMessage());
            }
        }
        return false;
    }

    public static TypeElement findEnclosingTypeElement( Element e ) {
        while( e != null && !(e instanceof TypeElement) ) {
            e = e.getEnclosingElement();
        }
        return TypeElement.class.cast( e );
    }
}
