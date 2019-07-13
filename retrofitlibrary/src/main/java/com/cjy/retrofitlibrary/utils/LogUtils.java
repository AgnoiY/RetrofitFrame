package com.cjy.retrofitlibrary.utils;

import android.util.Log;

import com.cjy.retrofitlibrary.RetrofitHttp;


/**
 * log工具类
 * Data：2018/12/18
 *
 * @author yong
 */
public class LogUtils {

    private static final String TAG = "log_yong";
    private static boolean isShowLog = RetrofitHttp.Configure.get().isShowLog();

    LogUtils() {
        throw new IllegalStateException("LogUtils class");
    }

    /**
     * 输出debug调试信息
     *
     * @param msg
     */
    public static void d(String msg) {
        if (isShowLog)
            Log.d(TAG, msg);
    }

    public static void d(String tag, Object msg) {
        if (isShowLog)
            d(tag + ":  " + msg);
    }

    /**
     * 输出数据资源信息
     *
     * @param msg
     */
    public static void i(String msg) {
        if (isShowLog)
            Log.i(TAG, msg);
    }

    public static void i(String tag, Object msg) {
        if (isShowLog)
            i(tag + ":  " + msg);
    }

    /**
     * 输出错误信息
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isShowLog)
            Log.e(TAG, msg);
    }

    public static void e(String tag, Object msg) {
        if (isShowLog)
            e(tag + ":  " + msg);
    }

    /**
     * 输出警告信息
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isShowLog)
            Log.w(TAG, msg);
    }

    public static void w(Throwable tr) {
        if (isShowLog)
            Log.w(TAG, tr);
    }

    public static void w(String msg, Throwable tr) {
        if (isShowLog)
            Log.w(TAG, msg, tr);
    }

    public static void w(String tag, Object msg) {
        if (isShowLog)
            w(tag + ":  " + msg);
    }

    public static void w(String tag, Object msg, Throwable tr) {
        if (isShowLog)
            w(tag + ":  " + msg, tr);
    }
}