package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import java.util.List;
import kotlin.jvm.internal.ShortCompanionObject;

/* compiled from: GpsUtil.java */
/* loaded from: classes.dex */
public class ba {
    public static short a(@NonNull List<y> list, boolean z, List<ct> list2) {
        list.clear();
        short s = ShortCompanionObject.MAX_VALUE;
        if (list2 != null) {
            double d = 0.0d;
            int i = 0;
            for (ct ctVar : list2) {
                int b = ctVar.b();
                float d2 = ctVar.d();
                boolean a = ctVar.a();
                if (b > 1 && b <= 32) {
                    if (a && d2 > 10.0d) {
                        double c = ctVar.c();
                        Double.isNaN(c);
                        d += c;
                        i++;
                    }
                    if (z) {
                        y yVar = new y();
                        yVar.a = (byte) b;
                        yVar.b = (byte) Math.round(ctVar.c());
                        yVar.c = (byte) Math.round(d2);
                        yVar.d = (short) Math.round(ctVar.e());
                        yVar.e = a ? (byte) 1 : (byte) 0;
                        list.add(yVar);
                    }
                }
                if (i > 0) {
                    double d3 = i;
                    Double.isNaN(d3);
                    s = (short) Math.round(((float) (d / d3)) * 100.0f);
                }
            }
        }
        return s;
    }

    public static void a(@NonNull v vVar, @NonNull Location location, long j, long j2) {
        vVar.b = j;
        vVar.a = j2;
        vVar.c = (int) (location.getLongitude() * 1000000.0d);
        vVar.d = (int) (location.getLatitude() * 1000000.0d);
        vVar.e = (int) location.getAltitude();
        vVar.f = (int) location.getAccuracy();
        vVar.g = (int) location.getSpeed();
        vVar.h = (short) location.getBearing();
        Bundle extras = location.getExtras();
        vVar.i = (byte) 0;
        if (extras != null) {
            try {
                vVar.i = (byte) extras.getInt("satellites", 0);
            } catch (Exception unused) {
            }
        }
    }

    public static void a(@NonNull v vVar, short s, @NonNull Location location, long j, long j2) {
        vVar.j = s;
        a(vVar, location, j, j2);
    }

    public static boolean a(Context context, Location location) {
        if (Build.VERSION.SDK_INT >= 18) {
            if (!az.a(location)) {
                return false;
            }
        } else if (!Build.MODEL.equals("sdk") && !az.b(context)) {
            return false;
        }
        return true;
    }

    public static boolean a(Location location) {
        return location != null && "gps".equalsIgnoreCase(location.getProvider()) && location.getLatitude() > -90.0d && location.getLatitude() < 90.0d && location.getLongitude() > -180.0d && location.getLongitude() < 180.0d;
    }
}
