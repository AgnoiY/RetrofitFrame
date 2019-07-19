package com.cjy.rrtrofitframe;

import com.cjy.retrofitlibrary.BuildConfig;

/**
 * <网络请求url地址>
 * Data：2018/12/18
 *
 * @author yong
 */
public class UrlConstans {

    UrlConstans() {
        throw new IllegalStateException("UrlConstans class");
    }

    /**
     * 服务器地址
     */
    private static final String DEF_TEST_SERVER = "http://databox.worken.cn/data-box-app/";//测试环境
    private static final String DEF_RELEASE_SERVER = "http://databox.worken.cn/data-box-app/";//正式环境

    public static final String BASESERVER = BuildConfig.DEBUG ? DEF_TEST_SERVER : DEF_RELEASE_SERVER;

    /**
     * 用户登陆
     */
    public static final String LOGIN = "login";
    public static final String VERSION = "common/updateVersion";
}
