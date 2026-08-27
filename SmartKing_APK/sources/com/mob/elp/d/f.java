package com.mob.elp.d;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mob.MobSDK;

/* compiled from: NotificationColorUtils.java */
/* loaded from: classes.dex */
public class f {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: NotificationColorUtils.java */
    /* loaded from: classes.dex */
    public static class a implements b {
        final /* synthetic */ int[] a;

        a(int[] iArr) {
            this.a = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NotificationColorUtils.java */
    /* loaded from: classes.dex */
    public interface b {
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0034 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0035 A[Catch: all -> 0x0059, TryCatch #0 {all -> 0x0059, blocks: (B:3:0x0001, B:5:0x0011, B:6:0x001a, B:8:0x001f, B:13:0x0035, B:15:0x004e, B:19:0x0023, B:21:0x0029, B:22:0x0016), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int a(android.content.Context r5) {
        /*
            r0 = 0
            android.app.Notification$Builder r1 = new android.app.Notification$Builder     // Catch: java.lang.Throwable -> L59
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L59
            java.lang.String r2 = "Notification Title"
            r1.setContentTitle(r2)     // Catch: java.lang.Throwable -> L59
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L59
            r3 = 16
            if (r2 < r3) goto L16
            android.app.Notification r1 = r1.build()     // Catch: java.lang.Throwable -> L59
            goto L1a
        L16:
            android.app.Notification r1 = r1.getNotification()     // Catch: java.lang.Throwable -> L59
        L1a:
            r2 = 0
            android.widget.RemoteViews r3 = r1.contentView     // Catch: java.lang.Throwable -> L59
            if (r3 == 0) goto L23
            android.widget.RemoteViews r1 = r1.contentView     // Catch: java.lang.Throwable -> L59
        L21:
            r2 = r1
            goto L32
        L23:
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L59
            r4 = 24
            if (r3 < r4) goto L32
            android.app.Notification$Builder r1 = android.app.Notification.Builder.recoverBuilder(r5, r1)     // Catch: java.lang.Throwable -> L59
            android.widget.RemoteViews r1 = r1.createContentView()     // Catch: java.lang.Throwable -> L59
            goto L21
        L32:
            if (r2 != 0) goto L35
            return r0
        L35:
            android.widget.FrameLayout r1 = new android.widget.FrameLayout     // Catch: java.lang.Throwable -> L59
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L59
            android.view.View r5 = r2.apply(r5, r1)     // Catch: java.lang.Throwable -> L59
            android.view.ViewGroup r5 = (android.view.ViewGroup) r5     // Catch: java.lang.Throwable -> L59
            r1 = 16908310(0x1020016, float:2.387729E-38)
            android.view.View r1 = r5.findViewById(r1)     // Catch: java.lang.Throwable -> L59
            android.widget.TextView r1 = (android.widget.TextView) r1     // Catch: java.lang.Throwable -> L59
            r2 = 1
            int[] r2 = new int[r2]     // Catch: java.lang.Throwable -> L59
            if (r1 != 0) goto L72
            com.mob.elp.d.f$a r1 = new com.mob.elp.d.f$a     // Catch: java.lang.Throwable -> L59
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L59
            a(r5, r1)     // Catch: java.lang.Throwable -> L59
            r5 = r2[r0]     // Catch: java.lang.Throwable -> L59
            return r5
        L59:
            r5 = move-exception
            com.mob.elp.d.d r1 = com.mob.elp.d.d.a()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "MobPush get notification color failed, error:"
            r2.append(r3)
            r2.append(r5)
            java.lang.String r5 = r2.toString()
            r1.a(r5)
        L72:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.elp.d.f.a(android.content.Context):int");
    }

    private static void a(View view, b bVar) {
        if (view != null) {
            a aVar = (a) bVar;
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if ("Notification Title".equals(textView.getText().toString())) {
                    aVar.a[0] = textView.getCurrentTextColor();
                }
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    a(viewGroup.getChildAt(i), bVar);
                }
            }
        }
    }

    public static boolean a() {
        try {
            int a2 = a(MobSDK.getContext()) | (-16777216);
            int red = Color.red(-16777216) - Color.red(a2);
            int green = Color.green(-16777216) - Color.green(a2);
            int blue = Color.blue(-16777216) - Color.blue(a2);
            return !(Math.sqrt((double) (((red * red) + (green * green)) + (blue * blue))) < 180.0d);
        } catch (Throwable th) {
            d.a().a("MobPush get notification theme failed, error:" + th);
            return false;
        }
    }
}
