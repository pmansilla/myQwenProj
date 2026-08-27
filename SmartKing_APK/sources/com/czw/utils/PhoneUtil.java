package com.czw.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import com.liulishuo.filedownloader.model.FileDownloadModel;

/* loaded from: classes.dex */
public class PhoneUtil {
    private PhoneUtil() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0052  */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getDisplayNameByNumber(android.content.Context r8, java.lang.String r9) {
        /*
            java.lang.String r0 = "debug_method"
            java.lang.String r1 = "getDisplayNameByNumber"
            android.util.Log.e(r0, r1)
            r0 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L58
            android.net.Uri r8 = android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L58
            android.net.Uri$Builder r8 = r8.buildUpon()     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L58
            android.net.Uri$Builder r8 = r8.appendPath(r9)     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L58
            android.net.Uri r2 = r8.build()     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L58
            java.lang.String r8 = "_id"
            java.lang.String r9 = "_display_name"
            java.lang.String[] r3 = new java.lang.String[]{r8, r9}     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L58
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L58
            if (r8 == 0) goto L4f
            boolean r9 = r8.moveToFirst()     // Catch: java.lang.Throwable -> L46 java.lang.Exception -> L49
            if (r9 == 0) goto L4f
            java.lang.String r9 = "_display_name"
            int r9 = r8.getColumnIndex(r9)     // Catch: java.lang.Throwable -> L46 java.lang.Exception -> L49
            java.lang.String r9 = r8.getString(r9)     // Catch: java.lang.Throwable -> L46 java.lang.Exception -> L49
            java.lang.String r0 = "debug_name"
            android.util.Log.e(r0, r9)     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L46
            goto L50
        L41:
            r0 = move-exception
            r7 = r0
            r0 = r8
            r8 = r7
            goto L5a
        L46:
            r9 = move-exception
            r0 = r8
            goto L63
        L49:
            r9 = move-exception
            r7 = r0
            r0 = r8
            r8 = r9
            r9 = r7
            goto L5a
        L4f:
            r9 = r0
        L50:
            if (r8 == 0) goto L62
            r8.close()
            goto L62
        L56:
            r9 = move-exception
            goto L63
        L58:
            r8 = move-exception
            r9 = r0
        L5a:
            r8.printStackTrace()     // Catch: java.lang.Throwable -> L56
            if (r0 == 0) goto L62
            r0.close()
        L62:
            return r9
        L63:
            if (r0 == 0) goto L68
            r0.close()
        L68:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.utils.PhoneUtil.getDisplayNameByNumber(android.content.Context, java.lang.String):java.lang.String");
    }

    public static String numberToName(Context context, String str) {
        String str2;
        Cursor query = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"display_name", "data1", "contact_id"}, null, null, null);
        while (true) {
            if (!query.moveToNext()) {
                str2 = null;
                break;
            }
            str2 = query.getString(0);
            if (str.equals(query.getString(1).replace("-", ""))) {
                break;
            }
        }
        query.close();
        return str2;
    }

    public static String queryContacts(Context context, String str) {
        Cursor query = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (!query.moveToNext()) {
            return null;
        }
        Log.e("debug_method", query.getString(query.getColumnIndex(FileDownloadModel.ID)) + "==" + query.getString(query.getColumnIndex(FileDownloadModel.ID)));
        return null;
    }
}
