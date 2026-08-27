package com.mob.elp.a;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.mob.MobSDK;
import com.mob.elp.MobELP;
import com.mob.elp.PushMessage;
import com.mob.mcl.MCLSDK;
import com.mob.tools.utils.ActivityTracker;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* compiled from: ELPImpl.java */
/* loaded from: classes.dex */
public class a implements MCLSDK.ELPMessageListener, ActivityTracker.Tracker {
    public static final ExecutorService h = Executors.newFixedThreadPool(5);
    private static volatile a i;
    public String a;
    private Activity b;
    private HashMap<String, PushMessage> c = new HashMap<>();
    private HashMap<String, MobELP.PushMessageListener> d = new HashMap<>();
    private HashMap<String, PushMessage> e = new HashMap<>();
    private PendingIntent f;
    private BroadcastReceiver g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ELPImpl.java */
    /* renamed from: com.mob.elp.a.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0053a implements Runnable {
        RunnableC0053a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (a.this.c == null || a.this.c.size() <= 0) {
                    return;
                }
                Iterator it = a.this.c.keySet().iterator();
                if (it.hasNext()) {
                    String str = (String) it.next();
                    PushMessage pushMessage = (PushMessage) a.this.c.get(str);
                    a.this.c.remove(str);
                    a.a(a.this, pushMessage, str);
                }
            } catch (Throwable th) {
                com.mob.elp.d.d.a().a(th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ELPImpl.java */
    /* loaded from: classes.dex */
    public class b implements Handler.Callback {
        final /* synthetic */ MobELP.PushMessageListener a;
        final /* synthetic */ PushMessage b;

        b(a aVar, MobELP.PushMessageListener pushMessageListener, PushMessage pushMessage) {
            this.a = pushMessageListener;
            this.b = pushMessage;
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            this.a.messageReceived(this.b);
            return false;
        }
    }

    private a() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(PushMessage pushMessage) {
        Iterator<String> it = this.d.keySet().iterator();
        while (it.hasNext()) {
            MobELP.PushMessageListener pushMessageListener = this.d.get(it.next());
            if (pushMessageListener != null) {
                UIHandler.sendEmptyMessage(0, new b(this, pushMessageListener, pushMessage));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(a aVar, Context context, PushMessage pushMessage, String str) {
        int i2;
        int i3;
        if (aVar == null) {
            throw null;
        }
        try {
            com.mob.elp.d.e eVar = new com.mob.elp.d.e();
            ArrayList<String> arrayList = new ArrayList<>();
            int screenWidth = ResHelper.getScreenWidth(context);
            if (pushMessage.unfold.showType == 1) {
                arrayList.addAll(pushMessage.unfold.images);
                int dipToPx = (screenWidth - ResHelper.dipToPx(context, 54)) / 4;
                i3 = ResHelper.dipToPx(context, 60);
                i2 = dipToPx;
            } else if (pushMessage.unfold.showType == 2) {
                arrayList.add(pushMessage.unfold.image);
                i3 = ResHelper.dipToPx(context, 90);
                i2 = screenWidth - ResHelper.dipToPx(context, 42);
            } else if (pushMessage.unfold.showType == 3) {
                arrayList.add(pushMessage.unfold.image);
                i3 = ResHelper.dipToPx(context, 60);
                i2 = i3;
            } else if (pushMessage.unfold.showType == 4) {
                arrayList.add(pushMessage.unfold.image);
                i3 = ResHelper.dipToPx(context, 100);
                i2 = screenWidth - ResHelper.dipToPx(context, 12);
            } else {
                i2 = 1;
                i3 = 1;
            }
            eVar.a(arrayList, i2, i3, new d(aVar, pushMessage, str));
        } catch (Throwable th) {
            com.mob.elp.d.d.a().a(th);
            MCLSDK.deleteMsg(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(a aVar, PushMessage pushMessage, String str) {
        if (aVar == null) {
            throw null;
        }
        try {
            com.mob.elp.b.b a = com.mob.elp.b.a.b().a();
            if (aVar.b == null || (a != null && a.isShowing())) {
                aVar.c.put(str, pushMessage);
            } else {
                com.mob.elp.b.a.b().a(aVar.b, pushMessage, str);
            }
        } catch (Throwable th) {
            com.mob.elp.d.d.a().a(th);
        }
    }

    public static a b() {
        if (i == null) {
            synchronized (a.class) {
                if (i == null) {
                    i = new a();
                }
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        AlarmManager alarmManager = (AlarmManager) MobSDK.getContext().getSystemService(NotificationCompat.CATEGORY_ALARM);
        PendingIntent pendingIntent = this.f;
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        Intent intent = new Intent("com.mob.elp.intent.CHECK_TCP_STATUS");
        intent.setPackage(MobSDK.getContext().getPackageName());
        this.f = PendingIntent.getBroadcast(MobSDK.getContext(), 0, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH);
        long elapsedRealtime = SystemClock.elapsedRealtime() + 240000;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 23) {
            alarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, this.f);
        } else if (i2 >= 19) {
            alarmManager.setExact(2, elapsedRealtime, this.f);
        } else {
            alarmManager.set(2, elapsedRealtime, this.f);
        }
    }

    public void a() {
        h.execute(new RunnableC0053a());
    }

    public void a(String str) {
        try {
            this.a = str;
            ActivityTracker.getInstance(MobSDK.getContext()).addTracker(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.mob.elp.intent.NOTIFICATION_OPENED");
            ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "registerReceiver", new Object[]{com.mob.elp.c.a.a().b(), intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            MCLSDK.addELPMessageListener(this);
            this.g = new e(this);
            ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "registerReceiver", new Object[]{this.g, new IntentFilter("com.mob.elp.intent.CHECK_TCP_STATUS")}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            c();
        } catch (Throwable th) {
            com.mob.elp.d.d.a().a(th);
        }
    }

    public void a(String str, MobELP.PushMessageListener pushMessageListener) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (pushMessageListener == null) {
            this.d.remove(str);
        } else {
            this.d.put(str, pushMessageListener);
        }
    }

    public void a(String str, PushMessage pushMessage) {
        this.c.put(str, pushMessage);
    }

    @Override // com.mob.mcl.MCLSDK.ELPMessageListener
    public boolean messageReceived(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        try {
            int i2 = bundle.getInt("msgType");
            String string = bundle.getString("workId");
            if (i2 == 1) {
                h.execute(new c(this, bundle, string));
            } else if (i2 == 2) {
                h.execute(new com.mob.elp.a.b(this, bundle, string));
            }
            return false;
        } catch (Throwable th) {
            com.mob.elp.d.d.a().a(th);
            return false;
        }
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onCreated(Activity activity, Bundle bundle) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onDestroyed(Activity activity) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onPaused(Activity activity) {
        if (this.b == activity) {
            this.b = null;
        }
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onResumed(Activity activity) {
        this.b = activity;
        h.execute(new RunnableC0053a());
        try {
            if (this.e == null || this.e.size() <= 0) {
                return;
            }
            Iterator<String> it = this.e.keySet().iterator();
            if (it.hasNext()) {
                String next = it.next();
                PushMessage pushMessage = this.e.get(next);
                this.e.remove(next);
                a(pushMessage);
            }
        } catch (Throwable th) {
            com.mob.elp.d.d.a().a(th);
        }
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onSaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onStarted(Activity activity) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onStopped(Activity activity) {
    }
}
