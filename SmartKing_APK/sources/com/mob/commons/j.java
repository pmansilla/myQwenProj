package com.mob.commons;

import android.net.Uri;
import android.os.Build;
import android.security.NetworkSecurityPolicy;
import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ReflectHelper;
import me.panpf.sketch.uri.HttpUriModel;
import me.panpf.sketch.uri.HttpsUriModel;

/* compiled from: ServerConfig.java */
/* loaded from: classes.dex */
public class j {
    public static String a() {
        String str = "api.fc.mob.com";
        try {
            boolean isPackageInstalled = DeviceHelper.getInstance(MobSDK.getContext()).isPackageInstalled(k.a(17));
            if (!MobSDK.checkV6() && !isPackageInstalled) {
                str = "m.data.mob.com";
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return b(str);
    }

    public static String a(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (str.startsWith(HttpUriModel.SCHEME)) {
            str = str.replace(HttpUriModel.SCHEME, "");
        }
        if (str.startsWith(HttpsUriModel.SCHEME)) {
            str = str.replace(HttpsUriModel.SCHEME, "");
        }
        switch (MobSDK.getDomain()) {
            case JP:
                str2 = "jp";
                break;
            case US:
                str2 = "us";
                break;
            default:
                str2 = "";
                break;
        }
        if (TextUtils.isEmpty(str2)) {
            return c(HttpUriModel.SCHEME + str);
        }
        if (str.startsWith(str2 + ".")) {
            return c(HttpUriModel.SCHEME + str);
        }
        return c(HttpUriModel.SCHEME + str2 + "." + str);
    }

    public static String b() {
        String str = "api.fd.mob.com";
        try {
            boolean isPackageInstalled = DeviceHelper.getInstance(MobSDK.getContext()).isPackageInstalled(k.a(17));
            if (!MobSDK.checkV6() && !isPackageInstalled) {
                str = "c.data.mob.com";
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return b(str);
    }

    public static String b(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (str.startsWith(HttpUriModel.SCHEME)) {
            str = str.replace(HttpUriModel.SCHEME, "");
        }
        if (str.startsWith(HttpsUriModel.SCHEME)) {
            str = str.replace(HttpsUriModel.SCHEME, "");
        }
        if (!MobSDK.checkV6()) {
            switch (MobSDK.getDomain()) {
                case JP:
                    str2 = "jp";
                    break;
                case US:
                    str2 = "us";
                    break;
                default:
                    str2 = "";
                    break;
            }
        } else {
            str2 = "v6";
        }
        if (TextUtils.isEmpty(str2)) {
            return c(HttpUriModel.SCHEME + str);
        }
        if (str.startsWith(str2 + ".")) {
            return c(HttpUriModel.SCHEME + str);
        }
        return c(HttpUriModel.SCHEME + str2 + "." + str);
    }

    public static String c(String str) {
        Uri parse;
        String scheme;
        String str2;
        String str3;
        try {
            if (TextUtils.isEmpty(str)) {
                return str;
            }
            boolean checkForceHttps = MobSDK.checkForceHttps();
            if (!checkForceHttps && (Build.VERSION.SDK_INT < 23 || NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted())) {
                return str;
            }
            String trim = str.trim();
            try {
                if (!trim.startsWith(HttpUriModel.SCHEME) || (parse = Uri.parse(trim.trim())) == null || (scheme = parse.getScheme()) == null || !scheme.equals("http")) {
                    return trim;
                }
                String host = parse.getHost();
                String path = parse.getPath();
                String query = parse.getQuery();
                if (host != null) {
                    int port = parse.getPort();
                    StringBuilder sb = new StringBuilder();
                    sb.append(host);
                    if (port > 0 && port != 80) {
                        str3 = ":" + port;
                        sb.append(str3);
                        host = sb.toString();
                        if (!checkForceHttps && Build.VERSION.SDK_INT >= 24 && ((Boolean) ReflectHelper.invokeInstanceMethod(NetworkSecurityPolicy.getInstance(), "isCleartextTrafficPermitted", host)).booleanValue()) {
                            return trim;
                        }
                    }
                    str3 = "";
                    sb.append(str3);
                    host = sb.toString();
                    if (!checkForceHttps) {
                        return trim;
                    }
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(HttpsUriModel.SCHEME);
                sb2.append(host);
                if (path == null) {
                    path = "";
                }
                sb2.append(path);
                if (query == null) {
                    str2 = "";
                } else {
                    str2 = "?" + query;
                }
                sb2.append(str2);
                return sb2.toString();
            } catch (Throwable th) {
                th = th;
                str = trim;
                MobLog.getInstance().d(th);
                return str;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
