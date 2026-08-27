package com.mob.commons.a;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import com.alibaba.fastjson.asm.Opcodes;
import com.amap.location.common.model.Adjacent;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import com.sun.mail.imap.IMAPStore;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* compiled from: RtClt.java */
/* loaded from: classes.dex */
public class r extends d {
    private static final String a;
    private PackageManager b;
    private Process c = null;
    private OutputStream d = null;
    private String e = null;
    private long f = 0;
    private boolean g = true;

    static {
        a = Build.VERSION.SDK_INT >= 16 ? "^u\\d+_a\\d+" : "^app_\\d+";
    }

    private ArrayList<HashMap<String, Object>> a(HashMap<String, String>[][] hashMapArr, ArrayList<long[]> arrayList) {
        ArrayList<HashMap<String, Object>> arrayList2 = new ArrayList<>(hashMapArr.length);
        for (HashMap<String, String>[] hashMapArr2 : hashMapArr) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("runtimes", 0L);
            hashMap.put("fg", 0);
            hashMap.put("bg", 0);
            hashMap.put("empty", 0);
            arrayList2.add(hashMap);
            int length = hashMapArr2.length - 1;
            while (length >= 0) {
                if (hashMapArr2[length] != null) {
                    hashMap.put("runtimes", Long.valueOf(((Long) ResHelper.forceCast(hashMap.get("runtimes"), 0L)).longValue() + (length == 0 ? 0L : arrayList.get(length)[1])));
                    if ("fg".equals(hashMapArr2[length].get("pcy"))) {
                        hashMap.put("fg", Integer.valueOf(((Integer) ResHelper.forceCast(hashMap.get("fg"), 0)).intValue() + 1));
                    } else if ("bg".equals(hashMapArr2[length].get("pcy"))) {
                        hashMap.put("bg", Integer.valueOf(((Integer) ResHelper.forceCast(hashMap.get("bg"), 0)).intValue() + 1));
                    } else {
                        hashMap.put("empty", Integer.valueOf(((Integer) ResHelper.forceCast(hashMap.get("empty"), 0)).intValue() + 1));
                    }
                    hashMap.put("pkg", hashMapArr2[length].get("pkg"));
                    hashMap.put(IMAPStore.ID_NAME, hashMapArr2[length].get(IMAPStore.ID_NAME));
                    hashMap.put(IMAPStore.ID_VERSION, hashMapArr2[length].get(IMAPStore.ID_VERSION));
                }
                length--;
            }
        }
        return arrayList2;
    }

    private HashMap<String, String> a(String str, String[] strArr) {
        CharSequence charSequence;
        try {
            if (this.b == null) {
                this.b = MobSDK.getContext().getPackageManager();
            }
            PackageInfo packageInfo = this.b.getPackageInfo(str, 0);
            boolean z = (packageInfo.applicationInfo.flags & 1) == 1;
            boolean z2 = (packageInfo.applicationInfo.flags & 128) == 1;
            if (!z && !z2) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("pkg", str);
                try {
                    charSequence = packageInfo.applicationInfo.loadLabel(this.b);
                } catch (Throwable unused) {
                    charSequence = null;
                }
                hashMap.put(IMAPStore.ID_NAME, charSequence == null ? packageInfo.packageName : charSequence.toString());
                hashMap.put(IMAPStore.ID_VERSION, packageInfo.versionName);
                hashMap.put("pcy", strArr[strArr.length - 3]);
                return hashMap;
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return null;
    }

    private HashMap<String, Integer> a(ArrayList<ArrayList<HashMap<String, String>>> arrayList) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        Iterator<ArrayList<HashMap<String, String>>> it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Iterator<HashMap<String, String>> it2 = it.next().iterator();
            while (it2.hasNext()) {
                HashMap<String, String> next = it2.next();
                String str = next.get("pkg") + ":" + next.get(IMAPStore.ID_VERSION);
                if (!hashMap.containsKey(str)) {
                    hashMap.put(str, Integer.valueOf(i));
                    i++;
                }
            }
        }
        return hashMap;
    }

    private void a(String str, ArrayList<ArrayList<HashMap<String, String>>> arrayList, ArrayList<long[]> arrayList2) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            try {
                HashMap<String, String[]> l = l();
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(str), "utf-8"));
                try {
                    try {
                        String readLine = bufferedReader.readLine();
                        for (int i = 0; i < 7; i++) {
                            readLine = bufferedReader.readLine();
                        }
                        ArrayList<HashMap<String, String>> arrayList3 = new ArrayList<>();
                        while (readLine != null) {
                            if ("======================".equals(readLine)) {
                                try {
                                    String[] split = bufferedReader.readLine().split("_");
                                    long[] jArr = {Long.parseLong(split[0]), Long.parseLong(split[1])};
                                    arrayList.add(arrayList3);
                                    arrayList2.add(jArr);
                                } catch (Throwable unused) {
                                }
                                arrayList3 = new ArrayList<>();
                                for (int i2 = 0; i2 < 7; i2++) {
                                    bufferedReader.readLine();
                                }
                            } else if (readLine.length() > 0) {
                                a(readLine, l, arrayList3);
                            }
                            readLine = bufferedReader.readLine();
                        }
                        a(bufferedReader);
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader2 = bufferedReader;
                        MobLog.getInstance().d(th);
                        a(bufferedReader2);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    a(bufferedReader);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedReader = bufferedReader2;
        }
    }

    private void a(String str, HashMap<String, String[]> hashMap, ArrayList<HashMap<String, String>> arrayList) {
        String[] split = str.replaceAll(" +", SQLBuilder.BLANK).split(SQLBuilder.BLANK);
        if (split == null || split.length < 10) {
            return;
        }
        String str2 = split[split.length - 1];
        if (!split[split.length - 2].matches(a) || Adjacent.TOP.equals(str2)) {
            return;
        }
        if (hashMap == null || hashMap.isEmpty()) {
            HashMap<String, String> a2 = a(str2, split);
            if (a2 != null) {
                arrayList.add(a2);
                return;
            }
            return;
        }
        String[] strArr = hashMap.get(str2);
        if (strArr != null) {
            HashMap<String, String> hashMap2 = new HashMap<>();
            hashMap2.put("pkg", str2);
            hashMap2.put(IMAPStore.ID_NAME, strArr[0]);
            hashMap2.put(IMAPStore.ID_VERSION, strArr[1]);
            hashMap2.put("pcy", split[split.length - 3]);
            arrayList.add(hashMap2);
        }
    }

    private void a(ArrayList<HashMap<String, Object>> arrayList, ArrayList<long[]> arrayList2) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "ARTSMT");
        hashMap.put("list", arrayList);
        hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
        hashMap.put("recordat", Long.valueOf(arrayList2.get(0)[0]));
        long j = 0;
        for (int i = 1; i < arrayList2.size(); i++) {
            j += arrayList2.get(i)[1];
        }
        hashMap.put("sdk_runtime_len", Long.valueOf(j));
        hashMap.put("top_count", Integer.valueOf(arrayList2.size()));
        com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap);
    }

    private boolean a(String str) {
        ArrayList<ArrayList<HashMap<String, String>>> arrayList = new ArrayList<>();
        ArrayList<long[]> arrayList2 = new ArrayList<>();
        a(str, arrayList, arrayList2);
        try {
            a(a(a(a(arrayList), arrayList), arrayList2), arrayList2);
        } catch (Throwable unused) {
        }
        return b(str);
    }

    private HashMap<String, String>[][] a(HashMap<String, Integer> hashMap, ArrayList<ArrayList<HashMap<String, String>>> arrayList) {
        HashMap<String, String>[][] hashMapArr = (HashMap[][]) Array.newInstance((Class<?>) HashMap.class, hashMap.size(), arrayList.size());
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ArrayList<HashMap<String, String>> arrayList2 = arrayList.get(i);
            int size2 = arrayList2.size();
            for (int i2 = 0; i2 < size2; i2++) {
                HashMap<String, String> hashMap2 = arrayList2.get(i2);
                hashMapArr[hashMap.get(hashMap2.get("pkg") + ":" + hashMap2.get(IMAPStore.ID_VERSION)).intValue()][i] = hashMap2;
            }
        }
        return hashMapArr;
    }

    private boolean b(String str) {
        try {
            File file = new File(str);
            file.delete();
            file.createNewFile();
            return true;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }

    private void h() throws Throwable {
        File dataCacheFile;
        if ((this.d == null || TextUtils.isEmpty(this.e)) && (dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.plst")) != null) {
            if (!dataCacheFile.exists()) {
                dataCacheFile.createNewFile();
            }
            this.e = dataCacheFile.getAbsolutePath();
            this.f = j();
            this.g = true;
            this.d = null;
            this.c = (Process) ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(com.mob.commons.k.a(146)), com.mob.commons.k.a(147), new Object[0]), com.mob.commons.k.a(Opcodes.LCMP), com.mob.commons.k.a(0));
            this.d = (OutputStream) ReflectHelper.invokeInstanceMethod(this.c, com.mob.commons.k.a(150), new Object[0]);
        }
    }

    private void i() {
        String str;
        try {
            h();
            if (com.mob.commons.b.c()) {
                long a2 = com.mob.commons.b.a();
                this.d.write((com.mob.commons.k.a(1) + SQLBuilder.BLANK + this.e + SQLBuilder.BLANK + com.mob.commons.k.a(2) + " \"======================\" >> " + this.e + "\n").getBytes("ascii"));
                if (this.g) {
                    str = com.mob.commons.k.a(3) + " \"" + a2 + "_0\" >> " + this.e + "\n";
                    this.g = false;
                } else {
                    str = com.mob.commons.k.a(3) + " \"" + a2 + "_" + com.mob.commons.b.d() + "\" >> " + this.e + "\n";
                }
                this.d.write(str.getBytes("ascii"));
                this.d.flush();
                if (a2 >= this.f) {
                    this.d.write((com.mob.commons.k.a(4) + "\n").getBytes("ascii"));
                    this.d.flush();
                    this.d.close();
                    this.c.waitFor();
                    this.c.destroy();
                    if (a(this.e)) {
                        long k = k();
                        if (k > 0) {
                            this.f = k;
                        }
                        this.g = true;
                    }
                    try {
                        this.c = (Process) ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(com.mob.commons.k.a(146)), com.mob.commons.k.a(147), new Object[0]), com.mob.commons.k.a(Opcodes.LCMP), com.mob.commons.k.a(0));
                        this.d = (OutputStream) ReflectHelper.invokeInstanceMethod(this.c, com.mob.commons.k.a(150), new Object[0]);
                    } catch (Throwable th) {
                        MobLog.getInstance().w(th);
                    }
                }
            }
        } catch (Throwable th2) {
            MobLog.getInstance().d(th2);
        }
    }

    private long j() {
        long a2 = com.mob.commons.b.a();
        try {
            File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.nulplt");
            if (dataCacheFile.exists()) {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dataCacheFile));
                long readLong = dataInputStream.readLong();
                dataInputStream.close();
                a2 = readLong;
            } else {
                dataCacheFile.createNewFile();
                k();
                a2 += com.mob.commons.b.y();
            }
            return a2;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return a2 + com.mob.commons.b.y();
        }
    }

    private long k() {
        DataOutputStream dataOutputStream;
        long a2 = com.mob.commons.b.a() + com.mob.commons.b.y();
        Closeable closeable = null;
        try {
            try {
                dataOutputStream = new DataOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.nulplt")));
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            dataOutputStream.writeLong(a2);
            a(dataOutputStream);
            return a2;
        } catch (Throwable th3) {
            th = th3;
            closeable = dataOutputStream;
            MobLog.getInstance().d(th);
            a(closeable);
            return 0L;
        }
    }

    private HashMap<String, String[]> l() {
        ArrayList<HashMap<String, String>> m = m();
        HashMap<String, String[]> hashMap = new HashMap<>();
        Iterator<HashMap<String, String>> it = m.iterator();
        while (it.hasNext()) {
            HashMap<String, String> next = it.next();
            hashMap.put(next.get("pkg"), new String[]{next.get(IMAPStore.ID_NAME), next.get(IMAPStore.ID_VERSION)});
        }
        return hashMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>> m() {
        /*
            r7 = this;
            r0 = 0
            android.content.Context r1 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> Lf
            com.mob.tools.utils.DeviceHelper r1 = com.mob.tools.utils.DeviceHelper.getInstance(r1)     // Catch: java.lang.Throwable -> Lf
            r2 = 0
            java.util.ArrayList r1 = r1.getIA(r2)     // Catch: java.lang.Throwable -> Lf
            goto L18
        Lf:
            r1 = move-exception
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.w(r1)
            r1 = r0
        L18:
            if (r1 != 0) goto L80
            android.content.Context r2 = com.mob.MobSDK.getContext()
            java.lang.String r3 = "comm/dbs/.al"
            java.io.File r2 = com.mob.tools.utils.ResHelper.getDataCacheFile(r2, r3)
            if (r2 == 0) goto L80
            boolean r3 = r2.exists()
            if (r3 == 0) goto L80
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            r3.<init>()     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            r4.<init>(r2)     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            java.util.zip.GZIPInputStream r2 = new java.util.zip.GZIPInputStream     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            r2.<init>(r4)     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            java.lang.String r5 = "utf-8"
            r4.<init>(r2, r5)     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            r2.<init>(r4)     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L70
            java.lang.String r0 = r2.readLine()     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L69
        L4b:
            if (r0 == 0) goto L60
            com.mob.tools.utils.Hashon r4 = new com.mob.tools.utils.Hashon     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L69
            r4.<init>()     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L69
            java.util.HashMap r0 = r4.fromJson(r0)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L69
            if (r0 == 0) goto L5b
            r3.add(r0)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L69
        L5b:
            java.lang.String r0 = r2.readLine()     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L69
            goto L4b
        L60:
            r7.a(r2)
            r1 = r3
            goto L80
        L65:
            r0 = move-exception
            r1 = r0
            r0 = r2
            goto L7c
        L69:
            r0 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
            goto L71
        L6e:
            r1 = move-exception
            goto L7c
        L70:
            r2 = move-exception
        L71:
            com.mob.tools.log.NLog r3 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L6e
            r3.d(r2)     // Catch: java.lang.Throwable -> L6e
            r7.a(r0)
            goto L80
        L7c:
            r7.a(r0)
            throw r1
        L80:
            if (r1 != 0) goto L87
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
        L87:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.r.m():java.util.ArrayList");
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        if (Build.VERSION.SDK_INT > 24) {
            return null;
        }
        return com.mob.commons.e.a("comm/locks/.rc_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        i();
        a(0, com.mob.commons.b.d() * 1000);
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.c();
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(0);
    }
}
