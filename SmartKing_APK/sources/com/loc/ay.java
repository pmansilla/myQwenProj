package com.loc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: DB.java */
/* loaded from: classes.dex */
public final class ay extends SQLiteOpenHelper {
    private static boolean b = true;
    private static boolean c = false;
    private au a;

    public ay(Context context, String str, au auVar) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        this.a = auVar;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        this.a.a(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
