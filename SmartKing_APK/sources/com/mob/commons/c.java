package com.mob.commons;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import com.autonavi.amap.mapcore.AeUtil;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobSDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.log.NLog;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.FileLocker;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.MobRSA;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.SQLiteHelper;
import com.tencent.bugly.BuglyStrategy;
import io.reactivex.annotations.SchedulerSupport;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

/* compiled from: DataHeap.java */
/* loaded from: classes.dex */
public class c implements Handler.Callback {
    private static c a;
    private SQLiteHelper.SingleTableDB c;
    private boolean f = true;
    private Hashon d = new Hashon();
    private Random e = new Random();
    private Handler b = MobHandlerThread.newHandler("d", this);

    private c() {
        b();
        c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a(SparseArray<String> sparseArray) {
        try {
            StringBuilder sb = new StringBuilder();
            int size = sparseArray.size();
            for (int i = 0; i < size; i++) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append('\'');
                sb.append(sparseArray.valueAt(i));
                sb.append('\'');
            }
            try {
                return SQLiteHelper.delete(b(), "time in (" + sb.toString() + SQLBuilder.PARENTHESES_RIGHT, null);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
                return SQLiteHelper.delete(b(), "time in (" + sb.toString() + SQLBuilder.PARENTHESES_RIGHT, null);
            }
        } catch (Throwable th2) {
            MobLog.getInstance().w(th2);
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a(String[][] strArr) {
        int i;
        Cursor query;
        try {
            query = SQLiteHelper.query(b(), new String[]{"time", AeUtil.ROOT_DATA_PATH_OLD_NAME}, null, null, null);
        } catch (Throwable th) {
            th = th;
            i = 0;
        }
        if (query == null) {
            return 0;
        }
        if (!query.moveToFirst()) {
            query.close();
            return 0;
        }
        long a2 = b.a();
        i = 0;
        do {
            try {
                String[] strArr2 = new String[2];
                strArr2[0] = query.getString(0);
                strArr2[1] = query.getString(1);
                long j = -1;
                try {
                    j = Long.parseLong(strArr2[0]);
                } catch (Throwable unused) {
                }
                if (j <= a2) {
                    strArr[i] = strArr2;
                    i++;
                }
                if (i >= strArr.length) {
                    break;
                }
            } catch (Throwable th2) {
                th = th2;
                MobLog.getInstance().w(th);
                return i;
            }
        } while (query.moveToNext());
        query.close();
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SparseArray<String> a(String[][] strArr, int i) {
        try {
            SparseArray<String> sparseArray = new SparseArray<>();
            HashMap hashMap = new HashMap();
            DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
            hashMap.put("plat", Integer.valueOf(deviceHelper.getPlatformCode()));
            hashMap.put("device", deviceHelper.getDeviceKey());
            hashMap.put(Dic.MAC, deviceHelper.getMacAddress());
            hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, deviceHelper.getModel());
            hashMap.put("duid", DeviceAuthorizer.authorize(null));
            hashMap.put(Dic.IMEI, deviceHelper.getIMEI());
            hashMap.put(Dic.SERIAL_NO, deviceHelper.getSerialno());
            hashMap.put("networktype", deviceHelper.getDetailNetworkTypeForStatic());
            hashMap.put("dataNetworkType", Integer.valueOf(deviceHelper.getDataNtType()));
            ArrayList arrayList = new ArrayList();
            byte[] rawMD5 = Data.rawMD5(deviceHelper.getManufacturer());
            for (int i2 = 0; i2 < i; i2++) {
                String[] strArr2 = strArr[i2];
                try {
                    HashMap fromJson = this.d.fromJson(new String(Data.AES128Decode(rawMD5, Base64.decode(strArr2[1], 2)), "utf-8").trim());
                    if (fromJson == null || fromJson.isEmpty() || a((String) ResHelper.forceCast(fromJson.get("type"), null))) {
                        sparseArray.put(i2, strArr2[0]);
                        arrayList.add(fromJson);
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                }
            }
            if (arrayList.isEmpty()) {
                return new SparseArray<>();
            }
            hashMap.put("datas", arrayList);
            hashMap.put("token", l.a().b());
            ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
            arrayList2.add(new KVPair<>("appkey", MobSDK.getAppkey()));
            arrayList2.add(new KVPair<>("m", b(this.d.fromHashMap(hashMap))));
            ArrayList<KVPair<String>> arrayList3 = new ArrayList<>();
            arrayList3.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
            arrayList3.add(new KVPair<>(k.a(68), com.mob.commons.b.d.d(MobSDK.getContext())));
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
            networkTimeOut.connectionTimeout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
            if ("200".equals(String.valueOf(this.d.fromJson(new NetworkHelper().httpPost(e(), arrayList2, (KVPair<String>) null, arrayList3, networkTimeOut)).get("status")))) {
                return sparseArray;
            }
            i.e((String) null);
            return null;
        } catch (Throwable th2) {
            i.e((String) null);
            MobLog.getInstance().w(th2);
            return null;
        }
    }

    public static synchronized c a() {
        c cVar;
        synchronized (c.class) {
            if (a == null) {
                a = new c();
            }
            cVar = a;
        }
        return cVar;
    }

    private boolean a(String str) {
        if (!TextUtils.isEmpty(str)) {
            return "ALSAMT".equals(str) ? b.i() : "ALSIMT".equals(str) ? b.h() : "ALSUMT".equals(str) ? b.j() : "ARTSMT".equals(str) ? b.c() : "DEXTMT".equals(str) ? b.m() : "BSIOMT".equals(str) ? b.n() : "LCMT".equals(str) ? b.p() : "O_LCMT".equals(str) ? b.A() : "WIMT".equals(str) ? b.r() : "WLMT".equals(str) ? b.t() : "PVMT".equals(str) ? b.z() : "XM_ARTSMT".equals(str) ? b.D() > 0 : "BKIOMT".equals(str) ? b.E() : "LEIOMT".equals(str) ? b.I() > 0 : "SIMUMT".equals(str) ? b.K() > 0 : "PDMT".equals(str) ? b.P() > 0 : "ACMT".equals(str) ? b.Q() > 0 : "SALMT".equals(str) ? b.R() > 0 : "IMCMT".equals(str) ? b.T() > 0 : "GMIOMT".equals(str) ? b.V() : "ADACMT".equals(str) ? b.W() > 0 : !"RSLMT".equals(str) || b.Y() > 0;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SQLiteHelper.SingleTableDB b() {
        File dataCacheFile;
        if (this.c == null && (dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dh")) != null) {
            if (dataCacheFile.length() > 209715200) {
                dataCacheFile.delete();
                dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.dh");
            }
            this.c = SQLiteHelper.getDatabase(dataCacheFile.getAbsolutePath(), "DataHeap_1");
            this.c.addField("time", "text", true);
            this.c.addField(AeUtil.ROOT_DATA_PATH_OLD_NAME, "text", true);
        }
        return this.c;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:36:0x0108 -> B:29:0x011d). Please report as a decompilation issue!!! */
    private File b(Object... objArr) throws Throwable {
        int i;
        InputStream inputStream;
        String str = (String) objArr[0];
        String str2 = (String) objArr[1];
        String str3 = (String) objArr[4];
        String str4 = (String) objArr[5];
        File file = null;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            File file2 = new File(MobSDK.getContext().getFilesDir(), k.a(5));
            byte[] bArr = (byte[]) objArr[2];
            try {
                i = Integer.parseInt(String.valueOf(objArr[3]));
            } catch (Throwable unused) {
                i = 0;
            }
            if (bArr == null || i <= 0 || bArr.length < i || !str.equals(Data.MD5(bArr, 0, i))) {
                File file3 = new File(file2, k.a(14));
                if (file3.exists() && str.equals(Data.MD5(file3))) {
                    inputStream = new FileInputStream(file3);
                } else {
                    d.a().a(20);
                    file3.delete();
                    inputStream = null;
                }
            } else {
                inputStream = new ByteArrayInputStream(bArr, 0, i);
            }
            if (inputStream != null) {
                file = new File(file2, String.valueOf(System.currentTimeMillis()));
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file4 = new File(file, file.getName() + ".zip");
                FileOutputStream fileOutputStream = new FileOutputStream(file4);
                Data.AES128Decode(str2, inputStream, fileOutputStream);
                inputStream.close();
                fileOutputStream.close();
                try {
                    try {
                        try {
                            DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
                            if (deviceHelper.checkADBModel(17) && deviceHelper.checkUA()) {
                                d.a().a(19);
                            } else {
                                d.a().a(14);
                                com.mob.commons.a.d.a(str, file4, str3, str4);
                            }
                            ResHelper.deleteFileAndFolder(file);
                        } catch (Throwable th) {
                            d.a().a(6, th);
                            ResHelper.deleteFileAndFolder(file);
                        }
                    } catch (Throwable th2) {
                        d.a().a(4, th2);
                    }
                } catch (Throwable th3) {
                    try {
                        ResHelper.deleteFileAndFolder(file);
                    } catch (Throwable th4) {
                        d.a().a(4, th4);
                    }
                    throw th3;
                }
            }
        }
        return file;
    }

    private String b(String str) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeLong(this.e.nextLong());
        dataOutputStream.writeLong(this.e.nextLong());
        dataOutputStream.flush();
        dataOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new GZIPOutputStream(byteArrayOutputStream2));
        bufferedOutputStream.write(str.getBytes("utf-8"));
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        byte[] AES128Encode = Data.AES128Encode(byteArray, byteArrayOutputStream2.toByteArray());
        byte[] encode = new MobRSA(1024).encode(byteArray, new BigInteger("ceeef5035212dfe7c6a0acdc0ef35ce5b118aab916477037d7381f85c6b6176fcf57b1d1c3296af0bb1c483fe5e1eb0ce9eb2953b44e494ca60777a1b033cc07", 16), new BigInteger("191737288d17e660c4b61440d5d14228a0bf9854499f9d68d8274db55d6d954489371ecf314f26bec236e58fac7fffa9b27bcf923e1229c4080d49f7758739e5bd6014383ed2a75ce1be9b0ab22f283c5c5e11216c5658ba444212b6270d629f2d615b8dfdec8545fb7d4f935b0cc10b6948ab4fc1cb1dd496a8f94b51e888dd", 16));
        ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream3);
        dataOutputStream2.writeInt(encode.length);
        dataOutputStream2.write(encode);
        dataOutputStream2.writeInt(AES128Encode.length);
        dataOutputStream2.write(AES128Encode);
        dataOutputStream2.flush();
        dataOutputStream2.close();
        return Base64.encodeToString(byteArrayOutputStream3.toByteArray(), 2);
    }

    private void b(final long j, final HashMap<String, Object> hashMap) {
        if (e.a(e.a("comm/locks/.dhlock"), new LockAction() { // from class: com.mob.commons.c.1
            @Override // com.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("time", String.valueOf(j));
                    DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
                    if (hashMap != null) {
                        hashMap.put("appkey", MobSDK.getAppkey());
                        hashMap.put("apppkg", deviceHelper.getPackageName());
                        hashMap.put("appver", deviceHelper.getAppVersionName());
                        long ak = b.ak();
                        if (ak != 0) {
                            hashMap.put("strategyId", Long.valueOf(ak));
                        }
                    }
                    contentValues.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, Base64.encodeToString(Data.AES128Encode(Data.rawMD5(deviceHelper.getManufacturer()), c.this.d.fromHashMap(hashMap).getBytes("utf-8")), 2));
                    SQLiteHelper.insert(c.this.b(), contentValues);
                    return false;
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                    return false;
                }
            }
        })) {
            return;
        }
        NLog mobLog = MobLog.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("DataHeap add log error data type = ");
        sb.append(hashMap == null ? null : hashMap.get("type"));
        sb.append(", updateTime = ");
        sb.append(j);
        mobLog.e(new Throwable(sb.toString()));
    }

    private void c() {
        String networkType;
        if (b.ad()) {
            return;
        }
        long N = b.N();
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        if (deviceHelper != null && ((networkType = deviceHelper.getNetworkType()) == null || SchedulerSupport.NONE.equals(networkType))) {
            N = 600000;
        }
        if (b.aq()) {
            this.b.sendEmptyMessageDelayed(1, N);
        }
    }

    private boolean d() {
        String networkType;
        if (b.L()) {
            return true;
        }
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        if (deviceHelper == null || (networkType = deviceHelper.getNetworkType()) == null || SchedulerSupport.NONE.equals(networkType)) {
            return false;
        }
        this.f = true;
        return this.f && e.a(e.a("comm/locks/.dhlock"), new LockAction() { // from class: com.mob.commons.c.2
            @Override // com.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                String[][] strArr = new String[50];
                int a2 = c.this.a(strArr);
                while (true) {
                    if (a2 <= 0) {
                        break;
                    }
                    SparseArray a3 = c.this.a(strArr, a2);
                    if (a3 == null) {
                        c.this.f = false;
                        break;
                    }
                    if (a3.size() > 0) {
                        c.this.a((SparseArray<String>) a3);
                    }
                    if (a2 < strArr.length) {
                        break;
                    }
                    a2 = c.this.a(strArr);
                }
                return false;
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String e() {
        /*
            java.lang.String r0 = com.mob.commons.i.g()     // Catch: java.lang.Throwable -> Lc
            java.lang.String r1 = com.mob.commons.j.b(r0)     // Catch: java.lang.Throwable -> La
            r0 = r1
            goto L15
        La:
            r1 = move-exception
            goto Le
        Lc:
            r1 = move-exception
            r0 = 0
        Le:
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.w(r1)
        L15:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L30
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = com.mob.commons.j.b()
            r0.append(r1)
            java.lang.String r1 = "/v5/gcl"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L30:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.c.e():java.lang.String");
    }

    public synchronized void a(long j, HashMap<String, Object> hashMap) {
        if (b.ad()) {
            return;
        }
        Message message = new Message();
        message.what = 2;
        message.obj = new Object[]{Long.valueOf(j), hashMap};
        if (hashMap != null) {
            MobLog.getInstance().d("type: " + hashMap.get("type"), new Object[0]);
        }
        this.b.sendMessage(message);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0023 -> B:6:0x002a). Please report as a decompilation issue!!! */
    public void a(Object... objArr) {
        File file = null;
        file = null;
        file = null;
        file = null;
        try {
            try {
                try {
                    d.a().a(13);
                    ResHelper.deleteFileAndFolder(b(objArr));
                } catch (Throwable th) {
                    try {
                        ResHelper.deleteFileAndFolder(file);
                    } catch (Throwable th2) {
                        d.a().a(4, th2);
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                d.a().a(5, th3);
                ResHelper.deleteFileAndFolder(null);
            }
        } catch (Throwable th4) {
            d a2 = d.a();
            a2.a(4, th4);
            file = a2;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0038, code lost:
    
        return false;
     */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean handleMessage(android.os.Message r8) {
        /*
            r7 = this;
            int r0 = r8.what
            r1 = 1
            r2 = 0
            switch(r0) {
                case 1: goto L2f;
                case 2: goto L8;
                default: goto L7;
            }
        L7:
            goto L38
        L8:
            java.lang.Object r8 = r8.obj
            java.lang.Object[] r8 = (java.lang.Object[]) r8
            r0 = r8[r2]
            r3 = -1
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            java.lang.Object r0 = com.mob.tools.utils.ResHelper.forceCast(r0, r3)
            java.lang.Long r0 = (java.lang.Long) r0
            long r3 = r0.longValue()
            r5 = 0
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 <= 0) goto L38
            r8 = r8[r1]
            java.util.HashMap r8 = (java.util.HashMap) r8
            r7.b(r3, r8)
            r7.c()
            goto L38
        L2f:
            android.os.Handler r8 = r7.b
            r8.removeMessages(r1)
            boolean r8 = r7.d()
        L38:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.c.handleMessage(android.os.Message):boolean");
    }
}
