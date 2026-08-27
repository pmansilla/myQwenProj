package com.mob.commons.authorize;

import android.text.TextUtils;
import android.util.Base64;
import com.autonavi.amap.mapcore.AeUtil;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.commons.LockAction;
import com.mob.commons.MobProduct;
import com.mob.commons.MobProductCollector;
import com.mob.commons.b;
import com.mob.commons.b.d;
import com.mob.commons.e;
import com.mob.commons.i;
import com.mob.commons.j;
import com.mob.commons.k;
import com.mob.commons.l;
import com.mob.tools.MobLog;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.FileLocker;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.tencent.bugly.BuglyStrategy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Authorizer.java */
/* loaded from: classes.dex */
public final class a {
    private static final String a = j.b("devs.data.mob.com");

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0074 A[Catch: all -> 0x0084, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0013, B:7:0x0019, B:10:0x002d, B:12:0x0035, B:15:0x0074, B:16:0x0077, B:20:0x0040, B:22:0x0048, B:24:0x0052, B:26:0x005b, B:28:0x0065, B:29:0x0069, B:31:0x006f, B:33:0x007b), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized java.lang.String a(com.mob.commons.MobProduct r10, boolean r11, java.lang.String r12) {
        /*
            r9 = this;
            monitor-enter(r9)
            android.content.Context r11 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> L84
            com.mob.tools.utils.DeviceHelper r11 = com.mob.tools.utils.DeviceHelper.getInstance(r11)     // Catch: java.lang.Throwable -> L84
            boolean r0 = r11.getSdcardState()     // Catch: java.lang.Throwable -> L84
            java.util.HashMap r1 = r9.e()     // Catch: java.lang.Throwable -> L84
            if (r1 == 0) goto L7b
            int r2 = r1.size()     // Catch: java.lang.Throwable -> L84
            if (r2 <= 0) goto L7b
            java.lang.String r2 = "duid"
            java.lang.Object r2 = r1.get(r2)     // Catch: java.lang.Throwable -> L84
            java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Throwable -> L84
            long r3 = com.mob.commons.i.F()     // Catch: java.lang.Throwable -> L84
            boolean r5 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Throwable -> L84
            if (r5 == 0) goto L2c
            goto L2d
        L2c:
            r12 = r2
        L2d:
            boolean r2 = android.text.TextUtils.isEmpty(r12)     // Catch: java.lang.Throwable -> L84
            r5 = 1
            r6 = 0
            if (r2 == 0) goto L40
            java.lang.String r12 = r9.a(r12)     // Catch: java.lang.Throwable -> L84
            java.lang.String r11 = "duid"
            r1.put(r11, r12)     // Catch: java.lang.Throwable -> L84
        L3e:
            r6 = 1
            goto L72
        L40:
            long r7 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L84
            int r2 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r2 < 0) goto L59
            java.lang.String r11 = r9.c(r12)     // Catch: java.lang.Throwable -> L84
            boolean r0 = android.text.TextUtils.isEmpty(r11)     // Catch: java.lang.Throwable -> L84
            if (r0 != 0) goto L72
            java.lang.String r12 = "duid"
            r1.put(r12, r11)     // Catch: java.lang.Throwable -> L84
            r12 = r11
            goto L3e
        L59:
            if (r0 == 0) goto L72
            java.lang.String r0 = r11.getWAbcd(r6)     // Catch: java.lang.Throwable -> L84
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Throwable -> L84
            if (r2 == 0) goto L69
            r11.saveWabcd(r12, r6)     // Catch: java.lang.Throwable -> L84
            goto L72
        L69:
            boolean r0 = r12.equals(r0)     // Catch: java.lang.Throwable -> L84
            if (r0 != 0) goto L72
            r11.saveWabcd(r12, r6)     // Catch: java.lang.Throwable -> L84
        L72:
            if (r6 == 0) goto L77
            r9.a(r1, r5)     // Catch: java.lang.Throwable -> L84
        L77:
            r9.a(r1, r6, r10)     // Catch: java.lang.Throwable -> L84
            goto L82
        L7b:
            java.lang.String r12 = r9.a(r12)     // Catch: java.lang.Throwable -> L84
            r9.a(r12, r10)     // Catch: java.lang.Throwable -> L84
        L82:
            monitor-exit(r9)
            return r12
        L84:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.authorize.a.a(com.mob.commons.MobProduct, boolean, java.lang.String):java.lang.String");
    }

    private String a(String str) {
        if (TextUtils.isEmpty(str) && DeviceAuthorizer.a != null) {
            return DeviceAuthorizer.a;
        }
        String b = b(str);
        return b == null ? str == null ? a(true) : str : b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0060 A[Catch: Throwable -> 0x00d0, TryCatch #0 {Throwable -> 0x00d0, blocks: (B:3:0x0009, B:7:0x0019, B:9:0x0023, B:21:0x005a, B:23:0x0060, B:26:0x0070, B:33:0x008e, B:35:0x0094, B:40:0x0087, B:41:0x006c, B:42:0x009c, B:45:0x00cc, B:48:0x0056, B:49:0x004e, B:50:0x0046, B:51:0x003e, B:52:0x002e, B:30:0x0078, B:36:0x0080), top: B:2:0x0009, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0056 A[Catch: Throwable -> 0x00d0, TryCatch #0 {Throwable -> 0x00d0, blocks: (B:3:0x0009, B:7:0x0019, B:9:0x0023, B:21:0x005a, B:23:0x0060, B:26:0x0070, B:33:0x008e, B:35:0x0094, B:40:0x0087, B:41:0x006c, B:42:0x009c, B:45:0x00cc, B:48:0x0056, B:49:0x004e, B:50:0x0046, B:51:0x003e, B:52:0x002e, B:30:0x0078, B:36:0x0080), top: B:2:0x0009, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x004e A[Catch: Throwable -> 0x00d0, TryCatch #0 {Throwable -> 0x00d0, blocks: (B:3:0x0009, B:7:0x0019, B:9:0x0023, B:21:0x005a, B:23:0x0060, B:26:0x0070, B:33:0x008e, B:35:0x0094, B:40:0x0087, B:41:0x006c, B:42:0x009c, B:45:0x00cc, B:48:0x0056, B:49:0x004e, B:50:0x0046, B:51:0x003e, B:52:0x002e, B:30:0x0078, B:36:0x0080), top: B:2:0x0009, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0046 A[Catch: Throwable -> 0x00d0, TryCatch #0 {Throwable -> 0x00d0, blocks: (B:3:0x0009, B:7:0x0019, B:9:0x0023, B:21:0x005a, B:23:0x0060, B:26:0x0070, B:33:0x008e, B:35:0x0094, B:40:0x0087, B:41:0x006c, B:42:0x009c, B:45:0x00cc, B:48:0x0056, B:49:0x004e, B:50:0x0046, B:51:0x003e, B:52:0x002e, B:30:0x0078, B:36:0x0080), top: B:2:0x0009, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x003e A[Catch: Throwable -> 0x00d0, TryCatch #0 {Throwable -> 0x00d0, blocks: (B:3:0x0009, B:7:0x0019, B:9:0x0023, B:21:0x005a, B:23:0x0060, B:26:0x0070, B:33:0x008e, B:35:0x0094, B:40:0x0087, B:41:0x006c, B:42:0x009c, B:45:0x00cc, B:48:0x0056, B:49:0x004e, B:50:0x0046, B:51:0x003e, B:52:0x002e, B:30:0x0078, B:36:0x0080), top: B:2:0x0009, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String a(boolean r11) {
        /*
            r10 = this;
            android.content.Context r0 = com.mob.MobSDK.getContext()
            com.mob.tools.utils.DeviceHelper r0 = com.mob.tools.utils.DeviceHelper.getInstance(r0)
            r1 = 0
            boolean r2 = r0.getSdcardState()     // Catch: java.lang.Throwable -> Ld0
            r3 = 0
            java.lang.String r4 = r0.getWAbcd(r3)     // Catch: java.lang.Throwable -> Ld0
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Throwable -> Ld0
            if (r5 != 0) goto L19
            return r4
        L19:
            java.lang.String r4 = r0.getModel()     // Catch: java.lang.Throwable -> Ld0
            boolean r5 = com.mob.commons.a.c()     // Catch: java.lang.Throwable -> Ld0
            if (r5 == 0) goto L2e
            boolean r5 = com.mob.commons.a.d()     // Catch: java.lang.Throwable -> Ld0
            if (r5 == 0) goto L2a
            goto L2e
        L2a:
            r5 = r1
            r6 = r5
            r7 = r6
            goto L3a
        L2e:
            java.lang.String r5 = r0.getIMEI()     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r6 = r0.getMacAddress()     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r7 = r0.getSerialno()     // Catch: java.lang.Throwable -> Ld0
        L3a:
            if (r4 != 0) goto L3e
            r4 = r1
            goto L42
        L3e:
            java.lang.String r4 = r4.trim()     // Catch: java.lang.Throwable -> Ld0
        L42:
            if (r6 != 0) goto L46
            r6 = r1
            goto L4a
        L46:
            java.lang.String r6 = r6.trim()     // Catch: java.lang.Throwable -> Ld0
        L4a:
            if (r5 != 0) goto L4e
            r5 = r1
            goto L52
        L4e:
            java.lang.String r5 = r5.trim()     // Catch: java.lang.Throwable -> Ld0
        L52:
            if (r7 != 0) goto L56
            r7 = r1
            goto L5a
        L56:
            java.lang.String r7 = r7.trim()     // Catch: java.lang.Throwable -> Ld0
        L5a:
            boolean r8 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> Ld0
            if (r8 == 0) goto L9c
            android.content.Context r8 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r8 = com.mob.commons.b.d.c(r8)     // Catch: java.lang.Throwable -> Ld0
            if (r8 != 0) goto L6c
            r8 = r1
            goto L70
        L6c:
            java.lang.String r8 = r8.trim()     // Catch: java.lang.Throwable -> Ld0
        L70:
            boolean r9 = android.text.TextUtils.isEmpty(r8)     // Catch: java.lang.Throwable -> Ld0
            if (r9 != 0) goto L78
            r5 = r8
            goto L9c
        L78:
            java.lang.String r8 = r0.getAdvertisingID()     // Catch: java.lang.Throwable -> L86
            if (r8 != 0) goto L80
            r5 = r1
            goto L8e
        L80:
            java.lang.String r8 = r8.trim()     // Catch: java.lang.Throwable -> L86
            r5 = r8
            goto L8e
        L86:
            r8 = move-exception
            com.mob.tools.log.NLog r9 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> Ld0
            r9.w(r8)     // Catch: java.lang.Throwable -> Ld0
        L8e:
            boolean r8 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> Ld0
            if (r8 == 0) goto L9c
            java.util.UUID r5 = java.util.UUID.randomUUID()     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> Ld0
        L9c:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld0
            r8.<init>()     // Catch: java.lang.Throwable -> Ld0
            r8.append(r4)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r4 = ":"
            r8.append(r4)     // Catch: java.lang.Throwable -> Ld0
            r8.append(r5)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r4 = ":"
            r8.append(r4)     // Catch: java.lang.Throwable -> Ld0
            r8.append(r6)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r4 = ":"
            r8.append(r4)     // Catch: java.lang.Throwable -> Ld0
            r8.append(r7)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r4 = r8.toString()     // Catch: java.lang.Throwable -> Ld0
            byte[] r4 = com.mob.tools.utils.Data.SHA1(r4)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r4 = com.mob.tools.utils.Data.byteToHex(r4)     // Catch: java.lang.Throwable -> Ld0
            if (r11 == 0) goto Lcf
            if (r2 == 0) goto Lcf
            r0.saveWabcd(r4, r3)     // Catch: java.lang.Throwable -> Ld0
        Lcf:
            return r4
        Ld0:
            r11 = move-exception
            com.mob.tools.log.NLog r0 = com.mob.tools.MobLog.getInstance()
            r0.w(r11)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.authorize.a.a(boolean):java.lang.String");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3 */
    private static HashMap<String, Object> a(File file) {
        ObjectInputStream objectInputStream;
        try {
            if (file.exists()) {
                try {
                    if (file.isFile()) {
                        try {
                            objectInputStream = new ObjectInputStream(new FileInputStream(file));
                            try {
                                HashMap<String, Object> hashMap = (HashMap) objectInputStream.readObject();
                                try {
                                    objectInputStream.close();
                                } catch (Throwable unused) {
                                }
                                return hashMap;
                            } catch (Throwable th) {
                                th = th;
                                MobLog.getInstance().w(th);
                                if (objectInputStream != null) {
                                    objectInputStream.close();
                                }
                                return null;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            objectInputStream = null;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }
        } catch (Throwable unused2) {
        }
        return null;
    }

    private static HashMap<String, Object> a(String str, byte[] bArr) {
        try {
            return new Hashon().fromJson(Data.AES128Decode(str, bArr));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return new HashMap<>();
        }
    }

    private void a(long j) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.digap")));
            dataOutputStream.writeLong(j);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006f A[Catch: Throwable -> 0x00e0, TryCatch #0 {Throwable -> 0x00e0, blocks: (B:5:0x0003, B:7:0x001b, B:11:0x0029, B:13:0x002f, B:15:0x0039, B:18:0x0053, B:22:0x005e, B:24:0x0064, B:28:0x006f, B:31:0x0080, B:34:0x0089, B:36:0x008f, B:38:0x0095, B:41:0x009b, B:43:0x00a1, B:45:0x00a7, B:50:0x00b2, B:54:0x0024), top: B:4:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b2 A[Catch: Throwable -> 0x00e0, TRY_LEAVE, TryCatch #0 {Throwable -> 0x00e0, blocks: (B:5:0x0003, B:7:0x001b, B:11:0x0029, B:13:0x002f, B:15:0x0039, B:18:0x0053, B:22:0x005e, B:24:0x0064, B:28:0x006f, B:31:0x0080, B:34:0x0089, B:36:0x008f, B:38:0x0095, B:41:0x009b, B:43:0x00a1, B:45:0x00a7, B:50:0x00b2, B:54:0x0024), top: B:4:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void a(android.content.Context r11) {
        /*
            if (r11 != 0) goto L3
            return
        L3:
            java.lang.String r0 = "comm/dbs/.duid"
            java.io.File r0 = com.mob.tools.utils.ResHelper.getCacheRootFile(r11, r0)     // Catch: java.lang.Throwable -> Le0
            java.util.HashMap r1 = a(r0)     // Catch: java.lang.Throwable -> Le0
            android.content.Context r2 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r3 = "comm/dbs/.duid"
            java.io.File r2 = com.mob.tools.utils.ResHelper.getDataCacheFile(r2, r3)     // Catch: java.lang.Throwable -> Le0
            r3 = 0
            r4 = 1
            if (r1 == 0) goto L24
            boolean r5 = r1.isEmpty()     // Catch: java.lang.Throwable -> Le0
            if (r5 == 0) goto L22
            goto L24
        L22:
            r5 = 1
            goto L29
        L24:
            java.util.HashMap r1 = b(r2)     // Catch: java.lang.Throwable -> Le0
            r5 = 0
        L29:
            com.mob.tools.utils.DeviceHelper r6 = com.mob.tools.utils.DeviceHelper.getInstance(r11)     // Catch: java.lang.Throwable -> Le0
            if (r1 == 0) goto Lb0
            java.lang.String r7 = "deviceInfo"
            java.lang.Object r1 = r1.get(r7)     // Catch: java.lang.Throwable -> Le0
            java.util.HashMap r1 = (java.util.HashMap) r1     // Catch: java.lang.Throwable -> Le0
            if (r1 == 0) goto Lb0
            java.lang.String r7 = "model"
            java.lang.Object r7 = r1.get(r7)     // Catch: java.lang.Throwable -> Le0
            java.lang.String r7 = (java.lang.String) r7     // Catch: java.lang.Throwable -> Le0
            java.lang.String r8 = "factory"
            java.lang.Object r1 = r1.get(r8)     // Catch: java.lang.Throwable -> Le0
            java.lang.String r1 = (java.lang.String) r1     // Catch: java.lang.Throwable -> Le0
            java.lang.String r8 = "unknown"
            boolean r8 = r8.equalsIgnoreCase(r7)     // Catch: java.lang.Throwable -> Le0
            r9 = 0
            if (r8 == 0) goto L53
            r7 = r9
        L53:
            java.lang.String r8 = "unknown"
            boolean r8 = r8.equalsIgnoreCase(r1)     // Catch: java.lang.Throwable -> Le0
            if (r8 == 0) goto L5c
            r1 = r9
        L5c:
            if (r5 == 0) goto L6c
            boolean r5 = android.text.TextUtils.isEmpty(r7)     // Catch: java.lang.Throwable -> Le0
            if (r5 != 0) goto L6a
            boolean r5 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> Le0
            if (r5 == 0) goto L6c
        L6a:
            r5 = 1
            goto L6d
        L6c:
            r5 = 0
        L6d:
            if (r5 != 0) goto Lb0
            java.lang.String r5 = r6.getModel()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r8 = r6.getManufacturer()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r10 = "unknown"
            boolean r10 = r10.equalsIgnoreCase(r5)     // Catch: java.lang.Throwable -> Le0
            if (r10 == 0) goto L80
            r5 = r9
        L80:
            java.lang.String r10 = "unknown"
            boolean r10 = r10.equalsIgnoreCase(r8)     // Catch: java.lang.Throwable -> Le0
            if (r10 == 0) goto L89
            r8 = r9
        L89:
            boolean r9 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> Le0
            if (r9 != 0) goto L9b
            boolean r9 = android.text.TextUtils.isEmpty(r7)     // Catch: java.lang.Throwable -> Le0
            if (r9 != 0) goto L9b
            boolean r5 = r5.equalsIgnoreCase(r7)     // Catch: java.lang.Throwable -> Le0
            if (r5 == 0) goto Lad
        L9b:
            boolean r5 = android.text.TextUtils.isEmpty(r8)     // Catch: java.lang.Throwable -> Le0
            if (r5 != 0) goto Laf
            boolean r5 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> Le0
            if (r5 != 0) goto Laf
            boolean r1 = r8.equalsIgnoreCase(r1)     // Catch: java.lang.Throwable -> Le0
            if (r1 != 0) goto Laf
        Lad:
            r5 = 1
            goto Lb0
        Laf:
            r5 = 0
        Lb0:
            if (r5 == 0) goto Le8
            r0.delete()     // Catch: java.lang.Throwable -> Le0
            r2.delete()     // Catch: java.lang.Throwable -> Le0
            r6.removeWABCD()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r0 = "comm/.di"
            java.io.File r0 = com.mob.tools.utils.ResHelper.getCacheRootFile(r11, r0)     // Catch: java.lang.Throwable -> Le0
            r0.delete()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r0 = ".dk"
            java.io.File r0 = com.mob.tools.utils.ResHelper.getCacheRootFile(r11, r0)     // Catch: java.lang.Throwable -> Le0
            r0.delete()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r0 = ".mcw"
            java.io.File r0 = com.mob.tools.utils.ResHelper.getCacheRootFile(r11, r0)     // Catch: java.lang.Throwable -> Le0
            r0.delete()     // Catch: java.lang.Throwable -> Le0
            java.lang.String r0 = ".slw"
            java.io.File r11 = com.mob.tools.utils.ResHelper.getCacheRootFile(r11, r0)     // Catch: java.lang.Throwable -> Le0
            r11.delete()     // Catch: java.lang.Throwable -> Le0
            goto Le8
        Le0:
            r11 = move-exception
            com.mob.tools.log.NLog r0 = com.mob.tools.MobLog.getInstance()
            r0.w(r11)
        Le8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.authorize.a.a(android.content.Context):void");
    }

    private static void a(File file, HashMap<String, Object> hashMap) {
        try {
            byte[] a2 = a(DeviceHelper.getInstance(MobSDK.getContext()).getModel(), hashMap);
            FileChannel channel = new FileOutputStream(file).getChannel();
            channel.write(ByteBuffer.wrap(a2));
            channel.force(true);
            channel.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.mob.commons.authorize.a$5] */
    private void a(final String str, final MobProduct mobProduct) {
        new Thread() { // from class: com.mob.commons.authorize.a.5
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (a.a) {
                    try {
                        HashMap hashMap = new HashMap();
                        hashMap.put("duid", str);
                        a.this.a((HashMap<String, Object>) hashMap, str);
                        a.this.a((HashMap<String, Object>) hashMap, mobProduct);
                        a.this.c((HashMap<String, Object>) hashMap);
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th);
                    }
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(HashMap<String, Object> hashMap, String str) {
        try {
            if (b.aq() && !b.L()) {
                String b = l.a().b();
                b(hashMap);
                HashMap hashMap2 = (HashMap) hashMap.get("deviceInfo");
                HashMap hashMap3 = new HashMap();
                hashMap3.put("token", b);
                for (Map.Entry entry : hashMap2.entrySet()) {
                    hashMap3.put(entry.getKey(), entry.getValue());
                }
                try {
                    hashMap3.put("carrier", Integer.valueOf(ResHelper.parseInt(String.valueOf(hashMap3.get("carrier")))));
                } catch (Throwable unused) {
                }
                ArrayList arrayList = (ArrayList) hashMap3.remove("ansmt");
                if (arrayList != null && !arrayList.isEmpty()) {
                    hashMap3.put("anmt", arrayList.get(arrayList.size() - 1));
                }
                DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
                hashMap3.put("duid", str);
                String[] queryIMEI = deviceHelper.queryIMEI();
                if (queryIMEI != null && queryIMEI.length > 0) {
                    hashMap3.put("iemtarr", queryIMEI);
                }
                try {
                    HashMap<String, String> listNetworkHardware = deviceHelper.listNetworkHardware();
                    if (listNetworkHardware != null && !listNetworkHardware.isEmpty()) {
                        ArrayList arrayList2 = new ArrayList();
                        for (Map.Entry<String, String> entry2 : listNetworkHardware.entrySet()) {
                            HashMap hashMap4 = new HashMap();
                            hashMap4.put("ss", entry2.getKey());
                            hashMap4.put(Dic.MAC, entry2.getValue());
                            arrayList2.add(hashMap4);
                        }
                        hashMap3.put("mcmtarr", arrayList2);
                    }
                } catch (Throwable unused2) {
                }
                HashMap<String, Long> memoryInfo = deviceHelper.getMemoryInfo();
                HashMap<String, HashMap<String, Long>> sizeInfo = deviceHelper.getSizeInfo();
                if (memoryInfo != null) {
                    hashMap3.put("ram", memoryInfo.get(FileDownloadModel.TOTAL));
                }
                if (sizeInfo != null) {
                    HashMap<String, Long> hashMap5 = sizeInfo.get("sdcard");
                    if (hashMap5 != null) {
                        hashMap3.put("sdcardStorage", hashMap5.get(FileDownloadModel.TOTAL));
                    }
                    HashMap<String, Long> hashMap6 = sizeInfo.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
                    if (hashMap6 != null) {
                        hashMap3.put("dataStorage", hashMap6.get(FileDownloadModel.TOTAL));
                    }
                }
                hashMap3.put("romImg", deviceHelper.getMIUIVersion());
                Hashon hashon = new Hashon();
                String encodeToString = Base64.encodeToString(Data.AES128Encode(f(), hashon.fromHashMap(hashMap3)), 2);
                ArrayList<KVPair<String>> arrayList3 = new ArrayList<>();
                arrayList3.add(new KVPair<>("m", encodeToString));
                NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
                networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
                networkTimeOut.connectionTimeout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
                NetworkHelper networkHelper = new NetworkHelper();
                String str2 = a + "/dinfo";
                ArrayList<KVPair<String>> arrayList4 = new ArrayList<>();
                arrayList4.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
                arrayList4.add(new KVPair<>(k.a(68), d.d(MobSDK.getContext())));
                if ("200".equals(String.valueOf(hashon.fromJson(networkHelper.httpPost(str2, arrayList3, (KVPair<String>) null, arrayList4, networkTimeOut)).get("status")))) {
                    i.i(str);
                    a(b.a() + b.O());
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(HashMap<String, Object> hashMap, boolean z) {
        try {
            if (!z) {
                try {
                    String str = (String) hashMap.get("duid");
                    DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
                    if (deviceHelper.getSdcardState()) {
                        HashMap<String, Object> e = e();
                        if (e != null) {
                            String str2 = (String) ResHelper.forceCast(e.get("duid"), null);
                            if (!TextUtils.isEmpty(str2) && !str2.equals(str)) {
                                hashMap.put("duid", str2);
                            }
                        } else {
                            String wAbcd = deviceHelper.getWAbcd(0);
                            if (!TextUtils.isEmpty(wAbcd) && !wAbcd.equals(str)) {
                                hashMap.put("duid", wAbcd);
                            }
                        }
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                    return;
                }
            }
            File c = c();
            a(c, hashMap);
            File d = d();
            if (c.getAbsolutePath().equals(d.getAbsolutePath())) {
                return;
            }
            d.delete();
            ResHelper.copyFile(c.getAbsolutePath(), d.getAbsolutePath());
        } catch (Throwable th2) {
            throw th2;
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.mob.commons.authorize.a$2] */
    private void a(final HashMap<String, Object> hashMap, final boolean z, final MobProduct mobProduct) {
        new Thread() { // from class: com.mob.commons.authorize.a.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (a.a) {
                    try {
                        boolean z2 = z;
                        if (a.this.a((HashMap<String, Object>) hashMap) || a.this.g()) {
                            a.this.a((HashMap<String, Object>) hashMap, (String) hashMap.get("duid"));
                            z2 = true;
                        }
                        if (a.this.a((HashMap<String, Object>) hashMap, mobProduct)) {
                            z2 = true;
                        }
                        if (z2) {
                            a.this.c((HashMap<String, Object>) hashMap);
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th);
                    }
                }
            }
        }.start();
    }

    private boolean a(MobProduct mobProduct, HashMap<String, Object> hashMap) throws Throwable {
        if (!b.aq() || b.L()) {
            return false;
        }
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("product", mobProduct.getProductTag()));
        String str = (String) hashMap.get("duid");
        arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
        arrayList.add(new KVPair<>("duid", str));
        arrayList.add(new KVPair<>("apppkg", String.valueOf(deviceHelper.getPackageName())));
        arrayList.add(new KVPair<>("appver", String.valueOf(deviceHelper.getAppVersion())));
        arrayList.add(new KVPair<>("sdkver", String.valueOf(mobProduct.getSdkver())));
        arrayList.add(new KVPair<>("network", String.valueOf(deviceHelper.getDetailNetworkTypeForStatic())));
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        networkTimeOut.connectionTimeout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        NetworkHelper networkHelper = new NetworkHelper();
        String str2 = a + "/dsign";
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
        arrayList2.add(new KVPair<>(k.a(68), d.d(MobSDK.getContext())));
        HashMap fromJson = new Hashon().fromJson(networkHelper.httpPost(str2, arrayList, (KVPair<String>) null, arrayList2, networkTimeOut));
        if ("true".equals(String.valueOf(fromJson.get("reup")))) {
            a(hashMap, str);
        }
        if (!"200".equals(String.valueOf(fromJson.get("status")))) {
            return false;
        }
        ((HashMap) ((HashMap) hashMap.get("appInfo")).get(deviceHelper.getPackageName())).put(mobProduct.getProductTag(), MobSDK.getAppkey());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(HashMap<String, Object> hashMap) {
        boolean z;
        HashMap hashMap2 = (HashMap) hashMap.get("deviceInfo");
        if (hashMap2 == null) {
            return true;
        }
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        Object obj = hashMap.get("duid");
        String n = i.n();
        if (TextUtils.isEmpty(n) && obj != null) {
            i.i(String.valueOf(obj));
        }
        if (!TextUtils.isEmpty(n) && obj != null && !n.equals(obj)) {
            return true;
        }
        Object obj2 = hashMap2.get("admt");
        String str = null;
        try {
            str = deviceHelper.getAdvertisingID();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        if (str != null && !str.equals(obj2)) {
            return true;
        }
        System.currentTimeMillis();
        if (b.aa()) {
            Object obj3 = hashMap2.get("pnmt");
            String ln = deviceHelper.getLN();
            if (ln != null && !ln.equals(obj3)) {
                return true;
            }
        }
        Object obj4 = hashMap2.get(Dic.SIM_SERIAL_NUMBER);
        String simSerialNumber = deviceHelper.getSimSerialNumber();
        if (simSerialNumber != null && !simSerialNumber.equals(obj4)) {
            return true;
        }
        Object obj5 = hashMap2.get(Dic.IMEI);
        String imei = deviceHelper.getIMEI();
        if (imei != null && !imei.equals(obj5)) {
            return true;
        }
        Object obj6 = hashMap2.get(Dic.SERIAL_NO);
        String serialno = deviceHelper.getSerialno();
        if (serialno != null && !serialno.equals(obj6)) {
            return true;
        }
        Object obj7 = hashMap2.get(Dic.MAC);
        String macAddress = deviceHelper.getMacAddress();
        if (macAddress != null && !macAddress.equals(obj7)) {
            return true;
        }
        Object obj8 = hashMap2.get(FileDownloadBroadcastHandler.KEY_MODEL);
        String model = deviceHelper.getModel();
        if (model != null && !model.equals(obj8)) {
            return true;
        }
        Object obj9 = hashMap2.get("factory");
        String manufacturer = deviceHelper.getManufacturer();
        if (manufacturer != null && !manufacturer.equals(obj9)) {
            return true;
        }
        Object obj10 = hashMap2.get("carrier");
        String carrier = deviceHelper.getCarrier();
        if (carrier != null && !carrier.equals(obj10)) {
            return true;
        }
        Object obj11 = hashMap2.get(Dic.IMSI);
        String imsi = deviceHelper.getIMSI();
        if (imsi != null && !imsi.equals(obj11)) {
            return true;
        }
        Object obj12 = hashMap2.get("ismtarr");
        String[] queryIMSI = deviceHelper.queryIMSI();
        if (queryIMSI != null && queryIMSI.length > 0) {
            if (obj12 == null) {
                return true;
            }
            try {
                ArrayList arrayList = (ArrayList) obj12;
                if (arrayList.size() != queryIMSI.length) {
                    return true;
                }
                boolean z2 = false;
                for (String str2 : queryIMSI) {
                    Iterator it = arrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z2 = true;
                            break;
                        }
                        if (str2.equals((String) it.next())) {
                            z2 = false;
                            break;
                        }
                    }
                }
                if (z2) {
                    return true;
                }
            } catch (Throwable unused) {
            }
        }
        Object obj13 = hashMap2.get("ansmt");
        if (obj13 == null && (obj13 = hashMap2.get("anmt")) != null) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(obj13);
            obj13 = arrayList2;
        }
        if (obj13 == null || !(obj13 instanceof ArrayList)) {
            return true;
        }
        String androidID = deviceHelper.getAndroidID();
        Iterator it2 = ((ArrayList) obj13).iterator();
        while (true) {
            if (!it2.hasNext()) {
                z = false;
                break;
            }
            Object next = it2.next();
            if (next != null && next.equals(androidID)) {
                z = true;
                break;
            }
        }
        if (!z) {
            return true;
        }
        Object obj14 = hashMap2.get("sysver");
        String oSVersionName = deviceHelper.getOSVersionName();
        if (oSVersionName != null && !oSVersionName.equals(obj14)) {
            return true;
        }
        Object obj15 = hashMap2.get("xp");
        boolean cx = deviceHelper.cx();
        if (obj15 == null || !String.valueOf(cx ? 1 : 0).equals(String.valueOf(obj15))) {
            return true;
        }
        Object obj16 = hashMap2.get("breaked");
        boolean isRooted = deviceHelper.isRooted();
        if ((obj16 == null && isRooted) || (obj16 != null && !String.valueOf(obj16).equals(String.valueOf(isRooted)))) {
            return true;
        }
        Object obj17 = hashMap2.get(k.a(69));
        String c = d.c(MobSDK.getContext());
        return (obj17 == null && !TextUtils.isEmpty(c)) || !(obj17 == null || String.valueOf(obj17).equals(c));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(HashMap<String, Object> hashMap, MobProduct mobProduct) {
        if (mobProduct == null) {
            mobProduct = new MobProduct() { // from class: com.mob.commons.authorize.a.3
                @Override // com.mob.commons.MobProduct
                public String getProductTag() {
                    return "COMMON";
                }

                @Override // com.mob.commons.MobProduct
                public int getSdkver() {
                    return MobSDK.SDK_VERSION_CODE;
                }
            };
        }
        boolean z = false;
        try {
            HashMap hashMap2 = (HashMap) hashMap.get("appInfo");
            if (hashMap2 == null) {
                hashMap2 = new HashMap();
                hashMap.put("appInfo", hashMap2);
                z = true;
            }
            String packageName = DeviceHelper.getInstance(MobSDK.getContext()).getPackageName();
            HashMap hashMap3 = (HashMap) hashMap2.get(packageName);
            if (hashMap3 == null) {
                hashMap3 = new HashMap();
                hashMap2.put(packageName, hashMap3);
                z = true;
            }
            String str = (String) hashMap3.get(mobProduct.getProductTag());
            String appkey = MobSDK.getAppkey();
            if (str != null && str.equals(appkey)) {
                return z;
            }
            if (a(mobProduct, hashMap)) {
                return true;
            }
            return z;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return z;
        }
    }

    private static byte[] a(String str, HashMap<String, Object> hashMap) {
        String fromHashMap = new Hashon().fromHashMap(hashMap);
        try {
            return Data.AES128Encode(str, fromHashMap);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return fromHashMap.getBytes();
        }
    }

    private String b(String str) {
        try {
            String wAbcd = DeviceHelper.getInstance(MobSDK.getContext()).getWAbcd(0);
            return !TextUtils.isEmpty(wAbcd) ? wAbcd : c(str);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x005c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0036 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0076 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.util.HashMap<java.lang.String, java.lang.Object> b(java.io.File r5) {
        /*
            boolean r0 = r5.exists()
            r1 = 0
            if (r0 == 0) goto L7a
            boolean r0 = r5.isFile()
            if (r0 == 0) goto L7a
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L22
            r0.<init>(r5)     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L22
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L22
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L22
            java.lang.Object r0 = r2.readObject()     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L60
            java.util.HashMap r0 = (java.util.HashMap) r0     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L60
            goto L33
        L1e:
            r0 = move-exception
            goto L24
        L20:
            r5 = move-exception
            goto L74
        L22:
            r0 = move-exception
            r2 = r1
        L24:
            com.mob.tools.log.NLog r3 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L60 java.lang.Throwable -> L63
            java.lang.String r0 = r0.getMessage()     // Catch: java.lang.Throwable -> L60 java.lang.Throwable -> L63
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L60 java.lang.Throwable -> L63
            r3.d(r0, r4)     // Catch: java.lang.Throwable -> L60 java.lang.Throwable -> L63
            r0 = r1
        L33:
            r1 = r2
            if (r0 == 0) goto L56
            boolean r2 = r0.isEmpty()     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L54
            if (r2 != 0) goto L56
            java.util.Set r2 = r0.keySet()     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L54
            java.lang.String r3 = "duid"
            boolean r2 = r2.contains(r3)     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L54
            if (r2 == 0) goto L56
            r5.delete()     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L54
            java.io.File r5 = c()     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L54
            a(r5, r0)     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L54
            r5 = r0
            goto L5a
        L54:
            r5 = move-exception
            goto L66
        L56:
            java.util.HashMap r5 = c(r5)     // Catch: java.lang.Throwable -> L20 java.lang.Throwable -> L54
        L5a:
            if (r1 == 0) goto L7b
            r1.close()     // Catch: java.lang.Throwable -> L7b
            goto L7b
        L60:
            r5 = move-exception
            r1 = r2
            goto L74
        L63:
            r5 = move-exception
            r0 = r1
            r1 = r2
        L66:
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L20
            r2.w(r5)     // Catch: java.lang.Throwable -> L20
            if (r1 == 0) goto L72
            r1.close()     // Catch: java.lang.Throwable -> L72
        L72:
            r5 = r0
            goto L7b
        L74:
            if (r1 == 0) goto L79
            r1.close()     // Catch: java.lang.Throwable -> L79
        L79:
            throw r5
        L7a:
            r5 = r1
        L7b:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.authorize.a.b(java.io.File):java.util.HashMap");
    }

    /* JADX WARN: Can't wrap try/catch for region: R(46:1|2|(1:4)|5|(1:7)|8|(1:10)|11|12|(1:14)|(3:16|17|(1:19))|21|(2:23|(1:25))|26|(1:28)|29|(3:30|31|32)|33|(1:35)|(3:36|37|(1:39))|(3:41|42|(1:44))|46|47|(1:49)|(3:51|52|(1:54))|56|57|58|59|(3:(1:64)(1:105)|(4:69|70|(1:72)(1:103)|(5:74|75|(6:77|78|79|80|(2:81|(2:83|(3:85|86|87)(1:89))(3:90|91|92))|88)|100|101))|(1:67))|106|(1:108)|109|(3:154|155|(1:157))|(13:115|(2:116|(2:118|(1:149)(2:123|124))(2:151|152))|(1:126)|127|128|129|130|131|132|133|(1:137)|138|140)|153|127|128|129|130|131|132|133|(2:135|137)|138|140) */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x023e, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x023f, code lost:
    
        com.mob.tools.MobLog.getInstance().w(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x021f, code lost:
    
        r2 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0220, code lost:
    
        com.mob.tools.MobLog.getInstance().w(r2);
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x018d A[Catch: Throwable -> 0x025f, TryCatch #4 {Throwable -> 0x025f, blocks: (B:2:0x0000, B:4:0x0012, B:5:0x001c, B:7:0x0026, B:8:0x002b, B:10:0x0048, B:21:0x007d, B:23:0x0083, B:25:0x008d, B:26:0x0092, B:28:0x009c, B:33:0x00b0, B:35:0x00b6, B:56:0x011b, B:106:0x0183, B:108:0x018d, B:109:0x0192, B:127:0x01f7, B:130:0x0227, B:133:0x0246, B:135:0x0250, B:137:0x0256, B:138:0x0259, B:144:0x023f, B:147:0x0220, B:160:0x01f0, B:162:0x017c, B:164:0x0114, B:166:0x00fc, B:168:0x00e4, B:170:0x00cc, B:173:0x00a9, B:175:0x0076, B:177:0x005e, B:59:0x0122, B:61:0x0128, B:96:0x016c, B:67:0x0175, B:47:0x00eb, B:49:0x00f5, B:12:0x004d, B:14:0x0057, B:129:0x0211, B:132:0x0234, B:42:0x00d3, B:44:0x00dd, B:31:0x00a2, B:37:0x00bb, B:39:0x00c5, B:155:0x019a, B:157:0x01a2, B:112:0x01b0, B:115:0x01b5, B:116:0x01bf, B:118:0x01c5, B:121:0x01cb, B:126:0x01d5, B:153:0x01de, B:52:0x0103, B:54:0x010d, B:17:0x0065, B:19:0x006f), top: B:1:0x0000, inners: #0, #1, #2, #3, #5, #7, #9, #11, #12, #13, #14 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x01c5 A[Catch: Throwable -> 0x01ac, TryCatch #12 {Throwable -> 0x01ac, blocks: (B:155:0x019a, B:157:0x01a2, B:112:0x01b0, B:115:0x01b5, B:116:0x01bf, B:118:0x01c5, B:121:0x01cb, B:126:0x01d5, B:153:0x01de), top: B:154:0x019a, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01d5 A[Catch: Throwable -> 0x01ac, TryCatch #12 {Throwable -> 0x01ac, blocks: (B:155:0x019a, B:157:0x01a2, B:112:0x01b0, B:115:0x01b5, B:116:0x01bf, B:118:0x01c5, B:121:0x01cb, B:126:0x01d5, B:153:0x01de), top: B:154:0x019a, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0250 A[Catch: Throwable -> 0x025f, TryCatch #4 {Throwable -> 0x025f, blocks: (B:2:0x0000, B:4:0x0012, B:5:0x001c, B:7:0x0026, B:8:0x002b, B:10:0x0048, B:21:0x007d, B:23:0x0083, B:25:0x008d, B:26:0x0092, B:28:0x009c, B:33:0x00b0, B:35:0x00b6, B:56:0x011b, B:106:0x0183, B:108:0x018d, B:109:0x0192, B:127:0x01f7, B:130:0x0227, B:133:0x0246, B:135:0x0250, B:137:0x0256, B:138:0x0259, B:144:0x023f, B:147:0x0220, B:160:0x01f0, B:162:0x017c, B:164:0x0114, B:166:0x00fc, B:168:0x00e4, B:170:0x00cc, B:173:0x00a9, B:175:0x0076, B:177:0x005e, B:59:0x0122, B:61:0x0128, B:96:0x016c, B:67:0x0175, B:47:0x00eb, B:49:0x00f5, B:12:0x004d, B:14:0x0057, B:129:0x0211, B:132:0x0234, B:42:0x00d3, B:44:0x00dd, B:31:0x00a2, B:37:0x00bb, B:39:0x00c5, B:155:0x019a, B:157:0x01a2, B:112:0x01b0, B:115:0x01b5, B:116:0x01bf, B:118:0x01c5, B:121:0x01cb, B:126:0x01d5, B:153:0x01de, B:52:0x0103, B:54:0x010d, B:17:0x0065, B:19:0x006f), top: B:1:0x0000, inners: #0, #1, #2, #3, #5, #7, #9, #11, #12, #13, #14 }] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x01d2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x019a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0175 A[Catch: Throwable -> 0x017b, TRY_LEAVE, TryCatch #0 {Throwable -> 0x017b, blocks: (B:59:0x0122, B:61:0x0128, B:96:0x016c, B:67:0x0175), top: B:58:0x0122, outer: #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void b(java.util.HashMap<java.lang.String, java.lang.Object> r12) {
        /*
            Method dump skipped, instructions count: 616
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.authorize.a.b(java.util.HashMap):void");
    }

    private static File c() {
        return ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.duid");
    }

    private String c(String str) {
        try {
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            i.i(System.currentTimeMillis());
        }
        if (!b.aq() || b.L()) {
            return null;
        }
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("plat", 1);
        hashMap.put(Dic.IMEI, deviceHelper.getIMEI());
        hashMap.put(Dic.SERIAL_NO, deviceHelper.getSerialno());
        hashMap.put(Dic.MAC, deviceHelper.getMacAddress());
        hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, deviceHelper.getModel());
        hashMap.put("factory", deviceHelper.getManufacturer());
        hashMap.put("admt", deviceHelper.getAdvertisingID());
        hashMap.put("oamt", d.c(MobSDK.getContext()));
        hashMap.put(Dic.IMSI, deviceHelper.getIMSI());
        hashMap.put("anmt", deviceHelper.getAndroidID());
        hashMap.put(Dic.SIM_SERIAL_NUMBER, deviceHelper.getSimSerialNumber());
        hashMap.put("duid", str);
        HashMap hashMap2 = (HashMap) new MobCommunicator(1024, "ceeef5035212dfe7c6a0acdc0ef35ce5b118aab916477037d7381f85c6b6176fcf57b1d1c3296af0bb1c483fe5e1eb0ce9eb2953b44e494ca60777a1b033cc07", "191737288d17e660c4b61440d5d14228a0bf9854499f9d68d8274db55d6d954489371ecf314f26bec236e58fac7fffa9b27bcf923e1229c4080d49f7758739e5bd6014383ed2a75ce1be9b0ab22f283c5c5e11216c5658ba444212b6270d629f2d615b8dfdec8545fb7d4f935b0cc10b6948ab4fc1cb1dd496a8f94b51e888dd").requestSynchronized(hashMap, a + "/dgen", false);
        if (hashMap2.get("dri") != null) {
            i.i(System.currentTimeMillis() + (((Integer) r2).intValue() * 60 * 60 * 1000));
        }
        Object obj = hashMap2.get("duid");
        if (obj != null) {
            String valueOf = String.valueOf(obj);
            if (deviceHelper.getSdcardState()) {
                deviceHelper.saveWabcd(valueOf, 0);
            }
            return valueOf;
        }
        return null;
    }

    private static HashMap<String, Object> c(File file) {
        if (file.exists()) {
            try {
                FileChannel channel = new FileInputStream(file).getChannel();
                ByteBuffer allocate = ByteBuffer.allocate((int) channel.size());
                do {
                } while (channel.read(allocate) > 0);
                return a(DeviceHelper.getInstance(MobSDK.getContext()).getModel(), allocate.array());
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
        return new HashMap<>();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(final HashMap<String, Object> hashMap) {
        e.a(e.a("comm/locks/.globalLock"), new LockAction() { // from class: com.mob.commons.authorize.a.4
            @Override // com.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                a.this.a((HashMap<String, Object>) hashMap, false);
                return false;
            }
        });
    }

    private File d() {
        File file = new File(ResHelper.getDataCache(MobSDK.getContext()), "comm/dbs/.duid");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashMap<String, Object> e() {
        try {
            return b(c());
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    private String f() {
        return k.a(156);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g() {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.digap");
        if (dataCacheFile == null || !dataCacheFile.exists()) {
            a(b.O() + b.a());
        } else {
            try {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dataCacheFile));
                long readLong = dataInputStream.readLong();
                dataInputStream.close();
                return readLong < b.a();
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String a() {
        try {
            HashMap<String, Object> e = e();
            String str = e != null ? (String) e.get("duid") : null;
            return str == null ? a((String) null) : str;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String a(final MobProduct mobProduct, final String str) {
        final String[] strArr = new String[1];
        e.a(e.a("comm/locks/.globalLock"), new LockAction() { // from class: com.mob.commons.authorize.a.1
            @Override // com.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                strArr[0] = a.this.a(mobProduct, false, str);
                return false;
            }
        });
        return strArr[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String a(final boolean z, final boolean z2) {
        final String[] strArr = new String[1];
        e.a(e.a("comm/locks/.globalLock"), new LockAction() { // from class: com.mob.commons.authorize.a.6
            @Override // com.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                try {
                    HashMap e = a.this.e();
                    if (e == null) {
                        e = new HashMap();
                    }
                    String str = (String) e.get("duid");
                    if (str == null && z2) {
                        str = a.this.a(!z);
                        if (!z) {
                            e.put("duid", str);
                            a.this.a((HashMap<String, Object>) e, false);
                        }
                    }
                    strArr[0] = str;
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                }
                return false;
            }
        });
        return strArr[0];
    }
}
