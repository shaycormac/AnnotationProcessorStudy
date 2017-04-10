package com.shay.complier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author 作者：Shay-Patrick-Cormac
 * @datetime 创建时间：2017-04-10 11:11 GMT+8
 * @email 邮箱： 574583006@qq.com
 * @content 说明：定义一个被注解类对象AnnotedClass,用于保存哪些被注解的对象。
 */
public class AnnotatedClass 
{
    private static class TypeUtils
    {
        static final ClassName BINDER = ClassName.get("com.shay.appapi", "ViewBinder");
        static final ClassName PROVIDER = ClassName.get("com.shay.appapi", "ViewFinder");
        
    }

    private TypeElement typeElement;
    private ArrayList<BinderViewField> fields;
    private Elements elements;

     AnnotatedClass(TypeElement typeElement, Elements elements) {
        this.typeElement = typeElement;
        this.elements = elements;
        fields = new ArrayList<BinderViewField>();
    }
    
    void addField(BinderViewField field)
    {
        fields.add(field);
    }
    
    JavaFile generateFile()
    {
        //generateMethod
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(typeElement.asType()), "host")
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtils.PROVIDER, "finder");
        for (BinderViewField field : fields) {
            builder.addStatement("host.$N =($T)(finder.findView(source,$L))", field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
        }

        MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(typeElement.asType()), "host")
                .addAnnotation(Override.class);
        for (BinderViewField field : fields) {
            unBindViewMethod.addStatement("host.$N = null", field.getFieldName());
        }
        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(typeElement.getSimpleName() + "$$ViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtils.BINDER, TypeName.get(typeElement.asType())))
                .addMethod(builder.build())
                .addMethod(unBindViewMethod.build())
                .build();
        String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();
        return JavaFile.builder(packageName, injectClass).build();
    }
}
