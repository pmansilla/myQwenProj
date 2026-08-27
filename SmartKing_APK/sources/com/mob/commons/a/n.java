package com.mob.commons.a;

import android.os.Message;
import android.os.SystemClock;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.ResHelper;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* compiled from: IMcClt.java */
/* loaded from: classes.dex */
public class n extends d {
    private Random a;
    private DeviceHelper b;
    private HashMap<String, Object> c;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: IMcClt.java */
    /* loaded from: classes.dex */
    public static class a implements Comparator<HashMap<String, Object>> {
        private a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(HashMap<String, Object> hashMap, HashMap<String, Object> hashMap2) {
            if ((hashMap == null || hashMap.isEmpty()) && (hashMap2 == null || hashMap2.isEmpty())) {
                return 0;
            }
            if ((hashMap == null || hashMap.isEmpty()) && hashMap2 != null && !hashMap2.isEmpty()) {
                return -1;
            }
            if (hashMap != null && !hashMap.isEmpty() && (hashMap2 == null || hashMap2.isEmpty())) {
                return 1;
            }
            try {
                String str = (String) hashMap.get("ip");
                String str2 = (String) hashMap2.get("ip");
                if (str == null || "".equals(str) || str2 == null || "".equals(str2)) {
                    return 0;
                }
                String[] split = str.split("\\.");
                String[] split2 = str2.split("\\.");
                if (split == null || split.length != 4 || split2 == null || split2.length != 4) {
                    return 0;
                }
                int intValue = Integer.valueOf(split[2]).intValue();
                int intValue2 = Integer.valueOf(split2[2]).intValue();
                if (intValue < intValue2) {
                    return -1;
                }
                if (intValue != intValue2) {
                    return 1;
                }
                int intValue3 = Integer.valueOf(split[3]).intValue();
                int intValue4 = Integer.valueOf(split2[3]).intValue();
                if (intValue3 < intValue4) {
                    return -1;
                }
                return intValue3 == intValue4 ? 0 : 1;
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
                return 0;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.HashMap<java.lang.String, java.lang.Object> a(java.lang.String r5) {
        /*
            r4 = this;
            r0 = 0
            android.content.Context r1 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            java.io.File r5 = com.mob.tools.utils.ResHelper.getDataCacheFile(r1, r5)     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            boolean r1 = r5.exists()     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            if (r1 != 0) goto L18
            java.util.HashMap r5 = new java.util.HashMap     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            r5.<init>()     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            r4.a(r0)
            return r5
        L18:
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            java.io.ObjectInputStream r5 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            r5.<init>(r1)     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L39
            java.lang.Object r1 = r5.readObject()     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L32
            java.util.HashMap r1 = (java.util.HashMap) r1     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L32
            r4.a(r5)
            r0 = r1
            goto L45
        L2d:
            r0 = move-exception
            r3 = r0
            r0 = r5
            r5 = r3
            goto L4f
        L32:
            r1 = move-exception
            r3 = r1
            r1 = r5
            r5 = r3
            goto L3b
        L37:
            r5 = move-exception
            goto L4f
        L39:
            r5 = move-exception
            r1 = r0
        L3b:
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L4d
            r2.d(r5)     // Catch: java.lang.Throwable -> L4d
            r4.a(r1)
        L45:
            if (r0 != 0) goto L4c
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
        L4c:
            return r0
        L4d:
            r5 = move-exception
            r0 = r1
        L4f:
            r4.a(r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.n.a(java.lang.String):java.util.HashMap");
    }

    private void a(HashMap<String, Object> hashMap, String str) {
        Closeable closeable = null;
        try {
            try {
                File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), str);
                if (hashMap != null && !hashMap.isEmpty()) {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataCacheFile));
                    try {
                        objectOutputStream.writeObject(hashMap);
                        a(objectOutputStream);
                        return;
                    } catch (Throwable th) {
                        th = th;
                        closeable = objectOutputStream;
                        MobLog.getInstance().w(th);
                        a(closeable);
                        return;
                    }
                }
                dataCacheFile.delete();
                a((Closeable) null);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void b(HashMap<String, Object> hashMap, String str) {
        MobLog.getInstance().d("[%s] %s", "IMcClt", "Write into queue");
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("type", str);
        hashMap2.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap);
        hashMap2.put("cl", f());
        long a2 = com.mob.commons.b.a();
        hashMap2.put("datetime", Long.valueOf(a2));
        com.mob.commons.c.a().a(a2, hashMap2);
        if (this.c == null) {
            this.c = new HashMap<>();
        }
        if (this.c.size() >= 500) {
            this.c.clear();
        }
        this.c.put((String) hashMap.get(Dic.BSSID), Long.valueOf(a2 + (com.mob.commons.b.U() * 1000)));
        a(this.c, "comm/dbs/.imcd");
    }

    private void h() {
        MobLog.getInstance().d("[%s] %s", "IMcClt", ">>> Pre-obtain");
        if (!"wifi".equals(this.b.getNetworkType())) {
            MobLog.getInstance().d("[%s] %s", "IMcClt", "No wifi");
            e();
            return;
        }
        if (this.c == null || this.c.isEmpty()) {
            this.c = a("comm/dbs/.imcd");
        }
        if (this.c == null || this.c.isEmpty()) {
            i();
        } else {
            String bssid = this.b.getBssid();
            if (!this.c.containsKey(bssid)) {
                i();
            } else if (com.mob.commons.b.a() >= ((Long) this.c.get(bssid)).longValue()) {
                i();
            } else {
                MobLog.getInstance().d("[%s] %s", "IMcClt", "Interval not reached");
            }
        }
        this.c = null;
        this.a = null;
        this.b = null;
    }

    private void i() {
        try {
            MobLog.getInstance().d("[%s] %s", "IMcClt", ">>> Obtain");
            if (!"wifi".equals(this.b.getNetworkType())) {
                MobLog.getInstance().d("[%s] %s", "IMcClt", "No wifi");
                return;
            }
            MobLog.getInstance().d("[%s] %s", "IMcClt", "Communicating");
            j();
            MobLog.getInstance().d("[%s] %s", "IMcClt", "Waiting for update");
            SystemClock.sleep(BootloaderScanner.TIMEOUT);
            MobLog.getInstance().d("[%s] %s", "IMcClt", "Obtaining");
            ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
            for (int i = 0; arrayList.isEmpty() && i < 8; i++) {
                arrayList = this.b.getArpList();
                SystemClock.sleep(arrayList.size() > 0 ? 0L : 1000L);
            }
            Collections.sort(arrayList, new a());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(Dic.BSSID, this.b.getBssid());
            hashMap.put(Dic.SSID, this.b.getSSID());
            hashMap.put("curip", this.b.getIPAddress());
            hashMap.put("list", arrayList);
            b(hashMap, "IMCMT");
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private void j() {
        String[] split = this.b.getIPAddress().split("\\.");
        int i = 2;
        if (split.length > 2) {
            String str = split[0] + "." + split[1] + "." + split[2] + ".";
            try {
                DatagramPacket datagramPacket = new DatagramPacket(new byte[0], 0, 0);
                DatagramSocket datagramSocket = new DatagramSocket();
                while (i < 255) {
                    datagramPacket.setAddress(InetAddress.getByName(str + String.valueOf(i)));
                    datagramSocket.send(datagramPacket);
                    i++;
                    if (i == 125) {
                        datagramSocket.close();
                        datagramSocket = new DatagramSocket();
                    }
                }
                datagramSocket.close();
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
    }

    private int k() {
        int i;
        if (com.mob.commons.b.T() > 0) {
            if (this.a == null) {
                this.a = new Random();
            }
            i = this.a.nextInt((int) com.mob.commons.b.T());
        } else {
            i = 0;
        }
        return i * 1000;
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.im_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (this.b == null) {
            this.b = DeviceHelper.getInstance(MobSDK.getContext());
        }
        if (message.what != 1) {
            return;
        }
        h();
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.T() > 0;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        if (com.mob.commons.b.T() > 0) {
            a(1, k());
        } else {
            MobLog.getInstance().d("[%s] %s", "IMcClt", "Config no process");
        }
    }
}
