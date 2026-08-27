package com.autonavi.httpdns;

import android.content.Context;
import com.loc.dc;
import com.loc.dd;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class HttpDnsManager {
    dd a;
    private ArrayList<String> b = new ArrayList<>(12);

    public HttpDnsManager(Context context) {
        this.a = null;
        this.a = dc.a(context, "154081");
        this.b.add("apilocatesrc.amap.com");
        this.a.a(this.b);
        this.a.a();
    }

    public String getIpByHostAsync(String str) {
        return this.a.a(str);
    }

    public String[] getIpsByHostAsync(String str) {
        if (!this.b.contains(str)) {
            this.b.add(str);
            this.a.a(this.b);
        }
        return this.a.b(str);
    }
}
