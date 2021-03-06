package com.cjy.retrofitlibrary;

/**
 * 自定义服务器错误
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
class ServerException extends RuntimeException {
    private final int code;
    private final String msg;

    public ServerException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
