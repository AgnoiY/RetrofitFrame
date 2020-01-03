package com.cjy.retrofitlibrary;

import java.util.List;

/**
 * <下载实体类基类>
 * <p>
 * Data：2019/07/23
 *
 * @author yong
 */
class BaseResponseModel<T> {

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
    /**
     * Token过期, 重新登录Activity
     */
    private Class loginClass;
    /**
     * Token过期, 重新登录Activity, 提示语
     */
    private String loginTip;

    public int getCode() {
        return code;
    }

    public BaseResponseModel<T> setCode(int code) {
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

    public BaseResponseModel<T> setCodes(int[] codes) {
        this.codes = codes;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResponseModel<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        if (data instanceof List && ((List<T>) data).isEmpty()) {
            return null;
        }
        return data;
    }

    public BaseResponseModel<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public BaseResponseModel<T> setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public Class getLoginClass() {
        return loginClass;
    }

    public BaseResponseModel<T> setLoginClass(Class loginClass) {
        if (loginClass == Object.class)
            loginClass = null;
        this.loginClass = loginClass;
        return this;
    }

    public String getLoginTip() {
        return loginTip;
    }

    public BaseResponseModel<T> setLoginTip(String loginTip) {
        this.loginTip = loginTip;
        return this;
    }
}
