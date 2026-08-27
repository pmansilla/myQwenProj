package com.amap.openapi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.SystemClock;
import android.util.Log;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AeUtil;
import com.liulishuo.filedownloader.model.ConnectionModel;
import java.util.List;
import java.util.Locale;

/* compiled from: DbManager.java */
/* loaded from: classes.dex */
public class u {
    private static final String[] a = {ConnectionModel.ID, "type", AeUtil.ROOT_DATA_PATH_OLD_NAME, "size"};
    private a b;
    private long c = a(true);
    private long d = a(false);

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DbManager.java */
    /* loaded from: classes.dex */
    public static class a extends SQLiteOpenHelper {
        a(Context context, String str, int i) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, i);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            try {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS base (id INTEGER PRIMARY KEY AUTOINCREMENT , type SMALLINT, data BLOB, size INTEGER, time INTEGER);");
            } catch (Exception unused) {
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            try {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS base");
                onCreate(sQLiteDatabase);
            } catch (Exception e) {
                ALLog.e("DbManager", "", (Throwable) e, true);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            try {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS base");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS byte_base");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS extend");
                onCreate(sQLiteDatabase);
            } catch (Exception e) {
                ALLog.e("DbManager", "", (Throwable) e, true);
            }
        }
    }

    public u(Context context) {
        this.b = new a(context, "aloccoll.db", 4);
    }

    private long a(boolean z) {
        Cursor cursor;
        SystemClock.elapsedRealtime();
        long j = 0;
        Cursor cursor2 = null;
        try {
            cursor = this.b.getReadableDatabase().query("base", new String[]{"SUM(size)"}, z ? "type=?" : "type!=?", new String[]{AmapLoc.RESULT_TYPE_GPS}, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    j = cursor.getLong(0);
                }
            } catch (Exception unused) {
            } catch (Throwable th) {
                cursor2 = cursor;
                th = th;
                com.amap.location.common.util.e.a(cursor2);
                throw th;
            }
        } catch (Exception unused2) {
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
        }
        com.amap.location.common.util.e.a(cursor);
        return j;
    }

    private long a(boolean z, long j) throws Exception {
        Cursor cursor;
        Cursor query;
        String str = z ? "type=0" : "type!=0";
        long j2 = -2147483648L;
        long j3 = 0;
        while (true) {
            if (j3 >= j) {
                break;
            }
            try {
                query = this.b.getReadableDatabase().query("base", new String[]{ConnectionModel.ID, "type", "size"}, "id>? AND " + str, new String[]{String.valueOf(j2)}, null, null, "id ASC", "100");
            } catch (Throwable th) {
                th = th;
                cursor = null;
            }
            try {
                boolean moveToNext = query.moveToNext();
                if (!moveToNext) {
                    com.amap.location.common.util.e.a(query);
                    break;
                }
                while (moveToNext) {
                    j2 = query.getLong(0);
                    j3 += query.getInt(2);
                    if (j3 < j) {
                        moveToNext = query.moveToNext();
                    }
                }
                com.amap.location.common.util.e.a(query);
            } catch (Throwable th2) {
                th = th2;
                cursor = query;
                com.amap.location.common.util.e.a(cursor);
                throw th;
            }
        }
        if (j3 > 0) {
            if (this.b.getWritableDatabase().delete("base", "id<=? AND " + str, new String[]{String.valueOf(j2)}) > 0) {
                if (z) {
                    this.c -= j3;
                    if (this.c < 0) {
                        this.c = 0L;
                    }
                } else {
                    this.d -= j3;
                    if (this.d < 0) {
                        this.d = 0L;
                    }
                }
            }
        }
        return j3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x006c, code lost:
    
        r0 = "@_3_2_@";
        r1 = java.lang.String.format(java.util.Locale.getDefault(), "@_3_2_1_@%d，%d, %d", java.lang.Long.valueOf(r3.a), java.lang.Integer.valueOf(r7), java.lang.Integer.valueOf(r3.c));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.amap.openapi.au a(boolean r17, int r18, long r19) {
        /*
            r16 = this;
            com.amap.openapi.au r3 = new com.amap.openapi.au
            r3.<init>()
            if (r17 == 0) goto Lb
            java.lang.String r4 = "type=0"
        L9:
            r8 = r4
            goto Le
        Lb:
            java.lang.String r4 = "type!=0"
            goto L9
        Le:
            r4 = 0
            r14 = r16
            com.amap.openapi.u$a r5 = r14.b     // Catch: java.lang.Throwable -> Lc4 java.lang.Exception -> Lca
            android.database.sqlite.SQLiteDatabase r5 = r5.getReadableDatabase()     // Catch: java.lang.Throwable -> Lc4 java.lang.Exception -> Lca
            java.lang.String r6 = "base"
            java.lang.String[] r7 = com.amap.openapi.u.a     // Catch: java.lang.Throwable -> Lc4 java.lang.Exception -> Lca
            r9 = 0
            r10 = 0
            r11 = 0
            java.lang.String r12 = "id ASC"
            java.lang.String r13 = java.lang.String.valueOf(r18)     // Catch: java.lang.Throwable -> Lc4 java.lang.Exception -> Lca
            android.database.Cursor r5 = r5.query(r6, r7, r8, r9, r10, r11, r12, r13)     // Catch: java.lang.Throwable -> Lc4 java.lang.Exception -> Lca
            r6 = 0
            r7 = 0
        L2a:
            boolean r8 = r5.moveToNext()     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r10 = 3
            r11 = 1
            if (r8 == 0) goto L6a
            int r8 = r5.getInt(r10)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r12 = r3.c     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            long r12 = (long) r12     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r15 = (r12 > r19 ? 1 : (r12 == r19 ? 0 : -1))
            if (r15 >= 0) goto L6a
            int r12 = r3.c     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r12 = r12 + r8
            long r12 = (long) r12     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r15 = (r12 > r19 ? 1 : (r12 == r19 ? 0 : -1))
            if (r15 > 0) goto L6a
            r12 = r18
            if (r7 >= r12) goto L6a
            long r9 = r5.getLong(r6)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r3.a = r9     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.util.List<com.amap.openapi.s> r9 = r3.b     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            com.amap.openapi.s r10 = new com.amap.openapi.s     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r11 = r5.getInt(r11)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r13 = 2
            byte[] r13 = r5.getBlob(r13)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r10.<init>(r11, r13)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r9.add(r10)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r9 = r3.c     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r9 = r9 + r8
            r3.c = r9     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r7 = r7 + 1
            goto L2a
        L6a:
            if (r17 == 0) goto L95
            java.lang.String r0 = "@_3_2_@"
            java.util.Locale r1 = java.util.Locale.getDefault()     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.String r2 = "@_3_2_1_@%d，%d, %d"
            java.lang.Object[] r8 = new java.lang.Object[r10]     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            long r9 = r3.a     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.Long r9 = java.lang.Long.valueOf(r9)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r8[r6] = r9     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.Integer r6 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r8[r11] = r6     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r6 = r3.c     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r7 = 2
            r8[r7] = r6     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.String r1 = java.lang.String.format(r1, r2, r8)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
        L91:
            com.amap.location.common.log.ALLog.trace(r0, r1)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            goto Lbb
        L95:
            java.lang.String r0 = "@_3_2_@"
            java.util.Locale r1 = java.util.Locale.getDefault()     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.String r2 = "@_3_2_2_@%d，%d, %d"
            java.lang.Object[] r8 = new java.lang.Object[r10]     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            long r9 = r3.a     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.Long r9 = java.lang.Long.valueOf(r9)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r8[r6] = r9     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.Integer r6 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r8[r11] = r6     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            int r6 = r3.c     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            r7 = 2
            r8[r7] = r6     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            java.lang.String r1 = java.lang.String.format(r1, r2, r8)     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            goto L91
        Lbb:
            int r0 = r3.c     // Catch: java.lang.Throwable -> Lc2 java.lang.Exception -> Lcb
            if (r0 != 0) goto Lc0
            goto Lcb
        Lc0:
            r4 = r3
            goto Lcb
        Lc2:
            r0 = move-exception
            goto Lc6
        Lc4:
            r0 = move-exception
            r5 = r4
        Lc6:
            com.amap.location.common.util.e.a(r5)
            throw r0
        Lca:
            r5 = r4
        Lcb:
            com.amap.location.common.util.e.a(r5)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.u.a(boolean, int, long):com.amap.openapi.au");
    }

    public void a() {
        try {
            if (this.b != null) {
                this.b.close();
                this.b = null;
            }
        } catch (Exception unused) {
        }
    }

    public void a(List<s> list) {
        SQLiteDatabase sQLiteDatabase;
        SQLiteStatement sQLiteStatement;
        SQLiteStatement sQLiteStatement2 = null;
        try {
            sQLiteDatabase = this.b.getWritableDatabase();
            try {
                sQLiteDatabase.beginTransaction();
                sQLiteStatement = sQLiteDatabase.compileStatement("INSERT INTO base(type,data,size,time) VALUES(?,?,?,?)");
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    long j = 0;
                    long j2 = 0;
                    int i = 0;
                    for (s sVar : list) {
                        sQLiteStatement.bindLong(1, sVar.b());
                        sQLiteStatement.bindBlob(2, sVar.c());
                        long a2 = sVar.a();
                        sQLiteStatement.bindLong(3, a2);
                        sQLiteStatement.bindLong(4, currentTimeMillis);
                        sQLiteStatement.executeInsert();
                        if (sVar.b() == 0) {
                            j += a2;
                            i++;
                        } else {
                            j2 += a2;
                        }
                    }
                    sQLiteDatabase.setTransactionSuccessful();
                    this.c += j;
                    this.d += j2;
                    ALLog.trace("@_3_2_@", String.format(Locale.getDefault(), "@_3_2_3_@" + currentTimeMillis + ";@_3_2_4_@%d，%d;@_3_2_5_@%d，%d", Integer.valueOf(list.size()), Long.valueOf(j), Integer.valueOf(i), Long.valueOf(j2), Integer.valueOf(list.size() - i)));
                    if (sQLiteStatement != null) {
                        try {
                            sQLiteStatement.close();
                        } catch (Throwable unused) {
                        }
                    }
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Exception unused2) {
                        }
                    }
                } catch (Exception unused3) {
                    sQLiteStatement2 = sQLiteStatement;
                    if (sQLiteStatement2 != null) {
                        try {
                            sQLiteStatement2.close();
                        } catch (Throwable unused4) {
                        }
                    }
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Exception unused5) {
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    if (sQLiteStatement != null) {
                        try {
                            sQLiteStatement.close();
                        } catch (Throwable unused6) {
                        }
                    }
                    if (sQLiteDatabase == null) {
                        throw th;
                    }
                    try {
                        sQLiteDatabase.endTransaction();
                        throw th;
                    } catch (Exception unused7) {
                        throw th;
                    }
                }
            } catch (Exception unused8) {
            } catch (Throwable th2) {
                th = th2;
                sQLiteStatement = null;
            }
        } catch (Exception unused9) {
            sQLiteDatabase = null;
        } catch (Throwable th3) {
            th = th3;
            sQLiteDatabase = null;
            sQLiteStatement = null;
        }
    }

    public boolean a(long j) {
        if (j < 4611686018427387903L && this.c + this.d + j < 10485760) {
            return true;
        }
        long max = Math.max(204800L, j);
        try {
            long a2 = a(false, max);
            if (a2 < max) {
                a(true, max - a2);
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean a(au auVar) {
        String str;
        String format;
        boolean z = true;
        if (auVar != null && auVar.b.size() != 0) {
            try {
                boolean z2 = auVar.b.get(0).b() == 0;
                int delete = this.b.getWritableDatabase().delete("base", z2 ? "type=0 AND id<=?" : "type!=0 AND id<=?", new String[]{String.valueOf(auVar.a)});
                if (delete > 0) {
                    if (z2) {
                        this.c -= auVar.c;
                        if (this.c < 0) {
                            this.c = 0L;
                        }
                    } else {
                        this.d -= auVar.c;
                        if (this.d < 0) {
                            this.d = 0L;
                        }
                    }
                }
                try {
                    if (z2) {
                        str = "@_3_2_@";
                        format = String.format(Locale.getDefault(), "@_3_2_6_@%d，%d，%d", Long.valueOf(auVar.a), Integer.valueOf(delete), Integer.valueOf(auVar.c));
                    } else {
                        str = "@_3_2_@";
                        format = String.format(Locale.getDefault(), "@_3_2_7_@%d，%d，%d", Long.valueOf(auVar.a), Integer.valueOf(delete), Integer.valueOf(auVar.c));
                    }
                    ALLog.trace(str, format);
                } catch (Exception e) {
                    e = e;
                    ALLog.trace("@_3_2_@", "@_3_2_8_@" + Log.getStackTraceString(e));
                    return z;
                }
            } catch (Exception e2) {
                e = e2;
                z = false;
            }
        }
        return z;
    }

    public int b() {
        return (int) this.c;
    }

    public int c() {
        return (int) this.d;
    }
}
