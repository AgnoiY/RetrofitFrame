package com.cjy.retrofitlibrary.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjy.retrofitlibrary.R;
import com.cjy.retrofitlibrary.annotation.toast.ToastCancel;
import com.cjy.retrofitlibrary.annotation.toast.ToastContext;
import com.cjy.retrofitlibrary.annotation.toast.ToastFail;
import com.cjy.retrofitlibrary.annotation.toast.ToastInfo;
import com.cjy.retrofitlibrary.annotation.toast.ToastMsg;
import com.cjy.retrofitlibrary.annotation.toast.ToastSuccess;

/**
 * <提示信息弹窗>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class AutoDefineToast {

    AutoDefineToast() {
        throw new IllegalStateException("AutoDefineToast class");
    }

    private static Toast toast;

    /**
     * 成功提示信息
     *
     * @param context
     * @param msg
     */
    @ToastSuccess
    public static void showSuccessToast(@ToastContext Context context, @ToastMsg String msg) {
        View view = View.inflate(context, R.layout.toast_auto_define, null);
        ImageView toastAutoDefineIv = view.findViewById(R.id.toast_auto_define_iv);
        TextView toastAutoDefineTv = view.findViewById(R.id.toast_auto_define_tv);
        toastAutoDefineTv.setText(msg);
        toastAutoDefineIv.setBackgroundResource(R.mipmap.ic_notify_success);
        toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 错误提示信息
     *
     * @param context
     * @param msg
     */
    @ToastFail
    public static void showFailToast(@ToastContext Context context, @ToastMsg String msg) {
        View view = View.inflate(context, R.layout.toast_auto_define, null);
        ImageView toastAutoDefineIv = view.findViewById(R.id.toast_auto_define_iv);
        TextView toastAutoDefineTv = view.findViewById(R.id.toast_auto_define_tv);
        toastAutoDefineTv.setText(msg);
        toastAutoDefineIv.setBackgroundResource(R.mipmap.ic_notify_fail);
        toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 提示信息
     *
     * @param context
     * @param msg
     */
    @ToastInfo
    public static void showInfoToast(@ToastContext Context context, @ToastMsg String msg) {
        View view = View.inflate(context, R.layout.toast_auto_define, null);
        ImageView toastAutoDefineIv = view.findViewById(R.id.toast_auto_define_iv);
        TextView toastAutoDefineTv = view.findViewById(R.id.toast_auto_define_tv);
        toastAutoDefineTv.setText(msg);
        toastAutoDefineIv.setBackgroundResource(R.mipmap.ic_notify_info);
        toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 清空Toast
     */
    @ToastCancel
    public static void destory() {
        if (toast != null)
            toast.cancel();
        toast = null;
    }

}
