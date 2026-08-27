package com.amap.api.mapcore.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: TraceResultPool.java */
/* loaded from: classes.dex */
public class ha {
    private static volatile ha b;
    private Map<String, a> a;

    /* compiled from: TraceResultPool.java */
    /* loaded from: classes.dex */
    class a {
        private int b;
        private int c;
        private int e;
        private HashMap<Integer, List<LatLng>> g;
        private int d = 0;
        private int f = 0;
        private List<LatLng> h = new ArrayList();

        public a(int i, int i2, int i3, HashMap<Integer, List<LatLng>> hashMap) {
            this.b = 0;
            this.c = 0;
            this.e = 0;
            this.b = i2;
            this.g = hashMap;
            this.c = i;
            this.e = i3;
        }

        private void a(Handler handler, List<LatLng> list) {
            Message obtainMessage = handler.obtainMessage();
            obtainMessage.obj = list;
            obtainMessage.what = 100;
            obtainMessage.arg1 = this.d;
            Bundle bundle = new Bundle();
            bundle.putInt("lineID", this.c);
            obtainMessage.setData(bundle);
            handler.sendMessage(obtainMessage);
            this.d++;
            this.f++;
        }

        private void b(Handler handler) {
            if (this.f <= 0) {
                ha.this.a(handler, this.c, LBSTraceClient.MIN_GRASP_POINT_ERROR);
                return;
            }
            int a = gx.a(this.h);
            Message obtainMessage = handler.obtainMessage();
            obtainMessage.obj = this.h;
            obtainMessage.what = 101;
            obtainMessage.arg1 = a;
            obtainMessage.arg2 = this.e;
            Bundle bundle = new Bundle();
            bundle.putInt("lineID", this.c);
            obtainMessage.setData(bundle);
            handler.sendMessage(obtainMessage);
        }

        public HashMap<Integer, List<LatLng>> a() {
            return this.g;
        }

        public void a(Handler handler) {
            List<LatLng> list;
            for (int i = this.d; i <= this.b && (list = this.g.get(Integer.valueOf(i))) != null; i++) {
                this.h.addAll(list);
                a(handler, list);
            }
            if (this.d == this.b + 1) {
                b(handler);
            }
        }
    }

    public ha() {
        this.a = null;
        this.a = Collections.synchronizedMap(new HashMap());
    }

    public static ha a() {
        if (b == null) {
            synchronized (ha.class) {
                if (b == null) {
                    b = new ha();
                }
            }
        }
        return b;
    }

    public synchronized a a(String str) {
        if (this.a == null) {
            return null;
        }
        return this.a.get(str);
    }

    public void a(Handler handler, int i, String str) {
        Message obtainMessage = handler.obtainMessage();
        obtainMessage.obj = str;
        obtainMessage.what = 102;
        Bundle bundle = new Bundle();
        bundle.putInt("lineID", i);
        obtainMessage.setData(bundle);
        handler.sendMessage(obtainMessage);
    }

    public synchronized void a(String str, int i, int i2, int i3) {
        if (this.a != null) {
            this.a.put(str, new a(i, i2, i3, new HashMap(16)));
        }
    }

    public synchronized void a(String str, int i, List<LatLng> list) {
        if (this.a != null) {
            this.a.get(str).a().put(Integer.valueOf(i), list);
        }
    }
}
