package ycble.runchinaup.aider.phone;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ycble.runchinaup.aider.entity.NPContactEntity;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.util.PhoneDeviceUtil;

/* loaded from: classes2.dex */
public final class NPContactsUtil {
    private static final String NUMBER_RULE = "/^\\d+$/";
    private static final String phoneDataMimeType = "vnd.android.cursor.item/phone_v2";
    private static List<NPContactEntity> npContactInfoList = new ArrayList();
    private static List<NPContactEntity> tmpList = new ArrayList();

    private NPContactsUtil() {
    }

    private static void debugCursor(Cursor cursor) {
        if (cursor == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            try {
                String columnName = cursor.getColumnName(i);
                hashMap.put(columnName, cursor.getString(cursor.getColumnIndex(columnName)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ycBleLog.e("联系人信息json:" + hashMap.toString());
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c8, code lost:
    
        if (r1 == null) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d4, code lost:
    
        if (r1.getDisplay_name().matches(ycble.runchinaup.aider.phone.NPContactsUtil.NUMBER_RULE) == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00e0, code lost:
    
        if (r1.getDisplay_name_alt().matches(ycble.runchinaup.aider.phone.NPContactsUtil.NUMBER_RULE) == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00e3, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00e4, code lost:
    
        r9 = r1.getDisplay_name_alt();
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00e9, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00ea, code lost:
    
        r9 = r1.getDisplay_name();
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ef, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f1, code lost:
    
        return r9;
     */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static synchronized java.lang.String getContactName(java.lang.String r9) {
        /*
            java.lang.Class<ycble.runchinaup.aider.phone.NPContactsUtil> r0 = ycble.runchinaup.aider.phone.NPContactsUtil.class
            monitor-enter(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lf4
            r1.<init>()     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r2 = "联系人的号码是(带rom自定义的前缀):"
            r1.append(r2)     // Catch: java.lang.Throwable -> Lf4
            r1.append(r9)     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Lf4
            ycble.runchinaup.log.ycBleLog.e(r1)     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r1 = "86"
            boolean r1 = r9.startsWith(r1)     // Catch: java.lang.Throwable -> Lf4
            r2 = 3
            r3 = 2
            if (r1 == 0) goto L26
            java.lang.String r9 = r9.substring(r3)     // Catch: java.lang.Throwable -> Lf4
            goto L32
        L26:
            java.lang.String r1 = "+86"
            boolean r1 = r9.startsWith(r1)     // Catch: java.lang.Throwable -> Lf4
            if (r1 == 0) goto L32
            java.lang.String r9 = r9.substring(r2)     // Catch: java.lang.Throwable -> Lf4
        L32:
            java.lang.String r1 = "+"
            java.lang.String r4 = ""
            java.lang.String r9 = r9.replace(r1, r4)     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r1 = "-"
            java.lang.String r4 = ""
            java.lang.String r9 = r9.replace(r1, r4)     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r1 = " "
            java.lang.String r4 = ""
            java.lang.String r9 = r9.replace(r1, r4)     // Catch: java.lang.Throwable -> Lf4
            java.util.List<ycble.runchinaup.aider.entity.NPContactEntity> r1 = ycble.runchinaup.aider.phone.NPContactsUtil.npContactInfoList     // Catch: java.lang.Throwable -> Lf4
            if (r1 == 0) goto Lf2
            java.util.List<ycble.runchinaup.aider.entity.NPContactEntity> r1 = ycble.runchinaup.aider.phone.NPContactsUtil.npContactInfoList     // Catch: java.lang.Throwable -> Lf4
            int r1 = r1.size()     // Catch: java.lang.Throwable -> Lf4
            r4 = 1
            if (r1 >= r4) goto L59
            goto Lf2
        L59:
            r1 = 0
            java.util.List<ycble.runchinaup.aider.entity.NPContactEntity> r4 = ycble.runchinaup.aider.phone.NPContactsUtil.npContactInfoList     // Catch: java.lang.Throwable -> Lf4
            java.util.Iterator r4 = r4.iterator()     // Catch: java.lang.Throwable -> Lf4
        L60:
            boolean r5 = r4.hasNext()     // Catch: java.lang.Throwable -> Lf4
            if (r5 == 0) goto Lc8
            java.lang.Object r5 = r4.next()     // Catch: java.lang.Throwable -> Lf4
            ycble.runchinaup.aider.entity.NPContactEntity r5 = (ycble.runchinaup.aider.entity.NPContactEntity) r5     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r6 = r5.getData1()     // Catch: java.lang.Throwable -> Lf4
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Throwable -> Lf4
            if (r6 == 0) goto L82
            java.lang.String r6 = r5.getData4()     // Catch: java.lang.Throwable -> Lf4
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Throwable -> Lf4
            if (r6 == 0) goto L82
            monitor-exit(r0)
            return r9
        L82:
            java.lang.String r6 = r5.getData4()     // Catch: java.lang.Throwable -> Lf4
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Throwable -> Lf4
            if (r7 == 0) goto L90
            java.lang.String r6 = r5.getData1()     // Catch: java.lang.Throwable -> Lf4
        L90:
            java.lang.String r7 = "86"
            boolean r7 = r6.startsWith(r7)     // Catch: java.lang.Throwable -> Lf4
            if (r7 == 0) goto L9d
            java.lang.String r6 = r6.substring(r3)     // Catch: java.lang.Throwable -> Lf4
            goto La9
        L9d:
            java.lang.String r7 = "+86"
            boolean r7 = r6.startsWith(r7)     // Catch: java.lang.Throwable -> Lf4
            if (r7 == 0) goto La9
            java.lang.String r6 = r6.substring(r2)     // Catch: java.lang.Throwable -> Lf4
        La9:
            java.lang.String r7 = " "
            java.lang.String r8 = ""
            java.lang.String r6 = r6.replace(r7, r8)     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r7 = "+"
            java.lang.String r8 = ""
            java.lang.String r6 = r6.replace(r7, r8)     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r7 = "-"
            java.lang.String r8 = ""
            java.lang.String r6 = r6.replace(r7, r8)     // Catch: java.lang.Throwable -> Lf4
            boolean r6 = r6.equalsIgnoreCase(r9)     // Catch: java.lang.Throwable -> Lf4
            if (r6 == 0) goto L60
            r1 = r5
        Lc8:
            if (r1 == 0) goto Lf0
            java.lang.String r2 = r1.getDisplay_name()     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r3 = "/^\\d+$/"
            boolean r2 = r2.matches(r3)     // Catch: java.lang.Throwable -> Lf4
            if (r2 == 0) goto Lea
            java.lang.String r2 = r1.getDisplay_name_alt()     // Catch: java.lang.Throwable -> Lf4
            java.lang.String r3 = "/^\\d+$/"
            boolean r2 = r2.matches(r3)     // Catch: java.lang.Throwable -> Lf4
            if (r2 == 0) goto Le4
            monitor-exit(r0)
            return r9
        Le4:
            java.lang.String r9 = r1.getDisplay_name_alt()     // Catch: java.lang.Throwable -> Lf4
            monitor-exit(r0)
            return r9
        Lea:
            java.lang.String r9 = r1.getDisplay_name()     // Catch: java.lang.Throwable -> Lf4
            monitor-exit(r0)
            return r9
        Lf0:
            monitor-exit(r0)
            return r9
        Lf2:
            monitor-exit(r0)
            return r9
        Lf4:
            r9 = move-exception
            monitor-exit(r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: ycble.runchinaup.aider.phone.NPContactsUtil.getContactName(java.lang.String):java.lang.String");
    }

    public static List<NPContactEntity> getNpContactInfoList() {
        return npContactInfoList;
    }

    public static String queryContact(Context context, String str) {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        if (!PhoneDeviceUtil.hasPermissions(context, "android.permission.READ_CONTACTS")) {
            ycBleLog.e("没有 Manifest.permission.READ_CONTACTS 权限！！！，返回原始号码");
            return str;
        }
        String replace = str.replace(SQLBuilder.BLANK, "").replace("-", "");
        if (replace.startsWith("+86")) {
            replace = replace.substring(3);
        }
        if (replace.startsWith("86")) {
            replace = replace.substring(2);
        }
        String str9 = new String(replace);
        ycBleLog.e("处理过后的号码格式是:" + str9);
        String str10 = "86" + replace;
        String str11 = "+86" + replace;
        if (replace.length() == 11) {
            str2 = replace.substring(0, 3) + SQLBuilder.BLANK + replace.substring(3, 7) + SQLBuilder.BLANK + replace.substring(7);
            str5 = "86" + str2;
            str6 = "+86" + str2;
            str7 = "86 " + str2;
            str8 = "+86 " + str2;
            str3 = " 86 " + str2;
            str4 = " +86 " + str2;
        } else {
            str2 = replace;
            str3 = str2;
            str4 = str3;
            str5 = str4;
            str6 = str5;
            str7 = str6;
            str8 = str7;
        }
        Cursor query = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{"display_name", "display_name_alt", "data1", "data4"}, "data1=? or data1=? or data1=? or data1=? or data1=? or data1=? or data1=? or data1=? or data1=? or data1=? or data4=? or data4=? or data4=? or data4=? or data4=? or data4=? or data4=? or data4=? or data4=? or data4=?", new String[]{replace, str10, str11, str2, str5, str6, str7, str8, str3, str4, replace, str10, str11, str2, str5, str6, str7, str8, str3, str4}, "raw_contact_id");
        if (query != null) {
            if (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex("display_name_alt"));
                String string2 = query.getString(query.getColumnIndex("display_name"));
                if (!TextUtils.isEmpty(string2)) {
                    str9 = string2;
                } else if (!TextUtils.isEmpty(string)) {
                    str9 = string;
                }
            }
            query.close();
        }
        ycBleLog.e("联系人姓名查询结果:" + str9);
        return str9;
    }

    static synchronized void reLoadData(Context context) {
        Cursor query;
        synchronized (NPContactsUtil.class) {
            try {
                tmpList.clear();
                query = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, "raw_contact_id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (query == null) {
                ycBleLog.e("联系人列表为空");
                return;
            }
            ycBleLog.e("联系人列表不为空");
            while (query.moveToNext()) {
                try {
                    if (query.getString(query.getColumnIndex("mimetype")).equals(phoneDataMimeType)) {
                        tmpList.add(new NPContactEntity(query.getString(query.getColumnIndex("display_name_alt")), query.getString(query.getColumnIndex("display_name")), query.getString(query.getColumnIndex("data1")), query.getString(query.getColumnIndex("data4"))));
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            query.close();
            npContactInfoList.clear();
            npContactInfoList.addAll(tmpList);
            tmpList.clear();
        }
    }
}
