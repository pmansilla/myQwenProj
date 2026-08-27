package com.amap.openapi;

import android.util.Log;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.security.Core;

/* compiled from: OfflineLocator.java */
/* loaded from: classes.dex */
class bq {
    private static AmapLoc a(AmapLoc amapLoc, bu buVar, int i) {
        String str;
        if (buVar.e.length() <= 0) {
            return null;
        }
        String sb = buVar.e.toString();
        if (amapLoc == null) {
            str = null;
        } else {
            str = amapLoc.getLon() + "," + amapLoc.getLat();
        }
        try {
            String gwl = Core.gwl(sb, buVar.a, i, str);
            if (gwl == null) {
                return null;
            }
            String[] split = gwl.split(",");
            AmapLoc amapLoc2 = new AmapLoc();
            amapLoc2.setTime(System.currentTimeMillis());
            amapLoc2.setCoord(0);
            amapLoc2.setType(AmapLoc.TYPE_OFFLINE_WIFI);
            amapLoc2.setLon(Double.parseDouble(split[0]));
            amapLoc2.setLat(Double.parseDouble(split[1]));
            amapLoc2.setAccuracy(Integer.parseInt(split[2]));
            return amapLoc2;
        } catch (Throwable th) {
            ALLog.trace("@_18_2_@", "@_18_2_2_@" + Log.getStackTraceString(th));
            return null;
        }
    }

    private static AmapLoc a(bs bsVar) {
        if (!bsVar.a || bsVar.e <= 60) {
            return null;
        }
        try {
            String gcl = Core.gcl(bsVar.c, bsVar.b, bsVar.d);
            if (gcl == null) {
                return null;
            }
            String[] split = gcl.split(",");
            AmapLoc amapLoc = new AmapLoc();
            amapLoc.setTime(System.currentTimeMillis());
            amapLoc.setCoord(0);
            amapLoc.setType(AmapLoc.TYPE_OFFLINE_CELL);
            amapLoc.setLon(Double.parseDouble(split[0]));
            amapLoc.setLat(Double.parseDouble(split[1]));
            amapLoc.setAccuracy(Integer.parseInt(split[2]));
            return amapLoc;
        } catch (Throwable th) {
            ALLog.trace("@_18_2_@", "@_18_2_1_@" + Log.getStackTraceString(th));
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AmapLoc a(bs bsVar, bu buVar, int i) {
        AmapLoc a = a(bsVar);
        AmapLoc a2 = a(a, buVar, i);
        bsVar.i = a;
        buVar.f = a2;
        if (a2 != null) {
            return a2;
        }
        if (a != null) {
            return a;
        }
        return null;
    }
}
