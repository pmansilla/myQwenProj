package com.mob.mcl.c;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.mob.MobSDK;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.UIHandler;

/* compiled from: HeartBeatHelper.java */
/* loaded from: classes.dex */
public class d {
    private static volatile d e;
    private PendingIntent a;
    private int d = -1;
    private BroadcastReceiver b = b();
    private Context c = MobSDK.getContext();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: HeartBeatHelper.java */
    /* loaded from: classes.dex */
    public class a extends BroadcastReceiver {

        /* compiled from: HeartBeatHelper.java */
        /* renamed from: com.mob.mcl.c.d$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class RunnableC0061a implements Runnable {
            RunnableC0061a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (i.c().f() && i.c().a(2000, 0)) {
                    com.mob.mcl.d.b.a().a("tcp send ping success ");
                    d.this.d();
                } else {
                    if (d.this == null) {
                        throw null;
                    }
                    if (!i.c().i()) {
                        i.c().j();
                    }
                    i.c().a(5000);
                }
            }
        }

        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                if ("com.mob.mcl.intent.PING".equals(intent.getAction()) && context.getPackageName().equals(intent.getPackage())) {
                    com.mob.mcl.b.a.e.execute(new RunnableC0061a());
                }
            } catch (Throwable th) {
                com.mob.mcl.d.b.a().a(th);
            }
        }
    }

    private d() {
        c();
    }

    public static d a() {
        if (e == null) {
            synchronized (d.class) {
                if (e == null) {
                    e = new d();
                }
            }
        }
        return e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(d dVar, int i) {
        if (dVar.d == -1) {
            dVar.d = i;
            return;
        }
        dVar.d = i;
        if (i == 0 || i.c().f()) {
            return;
        }
        UIHandler.sendEmptyMessageDelayed(0, 200L, new b(dVar));
    }

    private BroadcastReceiver b() {
        return new a();
    }

    private void c() {
        try {
            ReflectHelper.invokeInstanceMethod(this.c, "registerReceiver", new Object[]{new c(this), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            ReflectHelper.invokeInstanceMethod(this.c, "registerReceiver", new Object[]{this.b, new IntentFilter("com.mob.mcl.intent.PING")}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
    }

    public void d() {
        AlarmManager alarmManager = (AlarmManager) this.c.getSystemService(NotificationCompat.CATEGORY_ALARM);
        PendingIntent pendingIntent = this.a;
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        Intent intent = new Intent("com.mob.mcl.intent.PING");
        intent.setPackage(this.c.getPackageName());
        this.a = PendingIntent.getBroadcast(this.c, 0, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH);
        long elapsedRealtime = SystemClock.elapsedRealtime() + (i.c().e * 1000);
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            alarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, this.a);
        } else if (i >= 19) {
            alarmManager.setExact(2, elapsedRealtime, this.a);
        } else {
            alarmManager.set(2, elapsedRealtime, this.a);
        }
    }
}
