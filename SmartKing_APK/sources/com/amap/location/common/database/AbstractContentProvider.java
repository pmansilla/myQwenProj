package com.amap.location.common.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class AbstractContentProvider extends ContentProvider {
    private final String d = "AbstractContentProvider";
    private String e = "";
    protected UriMatcher a = new UriMatcher(-1);
    protected HashMap<Integer, SQLiteOpenHelper> b = new HashMap<>();
    protected HashMap<Integer, String> c = new HashMap<>();

    private <T> T a(T t, Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public int a(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        a((AbstractContentProvider) uri, "uri");
        try {
            int match = this.a.match(uri);
            SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
            if (sQLiteOpenHelper == null) {
                return 0;
            }
            SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
            String str2 = this.c.get(Integer.valueOf(match));
            if (writableDatabase != null && str2 != null) {
                return writableDatabase.update(str2, contentValues, str, strArr);
            }
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    public int a(Uri uri, String str, String[] strArr) {
        a((AbstractContentProvider) uri, "uri");
        try {
            int match = this.a.match(uri);
            SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
            if (sQLiteOpenHelper == null) {
                return 0;
            }
            SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
            String str2 = this.c.get(Integer.valueOf(match));
            if (writableDatabase != null && str2 != null) {
                return writableDatabase.delete(str2, str, strArr);
            }
            return 0;
        } catch (Exception unused) {
            return -1;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x004a, code lost:
    
        r2.endTransaction();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int a(android.net.Uri r6, android.content.ContentValues[] r7) {
        /*
            r5 = this;
            java.lang.String r0 = "uri"
            r5.a(r6, r0)
            int r0 = r7.length
            r1 = 0
            android.content.UriMatcher r2 = r5.a     // Catch: java.lang.Throwable -> L52 java.lang.Exception -> L5a
            int r6 = r2.match(r6)     // Catch: java.lang.Throwable -> L52 java.lang.Exception -> L5a
            java.util.HashMap<java.lang.Integer, android.database.sqlite.SQLiteOpenHelper> r2 = r5.b     // Catch: java.lang.Throwable -> L52 java.lang.Exception -> L5a
            java.lang.Integer r3 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Throwable -> L52 java.lang.Exception -> L5a
            java.lang.Object r2 = r2.get(r3)     // Catch: java.lang.Throwable -> L52 java.lang.Exception -> L5a
            android.database.sqlite.SQLiteOpenHelper r2 = (android.database.sqlite.SQLiteOpenHelper) r2     // Catch: java.lang.Throwable -> L52 java.lang.Exception -> L5a
            r3 = 0
            if (r2 != 0) goto L1d
            return r3
        L1d:
            android.database.sqlite.SQLiteDatabase r2 = r2.getWritableDatabase()     // Catch: java.lang.Throwable -> L52 java.lang.Exception -> L5a
            java.util.HashMap<java.lang.Integer, java.lang.String> r4 = r5.c     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
            java.lang.Object r6 = r4.get(r6)     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
            java.lang.String r6 = (java.lang.String) r6     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
            if (r2 == 0) goto L48
            if (r6 != 0) goto L32
            goto L48
        L32:
            r2.beginTransaction()     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
        L35:
            if (r3 >= r0) goto L3f
            r4 = r7[r3]     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
            r2.insert(r6, r1, r4)     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
            int r3 = r3 + 1
            goto L35
        L3f:
            r2.setTransactionSuccessful()     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L50
            if (r2 == 0) goto L5f
            r2.endTransaction()     // Catch: java.lang.Exception -> L5f
            goto L5f
        L48:
            if (r2 == 0) goto L4d
            r2.endTransaction()     // Catch: java.lang.Exception -> L4d
        L4d:
            return r3
        L4e:
            r6 = move-exception
            goto L54
        L50:
            r1 = r2
            goto L5a
        L52:
            r6 = move-exception
            r2 = r1
        L54:
            if (r2 == 0) goto L59
            r2.endTransaction()     // Catch: java.lang.Exception -> L59
        L59:
            throw r6
        L5a:
            if (r1 == 0) goto L5f
            r1.endTransaction()     // Catch: java.lang.Exception -> L5f
        L5f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.location.common.database.AbstractContentProvider.a(android.net.Uri, android.content.ContentValues[]):int");
    }

    public long a(Uri uri) {
        a((AbstractContentProvider) uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0L;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str == null) {
            return 0L;
        }
        return DatabaseUtils.queryNumEntries(writableDatabase, str);
    }

    public long a(Uri uri, ContentValues contentValues) {
        a((AbstractContentProvider) uri, "uri");
        try {
            int match = this.a.match(uri);
            SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
            if (sQLiteOpenHelper == null) {
                return 0L;
            }
            SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
            String str = this.c.get(Integer.valueOf(match));
            if (writableDatabase != null && str != null) {
                return writableDatabase.insert(str, null, contentValues);
            }
            return 0L;
        } catch (Exception unused) {
            return -1L;
        }
    }

    public Cursor a(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return b(uri, strArr, str, strArr2, str2, null);
    }

    public Cursor a(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        a((AbstractContentProvider) uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return null;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str4 = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str4 == null) {
            return null;
        }
        SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
        sQLiteQueryBuilder.setTables(str4);
        return sQLiteQueryBuilder.query(writableDatabase, strArr, str, strArr2, null, null, str2, str3);
    }

    public abstract String a();

    public void a(Integer num, String str, SQLiteOpenHelper sQLiteOpenHelper) {
        if (sQLiteOpenHelper != null) {
            this.b.put(num, sQLiteOpenHelper);
            this.c.put(num, str);
            this.a.addURI(this.e, str, num.intValue());
        }
    }

    public Cursor b(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        int match;
        SQLiteOpenHelper sQLiteOpenHelper;
        a((AbstractContentProvider) uri, "uri");
        try {
            match = this.a.match(uri);
            sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        } catch (Exception unused) {
        }
        if (sQLiteOpenHelper == null) {
            return null;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str4 = this.c.get(Integer.valueOf(match));
        if (writableDatabase != null && str4 != null) {
            SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
            sQLiteQueryBuilder.setTables(str4);
            return sQLiteQueryBuilder.query(writableDatabase, strArr, str, strArr2, null, null, str2, str3);
        }
        return null;
    }

    protected abstract void b();

    @Override // android.content.ContentProvider
    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        a((AbstractContentProvider) uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str == null) {
            return 0;
        }
        int length = contentValuesArr.length;
        writableDatabase.beginTransaction();
        for (ContentValues contentValues : contentValuesArr) {
            try {
                writableDatabase.insert(str, null, contentValues);
            } catch (Throwable th) {
                writableDatabase.endTransaction();
                throw th;
            }
        }
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        return length;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        a((AbstractContentProvider) uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str2 = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str2 == null) {
            return 0;
        }
        return writableDatabase.delete(str2, str, strArr);
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/" + this.c.get(Integer.valueOf(this.a.match(uri)));
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        a((AbstractContentProvider) uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return ContentUris.withAppendedId(uri, -1L);
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str = this.c.get(Integer.valueOf(match));
        return (writableDatabase == null || str == null) ? ContentUris.withAppendedId(uri, -1L) : ContentUris.withAppendedId(uri, writableDatabase.insert(str, null, contentValues));
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        this.e = a();
        b();
        return false;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return a(uri, strArr, str, strArr2, str2, null);
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        a((AbstractContentProvider) uri, "uri");
        int match = this.a.match(uri);
        SQLiteOpenHelper sQLiteOpenHelper = this.b.get(Integer.valueOf(match));
        if (sQLiteOpenHelper == null) {
            return 0;
        }
        SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
        String str2 = this.c.get(Integer.valueOf(match));
        if (writableDatabase == null || str2 == null) {
            return 0;
        }
        return writableDatabase.update(str2, contentValues, str, strArr);
    }
}
