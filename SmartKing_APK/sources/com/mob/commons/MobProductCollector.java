package com.mob.commons;

import android.os.SystemClock;
import cn.sharesdk.framework.ShareSDK;
import com.mob.guard.MobGuard;
import com.mob.tools.MobLog;
import com.mob.tools.proguard.PublicMemberKeeper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/* loaded from: classes.dex */
public class MobProductCollector implements PublicMemberKeeper {
    public static final String[] MOB_PRODUCTS = {ShareSDK.SDK_TAG, "SMSSDK", "MOBLINK", "MOBPUSH", "SECVERIFY", "MOBADSDK", MobGuard.SDK_TAG, "GESVERIFY", "MOBAPM", "ADPUSH"};
    public static final String[] MOB_PRODUCTS_DEPRECATED = {"SHAREREC", "MOBAPI", "UMSSDK", "CMSSDK", "BBSSDK", "SHOPSDK", "PAYSDK", "MOBIM", "ANALYSDK", "MOBVERIFY"};
    public static final String[] MOB_SOLUTIONS = {"GROWSOLUTION"};
    private static boolean a = false;
    private static final HashMap<String, MobProduct> b = new HashMap<>();
    private static final HashMap<String, MobSolution> c = new HashMap<>();

    private static String a(int i) {
        long currentTimeMillis = System.currentTimeMillis() ^ SystemClock.elapsedRealtime();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(currentTimeMillis);
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            if ("char".equalsIgnoreCase(random.nextInt(2) % 2 == 0 ? "char" : "num")) {
                stringBuffer.insert(i2 + 1, (char) (random.nextInt(26) + 97));
            } else {
                stringBuffer.insert(stringBuffer.length(), random.nextInt(10));
            }
        }
        return stringBuffer.toString().substring(0, 40);
    }

    private static HashMap<String, MobProduct> a() {
        HashMap<String, MobProduct> hashMap = new HashMap<>();
        for (Object obj : g.a) {
            try {
                Class<?> cls = obj instanceof String ? Class.forName(String.valueOf(obj).trim()) : (Class) obj;
                int i = 0;
                if (MobProduct.class.isAssignableFrom(cls) && !MobProduct.class.equals(cls)) {
                    MobProduct mobProduct = (MobProduct) cls.newInstance();
                    String productTag = mobProduct.getProductTag();
                    String[] strArr = MOB_PRODUCTS;
                    int length = strArr.length;
                    while (true) {
                        if (i < length) {
                            String str = strArr[i];
                            if (str.equals(productTag)) {
                                hashMap.put(str, mobProduct);
                                break;
                            }
                            i++;
                        }
                    }
                } else if (!MobSolution.class.isAssignableFrom(cls) || MobSolution.class.equals(cls)) {
                    cls.newInstance();
                } else {
                    MobSolution mobSolution = (MobSolution) cls.newInstance();
                    String solutionTag = mobSolution.getSolutionTag();
                    String[] strArr2 = MOB_SOLUTIONS;
                    int length2 = strArr2.length;
                    while (true) {
                        if (i < length2) {
                            String str2 = strArr2[i];
                            if (str2.equals(solutionTag)) {
                                c.put(str2, mobSolution);
                                break;
                            }
                            i++;
                        }
                    }
                }
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
        return hashMap;
    }

    private static String b() {
        return a(40);
    }

    public static synchronized void collect() {
        synchronized (MobProductCollector.class) {
            getProducts();
        }
    }

    public static synchronized ArrayList<MobProduct> getProducts() {
        ArrayList<MobProduct> arrayList;
        synchronized (MobProductCollector.class) {
            if (!a) {
                b.putAll(a());
                a = true;
            }
            arrayList = new ArrayList<>();
            arrayList.addAll(b.values());
        }
        return arrayList;
    }

    public static synchronized String getUserIdentity() {
        String userIdentity;
        synchronized (MobProductCollector.class) {
            userIdentity = getUserIdentity(getProducts());
        }
        return userIdentity;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0119 A[Catch: all -> 0x02c5, Throwable -> 0x02c7, TryCatch #2 {Throwable -> 0x02c7, blocks: (B:4:0x0003, B:7:0x0022, B:10:0x0039, B:13:0x0050, B:16:0x0067, B:19:0x007e, B:22:0x0095, B:24:0x00d9, B:27:0x00e0, B:28:0x00e9, B:30:0x0119, B:31:0x012d, B:33:0x018a, B:56:0x01ec, B:58:0x01f8, B:59:0x0206, B:61:0x020c, B:73:0x024f, B:75:0x025b, B:76:0x026c, B:81:0x00e5, B:82:0x008b, B:83:0x0074, B:84:0x005d, B:85:0x0046, B:86:0x002f, B:87:0x0018), top: B:3:0x0003, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x018a A[Catch: all -> 0x02c5, Throwable -> 0x02c7, TRY_LEAVE, TryCatch #2 {Throwable -> 0x02c7, blocks: (B:4:0x0003, B:7:0x0022, B:10:0x0039, B:13:0x0050, B:16:0x0067, B:19:0x007e, B:22:0x0095, B:24:0x00d9, B:27:0x00e0, B:28:0x00e9, B:30:0x0119, B:31:0x012d, B:33:0x018a, B:56:0x01ec, B:58:0x01f8, B:59:0x0206, B:61:0x020c, B:73:0x024f, B:75:0x025b, B:76:0x026c, B:81:0x00e5, B:82:0x008b, B:83:0x0074, B:84:0x005d, B:85:0x0046, B:86:0x002f, B:87:0x0018), top: B:3:0x0003, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01f8 A[Catch: all -> 0x02c5, Throwable -> 0x02c7, TryCatch #2 {Throwable -> 0x02c7, blocks: (B:4:0x0003, B:7:0x0022, B:10:0x0039, B:13:0x0050, B:16:0x0067, B:19:0x007e, B:22:0x0095, B:24:0x00d9, B:27:0x00e0, B:28:0x00e9, B:30:0x0119, B:31:0x012d, B:33:0x018a, B:56:0x01ec, B:58:0x01f8, B:59:0x0206, B:61:0x020c, B:73:0x024f, B:75:0x025b, B:76:0x026c, B:81:0x00e5, B:82:0x008b, B:83:0x0074, B:84:0x005d, B:85:0x0046, B:86:0x002f, B:87:0x0018), top: B:3:0x0003, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x025b A[Catch: all -> 0x02c5, Throwable -> 0x02c7, TryCatch #2 {Throwable -> 0x02c7, blocks: (B:4:0x0003, B:7:0x0022, B:10:0x0039, B:13:0x0050, B:16:0x0067, B:19:0x007e, B:22:0x0095, B:24:0x00d9, B:27:0x00e0, B:28:0x00e9, B:30:0x0119, B:31:0x012d, B:33:0x018a, B:56:0x01ec, B:58:0x01f8, B:59:0x0206, B:61:0x020c, B:73:0x024f, B:75:0x025b, B:76:0x026c, B:81:0x00e5, B:82:0x008b, B:83:0x0074, B:84:0x005d, B:85:0x0046, B:86:0x002f, B:87:0x0018), top: B:3:0x0003, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized java.lang.String getUserIdentity(java.util.ArrayList<com.mob.commons.MobProduct> r18) {
        /*
            Method dump skipped, instructions count: 725
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.MobProductCollector.getUserIdentity(java.util.ArrayList):java.lang.String");
    }

    public static synchronized boolean registerProduct(MobProduct mobProduct) {
        synchronized (MobProductCollector.class) {
            if (mobProduct != null) {
                if (!b.containsKey(mobProduct.getProductTag())) {
                    b.put(mobProduct.getProductTag(), mobProduct);
                    return true;
                }
            }
            return false;
        }
    }

    public static void syncInit() {
        try {
            MOBLINK moblink = new MOBLINK();
            if (moblink instanceof MobProduct) {
                moblink.getProductTag();
            }
        } catch (Throwable unused) {
        }
    }
}
