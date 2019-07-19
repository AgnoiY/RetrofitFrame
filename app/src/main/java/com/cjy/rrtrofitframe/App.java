package com.cjy.rrtrofitframe;

import android.app.Application;

import com.cjy.retrofitlibrary.RetrofitLibrary;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("v", "1.6.0");
        headerMap.put("platform", "android");
        headerMap.put("profiles", "prd");
        RetrofitLibrary.getHttpConfigure()
                .setBaseHeader(headerMap)
                .setNotTipDialog(true);
        RetrofitLibrary.init(this, UrlConstans.BASESERVER);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RetrofitLibrary.onDestory();
    }
}
