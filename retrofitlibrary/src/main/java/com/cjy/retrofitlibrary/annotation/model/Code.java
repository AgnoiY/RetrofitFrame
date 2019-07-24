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
    /**
     * 200: 请求成功, 401: 登录Token过期
     *
     * @return
     */
    int[] value() default {200, 401};

    /**
     * Token过期, 重新登录Activity
     *
     * @return
     */
    Class login() default Object.class;

    /**
     * Token过期, 重新登录Activity提示语
     *
     * @return
     */
    String loginTip() default "";
}
