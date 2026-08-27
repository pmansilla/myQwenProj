package com.mob.elp.d;

import android.app.PendingIntent;
import android.content.Intent;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.mob.MobSDK;

/* compiled from: AppHelper.java */
/* loaded from: classes.dex */
public class a {
    public static void a(Intent intent, int i) {
        try {
            d.a().a("elp start specific Activity intent uri: " + intent.toUri(1));
            PendingIntent.getActivity(MobSDK.getContext(), i, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH).send();
        } catch (PendingIntent.CanceledException e) {
            d.a().a("elp start specific Activity error: " + e);
        }
    }
}
