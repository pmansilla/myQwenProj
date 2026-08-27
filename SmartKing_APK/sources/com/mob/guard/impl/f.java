package com.mob.guard.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.guard.MobGuardPullUpService;
import com.mob.guard.OnAppActiveListener;
import com.mob.mcl.MCLSDK;
import com.mob.tools.utils.ReflectHelper;

/* loaded from: classes.dex */
public class f {
    private static OnAppActiveListener a;

    /* loaded from: classes.dex */
    static class a implements ServiceConnection {
        final /* synthetic */ Context a;

        a(Context context) {
            this.a = context;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.a.unbindService(this);
            } catch (Throwable unused) {
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    /* loaded from: classes.dex */
    static class b extends g {
        final /* synthetic */ String a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;
        final /* synthetic */ String e;

        b(String str, String str2, String str3, String str4, String str5) {
            this.a = str;
            this.b = str2;
            this.c = str3;
            this.d = str4;
            this.e = str5;
        }

        @Override // com.mob.guard.impl.g
        public void a() throws Throwable {
            Thread.sleep(1000L);
            d.a(this.a, this.b, this.c, this.d, MCLSDK.getSuid(), this.e);
        }
    }

    public static void a(Context context, Intent intent, boolean z) {
        try {
            e.a().d("[MobGuard] pulled intent: " + intent, new Object[0]);
            String stringExtra = intent.getStringExtra(AeUtil.ROOT_DATA_PATH_OLD_NAME);
            String stringExtra2 = intent.getStringExtra("workId");
            String stringExtra3 = intent.getStringExtra("appkey");
            String stringExtra4 = intent.getStringExtra("pkg");
            String stringExtra5 = intent.getStringExtra("duid");
            String stringExtra6 = intent.getStringExtra("guardId");
            boolean booleanExtra = intent.getBooleanExtra("SERVICE_REWORK", false);
            boolean booleanExtra2 = intent.getBooleanExtra("fromActivity", false);
            if (z) {
                Intent intent2 = new Intent(context, (Class<?>) MobGuardPullUpService.class);
                intent2.putExtra("workId", stringExtra2);
                intent2.putExtra("appkey", stringExtra3);
                intent2.putExtra("pkg", stringExtra4);
                intent2.putExtra("duid", stringExtra5);
                intent2.putExtra("SERVICE_REWORK", booleanExtra);
                intent2.putExtra("guardId", stringExtra6);
                intent2.putExtra("fromActivity", z);
                try {
                    context.startService(intent);
                } catch (Throwable th) {
                    e.a().d(th);
                    try {
                        context.bindService(intent, new a(context), 1);
                    } catch (Throwable th2) {
                        e.a().d(th2);
                    }
                }
            }
            if (booleanExtra2) {
                return;
            }
            if (!TextUtils.isEmpty(stringExtra)) {
                try {
                    ReflectHelper.invokeStaticMethod(ReflectHelper.importClass("com.mob.pushsdk.MobPush"), "addGuardMessage", stringExtra);
                } catch (Throwable th3) {
                    e.a().d(th3);
                }
            }
            if (!TextUtils.isEmpty(stringExtra2) && !TextUtils.isEmpty(stringExtra6)) {
                new b(stringExtra5, stringExtra3, stringExtra4, stringExtra6, stringExtra2).start();
            }
            String string = context.getPackageManager().getPackageInfo(context.getPackageName(), 128).applicationInfo.metaData.getString("guard_listener");
            if (!TextUtils.isEmpty(string)) {
                try {
                    ((OnAppActiveListener) Class.forName(string).newInstance()).onAppActive(context);
                } catch (Throwable th4) {
                    Log.e("[MobGuard]", Log.getStackTraceString(th4));
                }
                Log.i("[MobGuard]", "guard_listener onAppActive was executed");
            }
            if (a != null) {
                a.onAppActive(context);
                Log.i("[MobGuard]", "onAppActiveListener onAppActive was executed");
            }
        } catch (Throwable th5) {
            e.a().d(th5);
        }
    }

    public static void a(OnAppActiveListener onAppActiveListener) {
        a = onAppActiveListener;
    }
}
