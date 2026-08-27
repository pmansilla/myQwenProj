package com.mob.mobapm.b;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class d extends SQLiteOpenHelper {
    public d(Context context) {
        super(context, "mob_apm.db", (SQLiteDatabase.CursorFactory) null, 3);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table if not exists Transactions (Id integer primary key, trans text, tag text)");
        sQLiteDatabase.execSQL("create table if not exists httpRequestData (Id integer primary key, trans text, tag text)");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS Transactions");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS httpRequestData");
        onCreate(sQLiteDatabase);
    }
}
