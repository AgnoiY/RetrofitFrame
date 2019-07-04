package com.cjy.rrtrofitframe;

import android.app.Application;

import com.cjy.retrofitlibrary.application.RetrofitLibrary;

/**
 * <应用初始化> <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitLibrary.init(this, UrlConstans.BASESERVER);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RetrofitLibrary.onDestory();
    }
}
