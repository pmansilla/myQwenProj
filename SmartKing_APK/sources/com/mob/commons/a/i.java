package com.mob.commons.a;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* compiled from: DUClt.java */
/* loaded from: classes.dex */
public class i extends d {
    private PackageManager a;

    private long a(String str, Long l) {
        System.currentTimeMillis();
        long longValue = l.longValue();
        new LinkedList();
        List<Long> b = b(str);
        if (b != null && !b.isEmpty()) {
            for (Long l2 : b) {
                if (l2.longValue() > longValue) {
                    longValue = l2.longValue();
                }
            }
        }
        return longValue;
    }

    private long a(String str, String str2) {
        try {
            return new SimpleDateFormat(str).parse(str2).getTime();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return 0L;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x00c5, code lost:
    
        if (android.text.TextUtils.isEmpty(r13) == false) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x00c7, code lost:
    
        r13 = c(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x00cb, code lost:
    
        if (r13 == null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x00cd, code lost:
    
        r0.put((java.lang.String) r13[0], (java.lang.Long) r13[1]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x00d8, code lost:
    
        r13 = r2.readLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00dc, code lost:
    
        if (r13 != null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00de, code lost:
    
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00f5, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00f2, code lost:
    
        if (r2 == null) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.HashMap<java.lang.String, java.lang.Long> a(java.lang.String r13) {
        /*
            r12 = this;
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r1 = 146(0x92, float:2.05E-43)
            r2 = 0
            java.lang.String r3 = com.mob.commons.k.a(r1)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.String r3 = com.mob.tools.utils.ReflectHelper.importClass(r3)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r4 = 147(0x93, float:2.06E-43)
            java.lang.String r5 = com.mob.commons.k.a(r4)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r6 = 0
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.Object r3 = com.mob.tools.utils.ReflectHelper.invokeStaticMethod(r3, r5, r7)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r5 = 148(0x94, float:2.07E-43)
            java.lang.String r7 = com.mob.commons.k.a(r5)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r8 = 1
            java.lang.Object[] r9 = new java.lang.Object[r8]     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r10.<init>()     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r11 = 151(0x97, float:2.12E-43)
            java.lang.String r11 = com.mob.commons.k.a(r11)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r10.append(r11)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.String r11 = " "
            r10.append(r11)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r10.append(r13)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.String r10 = r10.toString()     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r9[r6] = r10     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.Object r3 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r3, r7, r9)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r7 = 149(0x95, float:2.09E-43)
            java.lang.String r9 = com.mob.commons.k.a(r7)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.Object[] r10 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.Object r3 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r3, r9, r10)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.io.InputStream r3 = (java.io.InputStream) r3     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.io.InputStreamReader r9 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.String r10 = "utf-8"
            r9.<init>(r3, r10)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r3.<init>(r9)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.String r2 = r3.readLine()     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            boolean r9 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            if (r9 == 0) goto Lbf
            java.lang.String r1 = com.mob.commons.k.a(r1)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r1 = com.mob.tools.utils.ReflectHelper.importClass(r1)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r2 = com.mob.commons.k.a(r4)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.Object[] r4 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.Object r1 = com.mob.tools.utils.ReflectHelper.invokeStaticMethod(r1, r2, r4)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r2 = com.mob.commons.k.a(r5)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.Object[] r4 = new java.lang.Object[r8]     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            r5.<init>()     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            r9 = 153(0x99, float:2.14E-43)
            java.lang.String r9 = com.mob.commons.k.a(r9)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            r5.append(r9)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r9 = " "
            r5.append(r9)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            r5.append(r13)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r13 = r5.toString()     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            r4[r6] = r13     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.Object r13 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r1, r2, r4)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r1 = com.mob.commons.k.a(r7)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.Object[] r2 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.Object r13 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r13, r1, r2)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.io.InputStream r13 = (java.io.InputStream) r13     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r2 = "utf-8"
            r1.<init>(r13, r2)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            r2.<init>(r1)     // Catch: java.lang.Throwable -> Le2 java.lang.Throwable -> Le5
            java.lang.String r13 = r2.readLine()     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            goto Lc1
        Lbf:
            r13 = r2
            r2 = r3
        Lc1:
            boolean r1 = android.text.TextUtils.isEmpty(r13)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            if (r1 != 0) goto Lde
        Lc7:
            java.lang.Object[] r13 = r12.c(r13)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            if (r13 == 0) goto Ld8
            r1 = r13[r6]     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.String r1 = (java.lang.String) r1     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r13 = r13[r8]     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            java.lang.Long r13 = (java.lang.Long) r13     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            r0.put(r1, r13)     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
        Ld8:
            java.lang.String r13 = r2.readLine()     // Catch: java.lang.Throwable -> Le8 java.lang.Throwable -> Lea
            if (r13 != 0) goto Lc7
        Lde:
            r2.close()     // Catch: java.lang.Throwable -> Lf5
            goto Lf5
        Le2:
            r13 = move-exception
            r2 = r3
            goto Lf6
        Le5:
            r13 = move-exception
            r2 = r3
            goto Leb
        Le8:
            r13 = move-exception
            goto Lf6
        Lea:
            r13 = move-exception
        Leb:
            com.mob.tools.log.NLog r1 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> Le8
            r1.d(r13)     // Catch: java.lang.Throwable -> Le8
            if (r2 == 0) goto Lf5
            goto Lde
        Lf5:
            return r0
        Lf6:
            if (r2 == 0) goto Lfb
            r2.close()     // Catch: java.lang.Throwable -> Lfb
        Lfb:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.i.a(java.lang.String):java.util.HashMap");
    }

    private HashMap<String, Long> a(String str, byte[] bArr) {
        try {
            return new Hashon().fromJson(Data.AES128Decode(str, bArr));
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DUClt", th.getMessage());
            return new HashMap<>();
        }
    }

    private void a(long j) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dupdcd")));
            dataOutputStream.writeLong(j);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DUClt", th.getMessage());
        }
    }

    private void a(HashMap<String, Long> hashMap) {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dudcd");
        try {
            byte[] a = a(DeviceHelper.getInstance(MobSDK.getContext()).getModel(), hashMap);
            FileChannel channel = new FileOutputStream(dataCacheFile).getChannel();
            channel.write(ByteBuffer.wrap(a));
            channel.force(true);
            channel.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DUClt", th.getMessage());
        }
    }

    private boolean a(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & 1) == 1) || ((packageInfo.applicationInfo.flags & 128) == 1);
    }

    private boolean a(File file) {
        PackageInfo packageInfo;
        if (file != null && file.isDirectory()) {
            try {
                if (this.a == null) {
                    this.a = MobSDK.getContext().getPackageManager();
                }
                packageInfo = this.a.getPackageInfo(file.getName(), 0);
            } catch (Throwable th) {
                MobLog.getInstance().d("[%s] %s", "DUClt", "Name not found: " + th.getMessage());
                packageInfo = null;
            }
            if (packageInfo != null && !a(packageInfo)) {
                return true;
            }
        }
        return false;
    }

    private byte[] a(String str, HashMap<String, Long> hashMap) {
        String fromHashMap = new Hashon().fromHashMap(hashMap);
        try {
            return Data.AES128Encode(str, fromHashMap);
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DUClt", th.getMessage());
            return fromHashMap.getBytes();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x00c5, code lost:
    
        if (android.text.TextUtils.isEmpty(r13) == false) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x00cd, code lost:
    
        if (r13.startsWith("-") != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x00d5, code lost:
    
        if (r13.startsWith("d") == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00e8, code lost:
    
        r13 = r2.readLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x00ec, code lost:
    
        if (r13 != null) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00d7, code lost:
    
        r13 = c(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00db, code lost:
    
        if (r13 == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00dd, code lost:
    
        r1 = (java.lang.String) r13[0];
        r0.add((java.lang.Long) r13[1]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00ee, code lost:
    
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0105, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0102, code lost:
    
        if (r2 == null) goto L32;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<java.lang.Long> b(java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.i.b(java.lang.String):java.util.List");
    }

    private Object[] c(String str) {
        Object[] objArr = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}").matcher(str);
            if (!matcher.find()) {
                return null;
            }
            Long valueOf = Long.valueOf(a("yyyy-MM-dd HH:mm", matcher.group(0)));
            String substring = str.substring(matcher.end() + 1);
            if (valueOf.longValue() > System.currentTimeMillis()) {
                return null;
            }
            Object[] objArr2 = new Object[2];
            try {
                objArr2[0] = substring;
                objArr2[1] = valueOf;
                return objArr2;
            } catch (Throwable th) {
                th = th;
                objArr = objArr2;
                MobLog.getInstance().d("Simple err: " + th.getMessage(), new Object[0]);
                return objArr;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void h() {
        try {
            if (!DeviceHelper.getInstance(MobSDK.getContext()).checkPermission("android.permission.READ_EXTERNAL_STORAGE")) {
                MobLog.getInstance().d("[%s] %s", "DUClt", "No permission");
                return;
            }
            String str = Environment.getExternalStorageDirectory() + com.mob.commons.k.a(16);
            if (!new File(str).isDirectory()) {
                MobLog.getInstance().d("[%s] %s", "DUClt", "Can not read");
                return;
            }
            HashMap<String, Long> a = a(str);
            if (a == null || a.isEmpty()) {
                MobLog.getInstance().d("[%s] %s", "DUClt", "No subs");
                return;
            }
            long a2 = com.mob.commons.b.a();
            HashMap<String, Long> j = j();
            ArrayList arrayList = new ArrayList();
            HashMap hashMap = new HashMap();
            for (Map.Entry<String, Long> entry : a.entrySet()) {
                String key = entry.getKey();
                Long value = entry.getValue();
                File file = new File(str + key);
                if (a(file)) {
                    arrayList.add(file.getName());
                    long a3 = a(file.getAbsolutePath(), value);
                    Long l = j.get(file.getName());
                    if (a3 > (l != null ? l.longValue() : 0L)) {
                        j.put(file.getName(), Long.valueOf(a3));
                        hashMap.put(file.getName(), Long.valueOf(a3));
                    }
                }
            }
            this.a = null;
            ArrayList arrayList2 = new ArrayList();
            for (String str2 : j.keySet()) {
                if (!arrayList.contains(str2)) {
                    arrayList2.add(str2);
                }
            }
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                j.remove((String) it.next());
            }
            a(j);
            long a4 = com.mob.commons.b.a();
            if (!hashMap.isEmpty() || !arrayList2.isEmpty()) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("scanAt", Long.valueOf(a2));
                hashMap2.put("update", hashMap);
                hashMap2.put("delete", arrayList2);
                HashMap<String, Object> hashMap3 = new HashMap<>();
                hashMap3.put("type", "ADACMT");
                hashMap3.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap2);
                hashMap3.put("datetime", Long.valueOf(a4));
                com.mob.commons.c.a().a(a4, hashMap3);
            }
            long a5 = com.mob.commons.b.a() - a2;
            MobLog.getInstance().i("[%s] %s", "DUClt", "ttl: " + arrayList.size() + ", u: " + hashMap.size() + ", d: " + arrayList2.size() + ", dur: " + a5 + " ms");
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DUClt", th.getMessage());
        }
    }

    private long i() {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dupdcd");
        if (!dataCacheFile.exists()) {
            return 0L;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dataCacheFile));
            long readLong = dataInputStream.readLong();
            dataInputStream.close();
            return readLong;
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "DUClt", th.getMessage());
            return 0L;
        }
    }

    private HashMap<String, Long> j() {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dudcd");
        if (dataCacheFile.exists()) {
            try {
                FileChannel channel = new FileInputStream(dataCacheFile).getChannel();
                ByteBuffer allocate = ByteBuffer.allocate((int) channel.size());
                do {
                } while (channel.read(allocate) > 0);
                return a(DeviceHelper.getInstance(MobSDK.getContext()).getModel(), allocate.array());
            } catch (Throwable th) {
                MobLog.getInstance().d(th, "[%s] %s", "DUClt", th.getMessage());
            }
        }
        return new HashMap<>();
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.du_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (message.what != 1) {
            return;
        }
        long a = com.mob.commons.b.a();
        long i = i();
        if (a < i) {
            a(1, i - a);
            return;
        }
        h();
        long X = com.mob.commons.b.X() * 1000;
        a(a + X);
        a(1, X);
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.W() > 0;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        a(1);
        a(1, com.mob.commons.b.W() * 1000);
    }
}
