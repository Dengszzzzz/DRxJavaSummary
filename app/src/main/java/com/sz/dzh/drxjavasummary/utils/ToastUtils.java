package com.sz.dzh.drxjavasummary.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.sz.dzh.drxjavasummary.base.App;


/**
 * Created by dengzh on 2016/2/15 0015.
 */
public class ToastUtils {

    public static void showToast(String text) {
        showToastAvoidRepeated(text);
    }

    /**
     * 避免Toast重复显示
     * 之前显示的内容
     */
    private static String oldMsg;
    private static Toast toast   = null;
    private static long  oneTime = 0;
    private static long  twoTime = 0;
    public static void showToastAvoidRepeated(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(App.ctx, null, Toast.LENGTH_SHORT);
            toast.setText(message);  //先置null，在设值，解决小米带appName问题
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
