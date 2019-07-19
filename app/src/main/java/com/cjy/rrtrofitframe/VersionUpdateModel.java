package com.cjy.rrtrofitframe;

import com.cjy.retrofitlibrary.download.DownLoadServer;
import com.cjy.retrofitlibrary.model.BaseResponseModel;
import com.cjy.retrofitlibrary.model.DownloadModel;

/**
 * <下载地址>
 * Data：2018/12/18
 *
 * @author yong
 */

public class VersionUpdateModel extends DownloadModel<VersionUpdateModel> {

    /**
     * name : v1.1
     * androidApkUrl : http://databox.worken.cn/download/app-release.apk
     * versionCd : 1.1
     * versionDesc : 第二版
     * packageSize : 6666
     * isForce : 0
     * appstoreUrl :
     */

    private String name;
    @DownLoadServer
    private String androidApkUrl;
    private String versionCd;
    private String versionDesc;
    private int packageSize;
    private int isForce;
    private String appstoreUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAndroidApkUrl() {
        return androidApkUrl;
    }

    public void setAndroidApkUrl(String androidApkUrl) {
        this.androidApkUrl = androidApkUrl;
    }

    public String getVersionCd() {
        return versionCd;
    }

    public void setVersionCd(String versionCd) {
        this.versionCd = versionCd;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public int getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(int packageSize) {
        this.packageSize = packageSize;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public String getAppstoreUrl() {
        return appstoreUrl;
    }

    public void setAppstoreUrl(String appstoreUrl) {
        this.appstoreUrl = appstoreUrl;
    }
}
