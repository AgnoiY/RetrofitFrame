package com.cjy.retrofitlibrary;

import com.cjy.retrofitlibrary.utils.ResponseUtils;
import com.cjy.retrofitlibrary.model.DownloadModel;

import java.io.File;

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
        //写入文件
        ResponseUtils.get().downloadLocalFile(responseBody, new File(mDownloadModel.getLocalUrl()), mDownloadModel);
        return mDownloadModel;
    }
}