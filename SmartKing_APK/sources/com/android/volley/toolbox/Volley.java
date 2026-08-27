package com.android.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import com.android.volley.RequestQueue;
import java.io.File;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class Volley {
    private static final String DEFAULT_CACHE_DIR = "volley";

    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }

    public static RequestQueue newRequestQueue(Context context, HttpStack httpStack) {
        String str;
        File file = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        try {
            String packageName = context.getPackageName();
            str = String.valueOf(packageName) + FileUriModel.SCHEME + context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException unused) {
            str = "volley/0";
        }
        if (httpStack == null) {
            httpStack = Build.VERSION.SDK_INT >= 9 ? new HurlStack() : new HttpClientStack(AndroidHttpClient.newInstance(str));
        }
        RequestQueue requestQueue = new RequestQueue(new DiskBasedCache(file), new BasicNetwork(httpStack));
        requestQueue.start();
        return requestQueue;
    }
}
