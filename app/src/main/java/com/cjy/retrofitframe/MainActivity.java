package com.cjy.retrofitframe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cjy.retrofitframe.databinding.ActivityMainBinding;
import com.cjy.retrofitlibrary.ApiException;
import com.cjy.retrofitlibrary.DownloadCallback;
import com.cjy.retrofitlibrary.HttpObserver;
import com.cjy.retrofitlibrary.RetrofitDownload;
import com.cjy.retrofitlibrary.RetrofitLibrary;
import com.cjy.retrofitlibrary.dialog.ToastAutoDefine;
import com.cjy.retrofitlibrary.model.DownloadModel;

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
public class MainActivity extends AppCompatActivity implements DownloadCallback<VersionUpdateModel> {

    private ActivityMainBinding mMainBinding;
    private VersionUpdateModel updateModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initData();
    }

    private void initData() {
        mMainBinding.text.setOnClickListener(v -> {
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("appPlatform", "android");
            parameterMap.put("versionCd", "1.0.0");

            RetrofitLibrary.getHttp().post().apiUrl(UrlConstans.VERSION)
                    .addParameter(parameterMap).build()
                    .request(new HttpObserver<VersionUpdateModel>(this, true) {
                        @Override
                        public void onSuccess(String action, VersionUpdateModel value) {
                            updateModel = value;
                            updateModel = RetrofitDownload.get().getDownloadModel(value);
                            updateModel.setCallback(MainActivity.this);
                            updateModel.setLocalUrl(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    "WEIXIN" + ".apk").getAbsolutePath());
                            mMainBinding.downNumTv.setText("下载数量：" + RetrofitDownload.get().getDownloadList(DownloadModel.class).size());
                            mMainBinding.dbNumTv.setText("数据库列表总量：" + RetrofitDownload.get().getDownloadCount());
                            mMainBinding.progressTv.setText(String.format("%.2f", updateModel.getProgress() * 100) + "%");
                            mMainBinding.serverTv.setText("下载地址：" + updateModel.getServerUrl());
                            mMainBinding.downStateTv.setText("下载状态：" + updateModel.getStateText());
                            mMainBinding.progress.setProgress((int) (updateModel.getProgress() * 100));
                        }
                    });
        });
        mMainBinding.startBt.setOnClickListener(v -> RetrofitDownload.get().startDownload(updateModel));
        mMainBinding.pauseBt.setOnClickListener(v -> RetrofitDownload.get().stopDownload(updateModel));
        mMainBinding.deleteBt.setOnClickListener(v -> RetrofitDownload.get().removeDownload(updateModel, true));

        mMainBinding.weatherBt.setOnClickListener(v -> login("15713802736", "123456"));
    }

    private void login(String userid, String pwd) {

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("type", 2);
        parameterMap.put("loginName", userid);
        parameterMap.put("loginPwd", pwd);
//        parameterMap.put("mobile", userid);
//        parameterMap.put("password", pwd);

        RetrofitLibrary.getHttp()
                .addParameter(parameterMap).buildPost(UrlConstans.LOGIN)
                .request(new HttpObserver<LoginModel>(this, true) {
                    @Override
                    public void onSuccess(String action, LoginModel value) {
                        mMainBinding.text.setText(value.getToken());
                        RetrofitLibrary.getHttpConfigure().addHeader("token", value.getToken());
                        getList(value.getUserId(), value.getToken());
                    }
                });

    }

    private void getList(int id, String token) {

        Map<String, Object> map = new HashMap<>();
        map.put("userid", id);

        RetrofitLibrary.getHttp().get().apiUrl(UrlConstans.LIST)
                .addHeader("Authorization", "Bearer " + token)
                .addParameter("status", -7)
                .addHeader("token", token)
//                .setParameter(map)
                .build()
                .request(new HttpObserver<ListModel>(this, true) {
                    @Override
                    public void onSuccess(String action, ListModel value) {
//                        LogUtils.d(action, value.getData() + ": " + value.getData().size());
                    }
                });

    }

    private DownloadModel download() {
        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "WEIXIN" + ".apk");
        String url1 = "http://imtt.dd.qq.com/16891/50CC095EFBE6059601C6FB652547D737.apk?fsname=com.tencent.mm_6.6.7_1321.apk&csr=1bbd";
        String icon1 = "http://pp.myapp.com/ma_icon/0/icon_10910_1534495359/96";

        DownloadModel bean = new DownloadModel(url1);
        bean.setLocalUrl(file1.getAbsolutePath());

        return bean;
    }

    @Override
    public void onProgress(VersionUpdateModel model) {
        mMainBinding.downStateTv.setText("下载状态：" + model.getStateText());
        mMainBinding.progressTv.setText(String.format("%.2f", model.getProgress() * 100) + "%");
        mMainBinding.progress.setProgress((int) (model.getProgress() * 100));
    }

    @Override
    public void onError(Throwable e) {
        mMainBinding.dbNumTv.setText("数据库列表总量：" + RetrofitDownload.get().getDownloadCount());
        if (e instanceof ApiException)
            ToastAutoDefine.showFailToast(MainActivity.this, ((ApiException) e).getMsg());
    }

    @Override
    public void onSuccess(VersionUpdateModel model) {
        mMainBinding.dbNumTv.setText("数据库列表总量：" + RetrofitDownload.get().getDownloadCount());
    }
}
