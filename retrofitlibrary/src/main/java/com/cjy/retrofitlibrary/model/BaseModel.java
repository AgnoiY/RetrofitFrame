package com.cjy.retrofitlibrary.model;

import com.cjy.retrofitlibrary.annotation.model.Code;
import com.cjy.retrofitlibrary.annotation.model.Data;
import com.cjy.retrofitlibrary.annotation.model.Message;
import com.cjy.retrofitlibrary.annotation.model.Success;

/**
 * <自定义下载实体类基类>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class BaseModel<T> {

    /**
     * 数据对象/成功返回对象
     * 自定义字段：添加注解@Data
     */
    @Data
    private T data;

    /**
     * 状态码
     * 自定义字段：添加注解@Code
     * 状态码返回值(自定义值)value:　默认－200: 请求成功, 401: 登录Token过期
     * 登录Token过期,login:　重新登录Activity
     * loginTip:　重新登录提示语
     */
    @Code
    private int code;

    /**
     * 描述信息
     * 自定义字段：添加注解@Message
     */
    @Message
    private String msg;

    /**
     * 请求成功状态
     * 自定义字段：添加注解@Success
     */
    @Success
    private boolean isSuccess = true;

    public T getData() {
        return data;
    }

    public BaseModel<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public BaseModel<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseModel<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public BaseModel<T> setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }
}
