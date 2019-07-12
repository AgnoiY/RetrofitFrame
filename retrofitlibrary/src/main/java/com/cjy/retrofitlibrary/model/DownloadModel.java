package com.cjy.retrofitlibrary.model;

import com.cjy.retrofitlibrary.Api;
import com.cjy.retrofitlibrary.download.Column;
import com.cjy.retrofitlibrary.download.DownloadCallback;
import com.cjy.retrofitlibrary.download.Ignore;
import com.cjy.retrofitlibrary.download.NotNull;
import com.cjy.retrofitlibrary.download.PrimaryKey;
import com.cjy.retrofitlibrary.download.Table;

import java.io.Serializable;

import static com.cjy.retrofitlibrary.Constants.AUTO_INCREMENT;
import static com.cjy.retrofitlibrary.Constants.CURRENTSIZE;
import static com.cjy.retrofitlibrary.Constants.LOCALURL;
import static com.cjy.retrofitlibrary.Constants.PROGRESS;
import static com.cjy.retrofitlibrary.Constants.SERVERURL;
import static com.cjy.retrofitlibrary.Constants.STATE;
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
public class DownloadModel implements Serializable {

    @PrimaryKey(AUTO_INCREMENT)
    @Column(_ID)
    private Integer _id;

    @NotNull
    @Column(LOCALURL)
    private String localUrl;//本地存储地址

    @PrimaryKey(UNIQUE)
    @NotNull
    @Column(SERVERURL)
    private String serverUrl;//下载地址

    @NotNull
    @Column(TOTALSIZE)
    private long totalSize;//文件大小

    @NotNull
    @Column(CURRENTSIZE)
    private long currentSize;//当前大小

    @NotNull
    @Column(PROGRESS)
    private float progress;//下载百分比

    @NotNull
    @Column(STATE)
    private State state = State.NONE;//下载状态

    @Ignore
    private Api api;//接口service

    @Ignore
    private DownloadCallback callback;//回调接口

    public DownloadModel() {
    }

    public DownloadModel(String url) {
        setServerUrl(url);
    }

    public DownloadModel(String url, DownloadCallback callback) {
        setServerUrl(url);
        setCallback(callback);
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

    public void setState(State state) {
        this.state = state;
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

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }
}