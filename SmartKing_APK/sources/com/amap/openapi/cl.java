package com.amap.openapi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.amap.location.common.log.ALLog;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: EventManager.java */
/* loaded from: classes.dex */
public class cl {
    private static volatile cl a;
    private a b;
    private Object c = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EventManager.java */
    /* loaded from: classes.dex */
    public static class a extends SQLiteOpenHelper {
        a(Context context) {
            super(context, "OffEvent.db", (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            cm.a(sQLiteDatabase);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }
    }

    private cl() {
    }

    public static cl a() {
        if (a == null) {
            synchronized (cl.class) {
                if (a == null) {
                    a = new cl();
                }
            }
        }
        return a;
    }

    private String a(int i) {
        SQLiteDatabase sQLiteDatabase;
        Cursor cursor;
        String[] split;
        StringBuilder sb = new StringBuilder();
        ArrayList arrayList = new ArrayList();
        Cursor cursor2 = null;
        try {
            sQLiteDatabase = this.b.getWritableDatabase();
        } catch (Exception unused) {
            sQLiteDatabase = null;
        }
        if (sQLiteDatabase == null) {
            return sb.toString();
        }
        try {
            try {
                sQLiteDatabase.beginTransaction();
                cursor = sQLiteDatabase.query("ACL", new String[]{cm.a, cm.b}, null, null, null, null, "frequency DESC", String.valueOf(i));
                if (cursor != null) {
                    try {
                        if (cursor.moveToFirst()) {
                            while (!cursor.isAfterLast()) {
                                String string = cursor.getString(0);
                                int i2 = cursor.getInt(1);
                                if (string != null && (split = string.split("_")) != null && (split.length == 3 || split.length == 4)) {
                                    int i3 = split.length == 4 ? 0 : 1;
                                    if (sb.length() != 0) {
                                        sb.append("#");
                                    }
                                    sb.append(i3);
                                    sb.append("|");
                                    sb.append(string);
                                    sb.append("|");
                                    sb.append(i2);
                                    arrayList.add(string);
                                }
                                cursor.moveToNext();
                            }
                            Iterator it = arrayList.iterator();
                            while (it.hasNext()) {
                                sQLiteDatabase.delete("ACL", "id=?", new String[]{(String) it.next()});
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (cursor != null) {
                            try {
                                cursor.close();
                            } catch (Throwable unused2) {
                                throw th;
                            }
                        }
                        sQLiteDatabase.endTransaction();
                        throw th;
                    }
                }
                sQLiteDatabase.setTransactionSuccessful();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th2) {
                th = th2;
            }
            sQLiteDatabase.endTransaction();
            return sb.toString();
        } catch (Throwable th3) {
            th = th3;
            cursor = cursor2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x004d A[Catch: all -> 0x002c, Throwable -> 0x002e, TRY_LEAVE, TryCatch #7 {Throwable -> 0x002e, all -> 0x002c, blocks: (B:19:0x0021, B:21:0x0027, B:7:0x0034, B:17:0x004d), top: B:18:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0034 A[Catch: all -> 0x002c, Throwable -> 0x002e, TryCatch #7 {Throwable -> 0x002e, all -> 0x002c, blocks: (B:19:0x0021, B:21:0x0027, B:7:0x0034, B:17:0x004d), top: B:18:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0069 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(java.lang.String r13) {
        /*
            r12 = this;
            r0 = 0
            com.amap.openapi.cl$a r1 = r12.b     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            android.database.sqlite.SQLiteDatabase r1 = r1.getWritableDatabase()     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            java.lang.String r3 = "ACL"
            r10 = 1
            java.lang.String[] r4 = new java.lang.String[r10]     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            java.lang.String r2 = com.amap.openapi.cm.b     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            r11 = 0
            r4[r11] = r2     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            java.lang.String r5 = "id=?"
            java.lang.String[] r6 = new java.lang.String[r10]     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            r6[r11] = r13     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            r7 = 0
            r8 = 0
            r9 = 0
            r2 = r1
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L6d java.lang.Throwable -> L70
            if (r2 == 0) goto L31
            boolean r3 = r2.moveToFirst()     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            if (r3 == 0) goto L31
            int r3 = r2.getInt(r11)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            goto L32
        L2c:
            r13 = move-exception
            goto L8e
        L2e:
            r13 = move-exception
            r0 = r2
            goto L71
        L31:
            r3 = 0
        L32:
            if (r3 != 0) goto L4d
            android.content.ContentValues r3 = new android.content.ContentValues     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            r3.<init>()     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            java.lang.String r4 = "id"
            r3.put(r4, r13)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            java.lang.String r13 = "frequency"
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            r3.put(r13, r4)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            java.lang.String r13 = "ACL"
            r1.insert(r13, r0, r3)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            goto L67
        L4d:
            android.content.ContentValues r0 = new android.content.ContentValues     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            r0.<init>()     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            java.lang.String r4 = "frequency"
            int r3 = r3 + r10
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            r0.put(r4, r3)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            java.lang.String r3 = "ACL"
            java.lang.String r4 = "id=?"
            java.lang.String[] r5 = new java.lang.String[r10]     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            r5[r11] = r13     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
            r1.update(r3, r0, r4, r5)     // Catch: java.lang.Throwable -> L2c java.lang.Throwable -> L2e
        L67:
            if (r2 == 0) goto L8d
            r2.close()     // Catch: java.lang.Throwable -> L6c
        L6c:
            return
        L6d:
            r13 = move-exception
            r2 = r0
            goto L8e
        L70:
            r13 = move-exception
        L71:
            java.lang.String r1 = "@_18_7_@"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6d
            java.lang.String r3 = "@_18_7_4_@"
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L6d
            java.lang.String r13 = android.util.Log.getStackTraceString(r13)     // Catch: java.lang.Throwable -> L6d
            r2.append(r13)     // Catch: java.lang.Throwable -> L6d
            java.lang.String r13 = r2.toString()     // Catch: java.lang.Throwable -> L6d
            com.amap.location.common.log.ALLog.trace(r1, r13)     // Catch: java.lang.Throwable -> L6d
            if (r0 == 0) goto L8d
            r0.close()     // Catch: java.lang.Throwable -> L8d
        L8d:
            return
        L8e:
            if (r2 == 0) goto L93
            r2.close()     // Catch: java.lang.Throwable -> L93
        L93:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.cl.a(java.lang.String):void");
    }

    private void b(Context context) {
        if (this.b != null) {
            return;
        }
        this.b = new a(context);
    }

    public void a(Context context) {
        String a2;
        synchronized (this.c) {
            b(context);
        }
        if (TextUtils.isEmpty(com.amap.location.common.a.c(context))) {
            ALLog.trace("@_18_7_@", "@_18_7_5_@");
            return;
        }
        boolean a3 = cp.a(context, 500);
        int b = cp.b(context, 500);
        ALLog.trace("@_18_7_@", "@_18_7_1_@(" + a3 + "," + b + SQLBuilder.PARENTHESES_RIGHT);
        if (!a3 || b <= 0 || (a2 = a(b)) == null || a2.length() <= 0) {
            return;
        }
        com.amap.location.offline.upload.a.a(800002, a2.getBytes());
        int i = 0;
        String[] split = a2.split("#");
        if (split != null && split.length > 0) {
            i = split.length;
        }
        cp.c(context, i);
        ALLog.trace("@_18_7_@", "@_18_7_2_@" + i);
    }

    public void a(Context context, bs bsVar) {
        synchronized (this.c) {
            b(context);
        }
        if (TextUtils.isEmpty(bsVar.f)) {
            return;
        }
        a(bsVar.f.replace(":", "_"));
    }
}
