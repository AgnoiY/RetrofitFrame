package com.cjy.rrtrofitframe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cjy.retrofitlibrary.RetrofitLibrary;
import com.cjy.retrofitlibrary.HttpObserver;
import com.cjy.rrtrofitframe.databinding.ActivityMainBinding;

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
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mMainBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initData();
    }

    public void initData() {
        login("15713802736", "a123456");
    }

    public void login(String userid, String pwd) {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", userid);
        map.put("password", pwd);

        RetrofitLibrary.getRetrofitHttp().post().apiUrl(UrlConstans.LOGIN)
                .addParameter(map).build()
                .request(new HttpObserver<LoginModel>(this, true) {
                    @Override
                    public void onSuccess(String action, LoginModel value) {
                        mMainBinding.text.setText(value.getToken());
                    }
                });

    }

}
