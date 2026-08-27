package com.mob.commons.b;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* compiled from: Caches.java */
/* loaded from: classes.dex */
public class b {
    private static b a;
    private Context b;
    private HashMap<String, Object> c = new HashMap<>();

    private b(Context context) {
        this.b = context;
        try {
            HashMap hashMap = (HashMap) a(a(context, ".msas"));
            if (hashMap == null || hashMap.size() <= 0) {
                return;
            }
            this.c.putAll(hashMap);
        } catch (Throwable unused) {
        }
    }

    public static synchronized b a(Context context) {
        b bVar;
        synchronized (b.class) {
            if (a == null) {
                a = new b(context);
            }
            bVar = a;
        }
        return bVar;
    }

    private static File a(Context context, String str) {
        try {
            String b = b(context);
            if (b == null) {
                return null;
            }
            File file = new File(b, str);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            return file;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Object a(File file) {
        try {
            if (!file.exists()) {
                return null;
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObject = objectInputStream.readObject();
            objectInputStream.close();
            return readObject;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static boolean a(File file, Object obj) {
        try {
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static String b(Context context) {
        String str = context.getFilesDir().getAbsolutePath() + "/Mob/";
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str;
    }

    public synchronized HashMap<String, Object> a() {
        return this.c;
    }

    public synchronized boolean a(String str, String str2, String str3, String str4, boolean z) {
        HashMap hashMap;
        try {
            hashMap = new HashMap();
            if (!TextUtils.isEmpty(str)) {
                hashMap.put(com.mob.commons.k.a(69), str);
            }
            if (!TextUtils.isEmpty(str2)) {
                hashMap.put(com.mob.commons.k.a(75), str2);
            }
            if (!TextUtils.isEmpty(str3)) {
                hashMap.put(com.mob.commons.k.a(70), str3);
            }
            if (!TextUtils.isEmpty(str4)) {
                hashMap.put(com.mob.commons.k.a(71), str4);
            }
            hashMap.put(com.mob.commons.k.a(74), Boolean.valueOf(z));
        } catch (Throwable th) {
            throw th;
        }
        return a(a(this.b, ".msas"), hashMap);
    }
}
