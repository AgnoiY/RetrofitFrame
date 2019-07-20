package com.cjy.retrofitlibrary;

import android.content.Context;
import android.os.Looper;

import com.cjy.retrofitlibrary.dialog.AutoDefineToast;
import com.cjy.retrofitlibrary.download.ResponseUtils;
import com.cjy.retrofitlibrary.model.DownloadModel;
import com.cjy.retrofitlibrary.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 下载处理函数
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
class DownloadFunction implements Function<ResponseBody, Object> {

    private DownloadModel mDownloadModel;

    public DownloadFunction(DownloadModel downloadModel) {
        this.mDownloadModel = downloadModel;
    }

    @Override
    public Object apply(ResponseBody responseBody) throws Exception {
        /*下载处理*/
        mDownloadModel.setState(DownloadModel.State.LOADING);//下载中状态
        SQLiteHelper.get().insertOrUpdate(mDownloadModel);//更新数据库状态(后期考虑下性能问题)
        try {
            //写入文件
            ResponseUtils.get().downloadLocalFile(responseBody, new File(mDownloadModel.getLocalUrl()), mDownloadModel);
        } catch (FileNotFoundException e) {
            new android.os.Handler(Looper.getMainLooper()).post(() -> {
                Context mContext = RetrofitHttp.Configure.get().getContext();
                AutoDefineToast.showInfoToast(mContext, mContext.getString(R.string.file_not_found_permission_error));

            });
            SQLiteHelper.get().delete(mDownloadModel);
            LogUtils.w(e);
        }
        return mDownloadModel;
    }
}