package com.shay.complier;

import com.annotation.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author 作者：Shay-Patrick-Cormac
 * @datetime 创建时间：2017-04-10 11:18 GMT+8
 * @email 邮箱： 574583006@qq.com
 * @content 说明：定义一个BindViewField对象用于被注解的成员变量。
 */
public class BinderViewField
{
    private VariableElement variableElement;
    private int resId;

    BinderViewField(Element element) throws IllegalArgumentException
    {
        if (element.getKind()!= ElementKind.FIELD)
        {
            throw new IllegalArgumentException(String.format("只有属性才能被注解 @%s", BindView.class.getSimpleName()));
        }
        variableElement = (VariableElement) element;
        BindView bindView = variableElement.getAnnotation(BindView.class);
        resId = bindView.value();
        if (resId<0)
            throw new IllegalArgumentException(String.format("value() in %s for field %s is not valid !", BindView.class.getSimpleName(),
                            variableElement.getSimpleName()));
            
    }
    /**
     * 获取变量名称
     *
     * @return
     */
    Name getFieldName()
    {
        return variableElement.getSimpleName();
    }
    /**
     * 获取变量id
     *
     * @return
     */
    int getResId() {
        return resId;
    }
    /**
     * 获取变量类型
     *
     * @return
     */
    TypeMirror getFieldType()
    {
        return variableElement.asType();
    }
}
