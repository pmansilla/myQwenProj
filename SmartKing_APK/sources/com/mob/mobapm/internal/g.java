package com.mob.mobapm.internal;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

/* loaded from: classes.dex */
public class g {
    private static g b;
    private String a;

    private g() {
    }

    private HashMap<String, Object> a(File file) {
        Throwable th;
        ObjectInputStream objectInputStream;
        try {
            if (file.exists() && file.isFile()) {
                try {
                    objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    try {
                        HashMap<String, Object> hashMap = (HashMap) objectInputStream.readObject();
                        try {
                            objectInputStream.close();
                        } catch (Throwable unused) {
                        }
                        return hashMap;
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            MobLog.getInstance().w(th);
                            if (objectInputStream != null) {
                                objectInputStream.close();
                            }
                            return null;
                        } catch (Throwable th3) {
                            if (objectInputStream != null) {
                                try {
                                    objectInputStream.close();
                                } catch (Throwable unused2) {
                                }
                            }
                            throw th3;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    objectInputStream = null;
                }
            }
        } catch (Throwable unused3) {
        }
        return null;
    }

    private void a(String str) {
        com.mob.mobapm.b.a.a(str);
    }

    public static g b() {
        if (b == null) {
            synchronized (g.class) {
                if (b == null) {
                    b = new g();
                }
            }
        }
        return b;
    }

    private String c() {
        HashMap hashMap;
        try {
            HashMap<String, Object> d = d();
            if (d != null && !d.isEmpty() && (hashMap = (HashMap) d.get("deviceInfo")) != null && !hashMap.isEmpty()) {
                return (String) hashMap.get("oaid");
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }

    private HashMap<String, Object> d() {
        try {
            return a(ResHelper.getDataCacheFile(MobSDK.getContext(), "comm/dbs/.duid"));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private String e() {
        return com.mob.mobapm.b.a.i();
    }

    public String a() {
        if (TextUtils.isEmpty(this.a)) {
            String e = e();
            this.a = e;
            if (TextUtils.isEmpty(e)) {
                String c = c();
                this.a = c;
                if (!TextUtils.isEmpty(c)) {
                    a(this.a);
                }
            }
        }
        return this.a;
    }
}
