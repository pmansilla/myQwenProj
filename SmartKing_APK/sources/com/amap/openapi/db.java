package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GnssStatus;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import com.amap.location.common.log.ALLog;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: GnssStatusManager.java */
@RequiresApi(api = 24)
/* loaded from: classes.dex */
public class db extends GnssStatus.Callback {
    private cz b;
    private Context c;
    private final List<a> a = new CopyOnWriteArrayList();
    private b d = new b(this);

    /* compiled from: GnssStatusManager.java */
    /* loaded from: classes.dex */
    private static class a {
        private Handler a;

        void a(int i, Object obj) {
            Message obtainMessage = this.a.obtainMessage();
            obtainMessage.what = i;
            obtainMessage.obj = obj;
            obtainMessage.sendToTarget();
        }
    }

    /* compiled from: GnssStatusManager.java */
    /* loaded from: classes.dex */
    private class b extends BroadcastReceiver {
        private GnssStatus.Callback b;

        public b(GnssStatus.Callback callback) {
            this.b = callback;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (cr.a(context).a("gps")) {
                synchronized (db.this.a) {
                    if (db.this.a.size() > 0) {
                        try {
                            db.this.b.b(this.b);
                            db.this.b.a(this.b);
                        } catch (SecurityException e) {
                            ALLog.trace("@_24_5_@", "卫星老接口权限异常", e);
                        }
                    }
                }
            }
        }
    }

    public db(cz czVar, Context context) {
        this.b = czVar;
        this.c = context;
    }

    @Override // android.location.GnssStatus.Callback
    public void onFirstFix(int i) {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(3, Integer.valueOf(i));
            }
        }
    }

    @Override // android.location.GnssStatus.Callback
    public void onSatelliteStatusChanged(GnssStatus gnssStatus) {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(4, gnssStatus);
            }
        }
    }

    @Override // android.location.GnssStatus.Callback
    public void onStarted() {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(1, null);
            }
        }
    }

    @Override // android.location.GnssStatus.Callback
    public void onStopped() {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(2, null);
            }
        }
    }
}
