package com.cjy.retrofitlibrary;

import android.content.Context;

import com.cjy.retrofitlibrary.dialog.AutoDefineToast;
import com.cjy.retrofitlibrary.model.BaseModel;
import com.cjy.retrofitlibrary.utils.EntityGatherUtils;

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
        BaseModel mBaseModel = EntityGatherUtils.getResponseModel(tData);
        int code = mBaseModel.getCode();

        if (code == mBaseModel.getCodeSuccess() && mBaseModel.isSuccess()) { //成功
            t = (T) mBaseModel.getData();
            if (t == null || t instanceof String || t instanceof List) {
                t = tData;
            }
        } else if (code == mBaseModel.getCodeToken()) { //token过期，跳转登录页面重新登录
            isLoginToken();
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
            AutoDefineToast.showFailToast(mContext, desc);
    }

    /**
     * token过期，跳转登录页面重新登录
     */
    protected void isLoginToken() {
//        RetrofitLibrary.getApplication().startActivities();
    }

}
