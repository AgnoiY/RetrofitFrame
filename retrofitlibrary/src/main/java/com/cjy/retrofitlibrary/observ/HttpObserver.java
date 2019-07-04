package com.cjy.retrofitlibrary.observ;

import android.content.Context;

import com.cjy.retrofitlibrary.dialog.UITipDialog;
import com.cjy.retrofitlibrary.model.BaseResponseListModel;
import com.cjy.retrofitlibrary.model.BaseResponseModel;

import java.util.List;


/**
 * 根据业务进一步封装
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 * <p>
 * 重要提醒 : abstract 不能给删掉
 */
public abstract class HttpObserver<T> extends BaseHttpObserver<T> {

    private Context mContext;
    private BaseResponseModel mResponse;
    private BaseResponseListModel mResponseList;

    public HttpObserver() {
    }

    public HttpObserver(Context context, boolean isDialog) {
        this(context, isDialog, false);
    }

    /**
     * @param context
     * @param isDialog    是否显示加载进度对话框
     * @param isCabcelble 当返回键按下是否关闭加载进度对话框
     */
    public HttpObserver(Context context, boolean isDialog, boolean isCabcelble) {
        super(context, isDialog, isCabcelble);
        this.mContext = context;
    }

    @Override
    public T onConvert(T tData) {
        /**
         * 接口响应数据格式如@Response
         * 根据业务封装:
         * 1. response.isSuccess() (code==0) 业务逻辑成功回调convert()=>onSuccess()，否则失败回调onError()
         * 2.统一处理接口逻辑 例如:code==101 token过期等等
         */
        if (tData instanceof BaseResponseModel) {
            mResponse = (BaseResponseModel) tData;
            return convertModel(true);
        } else if (tData instanceof BaseResponseListModel) {
            mResponseList = (BaseResponseListModel) tData;
            return convertModel(false);
        }
        return null;
    }

    /**
     * 业务逻辑
     *
     * @param isResponse
     * @return T
     */
    private T convertModel(boolean isResponse) {

        T t = null;
        List<T> tList = null;
        int code;
        String msg;

        if (isResponse) {
            code = mResponse.getCode();
            msg = mResponse.getMsg();
        } else {
            code = mResponseList.getCode();
            msg = mResponseList.getMsg();
        }

        switch (code) {
            case 0://成功
                if (isResponse) {
                    t = (T) mResponse.getData();
                    if (t == null || t instanceof String) {
                        t = (T) mResponse;
                    }
                } else {
                    tList = (List<T>) mResponseList.getData();
                }
                break;
            case 401://token过期，跳转登录页面重新登录
                isLoginToken();
                break;
            default://统一为错误处理
                onError(getTag(), code, msg);
                break;
        }
        return isResponse ? t : (T) tList;
    }

    /**
     * 网络请求的错误信息
     * 如果有特殊处理需重写
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param desc   错误信息
     */
    public void onError(String action, int code, String desc) {
        UITipDialog.showFall(mContext, desc);
    }

    /**
     * token过期，跳转登录页面重新登录
     */
    protected void isLoginToken() {

    }

}
