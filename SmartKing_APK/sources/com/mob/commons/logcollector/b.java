package com.mob.commons.logcollector;

import android.content.ContentValues;
import android.database.Cursor;
import com.mob.tools.MobLog;

/* compiled from: DBProvider.java */
/* loaded from: classes.dex */
public class b {
    private static b b;
    private a a = new a();

    private b() {
    }

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (b == null) {
                b = new b();
            }
            bVar = b;
        }
        return bVar;
    }

    public int a(String str) {
        int i = 0;
        Cursor cursor = null;
        try {
            try {
                Cursor query = this.a.getWritableDatabase().query(str, null, null, null, null, null, null);
                if (query != null) {
                    try {
                        i = query.getCount();
                    } catch (Throwable th) {
                        th = th;
                        cursor = query;
                        MobLog.getInstance().w(th);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return i;
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
        return i;
    }

    public int a(String str, String str2, String[] strArr) {
        int i;
        try {
            i = this.a.getWritableDatabase().delete(str, str2, strArr);
        } catch (Exception e) {
            e = e;
            i = 0;
        }
        try {
            MobLog.getInstance().d("Deleted %d rows from table: %s", Integer.valueOf(i), str);
        } catch (Exception e2) {
            e = e2;
            MobLog.getInstance().w(e, "when delete database occur error table:%s,", str);
            return i;
        }
        return i;
    }

    public long a(String str, ContentValues contentValues) {
        try {
            return this.a.getWritableDatabase().replace(str, null, contentValues);
        } catch (Exception e) {
            MobLog.getInstance().w(e, "when insert database occur error table:%s,", str);
            return -1L;
        }
    }

    public Cursor a(String str, String[] strArr) {
        try {
            return this.a.getWritableDatabase().rawQuery(str, strArr);
        } catch (Exception e) {
            MobLog.getInstance().w(e);
            return null;
        }
    }
}
