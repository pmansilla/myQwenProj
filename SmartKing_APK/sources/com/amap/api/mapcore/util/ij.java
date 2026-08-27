package com.amap.api.mapcore.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: DB.java */
/* loaded from: classes.dex */
public class ij extends SQLiteOpenHelper {
    private static boolean b = true;
    private static boolean c = false;
    private Cif a;

    public ij(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i, Cif cif) {
        super(context, str, cursorFactory, i);
        this.a = cif;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        this.a.a(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        this.a.a(sQLiteDatabase, i, i2);
    }
}
