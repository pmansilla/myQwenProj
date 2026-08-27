package com.amap.openapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.security.Core;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.model.ConnectionModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: OfflineDatabase.java */
/* loaded from: classes.dex */
public class by {
    private static final String[] a = {ConnectionModel.ID, "lat", "lng", "acc", "conf", "timestamp"};
    private static final String[] b = {ConnectionModel.ID, "originid", "frequency"};
    private static volatile by c = null;
    private bx d;
    private cb e;
    private ReadWriteLock f = new ReentrantReadWriteLock();

    private by(Context context) {
        this.d = new bx(context);
        this.e = new cb(context);
    }

    private int a(SQLiteDatabase sQLiteDatabase, String str, long j) {
        Cursor cursor;
        Cursor cursor2 = null;
        try {
            cursor = sQLiteDatabase.query(str, new String[]{"frequency"}, "id=?", new String[]{String.valueOf(j)}, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int i = cursor.getInt(0);
                        if (cursor != null) {
                            try {
                                cursor.close();
                            } catch (Throwable unused) {
                            }
                        }
                        return i;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor2 = cursor;
                    if (cursor2 != null) {
                        try {
                            cursor2.close();
                        } catch (Throwable unused2) {
                        }
                    }
                    throw th;
                }
            }
            if (cursor == null) {
                return -1;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            cursor.close();
        } catch (Throwable unused3) {
            return -1;
        }
    }

    private ContentValues a(ContentValues contentValues, long j, String str, long j2, int i) {
        contentValues.clear();
        contentValues.put(ConnectionModel.ID, Long.valueOf(j));
        if (TextUtils.isEmpty(str)) {
            contentValues.put("originid", Long.valueOf(j2));
        } else {
            contentValues.put("originid", str);
        }
        contentValues.put("frequency", Integer.valueOf(i));
        return contentValues;
    }

