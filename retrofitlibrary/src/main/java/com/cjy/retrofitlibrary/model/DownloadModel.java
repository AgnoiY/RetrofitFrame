package com.cjy.retrofitlibrary.model;

import com.cjy.retrofitlibrary.Api;
import com.cjy.retrofitlibrary.DownloadCallback;
import com.cjy.retrofitlibrary.annotation.download.Column;
import com.cjy.retrofitlibrary.annotation.download.DownLoadServer;
import com.cjy.retrofitlibrary.annotation.download.Ignore;
import com.cjy.retrofitlibrary.annotation.download.NotNull;
import com.cjy.retrofitlibrary.annotation.download.PrimaryKey;
import com.cjy.retrofitlibrary.annotation.download.Table;
import com.cjy.retrofitlibrary.utils.AnnotationUtils;

import java.io.Serializable;
import java.lang.reflect.Field;

import static com.cjy.retrofitlibrary.Constants.AUTO_INCREMENT;
import static com.cjy.retrofitlibrary.Constants.CURRENTSIZE;
import static com.cjy.retrofitlibrary.Constants.LOCALURL;
import static com.cjy.retrofitlibrary.Constants.PROGRESS;
import static com.cjy.retrofitlibrary.Constants.SERVERURL;
import static com.cjy.retrofitlibrary.Constants.STATE;
import static com.cjy.retrofitlibrary.Constants.STATETEXT;
import static com.cjy.retrofitlibrary.Constants.TABLENAME;
import static com.cjy.retrofitlibrary.Constants.TOTALSIZE;
import static com.cjy.retrofitlibrary.Constants.UNIQUE;
import static com.cjy.retrofitlibrary.Constants._ID;

/**
 * 下载实体类
 * 备注:用户使用下载类需要继承此类
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
@Table(TABLENAME)
public class DownloadModel<T> extends BaseResponseModel<T> implements Serializable {

    @PrimaryKey(AUTO_INCREMENT)
    @Column(_ID)
    private Integer _id;

    @NotNull
    @Column(LOCALURL)
    private String localUrl; //本地存储地址

    @PrimaryKey(UNIQUE)
    @NotNull
    @Column(SERVERURL)
    private String serverUrl; //下载地址

    @NotNull
    @Column(TOTALSIZE)
    private long totalSize;//文件大小

    @NotNull
    @Column(CURRENTSIZE)
    private long currentSize; //当前大小

    @NotNull
    @Column(PROGRESS)
    private float progress; //下载百分比

    @NotNull
    @Column(STATE)
    private State state = State.NONE; //下载状态

    @NotNull
    @Column(STATETEXT)
    private String stateText = "下载"; //下载状态Text

    @Ignore
    private Api api; //接口service

    @Ignore
    private DownloadCallback callback; //回调接口

    public DownloadModel() {
    }

    public DownloadModel(String url) {
        setServerUrl(url);
    }

    public DownloadModel(String url, DownloadCallback callback) {
        setServerUrl(url);
        setCallback(callback);
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getLocalUrl() {
        return localUrl == null ? "" : localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getServerUrl() {
        if (serverUrl == null)
            getDownloadServer();
        return serverUrl == null ? "" : serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public float getProgress() {
        return progress;
    }

    public DownloadModel setProgress(float progress) {
        this.progress = progress;
        return this;
    }

    public State getState() {
        return state;
    }

    public DownloadModel setState(State state) {
        this.state = state;
        this.stateText = getStateText(state);
        return this;
    }

    public String getStateText() {
        return stateText;
    }

    public DownloadModel setStateText(String stateText) {
        this.stateText = stateText;
        return this;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public DownloadCallback getCallback() {
        return callback;
    }

    public DownloadModel setCallback(DownloadCallback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 获取对象中注解下载地址
     *
     * @return
     */
    public void getDownloadServer() {
        Class var = this.getClass();
        do {
            Field[] fields = var.getDeclaredFields();//获取类的各个属性值
            for (Field field : fields) {
                if (field.isAnnotationPresent(DownLoadServer.class)) {
                    serverUrl = (String) AnnotationUtils.getValueByFieldName(field.getName(), this);
                }
            }
            var = var.getSuperclass();

        } while (var != Object.class);
    }

    /**
     * 枚举下载状态
     */
    public enum State {
        NONE,           //无状态
        WAITING,        //等待
        LOADING,        //下载中
        PAUSE,          //暂停
        ERROR,          //错误
        FINISH,         //完成
    }

    /**
     * 设置存储的下载状态
     *
     * @param state
     * @return
     */
    private String getStateText(State state) {
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

}
