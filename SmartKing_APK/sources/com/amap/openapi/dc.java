package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Handler;
import android.os.Message;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: GpsStatusManager.java */
/* loaded from: classes.dex */
public class dc implements GpsStatus.Listener {
    private cz b;
    private Context c;
    private final List<b> a = new CopyOnWriteArrayList();
    private a d = new a(this);

    /* compiled from: GpsStatusManager.java */
    /* loaded from: classes.dex */
    private class a extends BroadcastReceiver {
        private GpsStatus.Listener b;

        public a(GpsStatus.Listener listener) {
            this.b = listener;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (cr.a(context).a("gps")) {
                synchronized (dc.this.a) {
                    if (dc.this.a.size() > 0) {
                        dc.this.b.b(this.b);
                        dc.this.b.a(this.b);
                    }
                }
            }
        }
    }

    /* compiled from: GpsStatusManager.java */
    /* loaded from: classes.dex */
    private static class b {
        private Handler a;

        void a(int i) {
            Message obtainMessage = this.a.obtainMessage();
            obtainMessage.arg1 = i;
            obtainMessage.sendToTarget();
        }
    }

    public dc(cz czVar, Context context) {
        this.b = czVar;
        this.c = context;
    }

    @Override // android.location.GpsStatus.Listener
    public void onGpsStatusChanged(int i) {
        synchronized (this.a) {
            Iterator<b> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(i);
            }
        }
    }
}
