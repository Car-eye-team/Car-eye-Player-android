package org.careye.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * SP 工具类
 * panzq
 * 20180611
 */
public class SPUtil {


    private static final String SP_S = "Car-eye";

    public static String SP_USERNAME = "username";
    public static String SP_PASSWORD = "password";

    public static void putString(Context context, String key, String value){
        if (context == null || TextUtils.isEmpty(key))
            return;

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_S, Context.MODE_PRIVATE);
        if (sp == null)
            return;

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void putInt(Context context, String key, int value) {
        if (context == null || TextUtils.isEmpty(key))
            return;

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_S, Context.MODE_PRIVATE);
        if (sp == null)
            return;

        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (context == null || TextUtils.isEmpty(key))
            return;

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_S, Context.MODE_PRIVATE);
        if (sp == null)
            return;

        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    public static int getInt(Context context, String key, int defValue) {
        if (context == null || TextUtils.isEmpty(key)) {
            return 0;
        }

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_S, Context.MODE_PRIVATE);
        if (sp == null) {
            return 0;
        }
        return sp.getInt(key, defValue);
    }


    public static boolean getBoolean(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return false;
        }

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_S, Context.MODE_PRIVATE);
        if (sp == null) {
            return false;
        }
        boolean ret = sp.getBoolean(key, false);
        return ret;
    }

    public static String getString(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return "";
        }

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_S, Context.MODE_PRIVATE);
        if (sp == null) {
            return "";
        }
        return sp.getString(key, "");
    }
}
