package com.cjy.retrofitlibrary.application;

import android.app.Application;
import android.support.annotation.StringRes;

import com.cjy.retrofitlibrary.cancel.RequestManagerImpl;
import com.cjy.retrofitlibrary.observ.RetrofitHttp;
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
                RetrofitHttp.Configure.get().baseUrl(baseUrl).init(application);
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
    }

    public static String getAppString(@StringRes int resId) {
        return mApplication.getString(resId);
    }

    public static Application getApplication() {
        return mApplication;
    }
}
