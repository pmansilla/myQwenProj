package cn.sharesdk.framework.utils;

import android.content.Context;
import com.mob.tools.utils.ResHelper;

/* compiled from: SizeHelper.java */
/* loaded from: classes.dex */
public class h {
    public static float a = 1.5f;
    public static int b = 540;
    private static Context c;

    public static int a(int i) {
        return ResHelper.designToDevice(c, a, i);
    }

    public static void a(Context context) {
        if (c == null || c != context.getApplicationContext()) {
            c = context;
        }
    }

    public static int b(int i) {
        return ResHelper.designToDevice(c, b, i);
    }
}
