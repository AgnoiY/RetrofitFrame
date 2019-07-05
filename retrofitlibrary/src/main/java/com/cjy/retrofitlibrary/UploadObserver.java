package com.cjy.retrofitlibrary;

import android.content.Context;

/**
 * 上传回调接口
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class UploadObserver<T> extends HttpObserver<T> implements UploadProgressCallback {

    public UploadObserver() {
    }

    /**
     * @param context
     * @param isDialog 是否显示加载进度对话框
     */
    public UploadObserver(Context context, boolean isDialog) {
        super(context, isDialog, true);
    }
}
