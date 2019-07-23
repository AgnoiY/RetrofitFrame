package com.cjy.retrofitlibrary;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cjy.retrofitlibrary.utils.LogUtils;
import com.cjy.retrofitlibrary.utils.Method;
import com.cjy.retrofitlibrary.utils.RequestUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Http请求类
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class RetrofitHttp {

    /*请求方式*/
    private Method method;
    /*请求参数*/
    private Map<String, Object> parameter;
    /*header*/
    private Map<String, Object> header;
    /*LifecycleProvider*/
    private LifecycleProvider lifecycle;
    /*ActivityEvent*/
    private ActivityEvent activityEvent;
    /*FragmentEvent*/
    private FragmentEvent fragmentEvent;
    /*标识请求的TAG*/
    private String tag;
    /*文件map*/
    private Map<String, File> fileMap;
    /*基础URL*/
    private String baseUrl;
    /*apiUrl*/
    private String apiUrl;
    /*String参数*/
    String bodyString;
    /*下载路径*/
    String downloadPath;
    /*是否强制JSON格式*/
    boolean isJson;

    /**
     * 构造函数
     */
    private RetrofitHttp(Builder builder) {
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.lifecycle = builder.lifecycle;
        this.activityEvent = builder.activityEvent;
        this.fragmentEvent = builder.fragmentEvent;
        this.tag = builder.tag;
        this.fileMap = builder.fileMap;
        this.baseUrl = builder.baseUrl;
        this.apiUrl = builder.apiUrl;
        this.isJson = builder.isJson;
        this.bodyString = builder.bodyString;
        this.method = builder.method;
        this.downloadPath = builder.downloadPath;
    }

    /**
     * 普通Http请求
     */
    public RetrofitHttp request(BaseHttpObserver httpObserver) {
        if (httpObserver == null) {
            throw new NullPointerException("BaseHttpObserver must not null!");
        } else {
            doRequest(httpObserver);
        }
        return this;
    }

    /**
     * 上传文件请求
     */
    public void upload(UploadObserver uploadCallback) {
        if (uploadCallback == null) {
            throw new NullPointerException("UploadObserver must not null!");
        } else {
            doUpload(uploadCallback);
        }
    }

    /**
     * 文件下载
     */
    public RetrofitHttp download(String filePath) {
//        if (callback == null) {
//            throw new NullPointerException("DownloadCallback must not null!");
//        } else if (!TextUtils.isEmpty(filePath)) {
//            doDownload(callback, filePath);
//        }
        return this;
    }

    /**
     * 普通请求
     *
     * @param httpObserver
     */
    private void doRequest(BaseHttpObserver httpObserver) {

        setObserver(httpObserver);

        /*请求方式处理*/
        Observable apiObservable = disposeApiObservable();

        setObservable(httpObserver, apiObservable);
    }

    /**
     * 文件上传
     *
     * @param uploadCallback
     */
    private void doUpload(UploadObserver uploadCallback) {

        setObserver(uploadCallback);

        /*处理文件集合*/
        List<MultipartBody.Part> fileList = new ArrayList<>();
        if (fileMap != null && fileMap.size() > 0) {
            int size = fileMap.size();
            int index = 1;
            File file;
            RequestBody requestBody;
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                file = entry.getValue();
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData(entry.getKey(), file.getName(),
                        new UploadRequestBody(requestBody, file, index, size, uploadCallback));
                fileList.add(part);
                index++;
            }
        }

        /*请求处理*/
        Observable apiObservable = RetrofitUtils.get().getRetrofit(getBaseUrl(), header).create(Api.class).upload(disposeApiUrl(), parameter, header, fileList);

        setObservable(uploadCallback, apiObservable);

    }

    /**
     * 文件下载
     *
     * @param downloadCallback
     */
    private void doDownload(DownloadCallback downloadCallback, String filePath) {

    }

    /**
     * 设置请求配置
     *
     * @param httpObserver
     */
    private void setObserver(BaseHttpObserver httpObserver) {
        /*加载失败提示弹出窗*/
        httpObserver.setToast(Configure.get().isToast);

        /*设置请求唯一标识*/
        httpObserver.setTag(TextUtils.isEmpty(tag) ? disposeApiUrl() : tag);

        /*设置请求唯一标识*/
        httpObserver.setDownloadPath(downloadPath);

        /*header处理*/
        disposeHeader();

        /*Parameter处理*/
        disposeParameter();
    }

    /**
     * 设置Observabl
     *
     * @param httpObserver
     */
    private <T> void setObservable(BaseHttpObserver<T> httpObserver, Observable apiObservable) {

        /* 被观察者 httpObservable */
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .baseObserver(httpObserver)
                .lifecycleProvider(lifecycle)
                .activityEvent(activityEvent)
                .fragmentEvent(fragmentEvent)
                .build();

        /* 观察者  httpObserver */
        /*设置监听*/
        httpObservable.observe().subscribe(httpObserver);

    }

    /**
     * 获取基础URL
     */
    private String getBaseUrl() {
        //如果没有重新指定URL则是用默认配置
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = Configure.get().getBaseUrl();
            if (TextUtils.isEmpty(baseUrl)) {
                LogUtils.e("baseUrl is null");
            }
        }
        return baseUrl;
    }

    /**
     * ApiUrl处理
     */
    private String disposeApiUrl() {
        if (TextUtils.isEmpty(apiUrl)) {
            LogUtils.d("apiUrl is null");
        }
        return apiUrl;
    }

    /**
     * 处理Header
     */
    private void disposeHeader() {

        /*header空处理*/
        if (header == null) {
            header = new TreeMap<>();
        }

        //添加基础 Header
        Map<String, Object> baseHeader = Configure.get().getBaseHeader();
        if (baseHeader != null && baseHeader.size() > 0) {
            header.putAll(baseHeader);
        }

        if (!header.isEmpty()) {
            //处理header中文或者换行符出错问题
            for (Map.Entry<String, Object> entry : header.entrySet()) {
                Object value = RequestUtils.getHeaderValueEncoded(entry.getValue());
                if (value != null)
                    header.put(entry.getKey(), value);
            }
        }

    }

    /**
     * 处理 Parameter
     */
    private void disposeParameter() {

        /*空处理*/
        if (parameter == null) {
            parameter = new TreeMap<>();
        }

        //添加基础 Parameter
        Map<String, Object> baseParameter = Configure.get().getBaseParameter();
        if (baseParameter != null && baseParameter.size() > 0) {
            parameter.putAll(baseParameter);
        }
    }

    /**
     * 处理ApiObservable
     */
    private Observable disposeApiObservable() {

        Observable apiObservable = null;

        /*是否JSON格式提交参数*/
        boolean hasBodyString = !TextUtils.isEmpty(bodyString);
        RequestBody requestBody = null;
        if (hasBodyString) {
            String mediaType = isJson ? "application/json; charset=utf-8" : "text/plain;charset=utf-8";
            requestBody = RequestBody.create(MediaType.parse(mediaType), bodyString);
        }

        /*Api接口*/
        Api apiService = RetrofitUtils.get().getRetrofit(getBaseUrl(), header).create(Api.class);
        /*未指定默认POST*/
        if (method == null) method = Method.POST;

        switch (method) {
            case GET:
                apiObservable = apiService.get(disposeApiUrl(), parameter, header);
                break;
            case POST:
                if (hasBodyString)
                    apiObservable = apiService.post(disposeApiUrl(), requestBody, header);
                else
                    apiObservable = apiService.post(disposeApiUrl(), parameter, header);
                break;
            case DELETE:
                apiObservable = apiService.delete(disposeApiUrl(), parameter, header);
                break;
            case PUT:
                apiObservable = apiService.put(disposeApiUrl(), parameter, header);
                break;
            default:
                break;
        }
        return apiObservable;
    }

    /**
     * Configure配置
     */
    public static final class Configure {

        /*请求基础路径*/
        private String baseUrl;
        /*超时时长*/
        private long timeout;
        /*时间单位*/
        private TimeUnit timeUnit;
        /*全局上下文*/
        private Context context;
        /*全局Handler*/
        private Handler handler;
        /*请求参数*/
        private Map<String, Object> parameter;
        /*header*/
        private Map<String, Object> header;
        /*是否显示Log*/
        private boolean showLog;
        /*加载失败提示弹出窗*/
        private boolean isToast;
        /*数据库库名*/
        private String sqliteName;
        /*数据库版本号*/
        private int sqliteVersion;

        public static Configure get() {
            return Configure.Holder.holders;
        }

        private static class Holder {
            private static Configure holders = new Configure();
        }

        private Configure() {
            timeout = Constants.TIME_OUT; //默认60秒
            timeUnit = TimeUnit.SECONDS; //默认秒
            showLog = true; //默认打印LOG
            isToast = true; //默认加载提示Toast
            sqliteName = "retrofit.download.db"; //默认数据库库名
            sqliteVersion = 1; //默认数据库版本号
        }

        /*请求基础路径*/
        public RetrofitHttp.Configure setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        /*基础参数*/
        public RetrofitHttp.Configure setBaseParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        public Map<String, Object> getBaseParameter() {
            return parameter;
        }

        /*基础Header*/
        public RetrofitHttp.Configure setBaseHeader(Map<String, Object> header) {
            if (this.header == null) {
                this.header = new HashMap<>();
            }
            this.header.putAll(header);
            return this;
        }

        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RetrofitHttp.Configure addHeader(String key, Object header) {
            if (this.header == null) {
                this.header = new HashMap<>();
            }
            this.header.put(key, header);
            return this;
        }

        public Map<String, Object> getBaseHeader() {
            return header;
        }

        /*超时时长*/
        public RetrofitHttp.Configure setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public long getTimeout() {
            return timeout;
        }

        /*是否显示LOG*/
        public RetrofitHttp.Configure showLog(boolean showLog) {
            this.showLog = showLog;
            return this;
        }

        public boolean isShowLog() {
            return showLog;
        }

        /*时间单位*/
        public RetrofitHttp.Configure setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        /*Handler*/
        public Handler getHandler() {
            return handler;
        }

        /*Context*/
        public Context getContext() {
            return context;
        }

        /*TipDialog*/
        public boolean isToast() {
            return isToast;
        }

        public Configure setToast(boolean isToast) {
            this.isToast = isToast;
            return this;
        }

        /*SQLiteName*/
        public String getSQLiteName() {
            return sqliteName;
        }

        public Configure setSQLiteName(String sqliteName) {
            this.sqliteName = sqliteName;
            return this;
        }

        /*SQLiteVersion*/
        public int getSQLiteVersion() {
            return sqliteVersion;
        }

        public Configure setSQLiteVersion(int sqliteVersion) {
            this.sqliteVersion = sqliteVersion;
            return this;
        }

        /*初始化全局上下文*/
        public RetrofitHttp.Configure init(Application app) {
            this.context = app.getApplicationContext();
            this.handler = new Handler(Looper.getMainLooper());
            return this;
        }

    }

    /**
     * Builder
     * 构造Request所需参数，按需设置
     */
    public static class Builder {
        /*请求方式*/
        private Method method;
        /*请求参数*/
        private Map<String, Object> parameter;
        /*header*/
        private Map<String, Object> header;
        /*LifecycleProvider*/
        private LifecycleProvider lifecycle;
        /*ActivityEvent*/
        private ActivityEvent activityEvent;
        /*FragmentEvent*/
        private FragmentEvent fragmentEvent;
        /*标识请求的TAG*/
        private String tag;
        /*文件map*/
        private Map<String, File> fileMap;
        /*基础URL*/
        private String baseUrl;
        /*apiUrl*/
        private String apiUrl;
        /*String参数*/
        private String bodyString;
        /*下载路径*/
        private String downloadPath;
        /*是否强制JSON格式*/
        private boolean isJson;

        private volatile Builder instance;

        public Builder getInstance() {
            Builder builder = instance;
            if (builder == null) {
                synchronized (RetrofitHttp.class) {
                    builder = instance;
                    if (builder == null) {
                        instance = builder = new Builder();
                    }
                }
            }
            return builder;
        }

        /*GET*/
        public RetrofitHttp.Builder get() {
            this.method = Method.GET;
            return this;
        }

        /*POST*/
        public RetrofitHttp.Builder post() {
            this.method = Method.POST;
            return this;
        }

        /*DELETE*/
        public RetrofitHttp.Builder delete() {
            this.method = Method.DELETE;
            return this;
        }

        /*PUT*/
        public RetrofitHttp.Builder put() {
            this.method = Method.PUT;
            return this;
        }

        /*基础URL*/
        public RetrofitHttp.Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /*API URL*/
        public RetrofitHttp.Builder apiUrl(@NonNull String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        /* 增加 Parameter 不断叠加参数 包括基础参数 */
        public RetrofitHttp.Builder addParameter(Map<String, Object> parameter) {
            if (this.parameter == null)
                this.parameter = parameter;
            else
                this.parameter.putAll(parameter);
            return this;
        }

        /* 增加 Parameter 不断叠加参数 包括基础参数 */
        public RetrofitHttp.Builder addParameter(String key, Object parameter) {
            if (this.parameter == null) {
                this.parameter = new TreeMap<>();
            }
            if (parameter != null) {
                this.parameter.put(key, parameter);
            }
            return this;
        }

        /*设置 Parameter 会覆盖 Parameter 包括基础参数*/
        public RetrofitHttp.Builder setParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        /* 设置String 类型参数  覆盖之前设置  isJson:是否强制JSON格式    bodyString设置后Parameter则无效 */
        public RetrofitHttp.Builder setBodyString(String bodyString, boolean isJson) {
            this.isJson = isJson;
            this.bodyString = bodyString;
            return this;
        }

        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RetrofitHttp.Builder addHeader(String key, Object header) {
            if (this.header == null) {
                this.header = new TreeMap<>();
            }
            this.header.put(key, header);
            return this;
        }

        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RetrofitHttp.Builder addHeader(Map<String, Object> header) {
            if (this.header == null)
                this.header = header;
            else
                this.header.putAll(header);
            return this;
        }

        /*设置 Header 会覆盖 Header 包括基础参数*/
        public RetrofitHttp.Builder setHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        /*LifecycleProvider*/
        public RetrofitHttp.Builder lifecycle(LifecycleProvider lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        /*ActivityEvent*/
        public RetrofitHttp.Builder activityEvent(ActivityEvent activityEvent) {
            this.activityEvent = activityEvent;
            return this;
        }

        /*FragmentEvent*/
        public RetrofitHttp.Builder fragmentEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
            return this;
        }

        /*tag*/
        public RetrofitHttp.Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /*downloadPath*/
        public RetrofitHttp.Builder downloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
            return this;
        }

        /*downloadPath*/
        public RetrofitHttp.Builder downloadPath(File downloadPath) {
            this.downloadPath = downloadPath.getAbsolutePath();
            return this;
        }

        /*文件*/
        public RetrofitHttp.Builder file(String name, File file) {
            if (fileMap == null) {
                fileMap = new IdentityHashMap();
            }
            this.fileMap.put(name, file);
            return this;
        }

        /*文件集合*/
        public RetrofitHttp.Builder file(Map<String, File> file) {
            this.fileMap = file;
            return this;
        }

        /*一个Key对应多个文件*/
        public RetrofitHttp.Builder file(String key, List<File> fileList) {
            if (fileMap == null) {
                fileMap = new IdentityHashMap();
            }
            if (fileList != null && !fileList.isEmpty()) {
                for (File file : fileList) {
                    fileMap.put(key, file);
                }
            }
            return this;
        }

        public RetrofitHttp.Builder clear() {
            this.method = Method.POST;
            this.parameter = null;
            this.header = null;
            this.lifecycle = null;
            this.activityEvent = null;
            this.fragmentEvent = null;
            this.tag = "";
            this.fileMap = null;
            this.apiUrl = "";
            this.bodyString = "";
            this.isJson = false;
            this.instance = null;
            this.downloadPath = "";
            return this;
        }

        public RetrofitHttp build() {
            return new RetrofitHttp(this);
        }
    }
}
