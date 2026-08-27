package com.amap.openapi;

import android.database.sqlite.SQLiteDatabase;

/* compiled from: EventTable.java */
/* loaded from: classes.dex */
public class cm {
    public static String a = "id";
    public static String b = "frequency";
    private static final String c = "CREATE TABLE IF NOT EXISTS ACL ( " + a + " TEXT PRIMARY KEY, " + b + " INTEGER DEFAULT 0);";

    public static void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(c);
    }
}
