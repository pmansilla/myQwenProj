package com.litesuits.orm.db.assit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class SQLiteHelper extends SQLiteOpenHelper {
    private OnUpdateListener onUpdateListener;

    /* loaded from: classes.dex */
    public interface OnUpdateListener {
        void onUpdate(SQLiteDatabase sQLiteDatabase, int i, int i2);
    }

    public SQLiteHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i, OnUpdateListener onUpdateListener) {
        super(context, str, cursorFactory, i);
        this.onUpdateListener = onUpdateListener;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (this.onUpdateListener != null) {
            this.onUpdateListener.onUpdate(sQLiteDatabase, i, i2);
        }
    }
}
