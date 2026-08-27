package com.amap.api.mapcore.util;

import com.amap.api.maps.AMapException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/* compiled from: BaseNetManager.java */
/* loaded from: classes.dex */
public class is {
    public static int a = 0;
    public static String b = "";
    private static is c;

    /* compiled from: BaseNetManager.java */
    /* loaded from: classes.dex */
    public interface a {
        URLConnection a(Proxy proxy, URL url);
    }

    public static is a() {
        if (c == null) {
            c = new is();
        }
        return c;
    }

    public iz a(ix ixVar, boolean z) throws hc {
        try {
            c(ixVar);
            return new iv(ixVar.a, ixVar.b, ixVar.c == null ? null : ixVar.c, z).a(ixVar.b(), ixVar.isIPRequest(), ixVar.getIPDNSName(), ixVar.getRequestHead(), ixVar.c(), ixVar.isIgnoreGZip());
        } catch (hc e) {
            throw e;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new hc(AMapException.ERROR_UNKNOWN);
        }
    }

    public byte[] a(ix ixVar) throws hc {
        try {
            iz a2 = a(ixVar, true);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (hc e) {
            throw e;
        }
    }

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
            hz.a(th, "bm", "msp");
            throw new hc(AMapException.ERROR_UNKNOWN);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void c(ix ixVar) throws hc {
        if (ixVar == null) {
            throw new hc("requeust is null");
        }
        if (ixVar.getURL() == null || "".equals(ixVar.getURL())) {
            throw new hc("request url is empty");
        }
    }
}
