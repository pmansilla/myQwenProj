package com.mob.mcl.c;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: BioTcpSession.java */
/* loaded from: classes.dex */
public class a {
    final Socket a;
    public final f b;
    public SocketAddress c;
    public final AtomicBoolean d = new AtomicBoolean(false);
    final Map<e, Long> e = new WeakHashMap();

    /* compiled from: BioTcpSession.java */
    /* renamed from: com.mob.mcl.c.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class C0060a extends Thread {
        public C0060a(String str) {
            super(str);
            setDaemon(true);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            a aVar = a.this;
            if (aVar == null) {
                throw null;
            }
            try {
                InputStream inputStream = aVar.a.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[8096];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (-1 == read) {
                        return;
                    }
                    int i = 0;
                    byteArrayOutputStream.write(bArr, 0, read);
                    if (read < 8096) {
                        byteArrayOutputStream.flush();
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        ByteBuffer wrap = ByteBuffer.wrap(byteArray);
                        while (wrap.hasRemaining() && wrap.get() != 1) {
                            i++;
                        }
                        wrap.position(i);
                        List<g> a = g.a(wrap);
                        Iterator it = ((ArrayList) a).iterator();
                        while (it.hasNext()) {
                            i += ((g) it.next()).a + 17;
                        }
                        aVar.a(a);
                        byteArrayOutputStream.reset();
                        if (byteArray.length - i > 0) {
                            byteArrayOutputStream.write(byteArray, i, byteArray.length - i);
                        }
                    }
                }
            } catch (Throwable th) {
                ((i) aVar.b).a(aVar, th);
                aVar.a(true);
            }
        }
    }

    public a(Socket socket, f fVar) {
        this.a = socket;
        this.b = fVar;
        this.d.getAndSet(true);
        ((i) fVar).a(this);
        new C0060a("mlp-worker").start();
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x004b, code lost:
    
        r1 = r2.getKey();
        r2 = r1.a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0053, code lost:
    
        monitor-enter(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0054, code lost:
    
        r1.b.set(r0);
        r1.a.countDown();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005e, code lost:
    
        monitor-exit(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0004, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void a(java.util.List<com.mob.mcl.c.g> r7) {
        /*
            r6 = this;
            java.util.Iterator r7 = r7.iterator()
        L4:
            boolean r0 = r7.hasNext()
            if (r0 == 0) goto L63
            java.lang.Object r0 = r7.next()
            com.mob.mcl.c.g r0 = (com.mob.mcl.c.g) r0
            com.mob.mcl.c.f r1 = r6.b
            r2 = 9001(0x2329, float:1.2613E-41)
            if (r1 == 0) goto L1f
            int r3 = r0.b
            if (r3 < r2) goto L1f
            com.mob.mcl.c.i r1 = (com.mob.mcl.c.i) r1
            r1.a(r6, r0)
        L1f:
            int r1 = r0.b
            if (r1 >= r2) goto L4
            java.util.Map<com.mob.mcl.c.e, java.lang.Long> r1 = r6.e
            java.util.Set r1 = r1.entrySet()
            java.util.Iterator r1 = r1.iterator()
        L2d:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L4
            java.lang.Object r2 = r1.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r3 = r2.getValue()
            java.lang.Long r3 = (java.lang.Long) r3
            long r4 = r0.c
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L2d
            java.lang.Object r1 = r2.getKey()
            com.mob.mcl.c.e r1 = (com.mob.mcl.c.e) r1
            java.util.concurrent.CountDownLatch r2 = r1.a
            monitor-enter(r2)
            java.util.concurrent.atomic.AtomicReference<com.mob.mcl.c.g> r3 = r1.b     // Catch: java.lang.Throwable -> L60
            r3.set(r0)     // Catch: java.lang.Throwable -> L60
            java.util.concurrent.CountDownLatch r0 = r1.a     // Catch: java.lang.Throwable -> L60
            r0.countDown()     // Catch: java.lang.Throwable -> L60
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L60
            goto L4
        L60:
            r7 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L60
            throw r7
        L63:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.mcl.c.a.a(java.util.List):void");
    }

    public void a(boolean z) {
        i iVar;
        if (this.d.getAndSet(false)) {
            try {
                this.a.close();
                iVar = (i) this.b;
            } catch (Throwable unused) {
            }
            if (iVar == null) {
                throw null;
            }
            com.mob.mcl.d.b.a().a("sc " + z);
            if (z) {
                com.mob.mcl.b.a.e.execute(new j(iVar));
            }
            this.d.getAndSet(false);
            this.e.clear();
        }
    }
}
