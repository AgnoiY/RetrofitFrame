package com.cjy.retrofitlibrary;

/**
 * <全局常量>
 * Data：2018/12/18
 *
 * @author yong
 */
public final class Constants {

    Constants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * 超时时长: 默认60秒
     */
    public static final long TIME_OUT = 60;

    /**
     * 数据库
     */
    public static final String TABLE = "table";
    public static final String TABLENAME = "download";
    public static final String AUTO_INCREMENT = " primary key autoincrement";
    public static final String UNIQUE = " unique on conflict replace";
    public static final String _ID = "_id";
    public static final String LOCALURL = "localUrl";
    public static final String SERVERURL = "serverUrl";
    public static final String TOTALSIZE = "totalSize";
    public static final String CURRENTSIZE = "currentSize";
    public static final String PROGRESS = "progress";
    public static final String STATE = "state";
    public static final String STATETEXT = "stateText";

}
