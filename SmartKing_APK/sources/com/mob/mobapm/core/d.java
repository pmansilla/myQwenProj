package com.mob.mobapm.core;

import android.text.TextUtils;
import com.autonavi.amap.mapcore.AeUtil;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.commons.MOBAPM;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.mobapm.MobAPM;
import com.mob.mobapm.internal.APMMobCommunicator;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/* loaded from: classes.dex */
public class d {
    private static String a = "http://api.data.sentinel.mob.com";
    private static String b = "http://api.manager.sentinel.mob.com";
    private static String c = "http://api.config.sentinel.mob.com";
    private static String d;
    private static APMMobCommunicator f;
    private static HashMap<String, Object> h;
    private static final Object e = new Object();
    public static final Hashon g = new Hashon();
    private static final Object i = new Object();

    static {
        try {
            a = MobSDK.checkRequestUrl(a);
            b = MobSDK.checkRequestUrl(b);
            c = MobSDK.checkRequestUrl(c);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: replace server url from commons has error:" + th, new Object[0]);
        }
        a();
    }

    private static Object a(String str) throws Throwable {
        if (MobAPM.goldenKey) {
            return b().a(str);
        }
        throw new Throwable("APM: Service closed");
    }

    protected static Object a(String str, HashMap<String, Object> hashMap, boolean z) throws Throwable {
        HashMap<String, Object> hashMap2 = h;
        if ((hashMap2 == null || hashMap2.isEmpty()) && !str.contains("config")) {
            c();
        }
        return a(hashMap, str, false, z);
    }

    public static Object a(HashMap<String, Object> hashMap) throws Throwable {
        String str = (String) hashMap.get(FileDownloadModel.URL);
        int intValue = ((Integer) hashMap.get("requestType")).intValue();
        String str2 = (String) hashMap.get("requestMethod");
        if (intValue == 2) {
            if (!MobAPM.goldenKey || TextUtils.isEmpty(str)) {
                return -1;
            }
            return a(str);
        }
        if (intValue != 1) {
            return null;
        }
        if (MobAPM.goldenKey) {
            return a(hashMap, str, "GET".equals(str2));
        }
        return -1;
    }

    private static Object a(HashMap<String, Object> hashMap, String str, boolean z) throws Throwable {
        Object a2;
        if (!MobAPM.goldenKey) {
            return new Throwable("APM: Service closed");
        }
        try {
            String str2 = hashMap.containsKey("param") ? (String) hashMap.get("param") : "";
            String str3 = hashMap.containsKey("header") ? (String) hashMap.get("header") : "";
            String str4 = hashMap.containsKey("body") ? (String) hashMap.get("body") : "";
            int intValue = hashMap.containsKey("postType") ? ((Integer) hashMap.get("postType")).intValue() : 0;
            if (z) {
                a2 = b().b(str, str3, false);
            } else {
                HashMap<String, String> hashMap2 = new HashMap<>();
                if (intValue == 2) {
                    hashMap2.put("Content-Type", "application/json");
                    a2 = b().b(str, false, hashMap2, str4);
                } else {
                    if (intValue != 1) {
                        return null;
                    }
                    a2 = b().a(str, false, hashMap2, str2);
                }
            }
            return a2;
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM:radar reqeust error:" + th.getMessage(), new Object[0]);
            throw th;
        }
    }

    private static Object a(HashMap<String, Object> hashMap, String str, boolean z, boolean z2) throws Throwable {
        if (!MobAPM.goldenKey) {
            return new Throwable("Mob Service forbidden");
        }
        try {
            com.mob.mobapm.d.a.a().d("APM:请求地址和参数>>>" + str + " -- " + hashMap, new Object[0]);
            Object a2 = b().a(hashMap, str, z, z2);
            com.mob.mobapm.d.a.a().d("APM:请求返回结果>>>" + str + " -- " + a2, new Object[0]);
            return a2;
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM:request error:" + th.getMessage(), new Object[0]);
            throw th;
        }
    }

    public static Object a(HashMap<String, Object> hashMap, boolean z) throws Throwable {
        if (!MobAPM.goldenKey) {
            return new Throwable("APM: Service closed");
        }
        if (hashMap != null) {
            hashMap.putAll(com.mob.mobapm.b.a.e());
        }
        hashMap.put("sdkVersion", MOBAPM.SDK_VERSION_NAME);
        hashMap.put("sdkVersionInt", Integer.valueOf(MOBAPM.SDK_VERSION_CODE));
        hashMap.put("appVersion", DeviceHelper.getInstance(MobSDK.getContext()).getAppVersionName() + SQLBuilder.PARENTHESES_LEFT + DeviceHelper.getInstance(MobSDK.getContext()).getAppVersion() + SQLBuilder.PARENTHESES_RIGHT);
        hashMap.remove("serialno");
        if (!z) {
            return -1;
        }
        return a(a + "/android/error-stack-msg", hashMap, false);
    }

