package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;

/* compiled from: OfflineDownloadManager.java */
/* loaded from: classes.dex */
public class bp {
    public static String a = "";
    public static boolean b = false;
    public static String d = "";
    private static volatile bp k;
    public bt f;
    bv g;
    private Context i;
    private a l;
    private by m;
    private ce n;
    private boolean j = true;
    List<bo> c = new Vector();
    private ExecutorService o = null;
    private ExecutorService p = null;
    private ExecutorService q = null;
    b e = null;
    bs h = null;
    private boolean r = true;

    /* compiled from: OfflineDownloadManager.java */
    /* loaded from: classes.dex */
    public interface a {
        void a();

        void a(bo boVar);

        void b(bo boVar);

        void c(bo boVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OfflineDownloadManager.java */
    /* loaded from: classes.dex */
    public class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                message.getData();
                Object obj = message.obj;
                if (obj instanceof bo) {
                    bo boVar = (bo) obj;
                    cm.a("OfflineMapHandler handleMessage CitObj  name: " + boVar.getCity() + " complete: " + boVar.getcompleteCode() + " status: " + boVar.getState());
                    if (bp.this.l != null) {
                        bp.this.l.a(boVar);
                    }
                } else {
                    cm.a("Do not callback by CityObject! ");
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private bp(Context context) {
        this.i = context;
    }

    public static bp a(Context context) {
        if (k == null) {
            synchronized (bp.class) {
                if (k == null && !b) {
                    k = new bp(context.getApplicationContext());
                }
            }
        }
        return k;
    }

    private void a(final bo boVar, final boolean z) {
        if (this.g == null) {
            this.g = new bv(this.i);
        }
        if (this.p == null) {
            this.p = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue(), new fe("AMapOfflineRemove"), new ThreadPoolExecutor.AbortPolicy());
        }
        try {
            this.p.execute(new Runnable() { // from class: com.amap.api.mapcore.util.bp.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (boVar.c().equals(boVar.a)) {
                            if (bp.this.l != null) {
                                bp.this.l.c(boVar);
                                return;
                            }
                            return;
                        }
                        if (boVar.getState() != 7 && boVar.getState() != -1) {
                            bp.this.g.a(boVar);
                            if (bp.this.l != null) {
                                bp.this.l.c(boVar);
                                return;
                            }
                            return;
                        }
                        bp.this.g.a(boVar);
                        if (!z || bp.this.l == null) {
                            return;
                        }
                        bp.this.l.c(boVar);
                    } catch (Throwable th) {
                        ic.c(th, "requestDelete", "removeExcecRunnable");
                    }
                }
            });
        } catch (Throwable th) {
            ic.c(th, "requestDelete", "removeExcecRunnable");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(String str, String str2) {
        for (int i = 0; i < str2.length(); i++) {
            try {
                if (str.charAt(i) > str2.charAt(i)) {
                    return true;
                }
                if (str.charAt(i) < str2.charAt(i)) {
                    return false;
                }
            } catch (Throwable unused) {
            }
        }
        return false;
    }

    public static void f() {
        k = null;
        b = true;
    }

    private void f(final bo boVar) throws AMapException {
        k();
        if (boVar == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        if (this.q == null) {
            this.q = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue(), new fe("AMapOfflineDownload"), new ThreadPoolExecutor.AbortPolicy());
        }
        try {
            this.q.execute(new Runnable() { // from class: com.amap.api.mapcore.util.bp.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (bp.this.j) {
                            bp.this.k();
                            bq c = new br(bp.this.i, bp.d).c();
                            if (c != null) {
                                bp.this.j = false;
                                if (c.a()) {
                                    bp.this.b();
                                }
                            }
                        }
                        boVar.setVersion(bp.d);
                        boVar.f();
                    } catch (AMapException e) {
                        e.printStackTrace();
                    } catch (Throwable th) {
                        ic.c(th, "OfflineDownloadManager", "startDownloadRunnable");
                    }
                }
            });
        } catch (Throwable th) {
            ic.c(th, "startDownload", "downloadExcecRunnable");
        }
    }

    public static void g(String str) {
        a = str;
    }

    private void h() {
        try {
            bz a2 = this.n.a("000001");
            if (a2 != null) {
                this.n.c("000001");
                a2.c("100000");
                this.n.a(a2);
            }
        } catch (Throwable th) {
            ic.c(th, "OfflineDownloadManager", "changeBadCase");
        }
    }

    private void h(String str) throws JSONException {
        List<OfflineMapProvince> a2 = cm.a(str, this.i.getApplicationContext());
        if (a2 == null || a2.size() == 0 || this.f == null) {
            return;
        }
        this.f.a(a2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public bo i(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }
        synchronized (this.c) {
            for (bo boVar : this.c) {
                if (str.equals(boVar.getCity()) || str.equals(boVar.getPinyin())) {
                    return boVar;
                }
            }
            return null;
        }
    }

    private void i() {
        if ("".equals(fr.c(this.i))) {
            return;
        }
        File file = new File(fr.c(this.i) + "offlinemapv4.png");
        String a2 = !file.exists() ? cm.a(this.i, "offlinemapv4.png") : cm.c(file);
        if (a2 != null) {
            try {
                h(a2);
            } catch (JSONException e) {
                if (file.exists()) {
                    file.delete();
                }
                ic.c(e, "MapDownloadManager", "paseJson io");
                e.printStackTrace();
            }
        }
    }

    private bo j(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }
        synchronized (this.c) {
            for (bo boVar : this.c) {
                if (str.equals(boVar.getCode())) {
                    return boVar;
                }
            }
            return null;
        }
    }

    private void j() {
        Iterator<bz> it = this.n.a().iterator();
        while (it.hasNext()) {
            bz next = it.next();
            if (next != null && next.d() != null && next.f().length() >= 1) {
                if (next.l != 4 && next.l != 7 && next.l >= 0) {
                    next.l = 3;
                }
                bo i = i(next.d());
                if (i != null) {
                    String e = next.e();
                    if (e == null || !a(d, e)) {
                        i.a(next.l);
                        i.setCompleteCode(next.h());
                    } else {
                        i.a(7);
                    }
                    if (next.e().length() > 0) {
                        i.setVersion(next.e());
                    }
                    List<String> b2 = this.n.b(next.f());
                    StringBuffer stringBuffer = new StringBuffer();
                    Iterator<String> it2 = b2.iterator();
                    while (it2.hasNext()) {
                        stringBuffer.append(it2.next());
                        stringBuffer.append(";");
                    }
                    i.a(stringBuffer.toString());
                    if (this.f != null) {
                        this.f.a(i);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() throws AMapException {
        if (!fr.d(this.i)) {
            throw new AMapException(AMapException.ERROR_CONNECTION);
        }
    }

    public void a() {
        this.n = ce.a(this.i.getApplicationContext());
        h();
        this.e = new b(this.i.getMainLooper());
        this.f = new bt(this.i, this.e);
        this.m = by.a(1);
        g(fr.c(this.i));
        try {
            i();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        synchronized (this.c) {
            Iterator<OfflineMapProvince> it = this.f.a().iterator();
            while (it.hasNext()) {
                Iterator<OfflineMapCity> it2 = it.next().getCityList().iterator();
                while (it2.hasNext()) {
                    OfflineMapCity next = it2.next();
                    if (next != null) {
                        this.c.add(new bo(this.i, next));
                    }
                }
            }
        }
        this.h = new bs(this.i);
        this.h.start();
    }

    public void a(bo boVar) {
        a(boVar, false);
    }

    public void a(a aVar) {
        this.l = aVar;
    }

    public void a(final String str) {
        try {
            if (str == null) {
                if (this.l != null) {
                    this.l.b(null);
                }
            } else {
                if (this.o == null) {
                    this.o = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue(), new fe("AMapOfflineCheckUpdate"), new ThreadPoolExecutor.AbortPolicy());
                }
                this.o.execute(new Runnable() { // from class: com.amap.api.mapcore.util.bp.1
                    @Override // java.lang.Runnable
                    public void run() {
                        bo i = bp.this.i(str);
                        if (i != null) {
                            try {
                                if (!i.c().equals(i.c) && !i.c().equals(i.e)) {
                                    String pinyin = i.getPinyin();
                                    if (pinyin.length() > 0) {
                                        String d2 = bp.this.n.d(pinyin);
                                        if (d2 == null) {
                                            d2 = i.getVersion();
                                        }
                                        if (bp.d.length() > 0 && d2 != null && bp.this.a(bp.d, d2)) {
                                            i.j();
                                        }
                                    }
                                }
                                if (bp.this.l != null) {
                                    synchronized (bp.this) {
                                        try {
                                            bp.this.l.b(i);
                                        } catch (Throwable th) {
                                            ic.c(th, "OfflineDownloadManager", "checkUpdatefinally");
                                        }
                                    }
                                    return;
                                }
                                return;
                            } catch (Exception unused) {
                                if (bp.this.l != null) {
                                    synchronized (bp.this) {
                                        try {
                                            bp.this.l.b(i);
                                        } catch (Throwable th2) {
                                            ic.c(th2, "OfflineDownloadManager", "checkUpdatefinally");
                                        }
                                        return;
                                    }
                                }
                                return;
                            } catch (Throwable th3) {
                                if (bp.this.l != null) {
                                    synchronized (bp.this) {
                                        try {
                                            bp.this.l.b(i);
                                        } catch (Throwable th4) {
                                            ic.c(th4, "OfflineDownloadManager", "checkUpdatefinally");
                                        }
                                    }
                                }
                                throw th3;
                            }
                        }
                        bp.this.k();
                        bq c = new br(bp.this.i, bp.d).c();
                        if (bp.this.l != null) {
                            if (c == null) {
                                if (bp.this.l != null) {
                                    synchronized (bp.this) {
                                        try {
                                            bp.this.l.b(i);
                                        } catch (Throwable th5) {
                                            ic.c(th5, "OfflineDownloadManager", "checkUpdatefinally");
                                        }
                                    }
                                    return;
                                }
                                return;
                            }
                            if (c.a()) {
                                bp.this.b();
                            }
                        }
                        if (bp.this.l != null) {
                            synchronized (bp.this) {
                                try {
                                    bp.this.l.b(i);
                                } catch (Throwable th6) {
                                    ic.c(th6, "OfflineDownloadManager", "checkUpdatefinally");
                                }
                            }
                        }
                    }
                });
            }
        } catch (Throwable th) {
            ic.c(th, "OfflineDownloadManager", "checkUpdate");
        }
    }

    public void a(ArrayList<bz> arrayList) {
        j();
        if (this.l != null) {
            try {
                this.l.a();
            } catch (Throwable th) {
                ic.c(th, "OfflineDownloadManager", "verifyCallBack");
            }
        }
    }

    protected void b() throws AMapException {
        if (this.f == null) {
            return;
        }
        bw bwVar = new bw(this.i, "");
        bwVar.a(this.i);
        List<OfflineMapProvince> c = bwVar.c();
        if (this.c != null) {
            this.f.a(c);
        }
        if (this.c != null) {
            synchronized (this.c) {
                Iterator<OfflineMapProvince> it = this.f.a().iterator();
                while (it.hasNext()) {
                    Iterator<OfflineMapCity> it2 = it.next().getCityList().iterator();
                    while (it2.hasNext()) {
                        OfflineMapCity next = it2.next();
                        for (bo boVar : this.c) {
                            if (next.getPinyin().equals(boVar.getPinyin())) {
                                String version = boVar.getVersion();
                                if (boVar.getState() == 4 && d.length() > 0 && a(d, version)) {
                                    boVar.j();
                                    boVar.setUrl(next.getUrl());
                                    boVar.t();
                                } else {
                                    boVar.setCity(next.getCity());
                                    boVar.setUrl(next.getUrl());
                                    boVar.t();
                                    boVar.setAdcode(next.getAdcode());
                                    boVar.setVersion(next.getVersion());
                                    boVar.setSize(next.getSize());
                                    boVar.setCode(next.getCode());
                                    boVar.setJianpin(next.getJianpin());
                                    boVar.setPinyin(next.getPinyin());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void b(bo boVar) {
        try {
            if (this.m != null) {
                this.m.a(boVar, this.i, null);
            }
        } catch (hc e) {
            e.printStackTrace();
        }
    }

    public boolean b(String str) {
        return i(str) != null;
    }

    public void c() {
        synchronized (this.c) {
            for (bo boVar : this.c) {
                if (boVar.c().equals(boVar.c) || boVar.c().equals(boVar.b)) {
                    d(boVar);
                    boVar.g();
                }
            }
        }
    }

    public void c(bo boVar) {
        if (this.f != null) {
            this.f.a(boVar);
        }
        if (this.e != null) {
            Message obtainMessage = this.e.obtainMessage();
            obtainMessage.obj = boVar;
            this.e.sendMessage(obtainMessage);
        }
    }

    public void c(String str) {
        bo i = i(str);
        if (i != null) {
            d(i);
            a(i, true);
        } else if (this.l != null) {
            try {
                this.l.c(i);
            } catch (Throwable th) {
                ic.c(th, "OfflineDownloadManager", "remove");
            }
        }
    }

    public void d() {
        synchronized (this.c) {
            Iterator<bo> it = this.c.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                bo next = it.next();
                if (next.c().equals(next.c)) {
                    next.g();
                    break;
                }
            }
        }
    }

    public void d(bo boVar) {
        if (this.m != null) {
            this.m.a(boVar);
        }
    }

    public void d(String str) throws AMapException {
        bo i = i(str);
        if (str == null || str.length() < 1 || i == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        f(i);
    }

    public void e() {
        if (this.o != null && !this.o.isShutdown()) {
            this.o.shutdownNow();
        }
        if (this.q != null && !this.q.isShutdown()) {
            this.q.shutdownNow();
        }
        if (this.h != null) {
            if (this.h.isAlive()) {
                this.h.interrupt();
            }
            this.h = null;
        }
        if (this.e != null) {
            this.e.removeCallbacksAndMessages(null);
            this.e = null;
        }
        if (this.m != null) {
            this.m.b();
        }
        if (this.f != null) {
            this.f.g();
        }
        f();
        this.j = true;
        g();
    }

    public void e(bo boVar) {
        if (this.m != null) {
            this.m.b(boVar);
        }
    }

    public void e(String str) throws AMapException {
        bo j = j(str);
        if (j == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        f(j);
    }

    public String f(String str) {
        bo i;
        return (str == null || (i = i(str)) == null) ? "" : i.getAdcode();
    }

    public void g() {
        synchronized (this) {
            this.l = null;
        }
    }
}
