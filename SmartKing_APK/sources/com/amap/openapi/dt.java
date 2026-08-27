package com.amap.openapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import com.amap.location.common.HeaderConfig;
import com.amap.location.uptunnel.core.db.DBProvider;
import java.util.Calendar;

/* compiled from: UpTunnelContext.java */
/* loaded from: classes.dex */
public class dt {
    private Context a;
    private DBProvider b;
    private SharedPreferences c;
    private Calendar d;

    public dt(Context context) {
        this.a = context;
        try {
            this.d = Calendar.getInstance();
            this.c = context.getSharedPreferences(HeaderConfig.getProcessName() + "_tunnel", 0);
            this.b = DBProvider.a(context);
        } catch (Throwable unused) {
        }
    }

    public static Uri b(int i) {
        String str;
        switch (i) {
            case 1:
                str = "count";
                break;
            case 2:
                str = "event";
                break;
            case 3:
                str = "key_log";
                break;
            case 4:
                str = "log";
                break;
            case 5:
                str = "data_block";
                break;
            default:
                return null;
        }
        return DBProvider.a(str);
    }

    public static String c(int i) {
        switch (i) {
            case 1:
                return "count";
            case 2:
                return "event";
            case 3:
                return "key_log";
            case 4:
                return "log";
            case 5:
                return "data_block";
            default:
                return "";
        }
    }

    public synchronized long a(int i, int i2) {
        int i3 = this.c.getInt("last_upload_day_" + i, -1);
        this.d.setTimeInMillis(System.currentTimeMillis());
        int i4 = this.d.get(6);
        if (i4 == i3) {
            return this.c.getLong("uploaded_size_" + i + "_" + i2, 0L);
        }
        SharedPreferences.Editor edit = this.c.edit();
        edit.putInt("last_upload_day_" + i, i4);
        edit.putLong("uploaded_size_" + i + "_" + i2, 0L);
        StringBuilder sb = new StringBuilder("uploaded_size_");
        sb.append(i);
        sb.append("_");
        sb.append(i2 == 0 ? 1 : 0);
        edit.putLong(sb.toString(), 0L);
        edit.apply();
        return 0L;
    }

    public synchronized long a(int i, int i2, long j) {
        int i3 = this.c.getInt("last_upload_day_" + i, -1);
        this.d.setTimeInMillis(System.currentTimeMillis());
        int i4 = this.d.get(6);
        if (i4 == i3) {
            long j2 = this.c.getLong("uploaded_size_" + i + "_" + i2, 0L);
            long j3 = j2 + j;
            this.c.edit().putLong("uploaded_size_" + i + "_" + i2, j3).apply();
            return j3;
        }
        SharedPreferences.Editor edit = this.c.edit();
        edit.putInt("last_upload_day_" + i, i4);
        edit.putLong("uploaded_size_" + i + "_" + i2, j);
        StringBuilder sb = new StringBuilder("uploaded_size_");
        sb.append(i);
        sb.append("_");
        sb.append(i2 == 0 ? 1 : 0);
        edit.putLong(sb.toString(), 0L);
        edit.apply();
        return j;
    }

    public synchronized Context a() {
        return this.a;
    }

    public synchronized String a(int i) {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append(dl.a ? "http://aps.testing.amap.com/dataPipeline/uploadData" : "http://cgicol.amap.com/dataPipeline/uploadData");
        sb.append("?");
        sb.append("channel=");
        sb.append(i == 1 ? "statistics" : "report");
        sb.append("&version=v1");
        return sb.toString();
    }

    public synchronized DBProvider b() {
        return this.b;
    }

    public synchronized int c() {
        return com.amap.location.common.util.f.a(this.a);
    }
}
