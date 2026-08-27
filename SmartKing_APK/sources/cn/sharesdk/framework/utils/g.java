package cn.sharesdk.framework.utils;

import com.mob.commons.InternationalDomain;
import java.util.HashMap;

/* compiled from: ShareSDKDomain.java */
/* loaded from: classes.dex */
public class g {
    private HashMap<String, String> a = new HashMap<>();

    public g() {
        this.a.put("jp", "Japan");
        this.a.put("us", "United States of America");
    }

    public boolean a(InternationalDomain internationalDomain) {
        return this.a.containsKey(internationalDomain.getDomain());
    }
}
