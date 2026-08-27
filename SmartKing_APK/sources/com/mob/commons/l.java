package com.mob.commons;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import com.autonavi.amap.mapcore.AeUtil;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobSDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.MobLog;
import com.mob.tools.log.NLog;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.MobRSA;
import com.mob.tools.utils.ResHelper;
import com.tencent.bugly.BuglyStrategy;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import org.json.JSONObject;

/* compiled from: TokenFetcher.java */
/* loaded from: classes.dex */
public class l {
    private static final String a = j.b() + "/openid";
    private static String b;
    private static l c;
    private String d;
    private TreeMap<String, Object> g;
    private Context f = MobSDK.getContext();
    private DeviceHelper e = DeviceHelper.getInstance(this.f);

    private l() {
        b = k.a(155);
    }

    public static l a() {
        if (c == null) {
            synchronized (l.class) {
                if (c == null) {
                    c = new l();
                }
            }
        }
        return c;
    }

    private File a(Context context, String str) {
        try {
            if ((Build.VERSION.SDK_INT >= 29 && context.getApplicationInfo().targetSdkVersion >= 29) || !this.e.getSdcardState()) {
                return null;
            }
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (TextUtils.isEmpty(absolutePath)) {
                return null;
            }
            String str2 = absolutePath + "/Mob/";
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(str2, str);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            return file2;
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Get MDID error");
            return null;
        }
    }

