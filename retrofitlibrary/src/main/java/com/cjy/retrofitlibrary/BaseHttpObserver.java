package com.cjy.retrofitlibrary;

import android.content.Context;

import com.cjy.retrofitlibrary.utils.LogUtils;
import com.cjy.retrofitlibrary.utils.ThreadUtils;
import com.google.gson.Gson;

import org.json.JSONException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Http请求回调
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseHttpObserver<T> extends BaseObserver<T> implements CallBack<T> {

    /**
     * 加载失败提示弹出窗: 默认弹窗 － true: 加载
     */
    protected boolean isToast;

    /**
     * 下载路径
     */
    protected String downloadPath;

    public BaseHttpObserver() {
    }

    /**
     * @param context
     * @param isDialog    是否显示加载进度对话框
     * @param isCabcelble 当返回键按下是否关闭加载进度对话框
     */
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
        initSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        initError(e);
    }

    @Override
    public void onCanceled() {
        initCancel();
    }

    /**
     * 请求成功
     *
     * @param value
     */
    private void initSuccess(T value) {
        T result = parse((String) value);
        if (callSuccess && result != null) {
            onSuccess(getTag(), result);
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
     * @param e
     */
    private void initError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            onError(getTag(), exception.getCode(), exception.getMsg());
        } else {
            onError(getTag(), ExceptionEngine.UN_KNOWN_ERROR, RetrofitLibrary.getAppString(R.string.un_known_error));
        }
    }

    /**
     * 请求被取消
     */
    private void initCancel() {
        if (!ThreadUtils.isMainThread()) {
            RetrofitHttp.Configure.get().getHandler().post(this::onCancel);
        } else {
            onCancel();
        }
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
            LogUtils.i("当前类泛型:" + type);
        }
        return type;
    }

    /**
     * 加载失败提示弹出窗
     *
     * @param isToast 默认弹窗 － true: 加载
     * @return
     */
    protected BaseHttpObserver<T> setToast(boolean isToast) {
        this.isToast = isToast;
        return this;
    }

    /**
     * 设置下载路径
     *
     * @param downloadPath 下载路径
     * @return
     */
    protected BaseHttpObserver<T> setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
        return this;
    }
}
