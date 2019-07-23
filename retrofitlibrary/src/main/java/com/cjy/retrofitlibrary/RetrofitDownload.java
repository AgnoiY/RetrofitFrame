package com.cjy.retrofitlibrary;

import com.cjy.retrofitlibrary.model.DownloadModel;
import com.cjy.retrofitlibrary.utils.LogUtils;
import com.cjy.retrofitlibrary.utils.RequestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 下载管理类
 * 备注：单例模式 开始下载，暂停下载，暂停全部，移除下载，获取下载列表
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
public class RetrofitDownload {

    public static final String TAG = RetrofitDownload.class.getSimpleName();
    /*单例模式*/
    private static RetrofitDownload mInstance;
    /*下载集合*/
    private Set<DownloadModel> mDownloadSet;
    /*下载集合对应回调map*/
    private HashMap<String, DownloadObserver> mCallbackMap;

    private RetrofitDownload() {
        mDownloadSet = new HashSet<>();
        mCallbackMap = new HashMap<>();
    }

    public static RetrofitDownload get() {
        RetrofitDownload download = mInstance;
        if (download == null) {
            synchronized (RetrofitDownload.class) {
                download = mInstance;
                if (download == null) {
                    mInstance = download = new RetrofitDownload();
                }
            }
        }
        return download;
    }

    /**
     * 开始下载
     *
     * @param download
     */
    public void startDownload(final DownloadModel download) {

        if (download == null) return;

        /*正在下载不处理*/
        if (mCallbackMap.get(download.getServerUrl()) != null) {
            mCallbackMap.get(download.getServerUrl()).setDownload(download);
            return;
        }

        /*已完成下载*/
        if (download.getCurrentSize() == download.getTotalSize() && (download.getTotalSize() != 0)) {
            return;
        }

        /*判断本地文件是否存在*/
        boolean isFileExists = ComputeUtils.isFileExists(download.getLocalUrl());
        if (!isFileExists && download.getCurrentSize() > 0) {
            download.setCurrentSize(0);
        }

        DownloadObserver observer = new DownloadObserver(download, RetrofitHttp.Configure.get().getHandler());
        mCallbackMap.put(download.getServerUrl(), observer);
        Api api;
        if (mDownloadSet.contains(download)) {
            api = download.getApi();
        } else {

            //下载拦截器
            DownloadInterceptor downloadInterceptor = new DownloadInterceptor(observer);
            //OkHttpClient
            OkHttpClient okHttpClient = RetrofitUtils.get().getOkHttpClientDownload(downloadInterceptor);
            //Retrofit
            Retrofit retrofit = RetrofitUtils.get().getRetrofit(RequestUtils.getBasUrl(download.getServerUrl()), okHttpClient);
            api = retrofit.create(Api.class);
            download.setApi(api);
            mDownloadSet.add(download);

        }
        /* RANGE 断点续传下载 */
        //数据变换
        /* 被观察者 httpObservable */
        Observable apiObservable = api.download("bytes=" + download.getCurrentSize() + "-", download.getServerUrl());
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .baseObserver(observer)
                .downloadModel(download)
//                .lifecycleProvider(lifecycle)
//                .activityEvent(activityEvent)
//                .fragmentEvent(fragmentEvent)
                .build();

        /* 观察者  httpObserver */
        /*设置监听*/
        httpObservable.observe().subscribe(observer);

    }

    /**
     * 暂停/停止下载数据
     *
     * @param download
     */
    public void stopDownload(DownloadModel download) {

        if (download == null) return;

        /**
         * 1.暂停网络数据
         * 2.设置数据状态
         * 3.更新数据库
         */

        /*1.暂停网络数据*/
        if (mCallbackMap.containsKey(download.getServerUrl())) {
            DownloadObserver observer = mCallbackMap.get(download.getServerUrl());
            observer.onCanceled();//取消
            mCallbackMap.remove(download.getServerUrl());
        }

        /*2.设置数据状态*/
        download.setState(DownloadModel.State.PAUSE);//暂停状态
        download.setProgress(ComputeUtils.getProgress(download.getCurrentSize(), download.getTotalSize()));//计算进度
        download.getCallback().onProgress(download);//回调

        /*3.更新数据库*/
        SQLiteHelper.get().insertOrUpdate(download);

    }

    /**
     * 移除下载数据
     *
     * @param download
     * @param removeFile 是否移出本地文件
     */
    public void removeDownload(DownloadModel download, boolean removeFile) {

        if (download == null) return;

        //未完成下载时,暂停再移除
        if (download.getState() != DownloadModel.State.FINISH) {
            stopDownload(download);
        }
        //移除本地保存数据
        if (removeFile) {
            ComputeUtils.deleteFile(download.getLocalUrl());
        }

        mCallbackMap.remove(download.getServerUrl());
        mDownloadSet.remove(download);

        //移除数据
        SQLiteHelper.get().delete(download);
    }

    /**
     * 暂停/停止全部下载数据
     */
    public void stopAllDownload() {
        for (DownloadModel download : mDownloadSet) {
            stopDownload(download);
        }
        mCallbackMap.clear();
        mDownloadSet.clear();
        LogUtils.d(TAG, "stopAllDownload");
    }

    /**
     * 获取下载列表
     * tClass extends DownloadModel
     *
     * @return
     */
    public <T> List<T> getDownloadList(Class<T> tClass) {
        List<T> list = SQLiteHelper.get().query(tClass);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 获取下载数据
     * tClass extends DownloadModel
     *
     * @return
     */
    public <T extends DownloadModel> T getDownloadModel(DownloadModel model) {
        T t = SQLiteHelper.get().query(model);
        return t != null ? t : (T) model;
    }

    /**
     * 获取下载列表总量
     *
     * @return
     */
    public int getDownloadCount() {
        return SQLiteHelper.get().queryCount();
    }


}