    private static byte[] a() {
        String byteToHex;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            try {
                dataOutputStream.writeLong(new Random().nextLong());
                byteToHex = Data.byteToHex(byteArrayOutputStream.toByteArray());
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d(th);
            }
        } catch (Throwable th2) {
            try {
                com.mob.mobapm.d.a.a().d(th2);
                dataOutputStream.close();
            } finally {
                try {
                    dataOutputStream.close();
                } catch (Throwable th3) {
                    com.mob.mobapm.d.a.a().d(th3);
                }
            }
        }
        if (byteToHex.length() == 16) {
            return byteToHex.getBytes();
        }
        dataOutputStream.close();
        return "ar1xcsbglilzpjs5".getBytes();
    }

    public static synchronized APMMobCommunicator b() {
        APMMobCommunicator aPMMobCommunicator;
        synchronized (d.class) {
            if (f == null) {
                f = new APMMobCommunicator(1024, "930665c1fb1db3ce89aae20f9cfe049b57436060f045840870702fa444694cdcde320d6d06f58a3f6f0234ecea073069bc6e82ec1b51f96852bf44447187bf1f", "1cb7ded4a8927b030cc6a34f692c59fcdf3135a3a477227b0115d1236f9fedeaae79cf3fb2dce4b95279623917724064f2d36fbe998453bf4b864f8b0afaf6372b4a04b3dbb1bbe1d3cf0f46d2b0cf92645b1224b007379aa4513d3a37df57aac93e9b9fa12f80dff3023f06e1d975498a647ba7222f8608111c89d3a77d43cf");
            }
            aPMMobCommunicator = f;
        }
        return aPMMobCommunicator;
    }

    public static Object b(HashMap<String, Object> hashMap) throws Throwable {
        if (hashMap != null) {
            hashMap.putAll(com.mob.mobapm.b.a.e());
        }
        if (!c.e) {
            return -1;
        }
        return a(a + "/app-log", hashMap, false);
    }

    public static Object c(HashMap<String, Object> hashMap) throws Throwable {
        if (!MobAPM.goldenKey) {
            return -1;
        }
        return a(b + "/dialing-task/result", hashMap, false);
    }

    private static void c() {
        synchronized (i) {
            try {
                HashMap hashMap = new HashMap();
                hashMap.putAll(com.mob.mobapm.b.a.e());
                hashMap.remove("language");
                hashMap.remove("serialno");
                hashMap.put("sdkVersion", MOBAPM.SDK_VERSION_NAME);
                hashMap.put("sdkVersionInt", Integer.valueOf(MOBAPM.SDK_VERSION_CODE));
                Object a2 = b().a(c + "/config", g.fromHashMap(hashMap), false);
                if (a2 instanceof String) {
                    HashMap<String, Object> hashMap2 = (HashMap) g.fromJson((String) a2).get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
                    h = hashMap2;
                    if (hashMap2 != null && !hashMap2.isEmpty()) {
                        if (h.containsKey("openSentinel")) {
                            if (((Boolean) h.get("openSentinel")).booleanValue()) {
                                if (h.containsKey("openDialingTest")) {
                                    boolean booleanValue = ((Boolean) h.get("openDialingTest")).booleanValue();
                                    com.mob.mobapm.a.a.b().a(booleanValue);
                                    if (!booleanValue) {
                                        return;
                                    }
                                }
                                ArrayList arrayList = new ArrayList();
                                List<HashMap> list = (List) h.get("rules");
                                if (list != null && !list.isEmpty()) {
                                    for (HashMap hashMap3 : list) {
                                        hashMap3.remove(ConnectionModel.ID);
                                        hashMap3.remove("appKey");
                                        arrayList.add(hashMap3);
                                    }
                                    h.put("rules", arrayList);
                                }
                                List<HashMap> list2 = (List) h.get("dialingTasks");
                                if (list2 != null && !list2.isEmpty()) {
                                    HashMap hashMap4 = new HashMap();
                                    for (HashMap hashMap5 : list2) {
                                        hashMap4.put(String.valueOf(hashMap5.get(ConnectionModel.ID)), hashMap5);
                                    }
                                    com.mob.mobapm.b.a.i(hashMap4);
                                    com.mob.mobapm.a.a.b().a();
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM:config request error:" + th.getMessage(), new Object[0]);
            }
        }
    }

    public static Object d(HashMap<String, Object> hashMap) throws Throwable {
        if (hashMap != null) {
            hashMap.putAll(com.mob.mobapm.b.a.e());
            hashMap.remove("serialno");
        }
        if (!c.h) {
            return -1;
        }
        return a(a + "/socket-log", hashMap, false);
    }

    public static HashMap<String, Object> d() {
        HashMap<String, Object> hashMap = h;
        if (hashMap == null || hashMap.isEmpty()) {
            c();
        }
        return h;
    }

    public static Object e(HashMap<String, Object> hashMap) throws Throwable {
        if (hashMap != null) {
            hashMap.putAll(com.mob.mobapm.b.a.e());
        }
        if (!c.e) {
            return -1;
        }
        return a(a + "/http-log", hashMap, false);
    }

    public static String e() {
        synchronized (e) {
            if (d == null) {
                d = DeviceAuthorizer.authorize(new MOBAPM());
            }
        }
        return d;
    }

    public static Object f(HashMap<String, Object> hashMap) throws Throwable {
        if (hashMap != null) {
            hashMap.putAll(com.mob.mobapm.b.a.e());
        }
        if (!c.e) {
            return -1;
        }
        return a(a + "/http-analysis-log", hashMap, false);
    }
}
