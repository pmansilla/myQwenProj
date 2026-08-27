package com.mob.mobapm.b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mob.mobapm.bean.HttpTransaction;
import com.mob.mobapm.e.h;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class e {
    private static e c;
    public static long d;
    private d a;
    private Hashon b = new Hashon();

    private e(Context context) {
        this.a = new d(context);
    }

    private synchronized long a(String str) {
        Cursor rawQuery;
        SQLiteDatabase readableDatabase = this.a.getReadableDatabase();
        Cursor cursor = null;
        try {
            try {
                rawQuery = readableDatabase.rawQuery("select count(*) from " + str, null);
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            rawQuery.moveToFirst();
            long j = rawQuery.getLong(0);
            rawQuery.close();
            if (rawQuery != null) {
                try {
                    rawQuery.close();
                } catch (Exception unused) {
                }
            }
            if (readableDatabase != null) {
                try {
                    readableDatabase.close();
                } catch (Exception unused2) {
                }
            }
            return j;
        } catch (Exception e2) {
            cursor = rawQuery;
            e = e2;
            com.mob.mobapm.d.a.a().i("TransactionDao", "caculateDataCount" + e.toString());
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception unused3) {
                }
            }
            if (readableDatabase != null) {
                try {
                    readableDatabase.close();
                } catch (Exception unused4) {
                }
            }
            return 0L;
        } catch (Throwable th2) {
            th = th2;
            cursor = rawQuery;
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception unused5) {
                }
            }
            if (readableDatabase == null) {
                throw th;
            }
            try {
                readableDatabase.close();
                throw th;
            } catch (Exception unused6) {
                throw th;
            }
        }
    }

    public static e a(Context context) {
        if (c == null) {
            synchronized (e.class) {
                if (c == null) {
                    c = new e(context);
                }
            }
        }
        return c;
    }

    private synchronized void b(String str) {
        SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
        writableDatabase.execSQL(str);
        writableDatabase.close();
    }

    public synchronized List<HashMap<String, Object>> a(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4) {
        ArrayList arrayList;
        SQLiteDatabase readableDatabase = this.a.getReadableDatabase();
        arrayList = new ArrayList();
        try {
            Cursor query = readableDatabase.query("Transactions", strArr, str, strArr2, str2, str3, str4);
            while (query.moveToNext()) {
                query.getInt(query.getColumnIndex("Id"));
                HashMap fromJson = this.b.fromJson(this.b.fromObject(h.a().e(query.getString(query.getColumnIndex("trans")))));
                if (fromJson != null && !fromJson.isEmpty()) {
                    fromJson.remove("transStatus");
                    arrayList.add(fromJson);
                }
            }
            if (query != null) {
                query.close();
            }
            readableDatabase.close();
        } catch (Throwable th) {
            try {
                com.mob.mobapm.d.a.a().e("TransactionDao", "queryDatas" + th.toString());
            } finally {
                readableDatabase.close();
            }
        }
        return arrayList;
    }

    public synchronized void a() {
        b("delete from Transactions");
    }

    public synchronized void a(ContentValues contentValues) {
        try {
            long insert = this.a.getWritableDatabase().insert("Transactions", null, contentValues);
            com.mob.mobapm.d.a.a().d("APM: insert result: " + insert, new Object[0]);
        } finally {
            try {
            } finally {
            }
        }
    }

    public synchronized List<HashMap<String, Object>> b(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4) {
        ArrayList arrayList;
        SQLiteDatabase readableDatabase = this.a.getReadableDatabase();
        arrayList = new ArrayList();
        try {
            Cursor query = readableDatabase.query("httpRequestData", strArr, str, strArr2, str2, str3, str4);
            while (query.moveToNext()) {
                query.getInt(query.getColumnIndex("Id"));
                HashMap fromJson = this.b.fromJson(this.b.fromObject((HttpTransaction) h.a().a(query.getString(query.getColumnIndex("trans")), HttpTransaction.class)));
                if (fromJson != null && !fromJson.isEmpty()) {
                    fromJson.remove("transStatus");
                    arrayList.add(fromJson);
                }
            }
            if (query != null) {
                query.close();
            }
            readableDatabase.close();
        } catch (Throwable th) {
            try {
                com.mob.mobapm.d.a.a().e("TransactionDao", "queryRequestDatas" + th.toString());
            } finally {
                readableDatabase.close();
            }
        }
        return arrayList;
    }

    public synchronized void b() {
        b("delete from httpRequestData");
        d = 0L;
    }

    public synchronized void b(ContentValues contentValues) {
        if (d >= 500) {
            com.mob.mobapm.d.a.a().d("APM: count oversize", new Object[0]);
            return;
        }
        try {
            long insert = this.a.getWritableDatabase().insert("httpRequestData", null, contentValues);
            d++;
            com.mob.mobapm.d.a.a().d("APM: insertRequestData result: " + insert, new Object[0]);
        } finally {
            try {
            } finally {
            }
        }
    }

    public void c() {
        d = a("httpRequestData");
        com.mob.mobapm.d.a.a().i("TransactionDao", "httpBodyDataCount = " + d);
    }
}
