package com.mob.commons.a;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Message;
import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import com.sun.mail.imap.IMAPStore;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* compiled from: PaClt.java */
/* loaded from: classes.dex */
public class o extends d {
    private PackageManager a;
    private String b = null;
    private long c = 0;
    private String d = null;
    private boolean e = false;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0, types: [com.mob.commons.a.o] */
    /* JADX WARN: Type inference failed for: r5v13, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v0 */
    /* JADX WARN: Type inference failed for: r6v1, types: [java.lang.CharSequence, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v5 */
    private ArrayList<HashMap<String, Object>> a(Context context) throws Throwable {
        ArrayList l = !TextUtils.isEmpty(this.b) ? l() : null;
        if (l == null) {
            l = new ArrayList();
        }
        ArrayList<HashMap<String, String>> ia = DeviceHelper.getInstance(context).getIA(false);
        if (ia != null) {
            for (int i = 0; i < ia.size(); i++) {
                HashMap<String, String> hashMap = ia.get(i);
                ?? r6 = hashMap != null ? hashMap.get("pkg") : 0;
                if (!TextUtils.isEmpty(r6) && a(r6)) {
                    HashMap b = b(l, r6);
                    b.put("pkg", r6);
                    b.put(IMAPStore.ID_NAME, hashMap.get(IMAPStore.ID_NAME));
                    b.put(IMAPStore.ID_VERSION, hashMap.get(IMAPStore.ID_VERSION));
                    b.put("runtimes", Integer.valueOf((b.get("runtimes") == null ? 0 : ResHelper.parseInt(String.valueOf(b.get("runtimes")))) + com.mob.commons.b.f()));
                    ?? b2 = b(context);
                    if (!r6.equals(b2)) {
                        b.put("bg", Integer.valueOf((b.get("bg") == null ? 0 : ResHelper.parseInt(String.valueOf(b.get("bg")))) + com.mob.commons.b.f()));
                    }
                    if (this.d == null && b2 != 0 && b2.equals(r6)) {
                        b.put("fg", Integer.valueOf((b.get("fg") == null ? 0 : ResHelper.parseInt(String.valueOf(b.get("fg")))) + 1));
                        this.d = b2;
                    }
                    if (b2 != 0 && !b2.equals(this.d)) {
                        this.e = true;
                        this.d = b2;
                    }
                    if (this.e && b2 != 0 && b2.equals(r6)) {
                        b.put("fg", Integer.valueOf((b.get("fg") == null ? 0 : ResHelper.parseInt(String.valueOf(b.get("fg")))) + 1));
                        this.e = false;
                    }
                    if (!a(l, r6)) {
                        l.add(b);
                    }
                }
            }
        }
        return l;
    }

    private boolean a(String str) {
        try {
            if (this.a == null) {
                this.a = MobSDK.getContext().getPackageManager();
            }
            PackageInfo packageInfo = this.a.getPackageInfo(str, 0);
            return ((packageInfo.applicationInfo.flags & 1) == 0 && (packageInfo.applicationInfo.flags & 128) == 0) && ((packageInfo.applicationInfo.flags & 2097152) == 0);
        } catch (Throwable unused) {
            return false;
        }
    }

    private boolean a(ArrayList<HashMap<String, Object>> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return false;
        }
        try {
            b(arrayList);
        } catch (Throwable unused) {
        }
        k();
        return m();
    }

    private boolean a(ArrayList<HashMap<String, Object>> arrayList, String str) {
        Iterator<HashMap<String, Object>> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().get("pkg"))) {
                return true;
            }
        }
        return false;
    }

    private String b(Context context) {
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (runningTasks == null || runningTasks.get(0) == null) {
            return null;
        }
        return runningTasks.get(0).topActivity.getPackageName();
    }

    private HashMap<String, Object> b(ArrayList<HashMap<String, Object>> arrayList, String str) {
        Iterator<HashMap<String, Object>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            if (str.equals(next.get("pkg"))) {
                return next;
            }
        }
        return new HashMap<>();
    }

    private void b(ArrayList<HashMap<String, Object>> arrayList) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "PRTMT");
        hashMap.put("list", arrayList);
        hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
        com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap);
    }

    private void c(ArrayList<HashMap<String, Object>> arrayList) {
        ObjectOutputStream objectOutputStream;
        Closeable closeable = null;
        try {
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.b));
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            objectOutputStream.writeObject(arrayList);
            a(objectOutputStream);
        } catch (Throwable th3) {
            th = th3;
            closeable = objectOutputStream;
            MobLog.getInstance().w(th);
            a(closeable);
        }
    }

    private void h() throws Throwable {
        File dataCacheFile;
        if (TextUtils.isEmpty(this.b) && (dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.prcd")) != null) {
            if (!dataCacheFile.getParentFile().exists()) {
                dataCacheFile.getParentFile().mkdirs();
            }
            if (!dataCacheFile.exists()) {
                dataCacheFile.createNewFile();
            }
            this.b = dataCacheFile.getAbsolutePath();
            this.c = j();
        }
    }

    private void i() {
        MobLog.getInstance().i("paclt", new Object[0]);
        try {
            h();
            if (com.mob.commons.b.e()) {
                ArrayList<HashMap<String, Object>> a = a(MobSDK.getContext());
                if (!TextUtils.isEmpty(this.b)) {
                    c(a);
                }
                if (System.currentTimeMillis() < this.c + com.mob.commons.b.g() || !a(a)) {
                    return;
                }
                this.c = j();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private long j() {
        try {
            File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.nuprcd");
            if (dataCacheFile.exists()) {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(dataCacheFile));
                long readLong = dataInputStream.readLong();
                dataInputStream.close();
                return readLong;
            }
            if (!dataCacheFile.getParentFile().exists()) {
                dataCacheFile.getParentFile().mkdirs();
            }
            dataCacheFile.createNewFile();
            return -1L;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return -1L;
        }
    }

    private void k() {
        DataOutputStream dataOutputStream;
        long currentTimeMillis = System.currentTimeMillis();
        Closeable closeable = null;
        try {
            try {
                dataOutputStream = new DataOutputStream(new FileOutputStream(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.nuprcd")));
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            dataOutputStream.writeLong(currentTimeMillis);
            a(dataOutputStream);
        } catch (Throwable th3) {
            th = th3;
            closeable = dataOutputStream;
            MobLog.getInstance().d(th);
            a(closeable);
        }
    }

    private ArrayList<HashMap<String, Object>> l() {
        ObjectInputStream objectInputStream;
        Closeable closeable = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(this.b));
        } catch (Throwable th) {
            th = th;
        }
        try {
            ArrayList<HashMap<String, Object>> arrayList = (ArrayList) objectInputStream.readObject();
            a(objectInputStream);
            return arrayList;
        } catch (Throwable unused) {
            a(objectInputStream);
            return null;
        }
    }

    private boolean m() {
        try {
            File file = new File(this.b);
            file.delete();
            file.createNewFile();
            return true;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.pa_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        i();
        a(0, com.mob.commons.b.f() * 1000);
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.e();
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(0);
    }
}
