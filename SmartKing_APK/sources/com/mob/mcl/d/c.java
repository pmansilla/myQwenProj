package com.mob.mcl.d;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;

/* compiled from: MsgDB.java */
/* loaded from: classes.dex */
public class c {
    private a a;

    public c(Context context) {
        this.a = new a(context.getApplicationContext());
    }

    public void a() {
        try {
            SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
            writableDatabase.execSQL("delete from msg where expireTime < " + System.currentTimeMillis());
            writableDatabase.close();
        } catch (Throwable th) {
            b.a().a(th);
        }
    }

    public void a(String str) {
        try {
            SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
            writableDatabase.execSQL("delete from msg where workId = \"" + str + "\"");
            writableDatabase.close();
        } catch (Throwable th) {
            b.a().a(th);
        }
    }

    public void a(String str, long j) {
        try {
            SQLiteDatabase writableDatabase = this.a.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("workId", str);
            contentValues.put("expireTime", Long.valueOf(j));
            writableDatabase.replace(NotificationCompat.CATEGORY_MESSAGE, null, contentValues);
            writableDatabase.close();
        } catch (Throwable th) {
            b.a().a(th);
        }
    }

    public long b(String str) {
        try {
            SQLiteDatabase readableDatabase = this.a.getReadableDatabase();
            Cursor rawQuery = readableDatabase.rawQuery("select expireTime from msg where workId = \"" + str + "\"", null);
            if (rawQuery.moveToFirst()) {
                return rawQuery.getLong(rawQuery.getColumnIndex("expireTime"));
            }
            rawQuery.close();
            readableDatabase.close();
            return 0L;
        } catch (Throwable th) {
            b.a().a(th);
            return 0L;
        }
    }
}
