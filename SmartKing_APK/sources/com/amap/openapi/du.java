package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: UpTunnelManager.java */
/* loaded from: classes.dex */
public class du {
    private HandlerThread a;
    private Looper b;
    private b c;
    private ds d;
    private dt e;
    private a f;
    private final Object g = new byte[0];
    private ArrayList<Message> h = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UpTunnelManager.java */
    /* loaded from: classes.dex */
    public final class a extends BroadcastReceiver {
        private a() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            char c = 65535;
            if (action.hashCode() == -1172645946 && action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                c = 0;
            }
            if (c == 0 && du.this.c != null) {
                du.this.c.sendEmptyMessage(11);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: UpTunnelManager.java */
    /* loaded from: classes.dex */
    public class b extends Handler {
        private boolean b;

        b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (this.b) {
                return;
            }
            if (message.what == 10) {
                du.this.d.a(message);
                return;
            }
            if (message.what == 11) {
                du.this.d.a();
                return;
            }
            if (message.what == 12) {
                du.this.d.a((dm) message.obj);
            } else if (message.what == 13) {
                this.b = true;
                removeCallbacksAndMessages(null);
                du.this.d.b();
                try {
                    du.this.e.a().unregisterReceiver(du.this.f);
                } catch (Exception unused) {
                }
                post(new Runnable() { // from class: com.amap.openapi.du.b.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            b.this.getLooper().quit();
                        } catch (Throwable unused2) {
                        }
                    }
                });
            }
        }
    }

    public du(@NonNull final Context context, @NonNull final dk dkVar) {
        this.a = new HandlerThread("UpTunnelWorkThread") { // from class: com.amap.openapi.du.1
            @Override // android.os.HandlerThread
            protected void onLooperPrepared() {
                synchronized (du.this.g) {
                    du.this.e = new dt(context);
                    du.this.b = du.this.a.getLooper();
                    du.this.c = new b(du.this.b);
                    du.this.d = new ds();
                    du.this.d.a(du.this.e, dkVar, du.this.b);
                    du.this.b();
                    Iterator it = du.this.h.iterator();
                    while (it.hasNext()) {
                        du.this.c.sendMessage((Message) it.next());
                    }
                    du.this.h.clear();
                }
            }
        };
        this.a.start();
    }

    private void a(int i, int i2, int i3, Object obj) {
        if (this.c != null) {
            this.c.obtainMessage(i, i2, i3, obj).sendToTarget();
            return;
        }
        synchronized (this.g) {
            if (this.c != null) {
                this.c.obtainMessage(i, i2, i3, obj).sendToTarget();
            } else {
                this.h.add(Message.obtain(null, i, i2, i3, obj));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        this.f = new a();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        try {
            this.e.a().registerReceiver(this.f, intentFilter);
        } catch (Exception unused) {
        }
    }

    public void a() {
        a(13, 0, 0, null);
    }

    public void a(int i) {
        a(10, 1, i, null);
    }

    public void a(int i, byte[] bArr) {
        a(10, 2, i, bArr);
    }
}
