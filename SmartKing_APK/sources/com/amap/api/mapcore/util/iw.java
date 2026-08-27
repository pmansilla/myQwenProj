package com.amap.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.api.maps.AMapException;
import java.util.Map;

/* compiled from: NetManger.java */
/* loaded from: classes.dex */
public class iw extends is {
    private static iw c;
    private jy d;
    private Handler e;

    /* compiled from: NetManger.java */
    /* renamed from: com.amap.api.mapcore.util.iw$1, reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass1 extends jz {
        final /* synthetic */ ix a;
        final /* synthetic */ iy b;
        final /* synthetic */ iw c;

        @Override // com.amap.api.mapcore.util.jz
        public void runTask() {
            try {
                this.c.a(this.c.c(this.a, false), this.b);
            } catch (hc e) {
                this.c.a(e, this.b);
            }
        }
    }

    /* compiled from: NetManger.java */
    /* loaded from: classes.dex */
    static class a extends Handler {
        public a() {
        }

        private a(Looper looper) {
            super(looper);
        }

        /* synthetic */ a(Looper looper, AnonymousClass1 anonymousClass1) {
            this(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                switch (message.what) {
                    case 0:
                        ((jb) message.obj).b.a();
                        break;
                    case 1:
                        jb jbVar = (jb) message.obj;
                        jbVar.b.a(jbVar.a);
                        break;
                    default:
                        return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private iw(boolean z, int i) {
        if (z) {
            try {
                this.d = jy.b(i);
            } catch (Throwable th) {
                ic.c(th, "NetManger", "NetManger1");
                th.printStackTrace();
                return;
            }
        }
        if (Looper.myLooper() == null) {
            this.e = new a(Looper.getMainLooper(), null);
        } else {
            this.e = new a();
        }
    }

    public static iw a(boolean z) {
        return a(z, 5);
    }

    private static synchronized iw a(boolean z, int i) {
        iw iwVar;
        synchronized (iw.class) {
            try {
                if (c == null) {
                    c = new iw(z, i);
                } else if (z && c.d == null) {
                    c.d = jy.b(i);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            iwVar = c;
        }
        return iwVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(hc hcVar, iy iyVar) {
        jb jbVar = new jb();
        jbVar.a = hcVar;
        jbVar.b = iyVar;
        Message obtain = Message.obtain();
        obtain.obj = jbVar;
        obtain.what = 1;
        this.e.sendMessage(obtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(iz izVar, iy iyVar) {
        iyVar.a(izVar.b, izVar.a);
        jb jbVar = new jb();
        jbVar.b = iyVar;
        Message obtain = Message.obtain();
        obtain.obj = jbVar;
        obtain.what = 0;
        this.e.sendMessage(obtain);
    }

    public static iw b() {
        return a(true, 5);
    }

    public Map<String, String> b(ix ixVar, boolean z) throws hc {
        try {
            c(ixVar);
            return new iv(ixVar.a, ixVar.b, ixVar.c == null ? null : ixVar.c, z).a(ixVar.getURL(), ixVar.isIPRequest(), ixVar.getIPDNSName(), ixVar.getRequestHead(), ixVar.getParams(), ixVar.isIgnoreGZip());
        } catch (hc e) {
            throw e;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new hc(AMapException.ERROR_UNKNOWN);
        }
    }

    @Override // com.amap.api.mapcore.util.is
    public byte[] b(ix ixVar) throws hc {
        try {
            iz a2 = a(ixVar, false);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (hc e) {
            throw e;
        } catch (Throwable th) {
            th.printStackTrace();
            ic.e().b(th, "NetManager", "makeSyncPostRequest");
            throw new hc(AMapException.ERROR_UNKNOWN);
        }
    }

    public iz c(ix ixVar, boolean z) throws hc {
        try {
            c(ixVar);
            return new iv(ixVar.a, ixVar.b, ixVar.c == null ? null : ixVar.c, z).b(ixVar.getURL(), ixVar.isIPRequest(), ixVar.getIPDNSName(), ixVar.getRequestHead(), ixVar.getParams(), ixVar.isIgnoreGZip());
        } catch (hc e) {
            throw e;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new hc(AMapException.ERROR_UNKNOWN);
        }
    }

    public byte[] d(ix ixVar) throws hc {
        try {
            iz c2 = c(ixVar, false);
            if (c2 != null) {
                return c2.a;
            }
            return null;
        } catch (hc e) {
            throw e;
        }
    }

    public byte[] e(ix ixVar) throws hc {
        try {
            iz c2 = c(ixVar, true);
            if (c2 != null) {
                return c2.a;
            }
            return null;
        } catch (hc e) {
            throw e;
        }
    }
}
