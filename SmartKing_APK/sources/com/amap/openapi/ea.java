package com.amap.openapi;

import com.amap.location.common.log.ALLog;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.security.Core;
import java.util.HashMap;

/* compiled from: NetHelper.java */
/* loaded from: classes.dex */
public class ea {
    public static boolean a(IHttpClient iHttpClient, String str, byte[] bArr, int i) {
        try {
            byte[] a = com.amap.location.common.util.d.a(bArr);
            if (a != null && a.length != 0) {
                byte[] xxt = Core.xxt(a, 1);
                if (xxt != null && xxt.length != 0) {
                    if (iHttpClient != null) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("ext", "120");
                        HttpRequest httpRequest = new HttpRequest();
                        httpRequest.url = str;
                        httpRequest.headers = hashMap;
                        httpRequest.body = xxt;
                        httpRequest.timeout = i;
                        HttpResponse post = iHttpClient.post(httpRequest);
                        if (post != null && post.body != null) {
                            int i2 = post.statusCode;
                            String str2 = new String(post.body, "UTF-8");
                            if (i2 == 200 && "true".equals(str2)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
                ALLog.trace("HttpRequestHelper", "xxt is null");
                return false;
            }
            ALLog.trace("HttpRequestHelper", "gzip is null");
        } catch (Throwable unused) {
        }
        return false;
    }
}
