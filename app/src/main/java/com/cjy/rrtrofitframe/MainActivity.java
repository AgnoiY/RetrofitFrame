package com.cjy.rrtrofitframe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cjy.retrofitlibrary.HttpObserver;
import com.cjy.retrofitlibrary.RetrofitDownload;
import com.cjy.retrofitlibrary.RetrofitLibrary;
import com.cjy.retrofitlibrary.download.DownloadBean;
import com.cjy.retrofitlibrary.download.DownloadCallback;
import com.cjy.retrofitlibrary.model.DownloadModel;
import com.cjy.rrtrofitframe.databinding.ActivityMainBinding;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <功能详细描述>
 * 网络请求
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class MainActivity extends AppCompatActivity implements DownloadCallback<DownloadBean> {

    private ActivityMainBinding mMainBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initData();
    }

    private void initData() {
        login("15713802736", "a123456");
        DownloadBean bean = download();
        DownloadBean beanQuery = RetrofitDownload.get().getDownloadModel(bean);
        if (beanQuery != null)
            beanQuery.setCallback(this);
        mMainBinding.downNumTv.setText("下载数量：" + RetrofitDownload.get().getDownloadList(DownloadBean.class).size());
        mMainBinding.dbNumTv.setText("数据库列表总量：" + RetrofitDownload.get().getDownloadCount());
        mMainBinding.progressTv.setText((beanQuery == null ? 0 : String.format("%.2f", beanQuery.getProgress() * 100)) + "%");
        mMainBinding.serverTv.setText("下载地址：" + (beanQuery == null ? bean.getServerUrl() : beanQuery.getServerUrl()));
        mMainBinding.downStateTv.setText("下载状态：" + getStateText(beanQuery == null ? bean.getState() : beanQuery.getState()));
        mMainBinding.progress.setProgress(beanQuery == null ? 0 : (int) (beanQuery.getProgress() * 100));
        mMainBinding.startBt.setOnClickListener(v -> RetrofitDownload.get().startDownload(beanQuery == null ? bean : beanQuery));
        mMainBinding.pauseBt.setOnClickListener(v -> RetrofitDownload.get().stopDownload(beanQuery == null ? bean : beanQuery));
        mMainBinding.deleteBt.setOnClickListener(v -> RetrofitDownload.get().removeDownload(beanQuery, true));
    }

    private String getStateText(DownloadModel.State state) {
        String stateText = "下载";
        switch (state) {
            case NONE:
                stateText = "下载";
                break;
            case WAITING:
                stateText = "等待中";
                break;
            case LOADING:
                stateText = "下载中";
                break;
            case PAUSE:
                stateText = "暂停中";
                break;
            case ERROR:
                stateText = "错误";
                break;
            case FINISH:
                stateText = "完成";
                break;
            default:
                break;
        }
        return stateText;
    }

    private void login(String userid, String pwd) {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", userid);
        map.put("password", pwd);

        RetrofitLibrary.getHttp().post().apiUrl(UrlConstans.LOGIN)
                .addParameter(map).build()
                .request(new HttpObserver<LoginModel>(this, true) {
                    @Override
                    public void onSuccess(String action, LoginModel value) {
                        mMainBinding.text.setText(value.getToken());
                    }
                });

    }

    private DownloadBean download() {
        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "WEIXIN" + ".apk");
        String url1 = "http://imtt.dd.qq.com/16891/50CC095EFBE6059601C6FB652547D737.apk?fsname=com.tencent.mm_6.6.7_1321.apk&csr=1bbd";
        String icon1 = "http://pp.myapp.com/ma_icon/0/icon_10910_1534495359/96";

//        DownloadBean bean = new DownloadBean(url1, icon1, file1.getAbsolutePath());
        DownloadBean bean = new DownloadBean(url1, url1, file1.getAbsolutePath());

        bean.setCallback(this);

        return bean;
    }

    @Override
    public void onProgress(DownloadBean model) {
        mMainBinding.downStateTv.setText("下载状态：" + getStateText(model.getState()));
        mMainBinding.progressTv.setText(String.format("%.2f", model.getProgress() * 100) + "%");
        mMainBinding.progress.setProgress((int) (model.getProgress() * 100));
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onSuccess(DownloadBean model) {
        mMainBinding.dbNumTv.setText("数据库列表总量：" + RetrofitDownload.get().getDownloadCount());
    }
}
