package com.amap.openapi;

import android.database.sqlite.SQLiteDatabase;

/* compiled from: LocationApTable.java */
/* loaded from: classes.dex */
public class bv {
    public static String a = "id";
    public static String b = "lat";
    public static String c = "lng";
    public static String d = "acc";
    public static String e = "conf";
    public static String f = "timestamp";
    public static String g = "frequency";
    private static final String h = "CREATE TABLE IF NOT EXISTS AP ( " + a + " LONG PRIMARY KEY, " + b + " INTEGER, " + c + " INTEGER, " + d + " INTEGER, " + e + " INTEGER, " + f + " LONG, " + g + " INTEGER DEFAULT 0);";

    public static void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(h);
    }
}
