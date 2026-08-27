package com.loc;

import com.amap.location.common.model.AmapLoc;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import me.panpf.sketch.util.DiskLruCache;

/* compiled from: DiskLruCache.java */
/* loaded from: classes.dex */
public final class bc implements Closeable {
    private final File c;
    private final File d;
    private final File e;
    private final File f;
    private long h;
    private Writer k;
    private int n;
    static final Pattern a = Pattern.compile("[a-z0-9_-]{1,120}");
    private static final ThreadFactory p = new ThreadFactory() { // from class: com.loc.bc.1
        private final AtomicInteger a = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "disklrucache#" + this.a.getAndIncrement());
        }
    };
    static ThreadPoolExecutor b = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), p);
    private static final OutputStream r = new OutputStream() { // from class: com.loc.bc.3
        @Override // java.io.OutputStream
        public final void write(int i) throws IOException {
        }
    };
    private long j = 0;
    private int l = 1000;
    private final LinkedHashMap<String, c> m = new LinkedHashMap<>(0, 0.75f, true);
    private long o = 0;
    private final Callable<Void> q = new Callable<Void>() { // from class: com.loc.bc.2
        /* JADX INFO: Access modifiers changed from: private */
        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void call() throws Exception {
            synchronized (bc.this) {
                if (bc.this.k == null) {
                    return null;
                }
                bc.this.l();
                if (bc.this.j()) {
                    bc.this.i();
                    bc.e(bc.this);
                }
                return null;
            }
        }
    };
    private final int g = 1;
    private final int i = 1;

    /* compiled from: DiskLruCache.java */
    /* loaded from: classes.dex */
    public final class a {
        private final c b;
        private final boolean[] c;
        private boolean d;
        private boolean e;

        /* compiled from: DiskLruCache.java */
        /* renamed from: com.loc.bc$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private class C0046a extends FilterOutputStream {
            private C0046a(OutputStream outputStream) {
                super(outputStream);
            }

            /* synthetic */ C0046a(a aVar, OutputStream outputStream, byte b) {
                this(outputStream);
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public final void close() {
                try {
                    this.out.close();
                } catch (IOException unused) {
                    a.c(a.this);
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
            public final void flush() {
                try {
                    this.out.flush();
                } catch (IOException unused) {
                    a.c(a.this);
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public final void write(int i) {
                try {
                    this.out.write(i);
                } catch (IOException unused) {
                    a.c(a.this);
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public final void write(byte[] bArr, int i, int i2) {
                try {
                    this.out.write(bArr, i, i2);
                } catch (IOException unused) {
                    a.c(a.this);
                }
            }
        }

        private a(c cVar) {
            this.b = cVar;
            this.c = cVar.d ? null : new boolean[bc.this.i];
        }

        /* synthetic */ a(bc bcVar, c cVar, byte b) {
            this(cVar);
        }

        static /* synthetic */ boolean c(a aVar) {
            aVar.d = true;
            return true;
        }

        public final OutputStream a() throws IOException {
            FileOutputStream fileOutputStream;
            C0046a c0046a;
            if (bc.this.i <= 0) {
                throw new IllegalArgumentException("Expected index 0 to be greater than 0 and less than the maximum value count of " + bc.this.i);
            }
            synchronized (bc.this) {
                if (this.b.e != this) {
                    throw new IllegalStateException();
                }
                byte b = 0;
                if (!this.b.d) {
                    this.c[0] = true;
                }
                File b2 = this.b.b(0);
                try {
                    fileOutputStream = new FileOutputStream(b2);
                } catch (FileNotFoundException unused) {
                    bc.this.c.mkdirs();
                    try {
                        fileOutputStream = new FileOutputStream(b2);
                    } catch (FileNotFoundException unused2) {
                        return bc.r;
                    }
                }
                c0046a = new C0046a(this, fileOutputStream, b);
            }
            return c0046a;
        }

        public final void b() throws IOException {
            if (this.d) {
                bc.this.a(this, false);
                bc.this.c(this.b.b);
            } else {
                bc.this.a(this, true);
            }
            this.e = true;
        }

        public final void c() throws IOException {
            bc.this.a(this, false);
        }
    }

    /* compiled from: DiskLruCache.java */
    /* loaded from: classes.dex */
    public final class b implements Closeable {
        private final String b;
        private final long c;
        private final InputStream[] d;
        private final long[] e;

        private b(String str, long j, InputStream[] inputStreamArr, long[] jArr) {
            this.b = str;
            this.c = j;
            this.d = inputStreamArr;
            this.e = jArr;
        }

        /* synthetic */ b(bc bcVar, String str, long j, InputStream[] inputStreamArr, long[] jArr, byte b) {
            this(str, j, inputStreamArr, jArr);
        }

        public final InputStream a() {
            return this.d[0];
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public final void close() {
            for (InputStream inputStream : this.d) {
                be.a(inputStream);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DiskLruCache.java */
    /* loaded from: classes.dex */
    public final class c {
        private final String b;
        private final long[] c;
        private boolean d;
        private a e;
        private long f;

        private c(String str) {
            this.b = str;
            this.c = new long[bc.this.i];
        }

        /* synthetic */ c(bc bcVar, String str, byte b) {
            this(str);
        }

        private static IOException a(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        static /* synthetic */ void a(c cVar, String[] strArr) throws IOException {
            if (strArr.length != bc.this.i) {
                throw a(strArr);
            }
            for (int i = 0; i < strArr.length; i++) {
                try {
                    cVar.c[i] = Long.parseLong(strArr[i]);
                } catch (NumberFormatException unused) {
                    throw a(strArr);
                }
            }
        }

        static /* synthetic */ boolean a(c cVar) {
            cVar.d = true;
            return true;
        }

        public final File a(int i) {
            return new File(bc.this.c, this.b + "." + i);
        }

        public final String a() throws IOException {
            StringBuilder sb = new StringBuilder();
            for (long j : this.c) {
                sb.append(' ');
                sb.append(j);
            }
            return sb.toString();
        }

        public final File b(int i) {
            return new File(bc.this.c, this.b + "." + i + ".tmp");
        }
    }

    private bc(File file, long j) {
        this.c = file;
        this.d = new File(file, DiskLruCache.JOURNAL_FILE);
        this.e = new File(file, "journal.tmp");
        this.f = new File(file, "journal.bkp");
        this.h = j;
    }

    public static bc a(File file, long j) throws IOException {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        File file2 = new File(file, "journal.bkp");
        if (file2.exists()) {
            File file3 = new File(file, DiskLruCache.JOURNAL_FILE);
            if (file3.exists()) {
                file2.delete();
            } else {
                a(file2, file3, false);
            }
        }
        bc bcVar = new bc(file, j);
        if (bcVar.d.exists()) {
            try {
                bcVar.g();
                bcVar.h();
                bcVar.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bcVar.d, true), be.a));
                return bcVar;
            } catch (Throwable unused) {
                bcVar.d();
            }
        }
        file.mkdirs();
        bc bcVar2 = new bc(file, j);
        bcVar2.i();
        return bcVar2;
    }

    public static void a() {
        if (b == null || b.isShutdown()) {
            return;
        }
        b.shutdown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(a aVar, boolean z) throws IOException {
        c cVar = aVar.b;
        if (cVar.e != aVar) {
            throw new IllegalStateException();
        }
        if (z && !cVar.d) {
            for (int i = 0; i < this.i; i++) {
                if (!aVar.c[i]) {
                    aVar.c();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                }
                if (!cVar.b(i).exists()) {
                    aVar.c();
                    return;
                }
            }
        }
        for (int i2 = 0; i2 < this.i; i2++) {
            File b2 = cVar.b(i2);
            if (!z) {
                a(b2);
            } else if (b2.exists()) {
                File a2 = cVar.a(i2);
                b2.renameTo(a2);
                long j = cVar.c[i2];
                long length = a2.length();
                cVar.c[i2] = length;
                this.j = (this.j - j) + length;
            }
        }
        this.n++;
        cVar.e = null;
        if (cVar.d || z) {
            c.a(cVar);
            this.k.write("CLEAN " + cVar.b + cVar.a() + '\n');
            if (z) {
                long j2 = this.o;
                this.o = 1 + j2;
                cVar.f = j2;
            }
        } else {
            this.m.remove(cVar.b);
            this.k.write("REMOVE " + cVar.b + '\n');
        }
        this.k.flush();
        if (this.j > this.h || j()) {
            f().submit(this.q);
        }
    }

    private static void a(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void a(File file, File file2, boolean z) throws IOException {
        if (z) {
            a(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }

    private synchronized a d(String str) throws IOException {
        k();
        e(str);
        c cVar = this.m.get(str);
        byte b2 = 0;
        if (cVar == null) {
            cVar = new c(this, str, b2);
            this.m.put(str, cVar);
        } else if (cVar.e != null) {
            return null;
        }
        a aVar = new a(this, cVar, b2);
        cVar.e = aVar;
        this.k.write("DIRTY " + str + '\n');
        this.k.flush();
        return aVar;
    }

    static /* synthetic */ int e(bc bcVar) {
        bcVar.n = 0;
        return 0;
    }

    private static void e(String str) {
        if (a.matcher(str).matches()) {
            return;
        }
        throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
    }

    private static ThreadPoolExecutor f() {
        try {
            if (b == null || b.isShutdown()) {
                b = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(256), p);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return b;
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x00f3, code lost:
    
        throw new java.io.IOException("unexpected journal line: " + r3);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void g() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 331
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bc.g():void");
    }

    private void h() throws IOException {
        a(this.e);
        Iterator<c> it = this.m.values().iterator();
        while (it.hasNext()) {
            c next = it.next();
            int i = 0;
            if (next.e == null) {
                while (i < this.i) {
                    this.j += next.c[i];
                    i++;
                }
            } else {
                next.e = null;
                while (i < this.i) {
                    a(next.a(i));
                    a(next.b(i));
                    i++;
                }
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void i() throws IOException {
        if (this.k != null) {
            this.k.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e), be.a));
        try {
            bufferedWriter.write("libcore.io.DiskLruCache");
            bufferedWriter.write("\n");
            bufferedWriter.write(AmapLoc.RESULT_TYPE_WIFI_ONLY);
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.g));
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.i));
            bufferedWriter.write("\n");
            bufferedWriter.write("\n");
            for (c cVar : this.m.values()) {
                bufferedWriter.write(cVar.e != null ? "DIRTY " + cVar.b + '\n' : "CLEAN " + cVar.b + cVar.a() + '\n');
            }
            bufferedWriter.close();
            if (this.d.exists()) {
                a(this.d, this.f, true);
            }
            a(this.e, this.d, false);
            this.f.delete();
            this.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d, true), be.a));
        } catch (Throwable th) {
            bufferedWriter.close();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean j() {
        return this.n >= 2000 && this.n >= this.m.size();
    }

    private void k() {
        if (this.k == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() throws IOException {
        while (true) {
            if (this.j <= this.h && this.m.size() <= this.l) {
                return;
            } else {
                c(this.m.entrySet().iterator().next().getKey());
            }
        }
    }

    public final synchronized b a(String str) throws IOException {
        k();
        e(str);
        c cVar = this.m.get(str);
        if (cVar == null) {
            return null;
        }
        if (!cVar.d) {
            return null;
        }
        InputStream[] inputStreamArr = new InputStream[this.i];
        for (int i = 0; i < this.i; i++) {
            try {
                inputStreamArr[i] = new FileInputStream(cVar.a(i));
            } catch (FileNotFoundException unused) {
                for (int i2 = 0; i2 < this.i && inputStreamArr[i2] != null; i2++) {
                    be.a(inputStreamArr[i2]);
                }
                return null;
            }
        }
        this.n++;
        this.k.append((CharSequence) ("READ " + str + '\n'));
        if (j()) {
            f().submit(this.q);
        }
        return new b(this, str, cVar.f, inputStreamArr, cVar.c, (byte) 0);
    }

    public final void a(int i) {
        if (i < 10) {
            i = 10;
        } else if (i > 10000) {
            i = 10000;
        }
        this.l = i;
    }

    public final a b(String str) throws IOException {
        return d(str);
    }

    public final File b() {
        return this.c;
    }

    public final synchronized void c() throws IOException {
        k();
        l();
        this.k.flush();
    }

    public final synchronized boolean c(String str) throws IOException {
        k();
        e(str);
        c cVar = this.m.get(str);
        if (cVar != null && cVar.e == null) {
            for (int i = 0; i < this.i; i++) {
                File a2 = cVar.a(i);
                if (a2.exists() && !a2.delete()) {
                    throw new IOException("failed to delete " + a2);
                }
                this.j -= cVar.c[i];
                cVar.c[i] = 0;
            }
            this.n++;
            this.k.append((CharSequence) ("REMOVE " + str + '\n'));
            this.m.remove(str);
            if (j()) {
                f().submit(this.q);
            }
            return true;
        }
        return false;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final synchronized void close() throws IOException {
        if (this.k == null) {
            return;
        }
        Iterator it = new ArrayList(this.m.values()).iterator();
        while (it.hasNext()) {
            c cVar = (c) it.next();
            if (cVar.e != null) {
                cVar.e.c();
            }
        }
        l();
        this.k.close();
        this.k = null;
    }

    public final void d() throws IOException {
        close();
        be.a(this.c);
    }
}
