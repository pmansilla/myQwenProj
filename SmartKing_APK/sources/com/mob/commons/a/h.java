package com.mob.commons.a;

import android.os.Message;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.SmltHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/* compiled from: DClt.java */
/* loaded from: classes.dex */
public class h extends d {
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00ce, code lost:
    
        if (r0.size() == r6) goto L67;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008f A[Catch: Throwable -> 0x0323, TryCatch #5 {Throwable -> 0x0323, blocks: (B:3:0x0006, B:26:0x008f, B:27:0x0094, B:35:0x00a6, B:37:0x00ac, B:39:0x00b2, B:41:0x00b8, B:43:0x00be, B:45:0x00c4, B:47:0x00ca, B:49:0x00fd, B:51:0x02d2, B:52:0x02e3, B:56:0x02ef, B:59:0x02f6, B:60:0x031f, B:64:0x00d0, B:67:0x0085), top: B:2:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x009a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x02d2 A[Catch: Throwable -> 0x0323, TryCatch #5 {Throwable -> 0x0323, blocks: (B:3:0x0006, B:26:0x008f, B:27:0x0094, B:35:0x00a6, B:37:0x00ac, B:39:0x00b2, B:41:0x00b8, B:43:0x00be, B:45:0x00c4, B:47:0x00ca, B:49:0x00fd, B:51:0x02d2, B:52:0x02e3, B:56:0x02ef, B:59:0x02f6, B:60:0x031f, B:64:0x00d0, B:67:0x0085), top: B:2:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x02eb  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x02f6 A[Catch: Throwable -> 0x0323, TryCatch #5 {Throwable -> 0x0323, blocks: (B:3:0x0006, B:26:0x008f, B:27:0x0094, B:35:0x00a6, B:37:0x00ac, B:39:0x00b2, B:41:0x00b8, B:43:0x00be, B:45:0x00c4, B:47:0x00ca, B:49:0x00fd, B:51:0x02d2, B:52:0x02e3, B:56:0x02ef, B:59:0x02f6, B:60:0x031f, B:64:0x00d0, B:67:0x0085), top: B:2:0x0006 }] */
    /* JADX WARN: Type inference failed for: r10v16, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r10v17 */
    /* JADX WARN: Type inference failed for: r10v18 */
    /* JADX WARN: Type inference failed for: r10v19, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r10v20 */
    /* JADX WARN: Type inference failed for: r10v25 */
    /* JADX WARN: Type inference failed for: r10v26 */
    /* JADX WARN: Type inference failed for: r10v4 */
    /* JADX WARN: Type inference failed for: r10v5 */
    /* JADX WARN: Type inference failed for: r16v0, types: [com.mob.commons.a.h] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private long a(java.util.HashMap<java.lang.String, java.lang.Object> r17) {
        /*
            Method dump skipped, instructions count: 812
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.h.a(java.util.HashMap):long");
    }

    private void a(HashMap<String, Object> hashMap, String str) {
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("type", str);
        hashMap2.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap);
        long a = com.mob.commons.b.a();
        hashMap2.put("datetime", Long.valueOf(a));
        com.mob.commons.c.a().a(a, hashMap2);
    }

    private void b(HashMap<String, Object> hashMap) {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.lecd");
        if (dataCacheFile != null && (hashMap == null || hashMap.isEmpty())) {
            dataCacheFile.delete();
            return;
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataCacheFile));
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private void h() {
        Object[] i = i();
        StringBuilder sb = new StringBuilder();
        if (i != null && i.length == 2) {
            sb.append(i[0]);
        }
        SmltHelper smltHelper = new SmltHelper();
        int checkBaseband = smltHelper.checkBaseband(MobSDK.getContext());
        sb.append(checkBaseband);
        int checkBoard = smltHelper.checkBoard(MobSDK.getContext());
        sb.append(checkBoard);
        int checkPlatform = smltHelper.checkPlatform(MobSDK.getContext());
        sb.append(checkPlatform);
        int checkFlavor = smltHelper.checkFlavor(MobSDK.getContext());
        sb.append(checkFlavor);
        int checkCgroup = smltHelper.checkCgroup();
        sb.append(checkCgroup);
        int checkBluetooth = smltHelper.checkBluetooth(MobSDK.getContext());
        sb.append(checkBluetooth);
        int checkImei = smltHelper.checkImei(MobSDK.getContext());
        sb.append(checkImei);
        int checkCommonApp = smltHelper.checkCommonApp(MobSDK.getContext());
        sb.append(checkCommonApp);
        int checkCpuInfo = smltHelper.checkCpuInfo();
        sb.append(checkCpuInfo);
        String MD5 = Data.MD5(sb.toString());
        String m = com.mob.commons.i.m();
        if (MD5 == null || !MD5.equals(m)) {
            com.mob.commons.i.h(MD5);
            HashMap<String, Object> hashMap = new HashMap<>();
            if (i != null && i.length == 2 && (i[1] instanceof HashMap)) {
                hashMap.putAll((HashMap) i[1]);
            }
            hashMap.put("ckBaseband", Integer.valueOf(checkBaseband));
            hashMap.put("ckBoard", Integer.valueOf(checkBoard));
            hashMap.put("ckPlatform", Integer.valueOf(checkPlatform));
            hashMap.put("ckFlavor", Integer.valueOf(checkFlavor));
            hashMap.put("ckCgroup", Integer.valueOf(checkCgroup));
            hashMap.put("ckbmt", Integer.valueOf(checkBluetooth));
            hashMap.put("ckiemt", Integer.valueOf(checkImei));
            hashMap.put("ckCommonapp", Integer.valueOf(checkCommonApp));
            hashMap.put("ckCpuinfo", Integer.valueOf(checkCpuInfo));
            a(hashMap, "SIMUMT");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0066, code lost:
    
        if (r8.exists() != false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x003e, code lost:
    
        if (r8.exists() != false) goto L9;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x007e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0062 A[Catch: Throwable -> 0x006a, TRY_LEAVE, TryCatch #1 {Throwable -> 0x006a, blocks: (B:12:0x004e, B:14:0x0062), top: B:11:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0094 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a7 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b5 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c3 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00d1 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00df A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00ed A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00fb A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0109 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0117 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0125 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0133 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0141 A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x014f A[Catch: Throwable -> 0x008e, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x015d A[Catch: Throwable -> 0x008e, TRY_LEAVE, TryCatch #2 {Throwable -> 0x008e, blocks: (B:141:0x007e, B:23:0x0094, B:28:0x00a7, B:33:0x00b5, B:38:0x00c3, B:43:0x00d1, B:48:0x00df, B:53:0x00ed, B:58:0x00fb, B:63:0x0109, B:68:0x0117, B:73:0x0125, B:78:0x0133, B:83:0x0141, B:88:0x014f, B:93:0x015d), top: B:140:0x007e }] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x017c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.Object[] i() {
        /*
            Method dump skipped, instructions count: 502
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.h.i():java.lang.Object[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.HashMap<java.lang.String, java.lang.Object> j() {
        /*
            r5 = this;
            android.content.Context r0 = com.mob.MobSDK.getContext()
            java.lang.String r1 = "comm/dbs/.lecd"
            java.io.File r0 = com.mob.tools.utils.ResHelper.getDataCacheFile(r0, r1)
            if (r0 == 0) goto L18
            boolean r1 = r0.exists()
            if (r1 != 0) goto L18
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            return r0
        L18:
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L32
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L32
            java.io.ObjectInputStream r0 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L32
            r0.<init>(r2)     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L32
            java.lang.Object r2 = r0.readObject()     // Catch: java.lang.Throwable -> L2e java.lang.Throwable -> L46
            java.util.HashMap r2 = (java.util.HashMap) r2     // Catch: java.lang.Throwable -> L2e java.lang.Throwable -> L46
            r5.a(r0)
            r1 = r2
            goto L3e
        L2e:
            r2 = move-exception
            goto L34
        L30:
            r0 = move-exception
            goto L4a
        L32:
            r2 = move-exception
            r0 = r1
        L34:
            com.mob.tools.log.NLog r3 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L46
            r3.d(r2)     // Catch: java.lang.Throwable -> L46
            r5.a(r0)
        L3e:
            if (r1 != 0) goto L45
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
        L45:
            return r1
        L46:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L4a:
            r5.a(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.h.j():java.util.HashMap");
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.lesd_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (message.what == 1) {
            if (com.mob.commons.b.I() > 0) {
                DeviceHelper.getInstance(MobSDK.getContext()).getBatteryState(new ReflectHelper.ReflectRunnable<HashMap<String, Object>, Void>() { // from class: com.mob.commons.a.h.1
                    @Override // com.mob.tools.utils.ReflectHelper.ReflectRunnable
                    /* renamed from: a, reason: merged with bridge method [inline-methods] */
                    public Void run(HashMap<String, Object> hashMap) {
                        Message message2 = new Message();
                        message2.obj = hashMap;
                        message2.what = 2;
                        h.this.b(message2);
                        return null;
                    }
                });
            }
        } else {
            if (message.what == 2) {
                long a = a((HashMap<String, Object>) message.obj);
                if (a == 0) {
                    a = com.mob.commons.b.I() * 1000;
                }
                a(1, a);
                return;
            }
            if (message.what == 3) {
                long K = com.mob.commons.b.K();
                if (K > 0) {
                    h();
                    a(3, K * 1000);
                }
            }
        }
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.I() > 0 || com.mob.commons.b.K() > 0;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(1);
        b(3);
    }
}
