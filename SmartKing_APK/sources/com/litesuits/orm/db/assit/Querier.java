package com.litesuits.orm.db.assit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.litesuits.orm.log.OrmLog;

/* loaded from: classes.dex */
public class Querier {
    private static final String TAG = "Querier";

    /* loaded from: classes.dex */
    public static abstract class CursorParser<T> {
        private boolean parse = true;

        public abstract void parseEachCursor(SQLiteDatabase sQLiteDatabase, Cursor cursor) throws Exception;

        public final void process(SQLiteDatabase sQLiteDatabase, Cursor cursor) {
            try {
                try {
                    cursor.moveToFirst();
                    while (this.parse && !cursor.isAfterLast()) {
                        parseEachCursor(sQLiteDatabase, cursor);
                        cursor.moveToNext();
                    }
                    if (cursor == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (cursor == null) {
                        return;
                    }
                }
                cursor.close();
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }

        public T returnResult() {
            return null;
        }

        public final void stopParse() {
            this.parse = false;
        }
    }

    public static <T> T doQuery(SQLiteDatabase sQLiteDatabase, SQLStatement sQLStatement, CursorParser<T> cursorParser) {
        if (OrmLog.isPrint) {
            OrmLog.d(TAG, "----> Query Start: " + sQLStatement.toString());
        }
        Cursor rawQuery = sQLiteDatabase.rawQuery(sQLStatement.sql, (String[]) sQLStatement.bindArgs);
        if (rawQuery != null) {
            cursorParser.process(sQLiteDatabase, rawQuery);
            if (OrmLog.isPrint) {
                OrmLog.d(TAG, "<---- Query End , cursor size : " + rawQuery.getCount());
            }
        } else if (OrmLog.isPrint) {
            OrmLog.e(TAG, "<---- Query End : cursor is null");
        }
        return cursorParser.returnResult();
    }
}
