package com.cjy.retrofitlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cjy.retrofitlibrary.model.DownloadModel;
import com.cjy.retrofitlibrary.utils.EntityGatherUtils;
import com.cjy.retrofitlibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.cjy.retrofitlibrary.Constants.SERVERURL;
import static com.cjy.retrofitlibrary.Constants.TABLE;
import static com.cjy.retrofitlibrary.Constants._ID;

/**
 * 数据库辅助类
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper mInstance;
    private SQLiteDatabase database;

    public static SQLiteHelper get() {
        SQLiteHelper dbHelper = mInstance;
        if (dbHelper == null) {
            synchronized (SQLiteHelper.class) {
                dbHelper = mInstance;
                if (dbHelper == null) {
                    RetrofitHttp.Configure mConfigure = RetrofitHttp.Configure.get();
                    if (mConfigure.getContext() == null) {
                        throw new NullPointerException("RetrofitHttp not init!");
                    }
                    mInstance = dbHelper = new SQLiteHelper(mConfigure.getContext(),
                            mConfigure.getSQLiteName(), mConfigure.getSQLiteVersion());
                }
            }
        }
        return dbHelper;
    }

    private SQLiteHelper(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSQLite());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 创建数据库
     *
     * @return
     */
    private String createSQLite() {
        StringBuilder builder = new StringBuilder();
        builder.append("create table if not exists ");
        List<String> list = EntityGatherUtils.getEntityList(DownloadModel.class);
        for (int i = 0; i < list.size(); i += 2) {
            String key = list.get(i);
            String value = list.get(i + 1);
            if (key.equals(TABLE)) {
                builder.append(value + " (");
                continue;
            }

            builder.append(key + value);
        }
        return builder.replace(builder.length() - 1, builder.length(), ")").toString();
    }

    /**
     * 插入或者更新对象
     * 备注:有则更新，无则插入
     *
     * @param model
     * @return
     */
    public long insertOrUpdate(DownloadModel model) {
        long count = 0;
        if (database != null) {
            database = mInstance.getWritableDatabase();
            ContentValues values = getContentValues(model);
            if (TextUtils.isEmpty(getTable())) return 0;
            String selection = SERVERURL + "=?";
            String[] selectionArgs = new String[]{model.getServerUrl()};
            Cursor cursor = queryServerUrl(model.getServerUrl());
            if (cursor.moveToNext()) {
                count = database.update(getTable(), values, selection, selectionArgs);
            } else {
                count = database.insert(getTable(), null, values);
            }
            cursor.close();
            values.clear();
            database.close();
        }
        return count;
    }

    /**
     * 删除对象
     *
     * @param model
     * @return
     */
    public int delete(DownloadModel model) {
        int count = 0;
        database = mInstance.getWritableDatabase();
        if (database != null) {
            count = database.delete(getTable(), SERVERURL + "=?", new String[]{model.getServerUrl()});
            database.close();
        }
        return count;
    }

    /**
     * 下载地址条件查询列表中数据
     *
     * @param model
     * @param <T>
     * @return
     */
    public <T> T query(DownloadModel model) {
        T t = null;
        database = mInstance.getWritableDatabase();
        if (database != null) {
            Cursor cursor = queryServerUrl(model.getServerUrl());
            List<T> tList = (List<T>) getCursorModel(cursor, model.getClass());
            if (tList != null && !tList.isEmpty())
                t = tList.get(0);
            database.close();
        }
        return t;
    }

    /**
     * 查询全部列表
     *
     * @param var
     * @param <T>
     * @return
     */
    public <T> List<T> query(Class<T> var) {
        List<T> list = null;
        database = mInstance.getWritableDatabase();
        if (database != null) {
            list = getCursorModel(database.rawQuery("select * from " + getTable(), null), var);
            database.close();
        }
        return list;
    }

    /**
     * 查询数据总数
     *
     * @return
     */
    public int queryCount() {
        int count = 0;
        database = mInstance.getWritableDatabase();
        if (database != null && !TextUtils.isEmpty(getTable())) {
            Cursor cursor = database.rawQuery("select * from " + getTable(), null);
            count = cursor.getCount();
            cursor.close();
            database.close();
        }
        return count;
    }

    /**
     * 获取数据库表名
     *
     * @return
     */
    private String getTable() {
        List<String> list = EntityGatherUtils.getEntityList(DownloadModel.class);
        for (int i = 0; i < list.size(); i += 2) {
            String key = list.get(i);
            String value = list.get(i + 1);
            if (key.equals(TABLE)) {
                return value;
            }
        }
        return "";
    }

    /**
     * 下载网络地址查询数据
     *
     * @param serverUrl
     * @return
     */
    private Cursor queryServerUrl(String serverUrl) {
        String selection = SERVERURL + "=?";
        String[] selectionArgs = new String[]{serverUrl};
        return database.query(getTable(), null, selection, selectionArgs, null, null, null);
    }

    /**
     * 查询的数据转化Model
     *
     * @param cursor
     * @param var
     * @param <T>
     * @return
     */
    private <T> List<T> getCursorModel(Cursor cursor, Class<T> var) {
        ArrayList<T> tList = new ArrayList<>();
        if (cursor != null) {
            String[] columns = cursor.getColumnNames();
            while (cursor.moveToNext()) {
                try {
                    T t = var.newInstance();
                    for (String column : columns) {
                        String type = EntityGatherUtils.getFieldType(var, column);
                        t = EntityGatherUtils.setValueByFieldName(var, t, column, getCursorValue(type, cursor, column));
                    }
                    tList.add(t);
                } catch (IllegalAccessException | InstantiationException e) {
                    LogUtils.w(e);
                }
            }
            cursor.close();
        }
        return tList;
    }

    /**
     * 数据类型获取Cursor的值
     *
     * @param type
     * @param cursor
     * @param column
     * @return
     */
    private Object getCursorValue(String type, Cursor cursor, String column) {
        Object value = null;
        if (type.contains(String.class.getSimpleName())) {
            value = cursor.getString(cursor.getColumnIndex(column));
        } else if (type.contains(Integer.class.getSimpleName()) ||
                type.contains(int.class.getSimpleName())) {
            value = cursor.getInt(cursor.getColumnIndex(column));
        } else if (type.contains(Boolean.class.getSimpleName()) ||
                type.contains(boolean.class.getSimpleName())) {
            value = cursor.getInt(cursor.getColumnIndex(column)) == 1;
        } else if (type.contains(Long.class.getSimpleName()) ||
                type.contains(long.class.getSimpleName())) {
            value = cursor.getLong(cursor.getColumnIndex(column));
        } else if (type.contains(Double.class.getSimpleName()) ||
                type.contains(double.class.getSimpleName())) {
            value = cursor.getDouble(cursor.getColumnIndex(column));
        } else if (type.contains(Float.class.getSimpleName()) ||
                type.contains(float.class.getSimpleName())) {
            value = cursor.getFloat(cursor.getColumnIndex(column));
        } else if (type.contains(DownloadModel.State.class.getSimpleName())) {
            value = cursor.getString(cursor.getColumnIndex(column));
            value = DownloadModel.State.valueOf(value.toString());
        }
        return value;
    }

    /**
     * 获取　ContentValues
     *
     * @param var
     * @return
     */
    private ContentValues getContentValues(DownloadModel var) {
        ContentValues values = new ContentValues();
        List<String> list = EntityGatherUtils.getEntityList(var.getClass());
        for (int i = 0; i < list.size(); i += 2) {
            String key = list.get(i);
            if (key.equals(TABLE) || key.contains(_ID)) {
                continue;
            }
            Object object = EntityGatherUtils.getValueByFieldName(key, var);
            if (object instanceof String) {
                values.put(key, (String) object);
            } else if (object instanceof Integer) {
                values.put(key, (Integer) object);
            } else if (object instanceof Long) {
                values.put(key, (Long) object);
            } else if (object instanceof Float) {
                values.put(key, (Float) object);
            } else if (object instanceof Boolean) {
                Boolean aBoolean = (Boolean) object;
                values.put(key, aBoolean ? 1 : 0);
            } else if (object instanceof Enum) {
                values.put(key, ((Enum) object).name());
            }
        }
        return values;
    }

}
