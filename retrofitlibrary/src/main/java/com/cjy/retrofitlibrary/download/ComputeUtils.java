package com.cjy.retrofitlibrary.download;

import android.text.TextUtils;

import java.io.File;

/**
 * 计算工具类
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
public class ComputeUtils {

    ComputeUtils() {
        throw new IllegalStateException("ComputeUtils class");
    }


    /**
     * 计算进度值
     *
     * @param current
     * @param total
     * @return
     */
    public static float getProgress(long current, long total) {
        if (total == 0) return 0;
        float progress = keepDecimal((float) current / (float) total, 3);
        return progress;
    }

    /**
     * 保留小数
     *
     * @param decimal 　保留数
     * @param digit   　保留小数位数
     * @return
     */
    public static float keepDecimal(Object decimal, int digit) {

        String format = "%." + digit + "f";

        if (decimal != null && (decimal instanceof Float || decimal instanceof Double))
            return Float.valueOf(removeZero(String.format(format, decimal)));

        return 0;
    }

    /**
     * 去除小数点后的 0
     *
     * @param rate
     * @return
     */
    public static String removeZero(String rate) {
        rate = rate.replaceAll("0+?$", "");//去掉多余的0
        rate = rate.replaceAll("[.]$", "");//如最后一位是.则去掉
        return rate;
    }

    /**
     * 文件是否存在
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;

        File file = new File(filePath);
        if (isFileExists(filePath)) {
            if (file.delete()) {
                System.out.println("删除单个文件" + filePath + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + filePath + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + filePath + "不存在！");
            return false;
        }
    }
}
