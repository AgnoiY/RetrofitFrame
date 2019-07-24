package com.cjy.retrofitlibrary.model;

import java.util.List;

/**
 * <下载实体类基类>
 * <p>
 * Data：2019/07/23
 *
 * @author yong
 */
public class BaseModel<T> {

    /**
     * 数据对象/成功返回对象
     */
    private T data;
    /**
     * 状态码
     */
    private int code;

    /**
     * 状态码
     */
    private int[] codes;
    /**
     * 描述信息
     */
    private String msg;
    /**
     * 请求状态
     */
    private boolean isSuccess = true;

    public int getCode() {
        return code;
    }

    public BaseModel<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public int[] getCodes() {
        return codes;
    }

    /**
     * 请求成功状态码
     *
     * @return
     */
    public int getCodeSuccess() {
        if (codes != null && codes.length > 0)
            return codes[0];
        return 200;
    }

    /**
     * 请求Token过期状态码
     *
     * @return
     */
    public int getCodeToken() {
        if (codes != null && codes.length > 1)
            return codes[1];
        return 401;
    }

    public BaseModel<T> setCodes(int[] codes) {
        this.codes = codes;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseModel<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        if (data instanceof List && ((List<T>) data).isEmpty()) {
            return null;
        }
        return data;
    }

    public BaseModel<T> setData(T data) {
        this.data = data;
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
