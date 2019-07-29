package com.cjy.retrofitlibrary;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cjy.retrofitlibrary.annotation.toast.ToastFail;
import com.cjy.retrofitlibrary.annotation.toast.ToastInfo;
import com.cjy.retrofitlibrary.model.BaseModel;
import com.cjy.retrofitlibrary.utils.AnnotationUtils;
import com.cjy.retrofitlibrary.utils.LogUtils;

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
    }

    @Override
    public T onConvert(T tData) {
        /**
         * 接口响应数据格式如@Response
         * 根据业务封装:
         * 1. response.isSuccess() (code==200) 业务逻辑成功回调convert()=>onSuccess()，否则失败回调onError()
         * 2.统一处理接口逻辑 例如:code==401 token过期等等
         */
        return convertModel(tData);
    }

    /**
     * 业务逻辑
     *
     * @return T
     */
    private T convertModel(T tData) {
        T t = null;
        BaseModel mBaseModel = AnnotationUtils.getResponseModel(tData);
        int code = mBaseModel.getCode();

        if (code == mBaseModel.getCodeSuccess() && mBaseModel.isSuccess()) { //成功
            t = (T) mBaseModel.getData();
            if (t == null || t instanceof String || t instanceof List) {
                t = tData;
            }
        } else if (code == mBaseModel.getCodeToken()) { //token过期，跳转登录页面重新登录
            isLoginToken(mBaseModel.getLoginClass(), mBaseModel.getLoginTip());
        } else { //统一为错误处理
            onError(getTag(), code, mBaseModel.getMsg());
        }
        return t;
    }

    /**
     * 网络请求的错误信息
     * 如果有特殊处理需重写
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param desc   错误信息
     */
    @Override
    public void onError(String action, int code, String desc) {
        if (isToast)
            AnnotationUtils.getToastMethod(ToastFail.class, RetrofitLibrary.getApplication(), desc);
    }

    /**
     * token过期，跳转登录页面重新登录
     */
    protected void isLoginToken(Class loginClass, String loginTip) {
        Context mContext = RetrofitLibrary.getApplication();
        RetrofitHttp.Configure mConfigure = RetrofitHttp.Configure.get();
        if (loginClass == null) {
            loginClass = mConfigure.getLoginClass();
        }
        if (loginClass == null) {
            LogUtils.e(RetrofitLibrary.getAppString(R.string.login_else_where_Activity));
            return;
        }
        if (TextUtils.isEmpty(loginTip)) {
            loginTip = mConfigure.getLoginTip();
        }
        if (!TextUtils.isEmpty(loginTip)) {
            AnnotationUtils.getToastMethod(ToastInfo.class, mContext, loginTip);
        }

        Intent intent = new Intent(mContext, loginClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

}