    private ContentValues a(ContentValues contentValues, ci ciVar) {
        contentValues.clear();
        contentValues.put(ConnectionModel.ID, Long.valueOf(ciVar.a()));
        contentValues.put("lat", Integer.valueOf(ciVar.b()));
        contentValues.put("lng", Integer.valueOf(ciVar.c()));
        contentValues.put("acc", Short.valueOf(ciVar.d()));
        contentValues.put("conf", Byte.valueOf(ciVar.e()));
        contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000));
        return contentValues;
    }

    public static by a(@NonNull Context context) {
        if (c == null) {
            synchronized (by.class) {
                if (c == null) {
                    c = new by(context);
                }
            }
        }
        return c;
    }

    private HashSet<Long> a(List<Long> list) {
        HashSet<Long> hashSet = new HashSet<>();
        if (list != null) {
            Iterator<Long> it = list.iterator();
            while (it.hasNext()) {
                hashSet.add(Long.valueOf(cn.a(it.next().longValue())));
            }
        }
        return hashSet;
    }

    private void a(SQLiteDatabase sQLiteDatabase, String str, ContentValues contentValues) {
        try {
            sQLiteDatabase.replace(str, null, contentValues);
        } catch (Throwable unused) {
        }
    }

    private void a(SQLiteDatabase sQLiteDatabase, String str, ContentValues contentValues, boolean z) {
        sQLiteDatabase.insertWithOnConflict(str, null, contentValues, z ? 4 : 5);
    }

    private void a(SQLiteDatabase sQLiteDatabase, String str, HashSet<Long> hashSet, ContentValues contentValues) {
        contentValues.clear();
        contentValues.put("lat", (Integer) 0);
        contentValues.put("lng", (Integer) 0);
        contentValues.put("acc", (Integer) 0);
        contentValues.put("conf", (Integer) (-1));
        contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000));
        Iterator<Long> it = hashSet.iterator();
        while (it.hasNext()) {
            contentValues.put(ConnectionModel.ID, Long.valueOf(it.next().longValue()));
            a(sQLiteDatabase, str, contentValues);
        }
    }

    private void a(SQLiteDatabase sQLiteDatabase, HashSet<Long> hashSet, HashSet<Long> hashSet2) {
        if (hashSet != null) {
            Iterator<Long> it = hashSet.iterator();
            while (it.hasNext()) {
                sQLiteDatabase.delete("CL", "id=?", new String[]{String.valueOf(it.next().longValue())});
            }
        }
        if (hashSet2 != null) {
            Iterator<Long> it2 = hashSet2.iterator();
            while (it2.hasNext()) {
                sQLiteDatabase.delete("AP", "id=?", new String[]{String.valueOf(it2.next())});
            }
        }
    }

    private void a(String str, ContentValues contentValues, long j) {
        contentValues.clear();
        contentValues.put("conf", (Integer) 0);
        this.d.getWritableDatabase().update(str, contentValues, "id=?", new String[]{String.valueOf(j)});
    }

    private boolean a(SQLiteDatabase sQLiteDatabase, String str, long j, ContentValues contentValues) {
        int a2 = a(sQLiteDatabase, str, j);
        if (a2 < 0) {
            return false;
        }
        contentValues.clear();
        contentValues.put("frequency", Integer.valueOf(a2 + 1));
        sQLiteDatabase.update(str, contentValues, "id=?", new String[]{String.valueOf(j)});
        return true;
    }

    private HashSet<Long> b(List<String> list) {
        HashSet<Long> hashSet = new HashSet<>();
        if (list != null) {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                long a2 = cn.a(it.next());
                if (a2 != -1) {
                    hashSet.add(Long.valueOf(a2));
                }
            }
        }
        return hashSet;
    }

    private void b(SQLiteDatabase sQLiteDatabase, String str, long j, ContentValues contentValues) {
        try {
            contentValues.clear();
            contentValues.put("time", Long.valueOf(System.currentTimeMillis() / 1000));
            sQLiteDatabase.update(str, contentValues, "id=?", new String[]{String.valueOf(j)});
        } catch (Throwable unused) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x00af  */
    /* JADX WARN: Type inference failed for: r6v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.amap.openapi.bs a(java.lang.String r25, long r26) {
        /*
            Method dump skipped, instructions count: 199
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.a(java.lang.String, long):com.amap.openapi.bs");
    }

    public List<String> a(int i, int i2) {
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                try {
                    Cursor query = this.e.getReadableDatabase().query("CL", b, "frequency>=" + i + " AND time<" + ((System.currentTimeMillis() / 1000) - 86400), null, null, null, "frequency DESC", String.valueOf(i2));
                    if (query != null) {
                        try {
                            if (query.moveToFirst()) {
                                while (!query.isAfterLast()) {
                                    arrayList.add(query.getString(1));
                                    query.moveToNext();
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            cursor = query;
                            if (cursor != null) {
                                try {
                                    cursor.close();
                                } catch (Throwable unused) {
                                }
                            }
                            throw th;
                        }
                    }
                    if (query != null) {
                        query.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable unused2) {
        }
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00f6 A[Catch: Throwable -> 0x00fc, TryCatch #1 {Throwable -> 0x00fc, blocks: (B:38:0x00f1, B:31:0x00f6, B:32:0x00f9), top: B:37:0x00f1 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00f1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0105 A[Catch: Throwable -> 0x010b, TryCatch #0 {Throwable -> 0x010b, blocks: (B:49:0x0100, B:43:0x0105, B:44:0x0108), top: B:48:0x0100 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0100 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a() {
        /*
            Method dump skipped, instructions count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.a():void");
    }

    public void a(bs bsVar) {
        long j = bsVar.g;
        String str = bsVar.f;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        this.f.readLock().lock();
        try {
            try {
                SQLiteDatabase writableDatabase = this.d.getWritableDatabase();
                SQLiteDatabase writableDatabase2 = this.e.getWritableDatabase();
                if (bsVar.a) {
                    if (bsVar.e > 60) {
                        a(writableDatabase, "CL", j, contentValues);
                    }
                    if (bsVar.h) {
                        a(contentValues, j, str, 0L, 100000);
                        a(writableDatabase2, "CL", contentValues, true);
                    }
                } else {
                    a(contentValues, j, str, 0L, 0);
                    a(writableDatabase2, "CL", contentValues, true);
                    a(writableDatabase2, "CL", j, contentValues);
                }
            } catch (Throwable th) {
                ALLog.trace("@_18_4_@", "@_18_4_5_@" + Log.getStackTraceString(th));
            }
        } finally {
            this.f.readLock().unlock();
        }
    }

    public void a(bu buVar) {
        SQLiteDatabase sQLiteDatabase;
        if (buVar.b == null || buVar.b.size() <= 0) {
            return;
        }
        SQLiteDatabase sQLiteDatabase2 = null;
        try {
            sQLiteDatabase = this.d.getWritableDatabase();
            try {
                sQLiteDatabase2 = this.e.getWritableDatabase();
            } catch (Throwable unused) {
            }
        } catch (Throwable unused2) {
            sQLiteDatabase = null;
        }
        if (sQLiteDatabase == null || sQLiteDatabase2 == null) {
            return;
        }
        new StringBuilder();
        new StringBuilder();
        new StringBuilder();
        Iterator<Map.Entry<Long, bt>> it = buVar.b.entrySet().iterator();
        ContentValues contentValues = new ContentValues();
        this.f.readLock().lock();
        try {
            try {
                try {
                    sQLiteDatabase.beginTransaction();
                    sQLiteDatabase2.beginTransaction();
                    while (it.hasNext()) {
                        bt value = it.next().getValue();
                        if (value != null) {
                            if (value.d) {
                                if (value.g > 60) {
                                    a(sQLiteDatabase, "AP", value.a, contentValues);
                                }
                                if (value.h) {
                                    a(contentValues, value.a, null, value.b, 100000);
                                    a(sQLiteDatabase2, "AP", contentValues, true);
                                }
                            } else {
                                a(contentValues, value.a, null, value.b, 0);
                                a(sQLiteDatabase2, "AP", contentValues, true);
                                a(sQLiteDatabase2, "AP", value.a, contentValues);
                            }
                        }
                    }
                    sQLiteDatabase.setTransactionSuccessful();
                    sQLiteDatabase2.setTransactionSuccessful();
                    sQLiteDatabase.endTransaction();
                } catch (Throwable th) {
                    try {
                        sQLiteDatabase.endTransaction();
                        sQLiteDatabase2.endTransaction();
                    } catch (Throwable unused3) {
                    }
                    this.f.readLock().unlock();
                    throw th;
                }
            } catch (Throwable th2) {
                ALLog.trace("@_18_4_@", "@_18_4_6_@" + Log.getStackTraceString(th2));
                sQLiteDatabase.endTransaction();
            }
            sQLiteDatabase2.endTransaction();
        } catch (Throwable unused4) {
        }
        this.f.readLock().unlock();
    }

    public void a(bu buVar, AmapLoc amapLoc) {
        SQLiteDatabase sQLiteDatabase;
        SQLiteDatabase sQLiteDatabase2;
        SQLiteDatabase sQLiteDatabase3;
        try {
            sQLiteDatabase = this.d.getWritableDatabase();
            try {
                sQLiteDatabase3 = sQLiteDatabase;
                sQLiteDatabase2 = this.e.getWritableDatabase();
            } catch (Throwable unused) {
                sQLiteDatabase2 = null;
                sQLiteDatabase3 = sQLiteDatabase;
                if (sQLiteDatabase3 != null) {
                    return;
                } else {
                    return;
                }
            }
        } catch (Throwable unused2) {
            sQLiteDatabase = null;
        }
        if (sQLiteDatabase3 != null || sQLiteDatabase2 == null) {
            return;
        }
        new StringBuilder();
        Iterator<Map.Entry<Long, bt>> it = buVar.b.entrySet().iterator();
        ContentValues contentValues = new ContentValues();
        try {
            try {
                sQLiteDatabase3.beginTransaction();
                sQLiteDatabase2.beginTransaction();
                while (it.hasNext()) {
                    bt value = it.next().getValue();
                    if (value != null && value.d && value.g > 60 && Core.gd(value.e, value.f, amapLoc.getLat(), amapLoc.getLon()) > 100.0d) {
                        a("AP", contentValues, value.a);
                        a(contentValues, value.a, null, value.b, 100000);
                        a(sQLiteDatabase2, "AP", contentValues, false);
                    }
                }
                sQLiteDatabase3.setTransactionSuccessful();
                sQLiteDatabase2.setTransactionSuccessful();
                try {
                    sQLiteDatabase3.endTransaction();
                    sQLiteDatabase2.endTransaction();
                } catch (Throwable unused3) {
                }
            } catch (Throwable th) {
                try {
                    sQLiteDatabase3.endTransaction();
                    sQLiteDatabase2.endTransaction();
                } catch (Throwable unused4) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            ALLog.trace("@_18_4_@", "@_18_4_8_@" + Log.getStackTraceString(th2));
            try {
                sQLiteDatabase3.endTransaction();
                sQLiteDatabase2.endTransaction();
            } catch (Throwable unused5) {
            }
        }
    }

    public void a(ck ckVar) {
        SQLiteDatabase sQLiteDatabase;
        try {
            sQLiteDatabase = this.d.getWritableDatabase();
        } catch (Throwable unused) {
            sQLiteDatabase = null;
        }
        if (sQLiteDatabase == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        try {
            try {
                sQLiteDatabase.beginTransaction();
                for (int i = 0; i < ckVar.b(); i++) {
                    ci b2 = ckVar.b(i);
                    if (b2 != null) {
                        a(contentValues, b2);
                        a(sQLiteDatabase, "AP", contentValues);
                    }
                }
                for (int i2 = 0; i2 < ckVar.a(); i2++) {
                    ci a2 = ckVar.a(i2);
                    if (a2 != null) {
                        a(contentValues, a2);
                        a(sQLiteDatabase, "CL", contentValues);
                    }
                }
                sQLiteDatabase.setTransactionSuccessful();
                try {
                    sQLiteDatabase.endTransaction();
                } catch (Throwable unused2) {
                }
            } catch (Throwable th) {
                ALLog.trace("@_18_4_@", "@_18_4_9_@" + Log.getStackTraceString(th));
                try {
                    sQLiteDatabase.endTransaction();
                } catch (Throwable unused3) {
                }
            }
        } catch (Throwable th2) {
            try {
                sQLiteDatabase.endTransaction();
            } catch (Throwable unused4) {
            }
            throw th2;
        }
    }

    public void a(ck ckVar, List<Long> list, List<String> list2, Context context) {
        SQLiteDatabase sQLiteDatabase;
        SQLiteDatabase sQLiteDatabase2;
        SQLiteDatabase sQLiteDatabase3;
        try {
            sQLiteDatabase = this.d.getWritableDatabase();
            try {
                sQLiteDatabase3 = sQLiteDatabase;
                sQLiteDatabase2 = this.e.getWritableDatabase();
            } catch (Throwable unused) {
                sQLiteDatabase2 = null;
                sQLiteDatabase3 = sQLiteDatabase;
                if (sQLiteDatabase3 != null) {
                    return;
                } else {
                    return;
                }
            }
        } catch (Throwable unused2) {
            sQLiteDatabase = null;
        }
        if (sQLiteDatabase3 != null || sQLiteDatabase2 == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        this.f.writeLock().lock();
        try {
            try {
                try {
                    sQLiteDatabase3.beginTransaction();
                    sQLiteDatabase2.beginTransaction();
                    HashSet<Long> a2 = a(list);
                    HashSet<Long> hashSet = (HashSet) a2.clone();
                    for (int i = 0; i < ckVar.b(); i++) {
                        ci b2 = ckVar.b(i);
                        if (b2 != null) {
                            a2.remove(Long.valueOf(b2.a()));
                            a(contentValues, b2);
                            a(sQLiteDatabase3, "AP", contentValues);
                            if (b2.e() <= 60) {
                                hashSet.remove(Long.valueOf(b2.a()));
                                b(sQLiteDatabase2, "AP", b2.a(), contentValues);
                            }
                        }
                    }
                    a(sQLiteDatabase3, "AP", a2, contentValues);
                    HashSet<Long> b3 = b(list2);
                    HashSet<Long> hashSet2 = (HashSet) b3.clone();
                    for (int i2 = 0; i2 < ckVar.a(); i2++) {
                        ci a3 = ckVar.a(i2);
                        if (a3 != null) {
                            b3.remove(Long.valueOf(a3.a()));
                            a(contentValues, a3);
                            a(sQLiteDatabase3, "CL", contentValues);
                            if (a3.e() <= 60) {
                                hashSet2.remove(Long.valueOf(a3.a()));
                                b(sQLiteDatabase2, "CL", a3.a(), contentValues);
                            }
                        }
                    }
                    a(sQLiteDatabase3, "CL", b3, contentValues);
                    a(sQLiteDatabase2, hashSet2, hashSet);
                    a();
                    sQLiteDatabase3.setTransactionSuccessful();
                    sQLiteDatabase2.setTransactionSuccessful();
                    sQLiteDatabase3.endTransaction();
                } catch (Throwable unused3) {
                }
            } catch (Throwable th) {
                ALLog.trace("@_18_4_@", "@_18_4_10_@" + Log.getStackTraceString(th));
                sQLiteDatabase3.endTransaction();
            }
            sQLiteDatabase2.endTransaction();
            this.f.writeLock().unlock();
        } catch (Throwable th2) {
            try {
                sQLiteDatabase3.endTransaction();
                sQLiteDatabase2.endTransaction();
            } catch (Throwable unused4) {
            }
            this.f.writeLock().unlock();
            throw th2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void a(String str, bu buVar) {
        Cursor cursor;
        Cursor cursor2 = null;
        cursor2 = null;
        cursor2 = null;
        try {
            try {
                SQLiteDatabase readableDatabase = this.d.getReadableDatabase();
                cursor = readableDatabase.query("AP", a, "id IN (" + str + SQLBuilder.PARENTHESES_RIGHT, null, null, null, null);
                if (cursor != null) {
                    try {
                        boolean moveToFirst = cursor.moveToFirst();
                        cursor2 = moveToFirst;
                        if (moveToFirst) {
                            while (!cursor.isAfterLast()) {
                                long j = cursor.getLong(0);
                                long j2 = cursor.getLong(5);
                                if (7776000 + j2 < System.currentTimeMillis() / 1000) {
                                    readableDatabase.delete("AP", "id=?", new String[]{String.valueOf(j)});
                                } else {
                                    int i = cursor.getInt(1);
                                    int i2 = cursor.getInt(2);
                                    int i3 = cursor.getInt(3);
                                    int i4 = cursor.getInt(4);
                                    bt btVar = buVar.b.get(Long.valueOf(j));
                                    if (btVar != null) {
                                        btVar.d = true;
                                        btVar.g = i4;
                                        btVar.e = i;
                                        btVar.f = i2;
                                        if (i4 > 60 && i3 > 0 && i3 < 2000) {
                                            buVar.c++;
                                            StringBuilder sb = buVar.d;
                                            sb.append(btVar.b);
                                            sb.append(";");
                                            StringBuilder sb2 = buVar.e;
                                            sb2.append(i2);
                                            sb2.append(",");
                                            sb2.append(i);
                                            sb2.append(",");
                                            sb2.append(i3);
                                            sb2.append(",");
                                            sb2.append(btVar.c);
                                            sb2.append(";");
                                        }
                                        if (j2 + 604800 < System.currentTimeMillis() / 1000) {
                                            btVar.h = true;
                                        }
                                    }
                                }
                                cursor.moveToNext();
                            }
                            int length = buVar.e.length();
                            cursor2 = length;
                            if (length > 0) {
                                StringBuilder sb3 = buVar.e;
                                sb3.deleteCharAt(buVar.e.length() - 1);
                                cursor2 = sb3;
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor2 = cursor;
                        ALLog.trace("@_18_4_@", "@_18_4_2_@" + Log.getStackTraceString(th));
                        if (cursor2 != null) {
                            try {
                                cursor2.close();
                            } catch (Throwable unused) {
                                return;
                            }
                        }
                    }
                }
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Throwable unused2) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = cursor2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public List<Long> b(int i, int i2) {
        Cursor query;
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                try {
                    query = this.e.getReadableDatabase().query("AP", b, "frequency>=" + i + " AND time<" + ((System.currentTimeMillis() / 1000) - 86400), null, null, null, "frequency DESC", String.valueOf(i2));
                    if (query != null) {
                        try {
                            if (query.moveToFirst()) {
                                while (!query.isAfterLast()) {
                                    arrayList.add(Long.valueOf(query.getLong(1)));
                                    query.moveToNext();
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            cursor = query;
                            if (cursor != null) {
                                try {
                                    cursor.close();
                                } catch (Throwable unused) {
                                }
                            }
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                if (query != null) {
                    query.close();
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable unused2) {
        }
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00c5 A[Catch: Throwable -> 0x00cb, TryCatch #0 {Throwable -> 0x00cb, blocks: (B:39:0x00c0, B:32:0x00c5, B:33:0x00c8), top: B:38:0x00c0 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d6 A[Catch: Throwable -> 0x00dc, TryCatch #3 {Throwable -> 0x00dc, blocks: (B:51:0x00d1, B:45:0x00d6, B:46:0x00d9), top: B:50:0x00d1 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00d1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void b() {
        /*
            Method dump skipped, instructions count: 221
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.by.b():void");
    }

    public void b(bs bsVar) {
        long j = bsVar.g;
        String str = bsVar.f;
        ContentValues contentValues = new ContentValues();
        try {
            a("CL", contentValues, j);
            a(contentValues, j, str, 0L, 100000);
            a(this.e.getWritableDatabase(), "CL", contentValues, false);
        } catch (Throwable th) {
            ALLog.trace("@_18_4_@", "@_18_4_7_@" + Log.getStackTraceString(th));
        }
    }

    public void c() {
        try {
            SQLiteDatabase writableDatabase = this.d.getWritableDatabase();
            SQLiteDatabase writableDatabase2 = this.e.getWritableDatabase();
            writableDatabase.delete("CL", null, null);
            writableDatabase.delete("AP", null, null);
            writableDatabase2.delete("CL", null, null);
            writableDatabase2.delete("AP", null, null);
        } catch (Throwable unused) {
        }
    }
}
