package com.cjy.retrofitlibrary;

import com.cjy.retrofitlibrary.download.ResponseUtils;
import com.cjy.retrofitlibrary.model.DownloadModel;
import com.cjy.retrofitlibrary.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 下载处理函数
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
class DownloadFunction<T> implements Function<ResponseBody, ObservableSource<T>> {

    private DownloadModel mDownloadModel;
    private DownloadObserver mObserver;

    public DownloadFunction(DownloadModel downloadModel, DownloadObserver observer) {
        this.mDownloadModel = downloadModel;
        this.mObserver = observer;
    }

    @Override
    public Observable<T> apply(ResponseBody responseBody) throws Exception {
        /*下载处理*/
        mDownloadModel.setState(DownloadModel.State.LOADING);//下载中状态
        SQLiteHelper.get().insertOrUpdate(mDownloadModel);//更新数据库状态(后期考虑下性能问题)
        try {
            //写入文件
            ResponseUtils.get().downloadLocalFile(responseBody, new File(mDownloadModel.getLocalUrl()), mDownloadModel);
        } catch (FileNotFoundException e) {
            LogUtils.w(e);
           return   Observable.error(ExceptionEngine.handleException(e));
        }
        return Observable.just((T) mDownloadModel);
    }
}