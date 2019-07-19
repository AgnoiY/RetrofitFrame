package com.cjy.retrofitlibrary.download;


import com.cjy.retrofitlibrary.model.DownloadModel;

/**
 * 下载实体类，继承Download
 */
public class DownloadBean extends DownloadModel {

    /**
     * 额外字段，apk图标
     */
    @DownLoadServer
    private String icon;

    public DownloadBean() {

    }

    public DownloadBean(String url, String icon, String localUrl) {
        setLocalUrl(localUrl);
        setIcon(icon);
    }


    public String getIcon() {
        return icon == null ? "" : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
