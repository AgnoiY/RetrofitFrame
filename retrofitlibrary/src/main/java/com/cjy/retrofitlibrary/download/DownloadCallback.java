package com.cjy.retrofitlibrary.download;

import com.cjy.retrofitlibrary.model.DownloadModel;

/**
 * 下载回调
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
public interface   DownloadCallback<T extends DownloadModel> {

    /**
     * 进度回调
     *
     * @param model       下载状态
     */
    public abstract void onProgress(T model);

    /**
     * 下载出错
     *
     * @param e
     */
    public abstract void onError(Throwable e);

    /**
     * 下载成功
     *
     * @param model
     */
    public abstract void onSuccess(T model);
}
