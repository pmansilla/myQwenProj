package com.amap.openapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import com.amap.location.security.Core;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import no.nordicsemi.android.dfu.DfuBaseService;
import no.nordicsemi.android.support.v18.scanner.BuildConfig;

/* compiled from: OfflineProtocol.java */
/* loaded from: classes.dex */
public class cd {
    private Context a;
    private OfflineConfig b;
    private IOfflineCloudConfig c;
    private cg d = new cg();
    private by e;
    private boolean f;
    private a g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OfflineProtocol.java */
    /* loaded from: classes.dex */
    public interface a {
        void a();
    }

    public cd(Context context, OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig, a aVar) {
        this.a = context;
        this.b = offlineConfig;
        this.c = iOfflineCloudConfig;
        this.g = aVar;
    }

    private void a(ce ceVar) {
        this.f = false;
        if (ceVar == null) {
            return;
        }
        if (ceVar.a == 1) {
            cp.c(this.a);
        } else if (this.g != null) {
            this.g.a();
        }
    }

    private void a(ce ceVar, HttpResponse httpResponse) {
        if (httpResponse == null) {
            ALLog.trace("@_18_6_@", "@_18_6_12_@");
            a(ceVar);
            return;
        }
        List<String> list = httpResponse.headers.get("code");
        String str = list != null ? list.get(list.size() - 1) : null;
        ALLog.trace("@_18_6_@", "@_18_6_13_@" + str);
        if (ceVar == null) {
            this.f = false;
            ALLog.trace("@_18_6_@", "@_18_6_11_@");
            return;
        }
        if (!"260".equals(str)) {
            a(ceVar);
            return;
        }
        if (ceVar.a == 1) {
            cp.c(this.a);
            if (ceVar.b == 0) {
                cp.d(this.a);
            }
        }
        if (ceVar.a == 0) {
            cp.b(this.a);
        }
        boolean b = b(ceVar, httpResponse);
        this.f = false;
        if ((b || ceVar.a == 0) && this.g != null) {
            this.g.a();
        }
    }

    private ce b(byte b, int i) throws Exception {
        List<String> list;
        List<Long> list2;
        byte[] xxt;
        byte[] a2;
        if (this.e == null) {
            this.e = by.a(this.a);
        }
        if (b == 1) {
            int trainingThreshold = this.c.getTrainingThreshold();
            int maxNumPerRequest = this.c.getMaxNumPerRequest();
            List<Long> b2 = this.e.b(trainingThreshold, maxNumPerRequest);
            int size = b2.size();
            List<String> a3 = this.e.a(trainingThreshold, size < maxNumPerRequest ? maxNumPerRequest - size : (maxNumPerRequest * 2) / 10);
            int size2 = a3.size();
            if (size2 > 0 && size == maxNumPerRequest) {
                b2 = b2.subList(0, maxNumPerRequest - size2);
            }
            if (b2.size() + a3.size() < 5) {
                ALLog.trace("@_18_6_@", "@_18_6_6_@");
                return null;
            }
            ALLog.trace("@_18_6_@", "@_18_6_7_@(" + a3.size() + "," + b2.size() + SQLBuilder.PARENTHESES_RIGHT);
            list = a3;
            list2 = b2;
        } else {
            ALLog.trace("@_18_6_@", "@_18_6_8_@");
            list = null;
            list2 = null;
        }
        ce ceVar = new ce(b, list2, list);
        ceVar.b = i;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", DfuBaseService.MIME_TYPE_OCTET_STREAM);
        hashMap.put("Accept-Encoding", "gzip");
        hashMap.put("gzipped", AmapLoc.RESULT_TYPE_WIFI_ONLY);
        hashMap.put("v", BuildConfig.VERSION_NAME);
        hashMap.put("et", "110");
        ceVar.e = hashMap;
        byte[] a4 = this.d.a(b, BuildConfig.VERSION_NAME, this.b.productId, this.b.packageName, this.b.productVersion, (byte) com.amap.location.common.a.d(), this.b.imei, this.b.imsi, this.b.uuid, com.amap.location.common.a.c(this.a), com.amap.location.common.a.e(this.a), com.amap.location.common.a.c(), com.amap.location.common.a.b(), this.b.license, this.b.mapKey, list2, list);
        if (a4 == null || (xxt = Core.xxt(a4, 1)) == null || xxt.length == 0 || (a2 = com.amap.location.common.util.d.a(xxt)) == null || a2.length == 0) {
            return null;
        }
        ceVar.f = a2;
        return ceVar;
    }

    private boolean b(ce ceVar, HttpResponse httpResponse) {
        ck a2 = a(httpResponse);
        if (a2 == null) {
            ALLog.trace("@_18_6_@", "@_18_6_10_@");
            return false;
        }
        if (this.e == null) {
            this.e = by.a(this.a);
        }
        if (ceVar.a == 0) {
            this.e.a(a2);
            return true;
        }
        this.e.a(a2, ceVar.c, ceVar.d, this.a);
        return true;
    }

    public ck a(HttpResponse httpResponse) {
        try {
            List<String> list = httpResponse.headers.get("Content-Encoding");
            String str = (list == null || list.size() <= 0) ? null : list.get(0);
            byte[] bArr = httpResponse.body;
            if (bArr != null && bArr.length > 0) {
                if ("gzip".equals(str)) {
                    bArr = com.amap.location.common.util.d.b(bArr);
                }
                return ck.a(ByteBuffer.wrap(bArr));
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public void a(byte b, int i) {
        this.f = true;
        try {
            ce b2 = b(b, i);
            if (b2 == null || this.b.httpClient == null) {
                this.f = false;
                return;
            }
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.url = OfflineConfig.sUseTestNet ? "http://aps.testing.amap.com/LoadOfflineData/repeatData" : "http://offline.aps.amap.com/LoadOfflineData/repeatData";
            httpRequest.headers = b2.e;
            httpRequest.body = b2.f;
            a(b2, this.b.httpClient.post(httpRequest));
        } catch (Throwable th) {
            this.f = false;
            ALLog.trace("@_18_6_@", "@_18_6_2_@" + Log.getStackTraceString(th));
        }
    }

    public void a(@NonNull OfflineConfig offlineConfig) {
        this.b = offlineConfig;
    }

    public boolean a() {
        ALLog.trace("@_18_6_@", "@_18_6_5_@" + this.f);
        return this.f;
    }
}
