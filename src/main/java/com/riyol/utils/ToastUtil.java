package com.riyol.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by riyol on 17/12/8.
 */

public class ToastUtil {
    private static CharSequence sPreviousMsg;

    private static Toast sToast;

    private static long sPreviousTime;

    private static long sNextTime;

    public static void showToast(Context context, CharSequence msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            sToast.show();
            sPreviousTime = System.currentTimeMillis();
        } else {
            sNextTime = System.currentTimeMillis();
            if (msg.equals(sPreviousMsg)) {
                if (sNextTime - sPreviousTime > Toast.LENGTH_SHORT) {
                    sToast.show();
                }
            } else {
                sPreviousMsg = msg;
                sToast.setText(msg);
                sToast.show();
            }
        }
        sPreviousTime = sNextTime;
    }

    public static void showToast(Context context, @StringRes int resId) {
        showToast(context, context.getString(resId));
    }
}
