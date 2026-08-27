package com.amap.openapi;

import android.location.GpsStatus;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresPermission;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: NmeaManager.java */
/* loaded from: classes.dex */
public class cy {
    private final List<a> a = new CopyOnWriteArrayList();
    private cz b;
    private OnNmeaMessageListener c;
    private GpsStatus.NmeaListener d;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NmeaManager.java */
    /* loaded from: classes.dex */
    public static class a {
        cs a;
        private Handler b;

        /* compiled from: NmeaManager.java */
        /* renamed from: com.amap.openapi.cy$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class HandlerC0031a extends Handler {
            private cs a;

            HandlerC0031a(cs csVar, Looper looper) {
                super(looper);
                this.a = csVar;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                Bundle data = message.getData();
                this.a.a(data.getLong("timestamp"), data.getString("nmea"));
            }
        }

        a(cs csVar, Looper looper) {
            this.a = csVar;
            this.b = new HandlerC0031a(this.a, looper == null ? Looper.getMainLooper() : looper);
        }

        void a(long j, String str) {
            Message obtainMessage = this.b.obtainMessage();
            obtainMessage.getData().putLong("timestamp", j);
            obtainMessage.getData().putString("nmea", str);
            obtainMessage.sendToTarget();
        }

        boolean a(cs csVar, Looper looper) {
            if (looper == null) {
                looper = Looper.getMainLooper();
            }
            return this.a == csVar && this.b.getLooper() == looper;
        }
    }

    public cy(cz czVar) {
        this.b = czVar;
        if (Build.VERSION.SDK_INT >= 24) {
            this.c = new OnNmeaMessageListener() { // from class: com.amap.openapi.cy.1
                @Override // android.location.OnNmeaMessageListener
                public void onNmeaMessage(String str, long j) {
                    cy.this.a(j, str);
                }
            };
        } else {
            this.d = new GpsStatus.NmeaListener() { // from class: com.amap.openapi.cy.2
                @Override // android.location.GpsStatus.NmeaListener
                public void onNmeaReceived(long j, String str) {
                    cy.this.a(j, str);
                }
            };
        }
    }

    private a b(cs csVar) {
        for (a aVar : this.a) {
            if (aVar.a == csVar) {
                return aVar;
            }
        }
        return null;
    }

    public void a(long j, String str) {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(j, str);
            }
        }
    }

    public void a(cs csVar) {
        if (csVar == null) {
            return;
        }
        synchronized (this.a) {
            a b = b(csVar);
            if (b != null) {
                this.a.remove(b);
                if (this.a.size() == 0) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        if (this.c != null) {
                            this.b.a(this.c);
                        }
                    } else if (this.d != null) {
                        this.b.a(this.d);
                    }
                }
            }
        }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public boolean a(cs csVar, Looper looper) {
        boolean z = false;
        if (csVar == null) {
            return false;
        }
        synchronized (this.a) {
            a b = b(csVar);
            if (b != null) {
                return b.a(csVar, looper);
            }
            a aVar = new a(csVar, looper);
            this.a.add(aVar);
            if (this.a.size() != 1) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= 24) {
                if (this.c != null) {
                    z = this.b.a(this.c, looper);
                }
            } else if (this.d != null) {
                z = this.b.a(this.d, looper);
            }
            if (!z) {
                this.a.remove(aVar);
            }
            return z;
        }
    }
}
