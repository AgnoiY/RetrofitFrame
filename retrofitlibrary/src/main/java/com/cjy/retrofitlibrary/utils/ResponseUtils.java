package com.cjy.retrofitlibrary.utils;

import com.cjy.retrofitlibrary.model.DownloadModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * 响应工具类
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
public class ResponseUtils {


    private static ResponseUtils mInstance;

    private ResponseUtils() {
    }

    public static ResponseUtils get() {

        ResponseUtils responseUtils = mInstance;

        if (responseUtils == null) {
            synchronized (ResponseUtils.class) {
                responseUtils = mInstance;
                if (responseUtils == null) {
                    responseUtils = mInstance = new ResponseUtils();
                }
            }
        }
        return responseUtils;
    }


    /**
     * 下载文件到本地
     *
     * @param responseBody 响应体
     * @param file         目标文件
     * @param download     下载实体类
     */
    public void downloadLocalFile(ResponseBody responseBody, File file, DownloadModel download) throws IOException {


        //创建文件夹
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //初始化
        InputStream inputStream = responseBody.byteStream();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        FileChannel channelOut = randomAccessFile.getChannel();
        //总长度
        long allLength = download.getTotalSize() == 0 ? responseBody.contentLength() : download.getCurrentSize() + responseBody.contentLength();

        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, download.getCurrentSize(), allLength - download.getCurrentSize());

        byte[] buffer = new byte[1024 * 4];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, length);
        }

        inputStream.close();
        randomAccessFile.close();
        channelOut.close();
    }
}
