package com.cjy.retrofitlibrary.annotation.toast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 提示成功
 * <p>
 * Data：2019/07/25
 *
 * @author yong
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface ToastSuccess {
}
