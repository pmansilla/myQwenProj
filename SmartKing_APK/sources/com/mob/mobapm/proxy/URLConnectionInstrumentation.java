package com.mob.mobapm.proxy;

import com.mob.tools.proguard.ClassKeeper;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes.dex */
public final class URLConnectionInstrumentation implements ClassKeeper {
    private URLConnectionInstrumentation() {
    }

    public static URLConnection openConnection(URLConnection uRLConnection) {
        if (com.mob.mobapm.core.c.e) {
            if (uRLConnection instanceof HttpsURLConnection) {
                return new b((HttpsURLConnection) uRLConnection);
            }
            if (uRLConnection instanceof HttpURLConnection) {
                return new a((HttpURLConnection) uRLConnection);
            }
        }
        return uRLConnection;
    }

    public static URLConnection openConnectionWithProxy(URLConnection uRLConnection) {
        if (com.mob.mobapm.core.c.e) {
            if (uRLConnection instanceof HttpsURLConnection) {
                return new b((HttpsURLConnection) uRLConnection);
            }
            if (uRLConnection instanceof HttpURLConnection) {
                return new a((HttpURLConnection) uRLConnection);
            }
        }
        return uRLConnection;
    }
}
