package com.cjy.retrofitlibrary;

import android.app.Application;
import android.support.annotation.StringRes;

import com.cjy.retrofitlibrary.utils.LogUtils;

/**
 * <应用初始化> <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class RetrofitLibrary {

    RetrofitLibrary() {
        throw new IllegalStateException("RetrofitLibrary class");
    }

    private static Application mApplication;
    private static RetrofitHttp.Builder mHttpBuilder;

    /**
     * 初始化
     *
     * @param application
     */
    public static RetrofitHttp.Builder init(Application application, String baseUrl) {
        try {
            if (application != null) {
                mApplication = application;
                getHttpConfigure().setBaseUrl(baseUrl).init(application);
                mHttpBuilder = new RetrofitHttp.Builder().getInstance();
            }
        } catch (NullPointerException e) {
            LogUtils.w(e);
        }

        return mHttpBuilder;
    }

    /**
     * 退出应用，清理内存
     */
    public static void onDestory() {
        mHttpBuilder.clear();
        RequestManagerImpl.getInstance().cancelAll();
        getHttpConfigure().getHandler().removeCallbacksAndMessages(null);
    }

    /**
     * 获取初始化的Retrofit
     *
     * @return
     */
    public static RetrofitHttp.Builder getRetrofitHttp() {
        if (mHttpBuilder == null)
            mHttpBuilder = new RetrofitHttp.Builder().getInstance();
        mHttpBuilder.clear();
        return mHttpBuilder;
    }

    /**
     * 获取网络请求基础配置类，设置基础配置
     */
    public static RetrofitHttp.Configure getHttpConfigure() {
        return RetrofitHttp.Configure.get();
    }

    public static String getAppString(@StringRes int resId) {
        return mApplication.getString(resId);
    }

    public static Application getApplication() {
        return mApplication;
    }
}
