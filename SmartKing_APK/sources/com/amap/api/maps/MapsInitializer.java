package com.amap.api.maps;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.amap.api.mapcore.util.ai;
import com.amap.api.mapcore.util.he;
import com.amap.api.mapcore.util.hk;
import com.amap.api.mapcore.util.is;

/* loaded from: classes.dex */
public final class MapsInitializer {
    public static final int HTTP = 1;
    public static final int HTTPS = 2;
    private static boolean a = true;
    private static boolean b = true;
    private static boolean c = false;
    private static boolean d = false;
    private static int e = 1;
    public static String sdcardDir = "";

    public static void closeTileOverlay(boolean z) {
        d = z;
    }

    public static boolean getNetWorkEnable() {
        return a;
    }

    public static int getProtocol() {
        return e;
    }

    public static String getVersion() {
        return "6.9.3";
    }

    public static void initialize(Context context) throws RemoteException {
        if (context != null) {
            ai.a = context.getApplicationContext();
        } else {
            Log.w("MapsInitializer", "the context is null");
        }
    }

    public static boolean isDownloadCoordinateConvertLibrary() {
        return b;
    }

    public static boolean isLoadWorldGridMap() {
        return c;
    }

    public static boolean isTileOverlayClosed() {
        return d;
    }

    public static void loadWorldGridMap(boolean z) {
        c = z;
    }

    public static void setApiKey(String str) {
        if (str == null || str.trim().length() <= 0) {
            return;
        }
        he.a(ai.a, str);
    }

    public static void setBuildingHeight(int i) {
    }

    public static void setDownloadCoordinateConvertLibrary(boolean z) {
        b = z;
    }

    public static void setHost(String str) {
        if (TextUtils.isEmpty(str)) {
            is.a = -1;
            is.b = "";
        } else {
            is.a = 1;
            is.b = str;
        }
    }

    public static void setNetWorkEnable(boolean z) {
        a = z;
    }

    public static void setProtocol(int i) {
        e = i;
        hk.a().a(e == 2);
    }
}
