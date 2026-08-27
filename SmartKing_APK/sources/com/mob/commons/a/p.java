package com.mob.commons.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: PkgClt.java */
/* loaded from: classes.dex */
public class p extends d {
    private static final String[] a = {"android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_REMOVED", "android.intent.action.PACKAGE_REPLACED"};
    private BroadcastReceiver b;
    private Hashon c;

    private ArrayList<HashMap<String, String>> a(ArrayList<HashMap<String, String>> arrayList, ArrayList<HashMap<String, String>> arrayList2) {
        if (arrayList == null || arrayList.isEmpty() || arrayList2 == null || arrayList2.isEmpty()) {
            return arrayList;
        }
        ArrayList<HashMap<String, String>> arrayList3 = new ArrayList<>();
        Iterator<HashMap<String, String>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, String> next = it.next();
            boolean z = false;
            Iterator<String> it2 = next.keySet().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                String next2 = it2.next();
                if (next2 != null && next2.contains("first")) {
                    z = true;
                    break;
                }
            }
            if (z) {
                arrayList3.add(next);
            } else {
                String str = next.get("pkg");
                if (!TextUtils.isEmpty(str)) {
                    Iterator<HashMap<String, String>> it3 = arrayList2.iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            break;
                        }
                        HashMap<String, String> next3 = it3.next();
                        if (str.equals(next3.get("pkg"))) {
                            Iterator<String> it4 = next3.keySet().iterator();
                            while (true) {
                                if (!it4.hasNext()) {
                                    break;
                                }
                                String next4 = it4.next();
                                if (next4 != null && next4.contains("first")) {
                                    arrayList3.add(next3);
                                    z = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!z) {
                        arrayList3.add(next);
                    }
                }
            }
        }
        return arrayList3;
    }

    private void a(long j) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.nulal")));
            dataOutputStream.writeLong(j);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private void a(long j, String str, ArrayList<HashMap<String, String>> arrayList) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", str);
        hashMap.put("list", arrayList);
        hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
        com.mob.commons.c.a().a(j, hashMap);
    }

    private void a(ArrayList<HashMap<String, String>> arrayList) {
        try {
            b(arrayList);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            try {
                b(arrayList);
            } catch (Throwable unused) {
                MobLog.getInstance().d(th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(String str) {
        for (String str2 : a) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<HashMap<String, String>> b(ArrayList<HashMap<String, String>> arrayList, ArrayList<HashMap<String, String>> arrayList2) {
        ArrayList<HashMap<String, String>> arrayList3 = new ArrayList<>();
        Iterator<HashMap<String, String>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, String> next = it.next();
            String str = next.get("pkg");
            if (!TextUtils.isEmpty(str)) {
                boolean z = false;
                Iterator<HashMap<String, String>> it2 = arrayList2.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    if (str.equals(it2.next().get("pkg"))) {
                        z = true;
                        break;
                    }
                }
                if (!z) {
                    arrayList3.add(next);
                }
            }
        }
        return arrayList3;
    }

    private void b(ArrayList<HashMap<String, String>> arrayList) throws Throwable {
        OutputStreamWriter outputStreamWriter;
        try {
            outputStreamWriter = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.al"))), "utf-8");
            try {
                Iterator<HashMap<String, String>> it = arrayList.iterator();
                while (it.hasNext()) {
                    outputStreamWriter.append((CharSequence) n().fromHashMap(it.next())).append('\n');
                }
                a(outputStreamWriter);
            } catch (Throwable th) {
                th = th;
                a(outputStreamWriter);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            outputStreamWriter = null;
        }
    }

    private void h() {
        ArrayList<HashMap<String, String>> arrayList;
        ArrayList<HashMap<String, String>> arrayList2;
        ArrayList<HashMap<String, String>> i = i();
        if (i == null || i.isEmpty()) {
            try {
                arrayList = DeviceHelper.getInstance(MobSDK.getContext()).getIA(false);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
                arrayList = new ArrayList<>();
            }
            a(com.mob.commons.b.x(), "ALSAMT", arrayList);
            a(arrayList);
            a(com.mob.commons.b.a() + (com.mob.commons.b.k() * 1000));
            return;
        }
        long a2 = com.mob.commons.b.a();
        long j = j();
        if (j != 0 && a2 < j) {
            m();
            return;
        }
        try {
            arrayList2 = DeviceHelper.getInstance(MobSDK.getContext()).getIA(false);
        } catch (Throwable th2) {
            MobLog.getInstance().w(th2);
            arrayList2 = new ArrayList<>();
        }
        if (arrayList2 == null || arrayList2.isEmpty()) {
            return;
        }
        ArrayList<HashMap<String, String>> a3 = a(arrayList2, i);
        a(com.mob.commons.b.x(), "ALSAMT", a3);
        a(a3);
        a(a2 + (com.mob.commons.b.k() * 1000));
    }

    private ArrayList<HashMap<String, String>> i() {
        ArrayList<HashMap<String, String>> arrayList;
        BufferedReader bufferedReader;
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.al");
        if (dataCacheFile != null && dataCacheFile.exists()) {
            Closeable closeable = null;
            try {
                try {
                    arrayList = new ArrayList<>();
                    bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(dataCacheFile)), "utf-8"));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                    HashMap<String, String> fromJson = n().fromJson(readLine);
                    if (fromJson != null) {
                        arrayList.add(fromJson);
                    }
                }
                a(bufferedReader);
                return arrayList;
            } catch (Throwable th3) {
                closeable = bufferedReader;
                th = th3;
                a(closeable);
                throw th;
            }
        }
        return new ArrayList<>();
    }

    private long j() {
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.nulal");
        if (!dataCacheFile.exists()) {
            return 0L;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dataCacheFile));
            long readLong = dataInputStream.readLong();
            dataInputStream.close();
            return readLong;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return 0L;
        }
    }

    private void k() {
        if (!com.mob.commons.b.h() || !com.mob.commons.b.j()) {
            a(1);
            l();
        } else if (this.b == null) {
            this.b = new BroadcastReceiver() { // from class: com.mob.commons.a.p.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (p.this.a(intent != null ? intent.getAction() : null)) {
                        p.this.a(1);
                        p.this.a(1, BootloaderScanner.TIMEOUT);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            for (int i = 0; i < a.length; i++) {
                intentFilter.addAction(a[i]);
            }
            intentFilter.addDataScheme("package");
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "registerReceiver", new Object[]{this.b, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            } catch (Throwable unused) {
            }
        }
        a(2, DateUtils.MILLIS_PER_HOUR);
    }

    private void l() {
        if (this.b != null) {
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "unregisterReceiver", new Object[]{this.b}, new Class[]{BroadcastReceiver.class});
            } catch (Throwable unused) {
            }
            this.b = null;
        }
        if (this.c != null) {
            this.c = null;
        }
    }

    private void m() {
        ArrayList<HashMap<String, String>> arrayList;
        ArrayList<HashMap<String, String>> i = i();
        try {
            arrayList = DeviceHelper.getInstance(MobSDK.getContext()).getIA(false);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            arrayList = new ArrayList<>();
        }
        if (i == null || i.isEmpty()) {
            a(com.mob.commons.b.x(), "ALSAMT", arrayList);
            a(arrayList);
            a(com.mob.commons.b.a() + (com.mob.commons.b.k() * 1000));
            return;
        }
        ArrayList<HashMap<String, String>> b = b(arrayList, i);
        if (!b.isEmpty()) {
            a(com.mob.commons.b.a(), "ALSIMT", b);
        }
        ArrayList<HashMap<String, String>> b2 = b(i, arrayList);
        if (!b2.isEmpty()) {
            a(com.mob.commons.b.a(), "ALSUMT", b2);
        }
        a(a(arrayList, i));
    }

    private Hashon n() {
        if (this.c == null) {
            this.c = new Hashon();
        }
        return this.c;
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.pkg_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        switch (message.what) {
            case 1:
                m();
                return;
            case 2:
                k();
                return;
            default:
                return;
        }
    }

    @Override // com.mob.commons.a.d
    protected void b() {
        l();
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        if (!com.mob.commons.b.i()) {
            return true;
        }
        h();
        return true;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(2);
    }
}
