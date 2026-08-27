package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.HashMap;

/* compiled from: ImageCache.java */
/* loaded from: classes.dex */
public class fv {
    private static final Bitmap.CompressFormat a = Bitmap.CompressFormat.PNG;
    private in b;
    private fd<String, Bitmap> c;
    private a d;
    private final Object e = new Object();
    private boolean f = true;
    private HashMap<String, WeakReference<Bitmap>> g;

    /* compiled from: ImageCache.java */
    /* loaded from: classes.dex */
    public static class a {
        public File c;
        public String j;
        public int a = 5242880;
        public long b = 10485760;
        public Bitmap.CompressFormat d = fv.a;
        public int e = 100;
        public boolean f = true;
        public boolean g = true;
        public boolean h = false;
        public boolean i = true;

        public a(Context context, String str) {
            this.j = null;
            this.j = str;
            this.c = fv.a(context, str, null);
        }

        public a(Context context, String str, String str2) {
            this.j = null;
            this.j = str;
            this.c = fv.a(context, str, str2);
        }

        public void a(int i) {
            this.a = i;
        }

        public void a(long j) {
            if (j <= 0) {
                this.g = false;
            }
            this.b = j;
        }

        public void a(String str) {
            this.c = new File(str);
        }

        public void a(boolean z) {
            this.f = z;
        }

        public void b(String str) {
            this.c = fv.a(ai.a, this.j, str);
        }

        public void b(boolean z) {
            this.g = z;
        }
    }

    private fv(a aVar) {
        b(aVar);
    }

    public static int a(Bitmap bitmap) {
        return fr.d() ? bitmap.getByteCount() : bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static long a(File file) {
        if (fr.b()) {
            return file.getUsableSpace();
        }
        StatFs statFs = new StatFs(file.getPath());
        return statFs.getBlockSize() * statFs.getAvailableBlocks();
    }

    public static fv a(a aVar) {
        return new fv(aVar);
    }

    public static File a(Context context) {
        try {
            if (fr.a()) {
                return context.getExternalCacheDir();
            }
            return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static File a(Context context, String str, String str2) {
        File a2 = a(context);
        String path = (("mounted".equals(Environment.getExternalStorageState()) || !d()) && a2 != null) ? a2.getPath() : context.getCacheDir().getPath();
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append(File.separator);
        sb.append(str);
        if (!TextUtils.isEmpty(str2)) {
            sb.append(File.separator);
            sb.append(str2);
        }
        return new File(sb.toString());
    }

    private static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    private void b(a aVar) {
        this.d = aVar;
        if (this.d.f) {
            if (fw.a()) {
                this.g = new HashMap<>(64);
            }
            this.c = new fd<String, Bitmap>(this.d.a) { // from class: com.amap.api.mapcore.util.fv.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.amap.api.mapcore.util.fd
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public int b(String str, Bitmap bitmap) {
                    int a2 = fv.a(bitmap);
                    if (a2 == 0) {
                        return 1;
                    }
                    return a2;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.amap.api.mapcore.util.fd
                public void a(boolean z, String str, Bitmap bitmap, Bitmap bitmap2) {
                    if (!fr.c() || fv.this.g == null || bitmap == null || bitmap.isRecycled()) {
                        return;
                    }
                    fv.this.g.put(str, new WeakReference(bitmap));
                }
            };
        }
        if (aVar.h) {
            a();
        }
    }

    private void b(File file) throws IOException {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            throw new IOException("not a readable directory: " + file);
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                b(file2);
            }
            if (!file2.delete()) {
                throw new IOException("failed to delete file: " + file2);
            }
        }
    }

    public static String c(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("utf-8"));
            return a(messageDigest.digest());
        } catch (Throwable unused) {
            return String.valueOf(str.hashCode());
        }
    }

