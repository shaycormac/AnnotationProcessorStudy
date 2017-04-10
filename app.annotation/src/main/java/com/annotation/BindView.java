package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Shay-Patrick-Cormac
 * @datetime 2017-04-10 10:33 GMT+8
 * @email  574583006@qq.com
 * @content 
 *  app.annotation module is Java library
 *  app.annotation：
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindView
{
    int value();
}
