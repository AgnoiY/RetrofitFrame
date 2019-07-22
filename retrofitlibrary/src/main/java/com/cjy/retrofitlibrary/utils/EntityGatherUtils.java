package com.cjy.retrofitlibrary.utils;

import com.cjy.retrofitlibrary.download.Column;
import com.cjy.retrofitlibrary.download.NotNull;
import com.cjy.retrofitlibrary.download.PrimaryKey;
import com.cjy.retrofitlibrary.download.Table;
import com.cjy.retrofitlibrary.model.DownloadModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cjy.retrofitlibrary.Constants.TABLE;
import static com.cjy.retrofitlibrary.Constants._ID;

/**
 * <将对象转换成Map>
 * <p>
 * Data：2019/06/24
 *
 * @author yong
 */
public class EntityGatherUtils {

    private static final String TAG = EntityGatherUtils.class.getSimpleName();

    EntityGatherUtils() {
        throw new IllegalStateException("EntityGatherUtils class");
    }

    /**
     * 将对象转换成Map
     *
     * @param obj 带参数的对象
     * @return
     */
    public static Map<String, Object> getEntityMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Field[] fields = obj.getClass().getDeclaredFields();//获取类的各个属性值
        for (Field field : fields) {
            String fieldName = field.getName();//获取类的属性名称
            if (getValueByFieldName(fieldName, obj) != null)//获取类的属性名称对应的值
                map.put(fieldName, getValueByFieldName(fieldName, obj));
        }
        return map;
    }

    /**
     * 将对象中注解转换成List
     *
     * @param var 带参数的对象
     * @return
     */
    public static <T> List<String> getEntityList(Class<T> var) {
        List<String> list = new ArrayList<>();
        if (var == null) {
            return list;
        }
        Table table = var.getAnnotation(Table.class);
        if (table == null) return list;
        list.add(TABLE);
        list.add(table.value());
        do {
            Field[] fields = var.getDeclaredFields();//获取类的各个属性值
            list = getFieldList(list, fields);
            var = (Class<T>) var.getSuperclass();
        } while (var != Object.class);

        return list;
    }

    /**
     * 获取注解的属性值
     *
     * @param list
     * @param fields
     * @return
     */
    private static List<String> getFieldList(List<String> list, Field[] fields) {
        for (Field field : fields) {
            String fieldType = " " + field.getType().getSimpleName();//获取类的属性类型
            if (fieldType.contains(String.class.getSimpleName()) ||
                    fieldType.contains(DownloadModel.State.class.getSimpleName())) {
                fieldType = " varchar";
            } else if (fieldType.contains(int.class.getSimpleName()) ||
                    fieldType.contains(boolean.class.getSimpleName()) ||
                    fieldType.contains(Boolean.class.getSimpleName())) {
                fieldType = " Integer";
            }

            if (field.isAnnotationPresent(PrimaryKey.class)) {
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                fieldType = fieldType + primaryKey.value();
            }

            if (field.isAnnotationPresent(NotNull.class)) {
                NotNull notNull = field.getAnnotation(NotNull.class);
                fieldType += notNull.value();
            }

            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                int positionId = column.value().contains(_ID) ? 2 : list.size();
                list.add(positionId, column.value());
                list.add(positionId + 1, fieldType + ",");
            }
        }
        return list;
    }

    /**
     * 设置类的属性名称对应的值
     *
     * @param var
     * @param t
     * @param fieldName
     * @param value
     * @return
     */
    public static <T> T setValueByFieldName(Class<T> var, T t, String fieldName, Object value) {
        try {
            do {
                Field[] fields = var.getDeclaredFields();//获取类的各个属性值
                t = setFieldName(t, fields, fieldName, value);
                var = (Class<T>) var.getSuperclass();
            } while (var != Object.class);
            return t;
        } catch (IllegalAccessException e) {
            LogUtils.w(TAG, e);
            return null;
        }
    }

    /**
     * 设置类的属性名称对应的值
     *
     * @param object
     * @param fields
     * @param fieldName
     * @param value
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <T> T setFieldName(Object object, Field[] fields, String fieldName, Object value) throws IllegalAccessException {
        if (value != null)
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    field.set(object, value);
                }
            }
        return (T) object;
    }

    /**
     * 获取数据的类型
     *
     * @param var
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> String getFieldType(Class<T> var, String fieldName) {
        String type;
        do {
            Field[] fields = var.getDeclaredFields();//获取类的各个属性值
            type = getFieldType(fields, fieldName);
            if (type != null) return type;
            var = (Class<T>) var.getSuperclass();
        } while (var != Object.class);

        return type;
    }

    /**
     * 获取数据的类型
     *
     * @param fields
     * @param fieldName
     * @return
     */
    private static String getFieldType(Field[] fields, String fieldName) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName))
                return field.getType().getSimpleName();//获取类的属性类型
        }
        return null;
    }

    /**
     * 获取类的属性名称对应的值
     *
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getValueByFieldName(String fieldName, Object object) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        try {
            Class[] classes = new Class[]{};
            Object[] objects = new Object[]{};
            Method method = object.getClass().getMethod(getter, classes);
            return method.invoke(object, objects);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LogUtils.w(TAG, e);
            return null;
        }
    }
}
