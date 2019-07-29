package com.cjy.retrofitlibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 提示上下午
 * <p>
 * Data：2019/07/26
 *
 * @author yong
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
@Inherited
public @interface Constructors {
}
