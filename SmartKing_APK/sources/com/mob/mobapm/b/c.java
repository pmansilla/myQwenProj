package com.mob.mobapm.b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mob.mobapm.e.h;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class c {
    private static c c;
    private b a;
    private Hashon b = new Hashon();

    private c(Context context) {
        this.a = new b(context);
    }

    public static c a(Context context) {
        if (c == null) {
            synchronized (c.class) {
                if (c == null) {
                    c = new c(context);
                }
            }
        }
        return c;
    }

    private void a(String str) {
        SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
        writableDatabase.execSQL(str);
        writableDatabase.close();
    }

    public List<HashMap<String, Object>> a(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4) {
        SQLiteDatabase readableDatabase = this.a.getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        try {
            Cursor query = readableDatabase.query(true, "Sockets", strArr, str, strArr2, str2, str3, str4, "");
            while (query.moveToNext()) {
                HashMap fromJson = this.b.fromJson(this.b.fromObject(h.a().c(query.getString(query.getColumnIndex(query.getColumnName(1))))));
                if (fromJson != null && !fromJson.isEmpty()) {
                    fromJson.remove("transStatus");
                    arrayList.add(fromJson);
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Throwable th) {
            try {
                com.mob.mobapm.d.a.a().e("SocketDao", "queryDatas" + th.toString());
            } finally {
                readableDatabase.close();
            }
        }
        return arrayList;
    }

    public void a() {
        a("delete from Sockets");
    }

    public void a(ContentValues contentValues) {
        try {
            long insert = this.a.getWritableDatabase().insert("Sockets", null, contentValues);
            com.mob.mobapm.d.a.a().d("APM: insert socket result: " + insert, new Object[0]);
        } finally {
            try {
            } finally {
            }
        }
    }
}
