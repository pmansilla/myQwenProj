package com.amap.api.mapcore.util;

import com.amap.location.common.model.AmapLoc;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
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
public final class in implements Closeable {
    private final File c;
    private final File d;
    private final File e;
    private final File f;
    private final int g;
    private long h;
    private final int i;
    private Writer k;
    private int n;
    private io o;
    static final Pattern a = Pattern.compile("[a-z0-9_-]{1,120}");
    private static final ThreadFactory q = new ThreadFactory() { // from class: com.amap.api.mapcore.util.in.1
        private final AtomicInteger a = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "disklrucache#" + this.a.getAndIncrement());
        }
    };
    static ThreadPoolExecutor b = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), q);
    private static final OutputStream s = new OutputStream() { // from class: com.amap.api.mapcore.util.in.3
        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
        }
    };
    private long j = 0;
    private int l = 1000;
    private final LinkedHashMap<String, c> m = new LinkedHashMap<>(0, 0.75f, true);
    private long p = 0;
    private final Callable<Void> r = new Callable<Void>() { // from class: com.amap.api.mapcore.util.in.2
        @Override // java.util.concurrent.Callable
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void call() throws Exception {
            synchronized (in.this) {
                if (in.this.k == null) {
                    return null;
                }
                in.this.m();
                if (in.this.k()) {
                    in.this.j();
                    in.this.n = 0;
                }
                return null;
            }
        }
    };

    /* compiled from: DiskLruCache.java */
    /* loaded from: classes.dex */
    public final class a {
        private final c b;
        private final boolean[] c;
        private boolean d;
        private boolean e;

        /* compiled from: DiskLruCache.java */
        /* renamed from: com.amap.api.mapcore.util.in$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private class C0024a extends FilterOutputStream {
            private C0024a(OutputStream outputStream) {
                super(outputStream);
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                try {
                    this.out.close();
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
            public void flush() {
                try {
                    this.out.flush();
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(int i) {
                try {
                    this.out.write(i);
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(byte[] bArr, int i, int i2) {
                try {
                    this.out.write(bArr, i, i2);
                } catch (IOException unused) {
                    a.this.d = true;
                }
            }
        }

        private a(c cVar) {
            this.b = cVar;
            this.c = cVar.d ? null : new boolean[in.this.i];
        }

        public OutputStream a(int i) throws IOException {
            FileOutputStream fileOutputStream;
            C0024a c0024a;
            if (i < 0 || i >= in.this.i) {
                throw new IllegalArgumentException("Expected index " + i + " to be greater than 0 and less than the maximum value count of " + in.this.i);
            }
            synchronized (in.this) {
                if (this.b.e != this) {
                    throw new IllegalStateException();
                }
                if (!this.b.d) {
                    this.c[i] = true;
                }
                File b = this.b.b(i);
                try {
                    fileOutputStream = new FileOutputStream(b);
                } catch (FileNotFoundException unused) {
                    in.this.c.mkdirs();
                    try {
                        fileOutputStream = new FileOutputStream(b);
                    } catch (FileNotFoundException unused2) {
                        return in.s;
                    }
                }
                c0024a = new C0024a(fileOutputStream);
            }
            return c0024a;
        }

        public void a() throws IOException {
            if (this.d) {
                in.this.a(this, false);
                in.this.c(this.b.b);
            } else {
                in.this.a(this, true);
            }
            this.e = true;
        }

        public void b() throws IOException {
            in.this.a(this, false);
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

        public InputStream a(int i) {
            return this.d[i];
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            for (InputStream inputStream : this.d) {
                iq.a(inputStream);
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
            this.c = new long[in.this.i];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(String[] strArr) throws IOException {
            if (strArr.length != in.this.i) {
                throw b(strArr);
            }
            for (int i = 0; i < strArr.length; i++) {
                try {
                    this.c[i] = Long.parseLong(strArr[i]);
                } catch (NumberFormatException unused) {
                    throw b(strArr);
                }
            }
        }

        private IOException b(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        public File a(int i) {
            return new File(in.this.c, this.b + "." + i);
        }

        public String a() throws IOException {
            StringBuilder sb = new StringBuilder();
            for (long j : this.c) {
                sb.append(' ');
                sb.append(j);
            }
            return sb.toString();
        }

        public File b(int i) {
            return new File(in.this.c, this.b + "." + i + ".tmp");
        }
    }

    private in(File file, int i, int i2, long j) {
        this.c = file;
        this.g = i;
        this.d = new File(file, DiskLruCache.JOURNAL_FILE);
        this.e = new File(file, "journal.tmp");
        this.f = new File(file, "journal.bkp");
        this.i = i2;
        this.h = j;
    }

    private synchronized a a(String str, long j) throws IOException {
        l();
        e(str);
        c cVar = this.m.get(str);
        if (j != -1 && (cVar == null || cVar.f != j)) {
            return null;
        }
        if (cVar == null) {
            cVar = new c(str);
            this.m.put(str, cVar);
        } else if (cVar.e != null) {
            return null;
        }
        a aVar = new a(cVar);
        cVar.e = aVar;
        this.k.write("DIRTY " + str + '\n');
        this.k.flush();
        return aVar;
    }

    public static in a(File file, int i, int i2, long j) throws IOException {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
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
        in inVar = new in(file, i, i2, j);
        if (inVar.d.exists()) {
            try {
                inVar.h();
                inVar.i();
                inVar.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inVar.d, true), iq.a));
                return inVar;
            } catch (Throwable unused) {
                inVar.f();
            }
        }
        file.mkdirs();
        in inVar2 = new in(file, i, i2, j);
        inVar2.j();
        return inVar2;
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
                    aVar.b();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                }
                if (!cVar.b(i).exists()) {
                    aVar.b();
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
            cVar.d = true;
            this.k.write("CLEAN " + cVar.b + cVar.a() + '\n');
            if (z) {
                long j2 = this.p;
                this.p = 1 + j2;
                cVar.f = j2;
            }
        } else {
            this.m.remove(cVar.b);
            this.k.write("REMOVE " + cVar.b + '\n');
        }
        this.k.flush();
        if (this.j > this.h || k()) {
            b().submit(this.r);
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

    public static ThreadPoolExecutor b() {
        try {
            if (b == null || b.isShutdown()) {
                b = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(256), q);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return b;
    }

    private void d(String str) throws IOException {
        String substring;
        int indexOf = str.indexOf(32);
        if (indexOf == -1) {
            throw new IOException("unexpected journal line: " + str);
        }
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(32, i);
        if (indexOf2 == -1) {
            substring = str.substring(i);
            if (indexOf == "REMOVE".length() && str.startsWith("REMOVE")) {
                this.m.remove(substring);
                return;
            }
        } else {
            substring = str.substring(i, indexOf2);
        }
        c cVar = this.m.get(substring);
        if (cVar == null) {
            cVar = new c(substring);
            this.m.put(substring, cVar);
        }
        if (indexOf2 != -1 && indexOf == "CLEAN".length() && str.startsWith("CLEAN")) {
            String[] split = str.substring(indexOf2 + 1).split(SQLBuilder.BLANK);
            cVar.d = true;
            cVar.e = null;
            cVar.a(split);
            return;
        }
        if (indexOf2 == -1 && indexOf == "DIRTY".length() && str.startsWith("DIRTY")) {
            cVar.e = new a(cVar);
            return;
        }
        if (indexOf2 == -1 && indexOf == "READ".length() && str.startsWith("READ")) {
            return;
        }
        throw new IOException("unexpected journal line: " + str);
    }

    private void e(String str) {
        if (a.matcher(str).matches()) {
            return;
        }
        throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
    }

    private void h() throws IOException {
        ip ipVar = new ip(new FileInputStream(this.d), iq.a);
        try {
            String a2 = ipVar.a();
            String a3 = ipVar.a();
            String a4 = ipVar.a();
            String a5 = ipVar.a();
            String a6 = ipVar.a();
            if (!"libcore.io.DiskLruCache".equals(a2) || !AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(a3) || !Integer.toString(this.g).equals(a4) || !Integer.toString(this.i).equals(a5) || !"".equals(a6)) {
                throw new IOException("unexpected journal header: [" + a2 + ", " + a3 + ", " + a5 + ", " + a6 + "]");
            }
            int i = 0;
            while (true) {
                try {
                    d(ipVar.a());
                    i++;
                } catch (EOFException unused) {
                    this.n = i - this.m.size();
                    iq.a(ipVar);
                    return;
                }
            }
        } catch (Throwable th) {
            iq.a(ipVar);
            throw th;
        }
    }

    private void i() throws IOException {
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
    public synchronized void j() throws IOException {
        if (this.k != null) {
            this.k.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e), iq.a));
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
                if (cVar.e != null) {
                    bufferedWriter.write("DIRTY " + cVar.b + '\n');
                } else {
                    bufferedWriter.write("CLEAN " + cVar.b + cVar.a() + '\n');
                }
            }
            bufferedWriter.close();
            if (this.d.exists()) {
                a(this.d, this.f, true);
            }
            a(this.e, this.d, false);
            this.f.delete();
            this.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d, true), iq.a));
        } catch (Throwable th) {
            bufferedWriter.close();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean k() {
        return this.n >= 2000 && this.n >= this.m.size();
    }

    private void l() {
        if (this.k == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() throws IOException {
        while (true) {
            if (this.j <= this.h && this.m.size() <= this.l) {
                return;
            }
            String key = this.m.entrySet().iterator().next().getKey();
            c(key);
            if (this.o != null) {
                this.o.a(key);
            }
        }
    }

    public synchronized b a(String str) throws IOException {
        l();
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
                    iq.a(inputStreamArr[i2]);
                }
                return null;
            }
        }
        this.n++;
        this.k.append((CharSequence) ("READ " + str + '\n'));
        if (k()) {
            b().submit(this.r);
        }
        return new b(str, cVar.f, inputStreamArr, cVar.c);
    }

    public void a(int i) {
        if (i < 10) {
            i = 10;
        } else if (i > 10000) {
            i = 10000;
        }
        this.l = i;
    }

    public a b(String str) throws IOException {
        return a(str, -1L);
    }

    public File c() {
        return this.c;
    }

    public synchronized boolean c(String str) throws IOException {
        l();
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
            if (k()) {
                b().submit(this.r);
            }
            return true;
        }
        return false;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.k == null) {
            return;
        }
        Iterator it = new ArrayList(this.m.values()).iterator();
        while (it.hasNext()) {
            c cVar = (c) it.next();
            if (cVar.e != null) {
                cVar.e.b();
            }
        }
        m();
        this.k.close();
        this.k = null;
    }

    public synchronized boolean d() {
        return this.k == null;
    }

    public synchronized void e() throws IOException {
        l();
        m();
        this.k.flush();
    }

    public void f() throws IOException {
        close();
        iq.a(this.c);
    }
}
