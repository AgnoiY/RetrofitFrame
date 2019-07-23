package com.cjy.retrofitlibrary.model;

import com.cjy.retrofitlibrary.annotation.model.Code;
import com.cjy.retrofitlibrary.annotation.model.Data;
import com.cjy.retrofitlibrary.annotation.model.Message;

/**
 * <自定义下载实体类基类>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class BaseResponseModel<T> {

    /**
     * 数据对象/成功返回对象
     */
    @Data()
    private T data;
    /**
     * 状态码
     */
    @Code
    private int code;
    /**
     * 描述信息
     */
    @Message
    private String msg;

    public T getData() {
        return data;
    }

    public BaseResponseModel<T> setData(T data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public BaseResponseModel<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponseModel<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
