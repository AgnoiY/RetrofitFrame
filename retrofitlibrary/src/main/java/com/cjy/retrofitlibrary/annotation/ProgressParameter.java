package com.cjy.retrofitlibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加载过程接口
 * <p>
 * Data：2019/07/25
 *
 * @author yong
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Inherited
public @interface ProgressParameter {
}
