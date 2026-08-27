package cn.sharesdk.framework.b.a;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        Cursor rawQuery;
        Cursor cursor = null;
        try {
            try {
                rawQuery = this.a.getWritableDatabase().rawQuery("select count(*) from " + str, null);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            r2 = rawQuery.moveToNext() ? rawQuery.getInt(0) : 0;
            rawQuery.close();
        } catch (Exception e2) {
            e = e2;
            cursor = rawQuery;
            cn.sharesdk.framework.utils.e.b().w(e);
            cursor.close();
            return r2;
        } catch (Throwable th2) {
            th = th2;
            cursor = rawQuery;
            cursor.close();
            throw th;
        }
        return r2;
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
            cn.sharesdk.framework.utils.e.b().d("Deleted %d rows from table: %s", Integer.valueOf(i), str);
        } catch (Exception e2) {
            e = e2;
            cn.sharesdk.framework.utils.e.b().w(e, "when delete database occur error table:%s,", str);
            return i;
        }
        return i;
    }

    public long a(String str, ContentValues contentValues) {
        try {
            return this.a.getWritableDatabase().replace(str, null, contentValues);
        } catch (Exception e) {
            cn.sharesdk.framework.utils.e.b().w(e, "when insert database occur error table:%s,", str);
            return -1L;
        }
    }

    public Cursor a(String str, String[] strArr, String str2, String[] strArr2, String str3) {
        SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
        cn.sharesdk.framework.utils.e.b().d("Query table: %s", str);
        try {
            return writableDatabase.query(str, strArr, str2, strArr2, null, null, str3);
        } catch (Exception e) {
            cn.sharesdk.framework.utils.e.b().w(e, "when query database occur error table:%s,", str);
            return null;
        }
    }
}
