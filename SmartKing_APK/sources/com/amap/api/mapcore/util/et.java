package com.amap.api.mapcore.util;

import android.content.Context;
import android.util.Log;
import com.litesuits.orm.db.assit.SQLBuilder;

/* compiled from: AuthLogUtil.java */
/* loaded from: classes.dex */
public class et {
    static String a;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 80; i++) {
            sb.append("=");
        }
        a = sb.toString();
    }

    public static void a() {
        b(a);
        b("自6.6.0开始使用新版样式，旧版样式无法在新版接口setCustomMapStyle(CustomMapStyleOptions options)中使用，请到官网(lbs.amap.com)更新新版样式文件");
        b(a);
    }

    public static void a(Context context) {
        b(a);
        if (context != null) {
            a("key:" + hd.f(context));
        }
        b("鉴权失败，当前key没有自定义纹理和在线拉去样式的使用权限，自定义纹理和在线拉去样式相关内容，将不会呈现！");
        b(a);
    }

    static void a(String str) {
        if (str.length() >= 78) {
            b("|" + str.substring(0, 78) + "|");
            a(str.substring(78));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        sb.append(str);
        for (int i = 0; i < 78 - str.length(); i++) {
            sb.append(SQLBuilder.BLANK);
        }
        sb.append("|");
        b(sb.toString());
    }

    private static void b(String str) {
        Log.i("authErrLog", str);
    }
}