    private String a(TreeMap<String, Object> treeMap) {
        HashMap hashMap;
        String str = null;
        if (!b.aq() || treeMap == null || treeMap.isEmpty()) {
            return null;
        }
        try {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("factory", treeMap.get("factory"));
            hashMap2.put(FileDownloadBroadcastHandler.KEY_MODEL, treeMap.get(FileDownloadBroadcastHandler.KEY_MODEL));
            hashMap2.put("sysver", treeMap.get("sysver"));
            hashMap2.put(Dic.IMEI, treeMap.get(Dic.IMEI));
            hashMap2.put(Dic.SERIAL_NO, treeMap.get(Dic.SERIAL_NO));
            hashMap2.put("admt", treeMap.get("admt"));
            hashMap2.put("pkg", treeMap.get("pkg"));
            hashMap2.put("appver", treeMap.get("appver"));
            hashMap2.put("firstLaunchTime", treeMap.get("firstLaunchTime"));
            hashMap2.put("appInstallTime", treeMap.get("appInstallTime"));
            hashMap2.put("deviceId", treeMap.get("deviceId"));
            hashMap2.put("duid", treeMap.get("duid"));
            hashMap2.put("mdId", treeMap.get("mdId"));
            hashMap2.put("momt", treeMap.get("momt"));
            hashMap2.put("mvaId", treeMap.get("mvaId"));
            hashMap2.put("maaId", treeMap.get("maaId"));
            hashMap2.put("anmt", treeMap.get("anmt"));
            hashMap2.put("mcmtarr", treeMap.get("mcmtarr"));
            hashMap2.put("iemtarr", treeMap.get("iemtarr"));
            hashMap2.put("ismtarr", treeMap.get("ismtarr"));
            hashMap2.put("al", treeMap.get("al"));
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
            arrayList.add(new KVPair<>("m", b(new Hashon().fromHashMap(hashMap2))));
            ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
            arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
            arrayList2.add(new KVPair<>(k.a(68), com.mob.commons.b.d.d(MobSDK.getContext())));
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
            networkTimeOut.connectionTimeout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
            String httpPost = new NetworkHelper().httpPost(a, arrayList, (KVPair<String>) null, arrayList2, networkTimeOut);
            MobLog.getInstance().d("[%s] %s", "TokenFetcher", "Request: " + a + "\nvaluesEn: " + arrayList + "\nheaders: " + arrayList2 + "\nResponse: " + httpPost);
            HashMap fromJson = new Hashon().fromJson(httpPost);
            if (!"200".equals(String.valueOf(fromJson.get("code"))) || (hashMap = (HashMap) fromJson.get(AeUtil.ROOT_DATA_PATH_OLD_NAME)) == null) {
                return null;
            }
            String str2 = (String) hashMap.get("token");
            try {
                c.d = str2;
                c(str2);
                return str2;
            } catch (Throwable th) {
                str = str2;
                th = th;
                MobLog.getInstance().e(th, "[%s] %s", "TokenFetcher", "Fetch token from server error.");
                return str;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private HashMap<String, Object> a(String str, byte[] bArr) {
        try {
            return new Hashon().fromJson(Data.AES128Decode(str, bArr));
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Decrypt data error");
            return new HashMap<>();
        }
    }

    private void a(String str) {
        IOException e;
        String str2;
        NLog nLog;
        Object[] objArr;
        DataOutputStream dataOutputStream = null;
        try {
            try {
                File a2 = a(this.f, ".mdid");
                if (a2 != null && a2.exists()) {
                    DataOutputStream dataOutputStream2 = new DataOutputStream(new FileOutputStream(a2));
                    try {
                        dataOutputStream2.writeUTF(str);
                        dataOutputStream = dataOutputStream2;
                    } catch (Throwable th) {
                        th = th;
                        dataOutputStream = dataOutputStream2;
                        MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Cache mdid error");
                        if (dataOutputStream != null) {
                            try {
                                dataOutputStream.flush();
                                dataOutputStream.close();
                                return;
                            } catch (IOException e2) {
                                e = e2;
                                nLog = MobLog.getInstance();
                                str2 = "[%s] %s";
                                objArr = new Object[]{"TokenFetcher", "Close stream error"};
                                nLog.d(e, str2, objArr);
                            }
                        }
                        return;
                    }
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.flush();
                        dataOutputStream.close();
                    } catch (IOException e3) {
                        e = e3;
                        nLog = MobLog.getInstance();
                        str2 = "[%s] %s";
                        objArr = new Object[]{"TokenFetcher", "Close stream error"};
                        nLog.d(e, str2, objArr);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private boolean a(HashMap<String, Object> hashMap) {
        try {
            this.g.put("factory", this.e.getManufacturer());
            this.g.put(FileDownloadBroadcastHandler.KEY_MODEL, this.e.getModel());
            this.g.put("sysver", Integer.valueOf(this.e.getOSVersionInt()));
            this.g.put(Dic.IMEI, this.e.getIMEI());
            this.g.put(Dic.SERIAL_NO, this.e.getSerialno());
            this.g.put("admt", this.e.getAdvertisingID());
            this.g.put("pkg", this.e.getPackageName());
            this.g.put("appver", this.e.getAppVersionName());
            long G = i.G();
            if (G > 0) {
                this.g.put("firstLaunchTime", Long.valueOf(G));
            }
            long d = d();
            if (d > 0) {
                this.g.put("appInstallTime", Long.valueOf(d));
            }
            this.g.put("deviceId", this.e.getDeviceKey());
            this.g.put("duid", DeviceAuthorizer.authorizeForOnce());
            this.g.put("anmt", this.e.getAndroidID());
            String e = e();
            if (!TextUtils.isEmpty(e)) {
                this.g.put("mdId", e);
            }
            String MD5 = Data.MD5(new JSONObject(this.g).toString());
            String h = h();
            if (!TextUtils.isEmpty(h)) {
                this.g.put("momt", h);
            }
            String i = i();
            if (!TextUtils.isEmpty(i)) {
                this.g.put("mvaId", i);
            }
            String j = (hashMap == null || hashMap.isEmpty()) ? j() : (String) hashMap.get("maaid");
            if (!TextUtils.isEmpty(j)) {
                this.g.put("maaId", j);
            }
            HashMap<String, String> listNetworkHardware = this.e.listNetworkHardware();
            ArrayList arrayList = new ArrayList();
            if (listNetworkHardware != null && !listNetworkHardware.isEmpty()) {
                for (Map.Entry<String, String> entry : listNetworkHardware.entrySet()) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("ss", entry.getKey());
                    hashMap2.put(Dic.MAC, entry.getValue());
                    arrayList.add(hashMap2);
                }
                this.g.put("mcmtarr", arrayList);
            }
            String[] queryIMEI = this.e.queryIMEI();
            if (queryIMEI != null && queryIMEI.length > 0) {
                this.g.put("iemtarr", queryIMEI);
            }
            String[] queryIMSI = this.e.queryIMSI();
            if (queryIMSI != null && queryIMSI.length > 0) {
                this.g.put("ismtarr", queryIMSI);
            }
            ArrayList<HashMap<String, String>> ia = this.e.getIA(false);
            if (ia != null && !ia.isEmpty()) {
                this.g.put("al", ia);
            }
            TreeMap<String, Object> treeMap = new TreeMap<>();
            treeMap.put("generalMd5", MD5);
            treeMap.put("maaid", j);
            treeMap.put("mcmtarr", arrayList);
            treeMap.put("iemtarr", queryIMEI);
            treeMap.put("ismtarr", queryIMSI);
            treeMap.put("al", ia);
            b(treeMap);
            if (hashMap == null || hashMap.isEmpty()) {
                MobLog.getInstance().d("[%s] %s", "TokenFetcher", "No openids cache, treat as changed");
                return true;
            }
            if (!MD5.equals((String) hashMap.get("generalMd5"))) {
                MobLog.getInstance().d("[%s] %s", "TokenFetcher", "generalMd5 changed");
                return true;
            }
            ArrayList arrayList2 = (ArrayList) hashMap.get("mcmtarr");
            if (!arrayList.isEmpty()) {
                if (arrayList2 != null && !arrayList2.isEmpty()) {
                    if (arrayList2.size() != arrayList.size()) {
                        MobLog.getInstance().d("[%s] %s", "TokenFetcher", "mcidArr changed");
                        return true;
                    }
                    ArrayList arrayList3 = new ArrayList();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        HashMap hashMap3 = (HashMap) it.next();
                        if (hashMap3 != null && !hashMap3.isEmpty()) {
                            arrayList3.add(hashMap3.get(Dic.MAC));
                        }
                    }
                    ArrayList arrayList4 = new ArrayList();
                    Iterator it2 = arrayList2.iterator();
                    while (it2.hasNext()) {
                        HashMap hashMap4 = (HashMap) it2.next();
                        if (hashMap4 != null && !hashMap4.isEmpty()) {
                            arrayList4.add(hashMap4.get(Dic.MAC));
                        }
                    }
                    Iterator it3 = arrayList3.iterator();
                    while (it3.hasNext()) {
                        if (!arrayList4.contains((String) it3.next())) {
                            MobLog.getInstance().d("[%s] %s", "TokenFetcher", "mcidArr changed");
                            return true;
                        }
                    }
                }
                MobLog.getInstance().d("[%s] %s", "TokenFetcher", "mcidArr changed");
                return true;
            }
            ArrayList arrayList5 = (ArrayList) hashMap.get("iemtarr");
            if (queryIMEI != null && queryIMEI.length > 0) {
                if (arrayList5 != null && !arrayList5.isEmpty()) {
                    if (arrayList5.size() != queryIMEI.length) {
                        MobLog.getInstance().d("[%s] %s", "TokenFetcher", "ieidArr changed");
                        return true;
                    }
                    for (String str : queryIMEI) {
                        if (!arrayList5.contains(str)) {
                            MobLog.getInstance().d("[%s] %s", "TokenFetcher", "ieidArr changed");
                            return true;
                        }
                    }
                }
                MobLog.getInstance().d("[%s] %s", "TokenFetcher", "ieidArr changed");
                return true;
            }
            ArrayList arrayList6 = (ArrayList) hashMap.get("ismtarr");
            if (queryIMSI != null && queryIMSI.length > 0) {
                if (arrayList6 != null && !arrayList6.isEmpty()) {
                    if (arrayList6.size() != queryIMSI.length) {
                        MobLog.getInstance().d("[%s] %s", "TokenFetcher", "isidArr changed");
                        return true;
                    }
                    for (String str2 : queryIMSI) {
                        if (!arrayList6.contains(str2)) {
                            MobLog.getInstance().d("[%s] %s", "TokenFetcher", "isidArr changed");
                            return true;
                        }
                    }
                }
                MobLog.getInstance().d("[%s] %s", "TokenFetcher", "isidArr changed");
                return true;
            }
            ArrayList arrayList7 = (ArrayList) hashMap.get("al");
            if (ia != null && !ia.isEmpty()) {
                if (arrayList7 != null && !arrayList7.isEmpty()) {
                    if (arrayList7.size() != ia.size()) {
                        MobLog.getInstance().d("[%s] %s", "TokenFetcher", "al changed");
                        return true;
                    }
                    ArrayList arrayList8 = new ArrayList();
                    Iterator<HashMap<String, String>> it4 = ia.iterator();
                    while (it4.hasNext()) {
                        HashMap<String, String> next = it4.next();
                        if (next != null && !next.isEmpty()) {
                            arrayList8.add(next.get("pkg"));
                        }
                    }
                    ArrayList arrayList9 = new ArrayList();
                    Iterator it5 = arrayList7.iterator();
                    while (it5.hasNext()) {
                        HashMap hashMap5 = (HashMap) it5.next();
                        if (hashMap5 != null && !hashMap5.isEmpty()) {
                            arrayList9.add(hashMap5.get("pkg"));
                        }
                    }
                    Iterator it6 = arrayList8.iterator();
                    while (it6.hasNext()) {
                        if (!arrayList9.contains((String) it6.next())) {
                            MobLog.getInstance().d("[%s] %s", "TokenFetcher", "al changed");
                            return true;
                        }
                    }
                }
                MobLog.getInstance().d("[%s] %s", "TokenFetcher", "al changed");
                return true;
            }
            MobLog.getInstance().d("[%s] %s", "TokenFetcher", "No changes");
            return false;
        } catch (Throwable th) {
            MobLog.getInstance().e(th, "[%s] %s", "TokenFetcher", "Fetch token from server error.");
            return false;
        }
    }

    private byte[] a(String str, TreeMap<String, Object> treeMap) {
        try {
            return Data.AES128Encode(str, new JSONObject(treeMap).toString());
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Encypt data error");
            return null;
        }
    }

    private String b(String str) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        Random random = new Random();
        dataOutputStream.writeLong(random.nextLong());
        dataOutputStream.writeLong(random.nextLong());
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

    private void b(TreeMap<String, Object> treeMap) {
        IOException e;
        String str;
        NLog nLog;
        Object[] objArr;
        FileChannel fileChannel = null;
        try {
            try {
                File dataCacheFile = ResHelper.getDataCacheFile(this.f, ".opdn");
                byte[] a2 = a(b, treeMap);
                if (a2 != null && a2.length > 0) {
                    FileChannel channel = new FileOutputStream(dataCacheFile).getChannel();
                    try {
                        channel.write(ByteBuffer.wrap(a2));
                        channel.force(true);
                        fileChannel = channel;
                    } catch (Throwable th) {
                        th = th;
                        fileChannel = channel;
                        if (fileChannel != null) {
                            try {
                                fileChannel.close();
                            } catch (IOException e2) {
                                MobLog.getInstance().d(e2, "[%s] %s", "TokenFetcher", "Close stream error");
                            }
                        }
                        throw th;
                    }
                }
                if (fileChannel != null) {
                    try {
                        fileChannel.close();
                    } catch (IOException e3) {
                        e = e3;
                        nLog = MobLog.getInstance();
                        str = "[%s] %s";
                        objArr = new Object[]{"TokenFetcher", "Close stream error"};
                        nLog.d(e, str, objArr);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private String c() {
        this.g = new TreeMap<>();
        String str = null;
        try {
            String k = k();
            boolean a2 = a(l());
            MobLog.getInstance().d("[%s] %s", "TokenFetcher", "cachedToken: " + k);
            if (TextUtils.isEmpty(k)) {
                k = a(this.g);
            } else {
                MobLog.getInstance().d("[%s] %s", "TokenFetcher", "isChanged: " + a2);
                if (a2) {
                    k = a(this.g);
                }
            }
            str = k;
            c.d = str;
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Sync token from cache & net error");
        }
        return str;
    }

    private void c(String str) {
        IOException e;
        Object[] objArr;
        String str2;
        NLog nLog;
        MobLog.getInstance().d("[%s] %s", "TokenFetcher", "Write token cache");
        DataOutputStream dataOutputStream = null;
        try {
            try {
                File dataCacheFile = ResHelper.getDataCacheFile(this.f, ".optn");
                if (dataCacheFile != null) {
                    DataOutputStream dataOutputStream2 = new DataOutputStream(new FileOutputStream(dataCacheFile));
                    try {
                        dataOutputStream2.writeUTF(str);
                        dataOutputStream = dataOutputStream2;
                    } catch (Throwable th) {
                        th = th;
                        dataOutputStream = dataOutputStream2;
                        MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Cache token error");
                        if (dataOutputStream != null) {
                            try {
                                dataOutputStream.flush();
                                dataOutputStream.close();
                                return;
                            } catch (IOException e2) {
                                e = e2;
                                nLog = MobLog.getInstance();
                                str2 = "[%s] %s";
                                objArr = new Object[]{"TokenFetcher", "Close stream error"};
                                nLog.d(e, str2, objArr);
                            }
                        }
                        return;
                    }
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.flush();
                        dataOutputStream.close();
                    } catch (IOException e3) {
                        e = e3;
                        nLog = MobLog.getInstance();
                        str2 = "[%s] %s";
                        objArr = new Object[]{"TokenFetcher", "Close stream error"};
                        nLog.d(e, str2, objArr);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private long d() {
        PackageInfo packageInfo;
        try {
            PackageManager packageManager = this.f.getPackageManager();
            if (packageManager == null || (packageInfo = packageManager.getPackageInfo(this.e.getPackageName(), 0)) == null || Build.VERSION.SDK_INT < 9) {
                return 0L;
            }
            return packageInfo.firstInstallTime;
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Get ins time error");
            return 0L;
        }
    }

    private String e() {
        String f = f();
        if (TextUtils.isEmpty(f) && this.e.getSdcardState()) {
            f = g();
            if (!TextUtils.isEmpty(f)) {
                a(f);
            }
        }
        return f;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x007b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String f() {
        /*
            r11 = this;
            r0 = 0
            r1 = 1
            r2 = 0
            r3 = 2
            android.content.Context r4 = r11.f     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            java.lang.String r5 = ".mdid"
            java.io.File r4 = r11.a(r4, r5)     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            if (r4 == 0) goto L26
            boolean r5 = r4.exists()     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            if (r5 == 0) goto L26
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            r5.<init>(r4)     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            java.lang.String r5 = r4.readUTF()     // Catch: java.lang.Throwable -> L24 java.lang.Throwable -> L78
            r0 = r4
            goto L27
        L24:
            r5 = move-exception
            goto L4a
        L26:
            r5 = r0
        L27:
            if (r0 == 0) goto L41
            r0.close()     // Catch: java.io.IOException -> L2d
            goto L41
        L2d:
            r0 = move-exception
            com.mob.tools.log.NLog r4 = com.mob.tools.MobLog.getInstance()
            java.lang.String r6 = "[%s] %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r7 = "TokenFetcher"
            r3[r2] = r7
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            r4.d(r0, r6, r3)
        L41:
            r0 = r5
            goto L77
        L43:
            r4 = move-exception
            r10 = r4
            r4 = r0
            r0 = r10
            goto L79
        L48:
            r5 = move-exception
            r4 = r0
        L4a:
            com.mob.tools.log.NLog r6 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L78
            java.lang.String r7 = "[%s] %s"
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L78
            java.lang.String r9 = "TokenFetcher"
            r8[r2] = r9     // Catch: java.lang.Throwable -> L78
            java.lang.String r9 = "Read mdid cache error"
            r8[r1] = r9     // Catch: java.lang.Throwable -> L78
            r6.d(r5, r7, r8)     // Catch: java.lang.Throwable -> L78
            if (r4 == 0) goto L77
            r4.close()     // Catch: java.io.IOException -> L63
            goto L77
        L63:
            r4 = move-exception
            com.mob.tools.log.NLog r5 = com.mob.tools.MobLog.getInstance()
            java.lang.String r6 = "[%s] %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r7 = "TokenFetcher"
            r3[r2] = r7
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            r5.d(r4, r6, r3)
        L77:
            return r0
        L78:
            r0 = move-exception
        L79:
            if (r4 == 0) goto L93
            r4.close()     // Catch: java.io.IOException -> L7f
            goto L93
        L7f:
            r4 = move-exception
            com.mob.tools.log.NLog r5 = com.mob.tools.MobLog.getInstance()
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r6 = "TokenFetcher"
            r3[r2] = r6
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            java.lang.String r1 = "[%s] %s"
            r5.d(r4, r1, r3)
        L93:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.l.f():java.lang.String");
    }

    private String g() {
        try {
            return Data.byteToHex(Data.SHA1(UUID.randomUUID().toString()));
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Generate mdid error");
            return null;
        }
    }

    private String h() {
        return com.mob.commons.b.d.c(this.f);
    }

    private String i() {
        try {
            return Data.byteToHex(Data.SHA1(DeviceAuthorizer.authorizeForOnce() + this.e.getSignMD5()));
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Generate mvaid error");
            return null;
        }
    }

    private String j() {
        try {
            return Data.byteToHex(Data.SHA1(this.e.getPackageName() + UUID.randomUUID().toString()));
        } catch (Throwable th) {
            MobLog.getInstance().d(th, "[%s] %s", "TokenFetcher", "Generate mvaid error");
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x007b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String k() {
        /*
            r11 = this;
            r0 = 0
            r1 = 1
            r2 = 0
            r3 = 2
            android.content.Context r4 = r11.f     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            java.lang.String r5 = ".optn"
            java.io.File r4 = com.mob.tools.utils.ResHelper.getDataCacheFile(r4, r5)     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            if (r4 == 0) goto L26
            boolean r5 = r4.exists()     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            if (r5 == 0) goto L26
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            r5.<init>(r4)     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L48
            java.lang.String r5 = r4.readUTF()     // Catch: java.lang.Throwable -> L24 java.lang.Throwable -> L78
            r0 = r4
            goto L27
        L24:
            r5 = move-exception
            goto L4a
        L26:
            r5 = r0
        L27:
            if (r0 == 0) goto L41
            r0.close()     // Catch: java.io.IOException -> L2d
            goto L41
        L2d:
            r0 = move-exception
            com.mob.tools.log.NLog r4 = com.mob.tools.MobLog.getInstance()
            java.lang.String r6 = "[%s] %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r7 = "TokenFetcher"
            r3[r2] = r7
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            r4.d(r0, r6, r3)
        L41:
            r0 = r5
            goto L77
        L43:
            r4 = move-exception
            r10 = r4
            r4 = r0
            r0 = r10
            goto L79
        L48:
            r5 = move-exception
            r4 = r0
        L4a:
            com.mob.tools.log.NLog r6 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L78
            java.lang.String r7 = "[%s] %s"
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L78
            java.lang.String r9 = "TokenFetcher"
            r8[r2] = r9     // Catch: java.lang.Throwable -> L78
            java.lang.String r9 = "Read token cache error"
            r8[r1] = r9     // Catch: java.lang.Throwable -> L78
            r6.d(r5, r7, r8)     // Catch: java.lang.Throwable -> L78
            if (r4 == 0) goto L77
            r4.close()     // Catch: java.io.IOException -> L63
            goto L77
        L63:
            r4 = move-exception
            com.mob.tools.log.NLog r5 = com.mob.tools.MobLog.getInstance()
            java.lang.String r6 = "[%s] %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r7 = "TokenFetcher"
            r3[r2] = r7
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            r5.d(r4, r6, r3)
        L77:
            return r0
        L78:
            r0 = move-exception
        L79:
            if (r4 == 0) goto L93
            r4.close()     // Catch: java.io.IOException -> L7f
            goto L93
        L7f:
            r4 = move-exception
            com.mob.tools.log.NLog r5 = com.mob.tools.MobLog.getInstance()
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r6 = "TokenFetcher"
            r3[r2] = r6
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            java.lang.String r1 = "[%s] %s"
            r5.d(r4, r1, r3)
        L93:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.l.k():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0090 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.HashMap<java.lang.String, java.lang.Object> l() {
        /*
            r11 = this;
            r0 = 0
            r1 = 1
            r2 = 0
            r3 = 2
            android.content.Context r4 = r11.f     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5d
            java.lang.String r5 = ".opdn"
            java.io.File r4 = com.mob.tools.utils.ResHelper.getDataCacheFile(r4, r5)     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5d
            if (r4 == 0) goto L3b
            boolean r5 = r4.exists()     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5d
            if (r5 == 0) goto L3b
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5d
            r5.<init>(r4)     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5d
            java.nio.channels.FileChannel r4 = r5.getChannel()     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L5d
            long r5 = r4.size()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L8d
            int r5 = (int) r5     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L8d
            java.nio.ByteBuffer r5 = java.nio.ByteBuffer.allocate(r5)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L8d
        L26:
            int r6 = r4.read(r5)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L8d
            if (r6 <= 0) goto L2d
            goto L26
        L2d:
            byte[] r5 = r5.array()     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L8d
            java.lang.String r6 = com.mob.commons.l.b     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L8d
            java.util.HashMap r5 = r11.a(r6, r5)     // Catch: java.lang.Throwable -> L39 java.lang.Throwable -> L8d
            r0 = r4
            goto L3c
        L39:
            r5 = move-exception
            goto L5f
        L3b:
            r5 = r0
        L3c:
            if (r0 == 0) goto L56
            r0.close()     // Catch: java.io.IOException -> L42
            goto L56
        L42:
            r0 = move-exception
            com.mob.tools.log.NLog r4 = com.mob.tools.MobLog.getInstance()
            java.lang.String r6 = "[%s] %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r7 = "TokenFetcher"
            r3[r2] = r7
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            r4.d(r0, r6, r3)
        L56:
            r0 = r5
            goto L8c
        L58:
            r4 = move-exception
            r10 = r4
            r4 = r0
            r0 = r10
            goto L8e
        L5d:
            r5 = move-exception
            r4 = r0
        L5f:
            com.mob.tools.log.NLog r6 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L8d
            java.lang.String r7 = "[%s] %s"
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L8d
            java.lang.String r9 = "TokenFetcher"
            r8[r2] = r9     // Catch: java.lang.Throwable -> L8d
            java.lang.String r9 = "Read openid cache error"
            r8[r1] = r9     // Catch: java.lang.Throwable -> L8d
            r6.d(r5, r7, r8)     // Catch: java.lang.Throwable -> L8d
            if (r4 == 0) goto L8c
            r4.close()     // Catch: java.io.IOException -> L78
            goto L8c
        L78:
            r4 = move-exception
            com.mob.tools.log.NLog r5 = com.mob.tools.MobLog.getInstance()
            java.lang.String r6 = "[%s] %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r7 = "TokenFetcher"
            r3[r2] = r7
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            r5.d(r4, r6, r3)
        L8c:
            return r0
        L8d:
            r0 = move-exception
        L8e:
            if (r4 == 0) goto La8
            r4.close()     // Catch: java.io.IOException -> L94
            goto La8
        L94:
            r4 = move-exception
            com.mob.tools.log.NLog r5 = com.mob.tools.MobLog.getInstance()
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r6 = "TokenFetcher"
            r3[r2] = r6
            java.lang.String r2 = "Close stream error"
            r3[r1] = r2
            java.lang.String r1 = "[%s] %s"
            r5.d(r4, r1, r3)
        La8:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.l.l():java.util.HashMap");
    }

    public String b() {
        if (!b.ao()) {
            return null;
        }
        MobLog.getInstance().d("[%s] %s", "TokenFetcher", "Mem token: " + this.d);
        if (TextUtils.isEmpty(this.d)) {
            synchronized (l.class) {
                if (TextUtils.isEmpty(this.d)) {
                    return c();
                }
            }
        }
        return this.d;
    }
}
