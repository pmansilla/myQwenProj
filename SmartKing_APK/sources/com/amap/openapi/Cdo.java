package com.amap.openapi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.uptunnel.core.db.DBProvider;
import com.loc.fc;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UploadTunnelCmdTask.java */
/* renamed from: com.amap.openapi.do, reason: invalid class name */
/* loaded from: classes.dex */
public class Cdo implements Runnable {
    private dt a;
    private IHttpClient b;
    private dn c;

    public Cdo(dt dtVar, dn dnVar, IHttpClient iHttpClient) {
        this.a = dtVar;
        this.c = dnVar;
        this.b = iHttpClient;
    }

    private void a() {
        fc fcVar = new fc();
        JSONObject jSONObject = new JSONObject();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            jSONObject.put("time", com.amap.location.common.util.b.a(currentTimeMillis, null));
            jSONObject.put(IMAPStore.ID_COMMAND, this.c.a());
        } catch (JSONException unused) {
        }
        int a = bk.a(fcVar, this.a.a());
        int b = dz.b(fcVar, new int[]{dy.a(fcVar, 100003, dy.a(fcVar, jSONObject.toString().getBytes()), currentTimeMillis)});
        dz.a(fcVar);
        dz.a(fcVar, (byte) 1);
        dz.a(fcVar, a);
        dz.c(fcVar, b);
        dz.d(fcVar, dz.b(fcVar));
        ea.a(this.b, this.a.a(2), fcVar.f(), DateUtils.MILLIS_IN_MINUTE);
    }

    @Override // java.lang.Runnable
    public void run() {
        long j;
        long j2;
        long j3;
        long j4;
        Cursor rawQuery;
        DBProvider dBProvider;
        long j5;
        long j6;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        try {
            try {
                int c = this.a.c();
                if (c == -1) {
                    if (arrayList.size() > 0) {
                        while (i < arrayList.size()) {
                            com.amap.location.common.util.e.a((Cursor) arrayList.get(i));
                            i++;
                        }
                        arrayList.clear();
                        return;
                    }
                    return;
                }
                DBProvider b = this.a.b();
                SQLiteDatabase c2 = b.c();
                Uri b2 = dt.b(this.c.b());
                String c3 = dt.c(this.c.b());
                if (b.a(b2) <= 0) {
                    a();
                    if (arrayList.size() > 0) {
                        while (i < arrayList.size()) {
                            com.amap.location.common.util.e.a((Cursor) arrayList.get(i));
                            i++;
                        }
                        arrayList.clear();
                        return;
                    }
                    return;
                }
                String[] strArr = null;
                Cursor rawQuery2 = c2.rawQuery("select max(ID) from " + c3, null);
                arrayList.add(rawQuery2);
                rawQuery2.moveToFirst();
                long j7 = rawQuery2.getLong(0);
                com.amap.location.common.util.e.a(rawQuery2);
                if (c == 1) {
                    j = j7;
                    Cursor b3 = b.b(b2, new String[]{"ID"}, null, null, null, "0,1");
                    arrayList.add(b3);
                    b3.moveToFirst();
                    long j8 = b3.getLong(0);
                    com.amap.location.common.util.e.a(b3);
                    j3 = j8;
                    j2 = 0;
                } else {
                    j = j7;
                    if (c == 0) {
                        j3 = j + 1;
                        long j9 = 0;
                        while (true) {
                            if (j9 >= 400000) {
                                j4 = j9;
                                break;
                            }
                            rawQuery = c2.rawQuery("select min(ID) from (select * from " + c3 + " where id < " + j3 + " order by ID desc limit 0, 50)", strArr);
                            arrayList.add(rawQuery);
                            if (rawQuery == null || !rawQuery.moveToFirst()) {
                                break;
                            }
                            j4 = j9;
                            long j10 = rawQuery.getLong(0);
                            com.amap.location.common.util.e.a(rawQuery);
                            if (j10 <= 0) {
                                break;
                            }
                            Cursor rawQuery3 = c2.rawQuery("select sum(size) from " + c3 + " where ID >= " + j10 + " and ID < " + j3, null);
                            arrayList.add(rawQuery3);
                            rawQuery3.moveToFirst();
                            long j11 = j4 + rawQuery3.getLong(0);
                            com.amap.location.common.util.e.a(rawQuery3);
                            strArr = null;
                            j3 = j10;
                            j9 = j11;
                        }
                        j4 = j9;
                        com.amap.location.common.util.e.a(rawQuery);
                        j2 = 0;
                        if (j4 > 0) {
                        }
                    } else {
                        j2 = 0;
                    }
                    j3 = -1;
                }
                while (j3 > j2 && j >= j3) {
                    ArrayList arrayList2 = new ArrayList();
                    fc fcVar = new fc();
                    Cursor a = b.a(b2, dv.a, " id >= " + j3 + " and id <= " + j, null, null);
                    arrayList.add(a);
                    if (a == null || a.getCount() == 0) {
                        break;
                    }
                    long j12 = j3;
                    long j13 = -1;
                    long j14 = -1;
                    long j15 = j2;
                    while (true) {
                        if (!a.moveToNext()) {
                            dBProvider = b;
                            j5 = j13;
                            j3 = j12;
                            j6 = j14;
                            break;
                        }
                        long j16 = a.getLong(0) + 1;
                        dBProvider = b;
                        int i2 = a.getInt(1);
                        byte[] blob = a.getBlob(2);
                        j6 = a.getLong(3);
                        long j17 = j13 == -1 ? j6 : j13;
                        int i3 = a.getInt(4);
                        arrayList2.add(Integer.valueOf(dy.a(fcVar, i2, dy.a(fcVar, blob), j6)));
                        j15 += i3;
                        if (j15 >= 400000) {
                            j3 = j16;
                            j5 = j17;
                            break;
                        } else {
                            j14 = j6;
                            b = dBProvider;
                            j12 = j16;
                            j13 = j17;
                        }
                    }
                    com.amap.location.common.util.e.a(a);
                    int[] iArr = new int[arrayList2.size()];
                    for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                        iArr[i4] = ((Integer) arrayList2.get(i4)).intValue();
                    }
                    int a2 = bk.a(fcVar, this.a.a());
                    int b4 = dz.b(fcVar, iArr);
                    dz.a(fcVar);
                    dz.a(fcVar, (byte) 1);
                    dz.a(fcVar, a2);
                    dz.c(fcVar, b4);
                    dz.d(fcVar, dz.b(fcVar));
                    if (!ea.a(this.b, this.a.a(this.c.b()), fcVar.f(), 120000)) {
                        dl.a(800001, ("UpTunnel fail,条数是:" + arrayList2.size() + ", 最后一条 id  是:" + j3 + ",第一条时间：" + j5 + ",最后一条时间：" + j6).getBytes());
                    }
                    b = dBProvider;
                }
            } catch (Throwable th) {
                try {
                    dl.a(800001, Log.getStackTraceString(th).getBytes());
                } catch (Exception unused) {
                }
                if (arrayList.size() > 0) {
                    for (int i5 = 0; i5 < arrayList.size(); i5++) {
                        com.amap.location.common.util.e.a((Cursor) arrayList.get(i5));
                    }
                    arrayList.clear();
                }
            }
        } finally {
            if (arrayList.size() > 0) {
                for (int i6 = 0; i6 < arrayList.size(); i6++) {
                    com.amap.location.common.util.e.a((Cursor) arrayList.get(i6));
                }
                arrayList.clear();
            }
        }
    }
}
