package com.amap.api.mapcore.util;

import android.text.TextUtils;
import java.net.Proxy;
import java.util.Map;

/* compiled from: Request.java */
/* loaded from: classes.dex */
public abstract class ix {
    int a = 20000;
    int b = 20000;
    Proxy c = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public String b() {
        byte[] entityBytes = getEntityBytes();
        if (entityBytes == null || entityBytes.length == 0) {
            return getURL();
        }
        Map<String, String> params = getParams();
        if (params == null) {
            return getURL();
        }
        String a = iv.a(params);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getURL());
        stringBuffer.append("?");
        stringBuffer.append(a);
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] c() {
        byte[] entityBytes = getEntityBytes();
        if (entityBytes != null && entityBytes.length != 0) {
            return entityBytes;
        }
        String a = iv.a(getParams());
        return !TextUtils.isEmpty(a) ? hp.a(a) : entityBytes;
    }

    public byte[] getEntityBytes() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getIPDNSName() {
        return "";
    }

    public abstract Map<String, String> getParams();

    public abstract Map<String, String> getRequestHead();

    public abstract String getURL();

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isIPRequest() {
        return !TextUtils.isEmpty(getIPDNSName());
    }

    public boolean isIgnoreGZip() {
        return false;
    }

    public final void setConnectionTimeout(int i) {
        this.a = i;
    }

    public final void setProxy(Proxy proxy) {
        this.c = proxy;
    }

    public final void setSoTimeout(int i) {
        this.b = i;
    }
}
