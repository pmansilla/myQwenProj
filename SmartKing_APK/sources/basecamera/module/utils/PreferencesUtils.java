package basecamera.module.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class PreferencesUtils {
    public static String PREFERENCE_NAME = "FIT_PREFERENCE";
    private static PreferencesUtils instance;

    private PreferencesUtils() {
    }

    public static PreferencesUtils getInstance() {
        if (instance == null) {
            instance = new PreferencesUtils();
        }
        return instance;
    }

    public boolean clear(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.clear();
        edit.commit();
        return true;
    }

    public boolean deleteObject(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.remove(str);
        return edit.commit();
    }

    public int getInt(Context context, String str) {
        return getInt(context, str, -1);
    }

    public int getInt(Context context, String str, int i) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getInt(str, i);
    }

    public String getString(Context context, String str) {
        return getString(context, str, "");
    }

    public String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString(str, str2);
    }

    public boolean putInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putInt(str, i);
        return edit.commit();
    }

    public boolean putString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        edit.putString(str, str2);
        return edit.commit();
    }
}
