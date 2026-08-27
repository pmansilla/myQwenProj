package com.mob.elp.c;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.mob.MobSDK;
import com.mob.elp.PushMessage;
import com.mob.elp.a.f;
import com.mob.elp.d.c;
import com.mob.elp.d.d;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.Calendar;

/* compiled from: ELPNotification.java */
/* loaded from: classes.dex */
public class a {
    private static a g = null;
    private static boolean h = false;
    private NotificationManager a;
    private int b;
    private NotificationChannel c;
    private String d = "通知";
    private int e;
    private String f;

    /* compiled from: ELPNotification.java */
    /* renamed from: com.mob.elp.c.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class C0056a extends BroadcastReceiver {
        C0056a(a aVar) {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                try {
                    if ("com.mob.elp.intent.NOTIFICATION_OPENED".equals(intent.getAction())) {
                        f.a().a(context, (PushMessage) ResHelper.forceCast(intent.getExtras().getSerializable(NotificationCompat.CATEGORY_MESSAGE), null));
                    }
                } catch (Throwable th) {
                    d.a().a(th.toString());
                }
            }
        }
    }

    private a() {
        this.c = null;
        try {
            new Hashon();
            Context context = MobSDK.getContext();
            this.a = (NotificationManager) context.getSystemService("notification");
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(this.d, this.d, 4);
                this.c = notificationChannel;
                notificationChannel.enableLights(true);
                this.c.setLightColor(SupportMenu.CATEGORY_MASK);
                this.c.enableVibration(true);
            }
            try {
                this.e = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).icon;
            } catch (PackageManager.NameNotFoundException unused) {
                this.e = 0;
            }
            try {
                h = com.mob.elp.d.f.a();
            } catch (Throwable unused2) {
                h = false;
            }
            String b = c.b();
            this.f = c.a().c();
            b = b.contains(".") ? b.substring(0, b.indexOf(".")) : b;
            if (TextUtils.isEmpty(b)) {
                return;
            }
            try {
                Integer.parseInt(b);
            } catch (Throwable unused3) {
            }
        } catch (Throwable th) {
            d.a().a(th);
        }
    }

    public static synchronized a a() {
        a aVar;
        synchronized (a.class) {
            if (g == null) {
                g = new a();
            }
            aVar = g;
        }
        return aVar;
    }

    private boolean a(Context context) {
        try {
            DeviceHelper deviceHelper = DeviceHelper.getInstance(context);
            return ((Integer) ReflectHelper.invokeInstanceMethod(deviceHelper.getSystemServiceSafe("appops"), "checkOpNoThrow", Integer.valueOf(((Integer) ReflectHelper.getStaticField(ReflectHelper.importClass("android.app.AppOpsManager"), "OP_POST_NOTIFICATION")).intValue()), Integer.valueOf(context.getApplicationInfo().uid), deviceHelper.getPackageName())).intValue() == 0;
        } catch (Throwable th) {
            d.a().a(th);
            return true;
        }
    }

    public void a(PushMessage pushMessage, ArrayList<Bitmap> arrayList) {
        Notification.Builder builder;
        int layoutRes;
        RemoteViews remoteViews;
        boolean z;
        NotificationChannel notificationChannel;
        if (Build.VERSION.SDK_INT < 26 || (notificationChannel = this.c) == null) {
            builder = new Notification.Builder(MobSDK.getContext());
        } else {
            this.a.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(MobSDK.getContext(), this.d);
        }
        builder.setTicker(pushMessage.content);
        builder.setSmallIcon(this.e);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= 16) {
            builder.setPriority(1);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setColor(0);
        }
        Context context = MobSDK.getContext();
        if ("meizu".equalsIgnoreCase(this.f) || "xiaomi".equalsIgnoreCase(this.f)) {
            int i = pushMessage.unfold.showType;
            if (i == 1) {
                layoutRes = ResHelper.getLayoutRes(context, "elp_notify_window_no_padding");
            } else if (i == 2) {
                layoutRes = ResHelper.getLayoutRes(context, "elp_notify_card_no_padding");
            } else if (i == 3) {
                layoutRes = ResHelper.getLayoutRes(context, "elp_notify_nativ_no_padding");
            } else {
                if (i == 4) {
                    layoutRes = ResHelper.getLayoutRes(context, "elp_notify_banner_no_padding");
                }
                layoutRes = 0;
            }
        } else {
            int i2 = pushMessage.unfold.showType;
            if (i2 == 1) {
                layoutRes = ResHelper.getLayoutRes(context, "elp_notify_window");
            } else if (i2 == 2) {
                layoutRes = ResHelper.getLayoutRes(context, "elp_notify_card");
            } else if (i2 == 3) {
                layoutRes = ResHelper.getLayoutRes(context, "elp_notify_nativ");
            } else {
                if (i2 == 4) {
                    layoutRes = ResHelper.getLayoutRes(context, "elp_notify_banner");
                }
                layoutRes = 0;
            }
        }
        if (layoutRes <= 0) {
            remoteViews = null;
        } else {
            remoteViews = new RemoteViews(context.getPackageName(), layoutRes);
            remoteViews.setTextViewText(ResHelper.getIdRes(context, "tvTitle"), pushMessage.title);
            remoteViews.setTextViewText(ResHelper.getIdRes(context, "tvContent"), pushMessage.content);
            if (pushMessage.unfold.showType == 1) {
                remoteViews.setImageViewBitmap(ResHelper.getIdRes(context, "ivImg2"), arrayList.get(1));
                remoteViews.setImageViewBitmap(ResHelper.getIdRes(context, "ivImg3"), arrayList.get(2));
                remoteViews.setImageViewBitmap(ResHelper.getIdRes(context, "ivImg4"), arrayList.get(3));
            }
            remoteViews.setImageViewBitmap(ResHelper.getIdRes(context, "ivImg"), arrayList.get(0));
            if (h) {
                remoteViews.setTextColor(ResHelper.getIdRes(context, "tvTitle"), -1);
                remoteViews.setTextColor(ResHelper.getIdRes(context, "tvContent"), -7829368);
            }
        }
        if (remoteViews != null) {
            Context context2 = MobSDK.getContext();
            if (remoteViews != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    builder.setCustomContentView(remoteViews);
                    builder.setCustomBigContentView(remoteViews);
                } else {
                    builder.setContent(remoteViews);
                }
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(NotificationCompat.CATEGORY_MESSAGE, pushMessage);
            Intent intent = new Intent("com.mob.elp.intent.NOTIFICATION_OPENED");
            intent.putExtras(bundle);
            intent.setPackage(MobSDK.getContext().getPackageName());
            builder.setContentIntent(PendingIntent.getBroadcast(context2, f.a().b(), intent, AMapEngineUtils.HALF_MAX_P20_WIDTH));
            Notification build = Build.VERSION.SDK_INT >= 16 ? builder.build() : builder.getNotification();
            if (Build.VERSION.SDK_INT >= 16) {
                build.bigContentView = remoteViews;
            }
            if ("oppo".equalsIgnoreCase(this.f)) {
                build.flags = 18;
            }
            Calendar calendar = Calendar.getInstance();
            int i3 = calendar.get(11);
            int i4 = calendar.get(12);
            if (i3 == 0 && i4 >= 0 && i4 <= 0) {
                build.defaults = 0;
                build.sound = null;
                build.vibrate = null;
                build.ledOffMS = 0;
                build.ledOnMS = 0;
                build.ledARGB = 0;
            }
            try {
                z = Build.VERSION.SDK_INT >= 24 ? this.a.areNotificationsEnabled() : a(context2);
            } catch (Throwable th) {
                d.a().a(th);
                z = true;
            }
            NotificationManager notificationManager = this.a;
            int i5 = this.b + 1;
            this.b = i5;
            notificationManager.notify(i5, build);
            if (z) {
                f.a().a(context2, "show", pushMessage.workId);
            }
        }
    }

    public BroadcastReceiver b() {
        return new C0056a(this);
    }
}
