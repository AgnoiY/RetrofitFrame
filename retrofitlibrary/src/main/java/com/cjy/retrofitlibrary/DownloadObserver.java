package com.cjy.retrofitlibrary;


import android.os.Handler;

import com.cjy.retrofitlibrary.download.ComputeUtils;
import com.cjy.retrofitlibrary.download.DownloadCallback;
import com.cjy.retrofitlibrary.download.DownloadProgressCallback;
import com.cjy.retrofitlibrary.model.DownloadModel;

import java.lang.ref.SoftReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 下载观察者(监听)
 * 备注:在此处监听: 开始下载 、下载错误 、下载完成  等状态
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
class DownloadObserver<T extends DownloadModel> implements DownloadProgressCallback, Observer<T> {

    private DownloadModel download;
    private Handler handler;
    private Disposable disposable;
    private SoftReference<DownloadCallback> downloadCallback;

    public void setDownload(DownloadModel download) {
        this.download = download;
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    public DownloadObserver(DownloadModel download, Handler handler) {
        this.download = download;
        this.handler = handler;
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    /**
     * 开始下载/继续下载
     * 备注：继续下载需要获取之前下载的数据
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        setDownloadProgress(DownloadModel.State.WAITING, null, null);
    }

    /**
     * 下载出错
     * 备注：回调进度，回调onError
     */
    @Override
    public void onError(Throwable e) {
        setDownloadProgress(DownloadModel.State.ERROR, null, e);
    }

    /**
     * 下载完成
     * 备注：将开发者传入的Download子类回传
     */
    @Override
    public void onNext(T t) {
        setDownloadProgress(DownloadModel.State.FINISH, t, null);
    }

    @Override
    public void onComplete() {
        //
    }


    /**
     * 进度回调
     *
     * @param currentSize 当前值
     * @param totalSize   总大小
     */
    @Override
    public void progress(long currentSize, long totalSize) {
        if (download.getTotalSize() > totalSize) {
            currentSize = download.getTotalSize() - totalSize + currentSize;
        } else {
            download.setTotalSize(totalSize);
        }
        download.setCurrentSize(currentSize);
        handler.post(() -> {
            /*下载进度==总进度修改为完成状态*/
            if ((download.getCurrentSize() == download.getTotalSize()) && (download.getTotalSize() != 0)) {
                download.setState(DownloadModel.State.FINISH);
            }
            /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
            if (download.getState() != DownloadModel.State.PAUSE && downloadCallback.get() != null) {
                downloadCallback.get().onProgress(setProgress());
            }
        });
    }

    /**
     * 取消请求
     * 备注：暂停下载时调用
     */
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 更新数据库--回调
     *
     * @param state
     * @param t
     * @param e
     */
    private void setDownloadProgress(DownloadModel.State state, T t, Throwable e) {
        download.setState(state);
        setProgress();
        if (state == DownloadModel.State.ERROR || state == DownloadModel.State.FINISH) {
            RetrofitDownload.get().removeDownload(download, false);//移除下载
        }
        SQLiteHelper.get().insertOrUpdate(download);//更新数据库
        if (downloadCallback.get() != null) {//回调

            if (state == DownloadModel.State.FINISH) {
                downloadCallback.get().onSuccess(t);
                return;
            }

            downloadCallback.get().onProgress(download);

            if (state == DownloadModel.State.ERROR) {
                downloadCallback.get().onError(e);
            }
        }
    }

    /**
     * 设置下载进度
     */
    private DownloadModel setProgress() {
        float progress = ComputeUtils.getProgress(download.getCurrentSize(), download.getTotalSize());
        download.setProgress(progress);
        return download;
    }

}