    public static boolean d() {
        if (fr.b()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public Bitmap a(String str) {
        Bitmap bitmap;
        WeakReference<Bitmap> weakReference;
        if (!fr.c() || this.g == null || (weakReference = this.g.get(str)) == null) {
            bitmap = null;
        } else {
            bitmap = weakReference.get();
            if (bitmap == null || bitmap.isRecycled()) {
                bitmap = null;
            }
            this.g.remove(str);
        }
        if (bitmap == null && this.c != null) {
            bitmap = this.c.a((fd<String, Bitmap>) str);
        }
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        return bitmap;
    }

    public void a() {
        synchronized (this.e) {
            if (this.b == null || this.b.d()) {
                File file = this.d.c;
                if (this.d.g && file != null) {
                    try {
                        if (!file.exists()) {
                            file.mkdirs();
                        } else if (this.d.i) {
                            b(file);
                            file.mkdir();
                        }
                    } catch (Throwable unused) {
                    }
                    if (a(file) > this.d.b) {
                        try {
                            this.b = in.a(file, 1, 1, this.d.b);
                        } catch (Throwable unused2) {
                            this.d.c = null;
                        }
                    }
                }
            }
            this.f = false;
            this.e.notifyAll();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x0054, code lost:
    
        if (r1 != null) goto L48;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(java.lang.String r5, android.graphics.Bitmap r6) {
        /*
            r4 = this;
            if (r5 == 0) goto L69
            if (r6 == 0) goto L69
            boolean r0 = r6.isRecycled()
            if (r0 == 0) goto Lb
            goto L69
        Lb:
            com.amap.api.mapcore.util.fd<java.lang.String, android.graphics.Bitmap> r0 = r4.c
            if (r0 == 0) goto L14
            com.amap.api.mapcore.util.fd<java.lang.String, android.graphics.Bitmap> r0 = r4.c
            r0.a(r5, r6)
        L14:
            java.lang.Object r0 = r4.e
            monitor-enter(r0)
            com.amap.api.mapcore.util.in r1 = r4.b     // Catch: java.lang.Throwable -> L66
            if (r1 == 0) goto L64
            java.lang.String r5 = c(r5)     // Catch: java.lang.Throwable -> L66
            r1 = 0
            com.amap.api.mapcore.util.in r2 = r4.b     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L61
            com.amap.api.mapcore.util.in$b r2 = r2.a(r5)     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L61
            r3 = 0
            if (r2 != 0) goto L4d
            com.amap.api.mapcore.util.in r2 = r4.b     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L61
            com.amap.api.mapcore.util.in$a r5 = r2.b(r5)     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L61
            if (r5 == 0) goto L54
            java.io.OutputStream r2 = r5.a(r3)     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L61
            com.amap.api.mapcore.util.fv$a r1 = r4.d     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4b
            android.graphics.Bitmap$CompressFormat r1 = r1.d     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4b
            com.amap.api.mapcore.util.fv$a r3 = r4.d     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4b
            int r3 = r3.e     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4b
            r6.compress(r1, r3, r2)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4b
            r5.a()     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4b
            r2.close()     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L4b
            r1 = r2
            goto L54
        L48:
            r5 = move-exception
            r1 = r2
            goto L5b
        L4b:
            r1 = r2
            goto L61
        L4d:
            java.io.InputStream r5 = r2.a(r3)     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L61
            r5.close()     // Catch: java.lang.Throwable -> L5a java.lang.Throwable -> L61
        L54:
            if (r1 == 0) goto L64
        L56:
            r1.close()     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            goto L64
        L5a:
            r5 = move-exception
        L5b:
            if (r1 == 0) goto L60
            r1.close()     // Catch: java.lang.Throwable -> L60 java.lang.Throwable -> L66
        L60:
            throw r5     // Catch: java.lang.Throwable -> L66
        L61:
            if (r1 == 0) goto L64
            goto L56
        L64:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L66
            return
        L66:
            r5 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L66
            throw r5
        L69:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.fv.a(java.lang.String, android.graphics.Bitmap):void");
    }

    public void a(boolean z) {
        if (fr.c() && this.g != null) {
            this.g.clear();
        }
        if (this.c != null) {
            this.c.a();
        }
        synchronized (this.e) {
            if (this.b != null) {
                try {
                    if (!this.b.d()) {
                        if (z) {
                            this.b.f();
                        } else {
                            this.b.close();
                        }
                        this.b = null;
                    }
                } catch (Throwable unused) {
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0038, code lost:
    
        if (r5 != null) goto L40;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.graphics.Bitmap b(java.lang.String r5) {
        /*
            r4 = this;
            java.lang.String r5 = c(r5)
            java.lang.Object r0 = r4.e
            monitor-enter(r0)
        L7:
            boolean r1 = r4.f     // Catch: java.lang.Throwable -> L4c
            if (r1 == 0) goto L11
            java.lang.Object r1 = r4.e     // Catch: java.lang.Throwable -> L7 java.lang.Throwable -> L4c
            r1.wait()     // Catch: java.lang.Throwable -> L7 java.lang.Throwable -> L4c
            goto L7
        L11:
            com.amap.api.mapcore.util.in r1 = r4.b     // Catch: java.lang.Throwable -> L4c
            r2 = 0
            if (r1 == 0) goto L4a
            com.amap.api.mapcore.util.in r1 = r4.b     // Catch: java.lang.Throwable -> L3e java.lang.Throwable -> L46
            com.amap.api.mapcore.util.in$b r5 = r1.a(r5)     // Catch: java.lang.Throwable -> L3e java.lang.Throwable -> L46
            if (r5 == 0) goto L37
            r1 = 0
            java.io.InputStream r5 = r5.a(r1)     // Catch: java.lang.Throwable -> L3e java.lang.Throwable -> L46
            if (r5 == 0) goto L38
            r1 = r5
            java.io.FileInputStream r1 = (java.io.FileInputStream) r1     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L47
            java.io.FileDescriptor r1 = r1.getFD()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L47
            r3 = 2147483647(0x7fffffff, float:NaN)
            android.graphics.Bitmap r1 = com.amap.api.mapcore.util.ft.a(r1, r3, r3, r4)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L47
            r2 = r1
            goto L38
        L35:
            r1 = move-exception
            goto L40
        L37:
            r5 = r2
        L38:
            if (r5 == 0) goto L4a
        L3a:
            r5.close()     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            goto L4a
        L3e:
            r1 = move-exception
            r5 = r2
        L40:
            if (r5 == 0) goto L45
            r5.close()     // Catch: java.lang.Throwable -> L45 java.lang.Throwable -> L4c
        L45:
            throw r1     // Catch: java.lang.Throwable -> L4c
        L46:
            r5 = r2
        L47:
            if (r5 == 0) goto L4a
            goto L3a
        L4a:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L4c
            return r2
        L4c:
            r5 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L4c
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.fv.b(java.lang.String):android.graphics.Bitmap");
    }

    public void b() {
        if (fr.c() && this.g != null) {
            this.g.clear();
        }
        if (this.c != null) {
            this.c.a();
        }
        synchronized (this.e) {
            this.f = true;
            if (this.b != null && !this.b.d()) {
                try {
                    this.b.close();
                    b(a(ai.a, this.d.j, null));
                } catch (Throwable unused) {
                }
                this.b = null;
                a();
            }
        }
    }

    public void c() {
        synchronized (this.e) {
            if (this.b != null) {
                try {
                    this.b.e();
                } catch (Throwable unused) {
                }
            }
        }
    }
}
