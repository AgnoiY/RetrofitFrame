package com.cjy.retrofitlibrary.annotation.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求数据中标记
 * <p>
 * Data：2019/07/23
 *
 * @author yong
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Code {
    int[] value() default {200, 401};//200: 请求成功, 401: 登录Token过期
}
