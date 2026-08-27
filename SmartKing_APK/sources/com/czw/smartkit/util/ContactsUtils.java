package com.czw.smartkit.util;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class ContactsUtils {
    private static ContactsUtils contactsUtils = new ContactsUtils();
    public static ArrayList<ContactInfo> contactsInfos = new ArrayList<>();

    private ContactsUtils() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0056, code lost:
    
        if (android.text.TextUtils.isEmpty(r8) != false) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0058, code lost:
    
        r0.add(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x005f, code lost:
    
        if (r7.moveToNext() != false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0061, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x003f, code lost:
    
        if (r7.moveToFirst() != false) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0041, code lost:
    
        r8 = r7.getColumnIndex("data1");
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x004c, code lost:
    
        if (r7.getType(r8) != 3) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x004e, code lost:
    
        r8 = r7.getString(r8);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.util.List<java.lang.String> getData1(android.content.ContentResolver r7, java.lang.String r8, java.lang.String r9) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            android.net.Uri r2 = android.provider.ContactsContract.Data.CONTENT_URI
            java.lang.String r1 = "data1"
            java.lang.String[] r3 = new java.lang.String[]{r1}
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = "contact_id=? AND mimetype='"
            r1.append(r4)
            r1.append(r9)
            java.lang.String r9 = "'"
            r1.append(r9)
            java.lang.String r4 = r1.toString()
            r9 = 1
            java.lang.String[] r5 = new java.lang.String[r9]
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r9 = 0
            r5[r9] = r8
            r6 = 0
            r1 = r7
            android.database.Cursor r7 = r1.query(r2, r3, r4, r5, r6)
            if (r7 == 0) goto L64
            int r8 = r7.getCount()
            if (r8 <= 0) goto L64
            boolean r8 = r7.moveToFirst()
            if (r8 == 0) goto L61
        L41:
            java.lang.String r8 = "data1"
            int r8 = r7.getColumnIndex(r8)
            int r9 = r7.getType(r8)
            r1 = 3
            if (r9 != r1) goto L5b
            java.lang.String r8 = r7.getString(r8)
            boolean r9 = android.text.TextUtils.isEmpty(r8)
            if (r9 != 0) goto L5b
            r0.add(r8)
        L5b:
            boolean r8 = r7.moveToNext()
            if (r8 != 0) goto L41
        L61:
            r7.close()
        L64:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.smartkit.util.ContactsUtils.getData1(android.content.ContentResolver, java.lang.String, java.lang.String):java.util.List");
    }

    public static ContactsUtils getInstance() {
        return contactsUtils;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003b, code lost:
    
        if (r2.isEmpty() == false) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0080, code lost:
    
        if (r0.moveToNext() != false) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x003e, code lost:
    
        r3 = getData1(r8, r1, "vnd.android.cursor.item/name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0049, code lost:
    
        if (r3.isEmpty() == false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004b, code lost:
    
        r3 = r2.get(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0058, code lost:
    
        r2 = r2.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0060, code lost:
    
        if (r2.hasNext() == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0062, code lost:
    
        r4 = r2.next();
        r5 = new com.czw.smartkit.util.ContactInfo();
        r5.setContactId(r1);
        r5.setName(r3);
        r5.setPhone(r4);
        com.czw.smartkit.util.ContactsUtils.contactsInfos.add(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0052, code lost:
    
        r3 = r3.get(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0082, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0025, code lost:
    
        if (r0.moveToFirst() != false) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0027, code lost:
    
        r1 = r0.getString(r0.getColumnIndex(com.liulishuo.filedownloader.model.FileDownloadModel.ID));
        r2 = getData1(r8, r1, "vnd.android.cursor.item/phone_v2");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void loadContactsList(android.content.Context r8) {
        /*
            r7 = this;
            java.util.ArrayList<com.czw.smartkit.util.ContactInfo> r0 = com.czw.smartkit.util.ContactsUtils.contactsInfos
            r0.clear()
            android.content.ContentResolver r8 = r8.getContentResolver()
            android.net.Uri r2 = android.provider.ContactsContract.Contacts.CONTENT_URI     // Catch: java.lang.SecurityException -> L86
            java.lang.String r0 = "_id"
            java.lang.String[] r3 = new java.lang.String[]{r0}     // Catch: java.lang.SecurityException -> L86
            r4 = 0
            r5 = 0
            r6 = 0
            r1 = r8
            android.database.Cursor r0 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.SecurityException -> L86
            if (r0 == 0) goto L8a
            int r1 = r0.getCount()     // Catch: java.lang.SecurityException -> L86
            if (r1 <= 0) goto L8a
            boolean r1 = r0.moveToFirst()     // Catch: java.lang.SecurityException -> L86
            if (r1 == 0) goto L82
        L27:
            java.lang.String r1 = "_id"
            int r1 = r0.getColumnIndex(r1)     // Catch: java.lang.SecurityException -> L86
            java.lang.String r1 = r0.getString(r1)     // Catch: java.lang.SecurityException -> L86
            java.lang.String r2 = "vnd.android.cursor.item/phone_v2"
            java.util.List r2 = getData1(r8, r1, r2)     // Catch: java.lang.SecurityException -> L86
            boolean r3 = r2.isEmpty()     // Catch: java.lang.SecurityException -> L86
            if (r3 == 0) goto L3e
            goto L7c
        L3e:
            java.lang.String r3 = "vnd.android.cursor.item/name"
            java.util.List r3 = getData1(r8, r1, r3)     // Catch: java.lang.SecurityException -> L86
            boolean r4 = r3.isEmpty()     // Catch: java.lang.SecurityException -> L86
            r5 = 0
            if (r4 == 0) goto L52
            java.lang.Object r3 = r2.get(r5)     // Catch: java.lang.SecurityException -> L86
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.SecurityException -> L86
            goto L58
        L52:
            java.lang.Object r3 = r3.get(r5)     // Catch: java.lang.SecurityException -> L86
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.SecurityException -> L86
        L58:
            java.util.Iterator r2 = r2.iterator()     // Catch: java.lang.SecurityException -> L86
        L5c:
            boolean r4 = r2.hasNext()     // Catch: java.lang.SecurityException -> L86
            if (r4 == 0) goto L7c
            java.lang.Object r4 = r2.next()     // Catch: java.lang.SecurityException -> L86
            java.lang.String r4 = (java.lang.String) r4     // Catch: java.lang.SecurityException -> L86
            com.czw.smartkit.util.ContactInfo r5 = new com.czw.smartkit.util.ContactInfo     // Catch: java.lang.SecurityException -> L86
            r5.<init>()     // Catch: java.lang.SecurityException -> L86
            r5.setContactId(r1)     // Catch: java.lang.SecurityException -> L86
            r5.setName(r3)     // Catch: java.lang.SecurityException -> L86
            r5.setPhone(r4)     // Catch: java.lang.SecurityException -> L86
            java.util.ArrayList<com.czw.smartkit.util.ContactInfo> r4 = com.czw.smartkit.util.ContactsUtils.contactsInfos     // Catch: java.lang.SecurityException -> L86
            r4.add(r5)     // Catch: java.lang.SecurityException -> L86
            goto L5c
        L7c:
            boolean r1 = r0.moveToNext()     // Catch: java.lang.SecurityException -> L86
            if (r1 != 0) goto L27
        L82:
            r0.close()     // Catch: java.lang.SecurityException -> L86
            goto L8a
        L86:
            r8 = move-exception
            r8.printStackTrace()
        L8a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.smartkit.util.ContactsUtils.loadContactsList(android.content.Context):void");
    }
}
