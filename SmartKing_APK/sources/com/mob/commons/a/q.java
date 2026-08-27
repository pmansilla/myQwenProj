package com.mob.commons.a;

import android.os.Message;
import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
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

/* compiled from: PkgSClt.java */
/* loaded from: classes.dex */
public class q extends d {
    private Hashon a = new Hashon();
    private DeviceHelper b;

    q() {
    }

    private void a(long j) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.snulal")));
            dataOutputStream.writeLong(j);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private void a(String str, ArrayList<HashMap<String, String>> arrayList) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", str);
        hashMap.put("list", arrayList);
        long a = com.mob.commons.b.a();
        hashMap.put("datetime", Long.valueOf(a));
        com.mob.commons.c.a().a(a, hashMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.mob.commons.a.q] */
    private void a(ArrayList<HashMap<String, String>> arrayList) {
        OutputStreamWriter outputStreamWriter;
        OutputStreamWriter outputStreamWriter2 = null;
        ?? r1 = 0;
        OutputStreamWriter outputStreamWriter3 = null;
        try {
            try {
                outputStreamWriter = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.sal"))), "utf-8");
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            Iterator<HashMap<String, String>> it = arrayList.iterator();
            while (it.hasNext()) {
                r1 = 10;
                outputStreamWriter.append((CharSequence) this.a.fromHashMap(it.next())).append('\n');
            }
            a(outputStreamWriter);
            outputStreamWriter2 = r1;
        } catch (Throwable th3) {
            th = th3;
            outputStreamWriter3 = outputStreamWriter;
            MobLog.getInstance().d(th);
            a(outputStreamWriter3);
            outputStreamWriter2 = outputStreamWriter3;
        }
    }

    private void h() {
        ArrayList<HashMap<String, String>> arrayList;
        boolean z;
        ArrayList<HashMap<String, String>> i = i();
        try {
            if (this.b == null) {
                this.b = DeviceHelper.getInstance(MobSDK.getContext());
            }
            arrayList = this.b.getSA();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            arrayList = new ArrayList<>();
        }
        if (arrayList.isEmpty()) {
            return;
        }
        boolean z2 = i == null || i.isEmpty();
        if (!z2) {
            long j = j();
            z2 = j == 0 || com.mob.commons.b.a() >= j;
            if (!z2) {
                z2 = arrayList.size() != i.size();
                if (!z2) {
                    Iterator<HashMap<String, String>> it = arrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        String str = it.next().get("pkg");
                        if (!TextUtils.isEmpty(str)) {
                            Iterator<HashMap<String, String>> it2 = i.iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    z = false;
                                    break;
                                } else if (str.equals(it2.next().get("pkg"))) {
                                    z = true;
                                    break;
                                }
                            }
                            if (!z) {
                                z2 = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (z2) {
            a("SALMT", arrayList);
            a(arrayList);
            a(com.mob.commons.b.a() + (com.mob.commons.b.S() * 1000));
        }
    }

    private ArrayList<HashMap<String, String>> i() {
        ArrayList<HashMap<String, String>> arrayList;
        BufferedReader bufferedReader;
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.sal");
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
                    HashMap<String, String> fromJson = this.a.fromJson(readLine);
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
        File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.snulal");
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

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.pkgs_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (message.what != 1) {
            return;
        }
        long R = com.mob.commons.b.R();
        if (R <= 0) {
            return;
        }
        h();
        a(1, R * 1000);
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.R() > 0;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(1);
    }
}
