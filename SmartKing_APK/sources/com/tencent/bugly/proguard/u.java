package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.support.v4.media.session.PlaybackStateCompat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class u {
    private static u a;
    private final Context c;
    private long e;
    private long f;
    private Map<Integer, Long> d = new HashMap();
    private LinkedBlockingQueue<Runnable> g = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Runnable> h = new LinkedBlockingQueue<>();
    private final Object i = new Object();
    private int j = 0;
    private final p b = p.a();

    private u(Context context) {
        this.c = context;
    }

    public static synchronized u a() {
        u uVar;
        synchronized (u.class) {
            uVar = a;
        }
        return uVar;
    }

    public static synchronized u a(Context context) {
        u uVar;
        synchronized (u.class) {
            if (a == null) {
                a = new u(context);
            }
            uVar = a;
        }
        return uVar;
    }

    private void a(Runnable runnable, boolean z, boolean z2, long j) {
        if (runnable == null) {
            x.d("[UploadManager] Upload task should not be null", new Object[0]);
        }
        x.c("[UploadManager] Add upload task (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        if (!z2) {
            a(runnable, z);
            c(0);
            return;
        }
        if (runnable == null) {
            x.d("[UploadManager] Upload task should not be null", new Object[0]);
            return;
        }
        x.c("[UploadManager] Execute synchronized upload task (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        Thread a2 = z.a(runnable, "BUGLY_SYNC_UPLOAD");
        if (a2 == null) {
            x.e("[UploadManager] Failed to start a thread to execute synchronized upload task, add it to queue.", new Object[0]);
            a(runnable, true);
            return;
        }
        try {
            a2.join(j);
        } catch (Throwable th) {
            x.e("[UploadManager] Failed to join upload synchronized task with message: %s. Add it to queue.", th.getMessage());
            a(runnable, true);
            c(0);
        }
    }

    private boolean a(Runnable runnable, boolean z) {
        if (runnable == null) {
            x.a("[UploadManager] Upload task should not be null", new Object[0]);
            return false;
        }
        try {
            x.c("[UploadManager] Add upload task to queue (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            synchronized (this.i) {
                try {
                    if (z) {
                        this.g.put(runnable);
                    } else {
                        this.h.put(runnable);
                    }
                } finally {
                }
            }
            return true;
        } catch (Throwable th) {
            x.e("[UploadManager] Failed to add upload task to queue: %s", th.getMessage());
            return false;
        }
    }

    static /* synthetic */ int b(u uVar) {
        int i = uVar.j - 1;
        uVar.j = i;
        return i;
    }

    private void c(int i) {
        w a2 = w.a();
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        final LinkedBlockingQueue linkedBlockingQueue2 = new LinkedBlockingQueue();
        synchronized (this.i) {
            x.c("[UploadManager] Try to poll all upload task need and put them into temp queue (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            int size = this.g.size();
            final int size2 = this.h.size();
            if (size == 0 && size2 == 0) {
                x.c("[UploadManager] There is no upload task in queue.", new Object[0]);
                return;
            }
            if (a2 == null || !a2.c()) {
                size2 = 0;
            }
            for (int i2 = 0; i2 < size; i2++) {
                Runnable peek = this.g.peek();
                if (peek == null) {
                    break;
                }
                try {
                    linkedBlockingQueue.put(peek);
                    this.g.poll();
                } catch (Throwable th) {
                    x.e("[UploadManager] Failed to add upload task to temp urgent queue: %s", th.getMessage());
                }
            }
            for (int i3 = 0; i3 < size2; i3++) {
                Runnable peek2 = this.h.peek();
                if (peek2 == null) {
                    break;
                }
                try {
                    linkedBlockingQueue2.put(peek2);
                    this.h.poll();
                } catch (Throwable th2) {
                    x.e("[UploadManager] Failed to add upload task to temp urgent queue: %s", th2.getMessage());
                }
            }
            if (size > 0) {
                x.c("[UploadManager] Execute urgent upload tasks of queue which has %d tasks (pid=%d | tid=%d)", Integer.valueOf(size), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            }
            for (int i4 = 0; i4 < size; i4++) {
                final Runnable runnable = (Runnable) linkedBlockingQueue.poll();
                if (runnable == null) {
                    break;
                }
                synchronized (this.i) {
                    if (this.j < 2 || a2 == null) {
                        x.a("[UploadManager] Create and start a new thread to execute a upload task: %s", "BUGLY_ASYNC_UPLOAD");
                        if (z.a(new Runnable() { // from class: com.tencent.bugly.proguard.u.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                runnable.run();
                                synchronized (u.this.i) {
                                    u.b(u.this);
                                }
                            }
                        }, "BUGLY_ASYNC_UPLOAD") != null) {
                            synchronized (this.i) {
                                this.j++;
                            }
                        } else {
                            x.d("[UploadManager] Failed to start a thread to execute asynchronous upload task, will try again next time.", new Object[0]);
                            a(runnable, true);
                        }
                    } else {
                        a2.a(runnable);
                    }
                }
            }
            if (size2 > 0) {
                x.c("[UploadManager] Execute upload tasks of queue which has %d tasks (pid=%d | tid=%d)", Integer.valueOf(size2), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            }
            if (a2 != null) {
                a2.a(new Runnable(this) { // from class: com.tencent.bugly.proguard.u.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Runnable runnable2;
                        for (int i5 = 0; i5 < size2 && (runnable2 = (Runnable) linkedBlockingQueue2.poll()) != null; i5++) {
                            runnable2.run();
                        }
                    }
                });
            }
        }
    }

    public final synchronized long a(int i) {
        if (i >= 0) {
            Long l = this.d.get(Integer.valueOf(i));
            if (l != null) {
                return l.longValue();
            }
        } else {
            x.e("[UploadManager] Unknown upload ID: %d", Integer.valueOf(i));
        }
        return 0L;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final long a(boolean r12) {
        /*
            r11 = this;
            long r0 = com.tencent.bugly.proguard.z.b()
            r2 = 3
            if (r12 == 0) goto L9
            r3 = 5
            goto La
        L9:
            r3 = 3
        La:
            com.tencent.bugly.proguard.p r4 = r11.b
            java.util.List r4 = r4.a(r3)
            r5 = 0
            if (r4 == 0) goto L4d
            int r6 = r4.size()
            if (r6 <= 0) goto L4d
            r6 = 0
            java.lang.Object r12 = r4.get(r5)     // Catch: java.lang.Throwable -> L3c
            com.tencent.bugly.proguard.r r12 = (com.tencent.bugly.proguard.r) r12     // Catch: java.lang.Throwable -> L3c
            long r8 = r12.e     // Catch: java.lang.Throwable -> L3c
            int r10 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r10 < 0) goto L3a
            byte[] r0 = r12.g     // Catch: java.lang.Throwable -> L3c
            long r0 = com.tencent.bugly.proguard.z.b(r0)     // Catch: java.lang.Throwable -> L3c
            if (r3 != r2) goto L34
            r11.e = r0     // Catch: java.lang.Throwable -> L32
            goto L36
        L32:
            r12 = move-exception
            goto L3e
        L34:
            r11.f = r0     // Catch: java.lang.Throwable -> L32
        L36:
            r4.remove(r12)     // Catch: java.lang.Throwable -> L32
            goto L41
        L3a:
            r0 = r6
            goto L41
        L3c:
            r12 = move-exception
            r0 = r6
        L3e:
            com.tencent.bugly.proguard.x.a(r12)
        L41:
            int r12 = r4.size()
            if (r12 <= 0) goto L54
            com.tencent.bugly.proguard.p r12 = r11.b
            r12.a(r4)
            goto L54
        L4d:
            if (r12 == 0) goto L52
            long r0 = r11.f
            goto L54
        L52:
            long r0 = r11.e
        L54:
            java.lang.String r12 = "[UploadManager] Local network consume: %d KB"
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 1024(0x400, double:5.06E-321)
            long r3 = r0 / r3
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r2[r5] = r3
            com.tencent.bugly.proguard.x.c(r12, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.u.a(boolean):long");
    }

    public final synchronized void a(int i, long j) {
        if (i < 0) {
            x.e("[UploadManager] Unknown uploading ID: %d", Integer.valueOf(i));
            return;
        }
        this.d.put(Integer.valueOf(i), Long.valueOf(j));
        r rVar = new r();
        rVar.b = i;
        rVar.e = j;
        rVar.c = "";
        rVar.d = "";
        rVar.g = new byte[0];
        this.b.b(i);
        this.b.a(rVar);
        x.c("[UploadManager] Uploading(ID:%d) time: %s", Integer.valueOf(i), z.a(j));
    }

    public final void a(int i, am amVar, String str, String str2, t tVar, long j, boolean z) {
        try {
            try {
                a(new v(this.c, i, amVar.g, a.a((Object) amVar), str, str2, tVar, true, z), true, true, j);
            } catch (Throwable th) {
                th = th;
                if (x.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public final void a(int i, am amVar, String str, String str2, t tVar, boolean z) {
        try {
            try {
                a(new v(this.c, i, amVar.g, a.a((Object) amVar), str, str2, tVar, 0, 0, false, null), z, false, 0L);
            } catch (Throwable th) {
                th = th;
                if (x.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void a(long j, boolean z) {
        int i = z ? 5 : 3;
        r rVar = new r();
        rVar.b = i;
        rVar.e = z.b();
        rVar.c = "";
        rVar.d = "";
        rVar.g = z.c(j);
        this.b.b(i);
        this.b.a(rVar);
        if (z) {
            this.f = j;
        } else {
            this.e = j;
        }
        x.c("[UploadManager] Network total consume: %d KB", Long.valueOf(j / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
    }

    public final boolean b(int i) {
        if (com.tencent.bugly.b.c) {
            x.c("Uploading frequency will not be checked if SDK is in debug mode.", new Object[0]);
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis() - a(i);
        x.c("[UploadManager] Time interval is %d seconds since last uploading(ID: %d).", Long.valueOf(currentTimeMillis / 1000), Integer.valueOf(i));
        if (currentTimeMillis >= 30000) {
            return true;
        }
        x.a("[UploadManager] Data only be uploaded once in %d seconds.", 30L);
        return false;
    }
}
