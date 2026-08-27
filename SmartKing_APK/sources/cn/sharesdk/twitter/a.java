package cn.sharesdk.twitter;

import android.content.Context;
import android.content.Intent;

/* compiled from: IntentUtils.java */
/* loaded from: classes.dex */
public class a {
    public static boolean a(Context context, Intent intent) {
        return !context.getPackageManager().queryIntentActivities(intent, 0).isEmpty();
    }
}
