package com.cjy.retrofitlibrary;

import android.content.Context;

import com.cjy.retrofitlibrary.annotation.CabcelbleParameter;
import com.cjy.retrofitlibrary.annotation.Constructors;
import com.cjy.retrofitlibrary.annotation.ContextParameter;
import com.cjy.retrofitlibrary.annotation.MsgParameter;
import com.cjy.retrofitlibrary.annotation.ProgressParameter;
import com.cjy.retrofitlibrary.annotation.download.Column;
import com.cjy.retrofitlibrary.annotation.download.NotNull;
import com.cjy.retrofitlibrary.annotation.download.PrimaryKey;
import com.cjy.retrofitlibrary.annotation.download.Table;
import com.cjy.retrofitlibrary.annotation.model.Code;
import com.cjy.retrofitlibrary.annotation.model.Data;
import com.cjy.retrofitlibrary.annotation.model.Message;
import com.cjy.retrofitlibrary.annotation.model.ModelData;
import com.cjy.retrofitlibrary.annotation.model.Success;
import com.cjy.retrofitlibrary.dialog.ToastAutoDefine;
import com.cjy.retrofitlibrary.model.DownloadModel;
import com.cjy.retrofitlibrary.utils.LogUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cjy.retrofitlibrary.Constants.TABLE;
import static com.cjy.retrofitlibrary.Constants._ID;

/**
 * <注解转换>
 * <p>
 * Data：2019/06/24
 *
 * @author yong
 */
public class AnnotationUtils {

    private static final String TAG = AnnotationUtils.class.getSimpleName();

    AnnotationUtils() {
        throw new IllegalStateException("AnnotationUtils class");
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
     * 获取对象中注解值
     *
     * @return
     */
    public static <T> BaseResponseModel<T> getResponseModel(T t) {
        Class var = t.getClass();
        BaseResponseModel mBaseModel = new BaseResponseModel();

        ModelData modelData = (ModelData) var.getAnnotation(ModelData.class);
        if (modelData != null) {
            mBaseModel.setCode(200);
            mBaseModel.setData(t);
            return mBaseModel;
        }

        do {
            Field[] fields = var.getDeclaredFields();//获取类的各个属性值
            for (Field field : fields) {
                if (field.isAnnotationPresent(Code.class)) {
                    mBaseModel.setCode((int) AnnotationUtils.getValueByFieldName(field.getName(), t));
                    Code code = field.getAnnotation(Code.class);
                    mBaseModel.setCodes(code.value());
                    mBaseModel.setLoginClass(code.login());
                    mBaseModel.setLoginTip(code.loginTip());
                } else if (field.isAnnotationPresent(Message.class)) {
                    mBaseModel.setMsg((String) AnnotationUtils.getValueByFieldName(field.getName(), t));
                } else if (field.isAnnotationPresent(Data.class)) {
                    mBaseModel.setData(AnnotationUtils.getValueByFieldName(field.getName(), t));
                } else if (field.isAnnotationPresent(Success.class)) {
                    mBaseModel.setSuccess((boolean) AnnotationUtils.getValueByFieldName(field.getName(), t));
                }
            }
            var = var.getSuperclass();

        } while (var != Object.class);

        return mBaseModel;
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
        try {
            String getter = fieldName;
            Class var = object.getClass();
            String booleanName = getFieldType(var, fieldName);
            if (booleanName != null && !booleanName.equals(Boolean.class.getSimpleName()) &&
                    !booleanName.equals(boolean.class.getSimpleName())) {
                String firstLetter = fieldName.substring(0, 1).toUpperCase();
                getter = "get" + firstLetter + fieldName.substring(1);
            }
            Class[] classes = new Class[]{};
            Object[] objects = new Object[]{};
            Method method = var.getMethod(getter, classes);
            return method.invoke(object, objects);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 获取Toast工具中的方法
     *
     * @param annotationType
     * @param context
     * @param msg
     */
    public static void getToastMethod(Class<? extends Annotation> annotationType, Context context, String msg) {
        Class var = RetrofitHttp.Configure.get().getToastClass();
        if (var == null) {
            var = ToastAutoDefine.class;
        }
        getMethod(null, var, annotationType, context, msg);
    }

    /**
     * 获取对象中的的方法
     *
     * @param var
     * @param annotationType
     * @param objs
     */
    public static void getMethod(Object obj, Class var, Class<? extends Annotation> annotationType, Object... objs) {
        Method[] methods = var.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationType)) {
                Annotation[][] annotationss = method.getParameterAnnotations();
                Type[] types = method.getGenericParameterTypes();
                try {
                    method.invoke(obj != null ? obj : var, getParameterValue(annotationss, types, objs));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LogUtils.w(e);
                }
            }
        }
    }

    /**
     * 创建构造器
     *
     * @param var
     * @param objs
     * @return
     */
    public static Object newConstructor(Class var, Object... objs) {
        Constructor[] constructors = var.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(Constructors.class)) {
                Annotation[][] annotationss = constructor.getParameterAnnotations();
                Type[] types = constructor.getGenericParameterTypes();
                try {
                    return constructor.newInstance(getParameterValue(annotationss, types, objs));
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    LogUtils.w(e);
                }
            }
        }
        return null;
    }

    /**
     * 获取参数并设置
     *
     * @param annotationss
     * @param types
     * @param objs
     * @return
     */
    private static Object[] getParameterValue(Annotation[][] annotationss, Type[] types, Object... objs) {
        Object[] objects = new Object[types.length];
        for (int i = 0; i < annotationss.length; i++) {
            Type type = types[i];
            Annotation[] annotations = annotationss[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof ContextParameter && type == Context.class) {
                    objects[i] = objs[0];
                }

                if (annotation instanceof MsgParameter && type == String.class ||
                        annotation instanceof CabcelbleParameter && (type == Boolean.class || type == boolean.class)) {
                    objects[i] = objs[1];
                }

                if (annotation instanceof ProgressParameter && type == ProgressDialogObserver.class) {
                    objects[i] = objs[2];
                }

            }
        }
        return objects;
    }
}
