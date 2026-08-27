package com.amap.api.mapcore.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.trace.LBSTraceBase;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: TraceManager.java */
/* loaded from: classes.dex */
public class gz implements LocationSource.OnLocationChangedListener, LBSTraceBase {
    private static final TimeUnit s = TimeUnit.SECONDS;
    private Context b;
    private CoordinateConverter c;
    private ExecutorService d;
    private ExecutorService e;
    private TraceStatusListener h;
    private bi i;
    private long f = 2000;
    private int g = 5;
    private List<TraceLocation> j = new ArrayList();
    private int k = 0;
    private int l = 0;
    private long m = 0;
    private TraceLocation o = null;
    private List<LatLng> p = new ArrayList();
    private List<LatLng> q = new ArrayList();
    private List<LatLng> r = new ArrayList();
    int a = Runtime.getRuntime().availableProcessors();
    private BlockingQueue<Runnable> t = new LinkedBlockingQueue();
    private BlockingQueue<Runnable> u = new LinkedBlockingQueue();
    private c n = new c(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TraceManager.java */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        private int c;
        private int d;
        private List<TraceLocation> e;
        private TraceListener g;
        private List<TraceLocation> b = new ArrayList();
        private String f = fj.a();

        public a(int i, List<TraceLocation> list, int i2, TraceListener traceListener) {
            this.c = i2;
            this.d = i;
            this.e = list;
            this.g = traceListener;
        }

        private int a() {
            int i = 0;
            if (this.e == null || this.e.size() == 0) {
                return 0;
            }
            ArrayList arrayList = new ArrayList();
            for (TraceLocation traceLocation : this.e) {
                if (traceLocation != null) {
                    if (traceLocation.getSpeed() < 0.01d) {
                        arrayList.add(traceLocation);
                    } else {
                        i += a(arrayList);
                        arrayList.clear();
                    }
                }
            }
            return i;
        }

        private int a(List<TraceLocation> list) {
            int size = list.size();
            if (size <= 1) {
                return 0;
            }
            TraceLocation traceLocation = list.get(0);
            TraceLocation traceLocation2 = list.get(size - 1);
            if (traceLocation == null || traceLocation2 == null || traceLocation == null || traceLocation2 == null) {
                return 0;
            }
            return (int) ((traceLocation2.getTime() - traceLocation.getTime()) / 1000);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                gz.this.n.a(this.g);
                int a = a();
                if (this.e != null && this.e.size() >= 2) {
                    Iterator<TraceLocation> it = this.e.iterator();
                    while (it.hasNext()) {
                        TraceLocation copy = it.next().copy();
                        if (copy != null && copy.getLatitude() > 0.0d && copy.getLongitude() > 0.0d) {
                            this.b.add(copy);
                        }
                    }
                    int size = (this.b.size() - 2) / 500;
                    ha.a().a(this.f, this.d, size, a);
                    int i = 500;
                    int i2 = 0;
                    while (i2 <= size) {
                        if (i2 == size) {
                            i = this.b.size();
                        }
                        int i3 = i;
                        ArrayList arrayList = new ArrayList();
                        for (int i4 = 0; i4 < i3; i4++) {
                            TraceLocation remove = this.b.remove(0);
                            if (remove != null) {
                                if (this.c != 1) {
                                    if (this.c == 3) {
                                        gz.this.c.from(CoordinateConverter.CoordType.BAIDU);
                                    } else if (this.c == 2) {
                                        gz.this.c.from(CoordinateConverter.CoordType.GPS);
                                    }
                                    gz.this.c.coord(new LatLng(remove.getLatitude(), remove.getLongitude()));
                                    LatLng convert = gz.this.c.convert();
                                    if (convert != null) {
                                        remove.setLatitude(convert.latitude);
                                        remove.setLongitude(convert.longitude);
                                    }
                                }
                                arrayList.add(remove);
                            }
                        }
                        if (arrayList.size() >= 2 && arrayList.size() <= 500) {
                            gz.this.e.execute(new gy(gz.this.b, gz.this.n, arrayList, this.c, this.f, this.d, i2));
                            i2++;
                            try {
                                Thread.sleep(50L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        i = i3;
                    }
                    return;
                }
                ha.a().a(gz.this.n, this.d, LBSTraceClient.MIN_GRASP_POINT_ERROR);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TraceManager.java */
    /* loaded from: classes.dex */
    public class b implements TraceListener {
        private final List<TraceLocation> b;

        public b(List<TraceLocation> list) {
            this.b = list;
        }

        private void a(int i, List<LatLng> list) {
            try {
                synchronized (gz.this.r) {
                    gz.this.r.clear();
                    gz.this.r.addAll(list);
                }
                gz.this.q.clear();
                if (i == 0) {
                    gz.this.q.addAll(gz.this.r);
                } else {
                    gz.this.q.addAll(gz.this.p);
                    gz.this.q.addAll(gz.this.r);
                }
                gz.this.h.onTraceStatus(gz.this.j, gz.this.q, LBSTraceClient.TRACE_SUCCESS);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        @Override // com.amap.api.trace.TraceListener
        public void onFinished(int i, List<LatLng> list, int i2, int i3) {
            a(i, list);
        }

        @Override // com.amap.api.trace.TraceListener
        public void onRequestFailed(int i, String str) {
            ArrayList arrayList = new ArrayList();
            if (gz.this.r != null) {
                arrayList.addAll(gz.this.r);
            }
            if (this.b != null) {
                int size = this.b.size();
                if (this.b.size() > gz.this.g) {
                    for (int i2 = size - gz.this.g; i2 < size; i2++) {
                        TraceLocation traceLocation = this.b.get(i2);
                        if (traceLocation != null) {
                            arrayList.add(new LatLng(traceLocation.getLatitude(), traceLocation.getLongitude()));
                        }
                    }
                }
            }
            a(i, arrayList);
        }

        @Override // com.amap.api.trace.TraceListener
        public void onTraceProcessing(int i, int i2, List<LatLng> list) {
        }
    }

    /* compiled from: TraceManager.java */
    /* loaded from: classes.dex */
    static class c extends Handler {
        private TraceListener a;

        public c(Looper looper) {
            super(looper);
        }

        public void a(TraceListener traceListener) {
            this.a = traceListener;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Bundle data;
            try {
                if (this.a == null || (data = message.getData()) == null) {
                    return;
                }
                int i = data.getInt("lineID");
                switch (message.what) {
                    case 100:
                        this.a.onTraceProcessing(i, message.arg1, (List) message.obj);
                        break;
                    case 101:
                        this.a.onFinished(i, (List) message.obj, message.arg1, message.arg2);
                        break;
                    case 102:
                        this.a.onRequestFailed(i, (String) message.obj);
                        break;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public gz(Context context) {
        this.b = context.getApplicationContext();
        this.c = new CoordinateConverter(this.b);
        hk.a().a(this.b);
        this.d = new ThreadPoolExecutor(1, this.a * 2, 1L, s, this.t, new fe("AMapTraceManagerProcess"), new ThreadPoolExecutor.AbortPolicy());
        this.e = new ThreadPoolExecutor(1, this.a * 2, 1L, s, this.u, new fe("AMapTraceManagerRequest"), new ThreadPoolExecutor.AbortPolicy());
    }

    private double a(double d, double d2, double d3, double d4) {
        double d5 = d > d3 ? d - d3 : d3 - d;
        double d6 = d2 > d4 ? d2 - d4 : d4 - d2;
        return Math.sqrt((d5 * d5) + (d6 * d6));
    }

    private void a() {
        int size = this.j.size();
        if (size < this.g) {
            return;
        }
        if (size <= 50) {
            ArrayList arrayList = new ArrayList(this.j);
            queryProcessedTrace(0, arrayList, 1, new b(arrayList));
            return;
        }
        int i = size - 50;
        if (i < 0) {
            return;
        }
        a(new ArrayList(this.j.subList(i - this.g, i)));
        ArrayList arrayList2 = new ArrayList(this.j.subList(i, size));
        queryProcessedTrace(i, arrayList2, 1, new b(arrayList2));
    }

    private void a(List<TraceLocation> list) {
        synchronized (this.r) {
            if (list != null) {
                try {
                    if (list.size() >= 1) {
                        if (this.r.size() < 1) {
                            return;
                        }
                        LatLng latLng = null;
                        double d = 0.0d;
                        TraceLocation traceLocation = null;
                        double d2 = 0.0d;
                        for (TraceLocation traceLocation2 : list) {
                            if (traceLocation2 != null) {
                                if (traceLocation != null) {
                                    double a2 = a(traceLocation.getLatitude(), traceLocation.getLongitude(), traceLocation2.getLatitude(), traceLocation2.getLongitude());
                                    if (a2 <= 100.0d) {
                                        d2 += a2;
                                    }
                                }
                                traceLocation = traceLocation2;
                            }
                        }
                        Iterator<LatLng> it = this.r.iterator();
                        while (it.hasNext()) {
                            LatLng next = it.next();
                            if (next == null) {
                                it.remove();
                            } else {
                                if (latLng != null) {
                                    d += a(latLng.latitude, latLng.longitude, next.latitude, next.longitude);
                                    if (d >= d2) {
                                        break;
                                    }
                                    this.p.add(next);
                                    it.remove();
                                } else {
                                    this.p.add(next);
                                    it.remove();
                                }
                                latLng = next;
                            }
                        }
                    }
                } finally {
                }
            }
        }
    }

    private boolean a(TraceLocation traceLocation, TraceLocation traceLocation2) {
        return traceLocation != null && traceLocation2 != null && traceLocation.getLatitude() == traceLocation2.getLatitude() && traceLocation.getLongitude() == traceLocation2.getLongitude();
    }

    private void b() {
        if (this.i != null) {
            this.i.deactivate();
            this.i = null;
        }
    }

    private void c() {
        this.t.clear();
        this.u.clear();
        if (this.j != null) {
            synchronized (this.j) {
                if (this.j != null) {
                    this.j.clear();
                }
                this.l = 0;
                this.k = 0;
                this.m = 0L;
                this.o = null;
            }
        }
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void destroy() {
        try {
            stopTrace();
            if (this.d != null && !this.d.isShutdown()) {
                this.d.shutdownNow();
                this.d = null;
            }
            if (this.e != null && !this.e.isShutdown()) {
                this.e.shutdownNow();
                this.e = null;
            }
            this.j = null;
            this.h = null;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.b = null;
        this.c = null;
    }

    @Override // com.amap.api.maps.LocationSource.OnLocationChangedListener
    public void onLocationChanged(Location location) {
        if (this.h != null) {
            try {
                if (System.currentTimeMillis() - this.m >= 30000 && this.h != null) {
                    this.h.onTraceStatus(null, null, LBSTraceClient.LOCATE_TIMEOUT_ERROR);
                }
                this.m = System.currentTimeMillis();
                Bundle extras = location.getExtras();
                int i = extras.getInt(MyLocationStyle.ERROR_CODE);
                if (i != 0) {
                    Log.w("LBSTraceClient", "Locate failed [errorCode:\"" + i + "\"  errorInfo:" + extras.getString(MyLocationStyle.ERROR_INFO) + "\"]");
                    return;
                }
                synchronized (this.j) {
                    TraceLocation traceLocation = new TraceLocation(location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getBearing(), location.getTime());
                    if (a(this.o, traceLocation)) {
                        return;
                    }
                    this.j.add(traceLocation);
                    this.o = traceLocation;
                    this.k++;
                    if (this.k == this.g) {
                        this.l += this.k;
                        a();
                        this.k = 0;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void queryProcessedTrace(int i, List<TraceLocation> list, int i2, TraceListener traceListener) {
        try {
            this.d.execute(new a(i, list, i2, traceListener));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void setLocationInterval(long j) {
        this.f = j;
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void setTraceStatusInterval(int i) {
        this.g = Math.max(i, 2);
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void startTrace(TraceStatusListener traceStatusListener) {
        if (this.b == null) {
            Log.w("LBSTraceClient", "Context need to be initialized");
            return;
        }
        this.m = System.currentTimeMillis();
        this.h = traceStatusListener;
        if (this.i == null) {
            this.i = new bi(this.b);
            this.i.a(this.f);
            this.i.activate(this);
        }
    }

    @Override // com.amap.api.trace.LBSTraceBase
    public void stopTrace() {
        b();
        c();
    }
}
