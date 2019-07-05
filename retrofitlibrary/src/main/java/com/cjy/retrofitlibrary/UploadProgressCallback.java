package com.cjy.retrofitlibrary;

import java.io.File;

/**
 * 进度回调接口
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface UploadProgressCallback {

    /**
     * 上传进度回调
     *
     * @param file         源文件
     * @param currentSize  当前上传值
     * @param totalSize    总大小
     * @param progress     进度
     * @param currentIndex 当前下标
     * @param totalFile    总文件数
     */
    void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile);
}
