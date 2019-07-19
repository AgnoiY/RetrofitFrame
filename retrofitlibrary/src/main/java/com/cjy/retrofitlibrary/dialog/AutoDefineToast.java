package com.cjy.retrofitlibrary.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjy.retrofitlibrary.R;

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

    public static void showSuccessToast(Context context, String msg) {
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

    public static void showFailToast(Context context, String msg) {
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

    public static void showInfoToast(Context context, String msg) {
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
    public static void destory() {
        if (toast != null)
            toast.cancel();
        toast = null;
    }

}
