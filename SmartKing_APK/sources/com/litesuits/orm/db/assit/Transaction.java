package com.litesuits.orm.db.assit;

import android.database.sqlite.SQLiteDatabase;
import com.litesuits.orm.log.OrmLog;

/* loaded from: classes.dex */
public class Transaction {
    private static final String TAG = "Transaction";

    /* loaded from: classes.dex */
    public interface Worker<T> {
        T doTransaction(SQLiteDatabase sQLiteDatabase) throws Exception;
    }

    public static <T> T execute(SQLiteDatabase sQLiteDatabase, Worker<T> worker) {
        T t;
        sQLiteDatabase.beginTransaction();
        OrmLog.i(TAG, "----> BeginTransaction");
        try {
            try {
                t = worker.doTransaction(sQLiteDatabase);
                try {
                    sQLiteDatabase.setTransactionSuccessful();
                    if (OrmLog.isPrint) {
                        OrmLog.i(TAG, "----> Transaction Successful");
                    }
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    return t;
                }
            } catch (Exception e2) {
                e = e2;
                t = null;
            }
            return t;
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }
}
