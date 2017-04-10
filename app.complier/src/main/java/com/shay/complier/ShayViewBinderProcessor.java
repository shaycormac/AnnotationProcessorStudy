package com.shay.complier;

import com.annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author 作者：Shay-Patrick-Cormac
 * @datetime 创建时间：2017-04-10 11:06 GMT+8
 * @email 邮箱： 574583006@qq.com
 * @content 说明：app.complier根据注解在编译期间自动生成java代码
 * 优先需要自定义一个AbstractProcessor，然后Annotation生成代码，完整的AbstractProcessor
 * 
 * 定义好AnnotatedClass和BinderViewField之后，就下来实现一下根据注解生成代码过程
 * 
 * 原理是现解析保存被注解的类，然后再根据注解解析被注解的成员变量，进行保存，最后根据生成java类进行写文件
 */
@AutoService(Processor.class)
public class ShayViewBinderProcessor extends AbstractProcessor 
{
    //文件相关的辅助类
    private Filer filer;
    //元素相关的辅助类
    private Elements elementUtils;
    //日志相关的辅助类
    private Messager messager;

    private Map<String, AnnotatedClass> annotatedClassMap;
    /**
     * init(ProcessingEnvironment env): 每一个注解处理器类都必须有一个空的构造函数。然而，
     * 这里有一个特殊的init()方法，它会被注解处理工具调用，并输入ProcessingEnviroment参数。
     * ProcessingEnviroment提供很多有用的工具类Elements,Types和Filer。
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        annotatedClassMap = new TreeMap<>();
    }

    /**
     * 这相当于每个处理器的主函数main()。 在这里写扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnviroment，可以让查询出包含特定注解的被注解元素。
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //清空集合
        annotatedClassMap.clear();
        try{
            processBindView(roundEnvironment);
        }catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            errorInfo(e.getMessage());
        }

        for (AnnotatedClass annotatedClass : annotatedClassMap.values()) {
            try {
                annotatedClass.generateFile().writeTo(filer);
            } catch (IOException e) {
                errorInfo("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    private void errorInfo(String message,Object ... args) 
    {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }

    private void processBindView(RoundEnvironment roundEnv) throws IllegalArgumentException
    {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BinderViewField binderViewField = new BinderViewField(element);
            annotatedClass.addField(binderViewField);
        }
    }

    private AnnotatedClass getAnnotatedClass(Element element) 
    {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = annotatedClassMap.get(fullName);
        if (null==annotatedClass)
        {
            annotatedClass = new AnnotatedClass(typeElement, elementUtils);
            annotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    /**
     * 这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。
     * 换句话说，在这里定义你的注解处理器注册到哪些注解上。 
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    /**
     * 用来指定你使用的Java版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
