package com.cjy.retrofitlibrary.annotation.download;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置字段的约束
 * <p>
 * Data：2019/07/23
 *
 * @author yong
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface PrimaryKey {
    String value();
}
