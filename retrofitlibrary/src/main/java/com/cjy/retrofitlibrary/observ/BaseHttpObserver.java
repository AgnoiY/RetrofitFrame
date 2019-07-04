package com.cjy.retrofitlibrary.observ;

import android.content.Context;

import com.cjy.retrofitlibrary.R;
import com.cjy.retrofitlibrary.application.RetrofitLibrary;
import com.cjy.retrofitlibrary.exception.ApiException;
import com.cjy.retrofitlibrary.exception.ExceptionEngine;
import com.cjy.retrofitlibrary.interfaces.CallBack;
import com.cjy.retrofitlibrary.utils.LogUtils;
import com.cjy.retrofitlibrary.utils.ThreadUtils;
import com.google.gson.Gson;

import org.json.JSONException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.annotations.NonNull;

/**
 * Http请求回调
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseHttpObserver<T> extends BaseObserver<T> implements CallBack<T> {

    public BaseHttpObserver() {
    }

    public BaseHttpObserver(Context context, boolean isDialog, boolean isCabcelble) {
        super(context, isDialog, isCabcelble);
    }

    /**
     * 是否回调成功函数
     */
    private boolean callSuccess = true;

    @Override
    public void onNext(@NonNull T value) {
        super.onNext(value);
        inSuccess(getTag(), value);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            inError(getTag(), exception.getCode(), exception.getMsg());
        } else {
            inError(getTag(), ExceptionEngine.UN_KNOWN_ERROR, RetrofitLibrary.getAppString(R.string.un_known_error));
        }
    }

    @Override
    public void onCanceled() {
        onCanceledLogic();
    }

    /**
     * 请求成功
     *
     * @param action
     * @param value
     */
    private void inSuccess(String action, T value) {
        T result = parse((String) value);
        if (callSuccess && result != null) {
            onSuccess(action, result);
        }
    }

    /**
     * 解析数据
     *
     * @param data
     * @return
     */
    private T parse(String data) {
        T t = null;
        try {
            t = onConvert(new Gson().fromJson(data, getTypeClass()));
            callSuccess = true;
        } catch (JSONException e) {
            callSuccess = false;
            onError(getTag(), ExceptionEngine.ANALYTIC_CLIENT_DATA_ERROR, RetrofitLibrary.getAppString(R.string.data_parsing_error));
        }
        return t;
    }

    /**
     * 请求出错
     *
     * @param action
     * @param code
     * @param desc
     */
    private void inError(String action, int code, String desc) {
        onError(action, code, desc);
    }

    /**
     * Http被取消回调处理逻辑
     */
    private void onCanceledLogic() {
        if (!ThreadUtils.isMainThread()) {
            RetrofitHttp.Configure.get().getHandler().post(this::inCancel);
        } else {
            inCancel();
        }
    }

    /**
     * 请求取消
     */
    private void inCancel() {
        onCancel();
    }

    /**
     * 取消回调
     * 如果有特殊处理需重写
     */
    public void onCancel() {
        LogUtils.d(RetrofitLibrary.getAppString(R.string.request_cancelled));
    }

    /**
     * 获取当前类泛型
     */
    private Type getTypeClass() {
        Type type = null;
        ParameterizedType ptClass = (ParameterizedType) getClass().getGenericSuperclass();
        if (ptClass != null) {
            type = ptClass.getActualTypeArguments()[0];
            LogUtils.d("当前类泛型:" + type);
        }
        return type;
    }

}
