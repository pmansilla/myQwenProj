package com.mob.commons.logcollector;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Base64;
import com.mob.tools.MobLog;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: MessageUtils.java */
/* loaded from: classes.dex */
public class e {
    public static synchronized long a(long j, String str, int i, String str2) throws Throwable {
        synchronized (e.class) {
            if (TextUtils.isEmpty(str)) {
                return -1L;
            }
            b a = b.a();
            ContentValues contentValues = new ContentValues();
            contentValues.put("exception_time", Long.valueOf(j));
            contentValues.put("exception_msg", str.toString());
            contentValues.put("exception_level", Integer.valueOf(i));
            contentValues.put("exception_md5", str2);
            return a.a("table_exception", contentValues);
        }
    }

    public static synchronized long a(ArrayList<String> arrayList) throws Throwable {
        synchronized (e.class) {
            if (arrayList == null) {
                return 0L;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                sb.append("'");
                sb.append(arrayList.get(i));
                sb.append("'");
                sb.append(",");
            }
            String substring = sb.toString().substring(0, sb.length() - 1);
            int a = b.a().a("table_exception", "exception_md5 in ( " + substring + " )", null);
            MobLog.getInstance().i("delete COUNT == %s", Integer.valueOf(a));
            return a;
        }
    }

    private static synchronized ArrayList<d> a(String str, String[] strArr) throws Throwable {
        ArrayList<d> arrayList;
        synchronized (e.class) {
            arrayList = new ArrayList<>();
            d dVar = new d();
            b a = b.a();
            String str2 = " select exception_md5, exception_level, exception_time, exception_msg, sum(exception_counts) from table_exception group by exception_md5 having max(_id)";
            if (!TextUtils.isEmpty(str) && strArr != null && strArr.length > 0) {
                str2 = " select exception_md5, exception_level, exception_time, exception_msg, sum(exception_counts) from table_exception where " + str + " group by exception_md5 having max(_id)";
            }
            Cursor a2 = a.a(str2, strArr);
            while (true) {
                if (a2 == null || !a2.moveToNext()) {
                    break;
                }
                dVar.b.add(a2.getString(0));
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("type", Integer.valueOf(a2.getInt(1)));
                hashMap.put("errat", Long.valueOf(a2.getLong(2)));
                hashMap.put(NotificationCompat.CATEGORY_MESSAGE, Base64.encodeToString(a2.getString(3).getBytes(), 2));
                hashMap.put("times", Integer.valueOf(a2.getInt(4)));
                dVar.a.add(hashMap);
                if (dVar.b.size() == 50) {
                    arrayList.add(dVar);
                    dVar = new d();
                    break;
                }
            }
            a2.close();
            if (dVar.b.size() != 0) {
                arrayList.add(dVar);
            }
        }
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0009, code lost:
    
        if (r4.length <= 0) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized java.util.ArrayList<com.mob.commons.logcollector.d> a(java.lang.String[] r4) throws java.lang.Throwable {
        /*
            java.lang.Class<com.mob.commons.logcollector.e> r0 = com.mob.commons.logcollector.e.class
            monitor-enter(r0)
            java.lang.String r1 = "exception_level = ?"
            r2 = 0
            if (r4 == 0) goto Lb
            int r3 = r4.length     // Catch: java.lang.Throwable -> L26
            if (r3 > 0) goto Ld
        Lb:
            r4 = r2
            r1 = r4
        Ld:
            com.mob.commons.logcollector.b r2 = com.mob.commons.logcollector.b.a()     // Catch: java.lang.Throwable -> L26
            java.lang.String r3 = "table_exception"
            int r2 = r2.a(r3)     // Catch: java.lang.Throwable -> L26
            if (r2 <= 0) goto L1f
            java.util.ArrayList r4 = a(r1, r4)     // Catch: java.lang.Throwable -> L26
            monitor-exit(r0)
            return r4
        L1f:
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L26
            r4.<init>()     // Catch: java.lang.Throwable -> L26
            monitor-exit(r0)
            return r4
        L26:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.logcollector.e.a(java.lang.String[]):java.util.ArrayList");
    }
}
