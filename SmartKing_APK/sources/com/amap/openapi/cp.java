package com.amap.openapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.amap.location.common.log.ALLog;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.Calendar;

/* compiled from: SharedPrefUtil.java */
/* loaded from: classes.dex */
public class cp {
    private static SharedPreferences a = null;
    private static int b = -1;
    private static int c = -1;
    private static int d = -1;
    private static int e = -1;
    private static int f = -1;
    private static int g = -1;
    private static long h = -1;

    public static boolean a(Context context) {
        try {
            e(context);
            if (d == -1) {
                d = a.getInt("first_downloaded", 0);
            }
            return d == 1;
        } catch (Throwable unused) {
            return true;
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean a(Context context, int i) {
        int i2;
        try {
            e(context);
            if (b == -1) {
                b = a.getInt("last_upload_time", 0);
            }
            if (c == -1) {
                c = a.getInt("uploaded_count", 0);
            }
            i2 = Calendar.getInstance().get(6);
            ALLog.trace("@_18_8_@", "@_18_8_1_@(" + b + "," + i2 + "," + c + SQLBuilder.PARENTHESES_RIGHT);
        } catch (Throwable unused) {
        }
        if (i2 == b) {
            return c < i;
        }
        c = 0;
        SharedPreferences.Editor edit = a.edit();
        edit.putInt("uploaded_count", c);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
        return true;
    }

    @SuppressLint({"NewApi"})
    public static boolean a(Context context, long j) {
        try {
            e(context);
            if (h == -1) {
                h = a.getLong("config_time", 0L);
            }
            boolean z = h != j;
            if (z) {
                h = j;
                SharedPreferences.Editor edit = a.edit();
                edit.putLong("config_time", h);
                if (Build.VERSION.SDK_INT >= 9) {
                    edit.apply();
                } else {
                    edit.commit();
                }
            }
            return z;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static int b(Context context, int i) {
        try {
            e(context);
            if (c == -1) {
                c = a.getInt("uploaded_count", 0);
            }
            return Math.max(0, i - c);
        } catch (Throwable unused) {
            return 0;
        }
    }

    @SuppressLint({"NewApi"})
    public static void b(Context context) {
        try {
            e(context);
            d = 1;
            SharedPreferences.Editor edit = a.edit();
            edit.putInt("first_downloaded", d);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        } catch (Throwable unused) {
        }
    }

    @SuppressLint({"NewApi"})
    public static void c(Context context) {
        try {
            e(context);
            if (f == -1) {
                f = a.getInt("downloaded_count", 0);
            }
            f++;
            SharedPreferences.Editor edit = a.edit();
            edit.putInt("downloaded_count", f);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        } catch (Throwable unused) {
        }
    }

    @SuppressLint({"NewApi"})
    public static void c(Context context, int i) {
        try {
            e(context);
            int i2 = Calendar.getInstance().get(6);
            ALLog.trace("@_18_8_@", "@_18_8_2_@(" + i2 + "," + i + SQLBuilder.PARENTHESES_RIGHT);
            b = i2;
            c = c + i;
            SharedPreferences.Editor edit = a.edit();
            edit.putInt("last_upload_time", b);
            edit.putInt("uploaded_count", c);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        } catch (Throwable unused) {
        }
    }

    @SuppressLint({"NewApi"})
    public static void d(Context context) {
        try {
            e(context);
            if (g == -1) {
                g = a.getInt("nonwifi_downloaded_count", 0);
            }
            g++;
            SharedPreferences.Editor edit = a.edit();
            edit.putInt("nonwifi_downloaded_count", g);
            if (Build.VERSION.SDK_INT >= 9) {
                edit.apply();
            } else {
                edit.commit();
            }
        } catch (Throwable unused) {
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean d(Context context, int i) {
        int i2;
        try {
            e(context);
            if (e == -1) {
                e = a.getInt("last_download_time", 0);
            }
            i2 = Calendar.getInstance().get(6);
            ALLog.trace("@_18_8_@", "@_18_8_3_@(" + e + "," + i2 + SQLBuilder.PARENTHESES_RIGHT);
        } catch (Throwable unused) {
        }
        if (i2 == e) {
            if (f == -1) {
                f = a.getInt("downloaded_count", 0);
            }
            ALLog.trace("@_18_8_@", "@_18_8_4_@" + f);
            return f < i;
        }
        e = i2;
        f = 0;
        g = 0;
        SharedPreferences.Editor edit = a.edit();
        edit.putInt("last_download_time", e);
        edit.putInt("downloaded_count", f);
        edit.putInt("nonwifi_downloaded_count", g);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
        return true;
    }

    private static void e(Context context) {
        if (a == null) {
            a = context.getSharedPreferences("location_offline", 0);
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean e(Context context, int i) {
        int i2;
        try {
            e(context);
            if (e == -1) {
                e = a.getInt("last_download_time", 0);
            }
            i2 = Calendar.getInstance().get(6);
            ALLog.trace("@_18_8_@", "@_18_8_3_@(" + e + "," + i2 + SQLBuilder.PARENTHESES_RIGHT);
        } catch (Throwable unused) {
        }
        if (i2 == e) {
            if (g == -1) {
                g = a.getInt("nonwifi_downloaded_count", 0);
            }
            ALLog.trace("@_18_8_@", "@_18_8_5_@" + g);
            return g < i;
        }
        e = i2;
        f = 0;
        g = 0;
        SharedPreferences.Editor edit = a.edit();
        edit.putInt("last_download_time", e);
        edit.putInt("downloaded_count", f);
        edit.putInt("nonwifi_downloaded_count", g);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
        return true;
    }
}
