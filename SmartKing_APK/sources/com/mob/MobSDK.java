package com.mob;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.mob.MobUser;
import com.mob.PrivacyPolicy;
import com.mob.commons.InternationalDomain;
import com.mob.commons.MobProduct;
import com.mob.commons.MobProductCollector;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.commons.b;
import com.mob.commons.dialog.PolicyThrowable;
import com.mob.commons.dialog.a;
import com.mob.commons.dialog.entity.InternalPolicyUi;
import com.mob.commons.dialog.entity.MobPolicyUi;
import com.mob.commons.f;
import com.mob.commons.h;
import com.mob.commons.i;
import com.mob.commons.j;
import com.mob.commons.logcollector.DefaultLogsCollector;
import com.mob.tools.MobLog;
import com.mob.tools.log.NLog;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.UIHandler;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class MobSDK implements PublicMemberKeeper {
    public static final int CHANNEL_APICLOUD = 5;
    public static final int CHANNEL_COCOS = 1;
    public static final int CHANNEL_FLUTTER = 4;
    public static final int CHANNEL_JS = 3;
    public static final int CHANNEL_NATIVE = 0;
    public static final int CHANNEL_QUICKSDK = 6;
    public static final int CHANNEL_UNIAPP = 7;
    public static final int CHANNEL_UNITY = 2;
    public static final int POLICY_TYPE_TXT = 2;
    public static final int POLICY_TYPE_URL = 1;
    public static final int SDK_VERSION_CODE;
    public static final String SDK_VERSION_NAME;
    private static Context a = null;
    private static String b = null;
    private static String c = null;
    private static volatile boolean d = false;
    private static InternationalDomain e = null;
    private static volatile boolean f = false;
    private static volatile boolean g = false;
    private static volatile boolean h = false;

    static {
        String str;
        int i;
        try {
            str = "2021-03-23".replace("-", ".");
        } catch (Throwable unused) {
            str = "1.0.0";
        }
        try {
            i = Integer.parseInt("2021-03-23".replace("-", ""));
        } catch (Throwable unused2) {
            i = 1;
            SDK_VERSION_CODE = i;
            SDK_VERSION_NAME = str;
        }
        SDK_VERSION_CODE = i;
        SDK_VERSION_NAME = str;
    }

    private static void a(String str, String str2) {
        if (str == null || str2 == null) {
            Bundle bundle = null;
            try {
                bundle = a.getPackageManager().getPackageInfo(a.getPackageName(), 128).applicationInfo.metaData;
            } catch (Throwable unused) {
            }
            if (str == null && bundle != null) {
                str = bundle.getString("Mob-AppKey");
            }
            if (str2 == null && bundle != null) {
                str2 = bundle.getString("Mob-AppSecret");
            }
            if (str2 == null && bundle != null) {
                str2 = bundle.getString("Mob-AppSeret");
            }
        }
        b = str;
        c = str2;
    }

    public static synchronized void addUserWatcher(MobUser.UserWatcher userWatcher) {
        synchronized (MobSDK.class) {
            if (userWatcher != null) {
                MobUser.a(userWatcher);
            }
        }
    }

    private static void c() {
        ((DefaultLogsCollector) NLog.setDefaultCollector(DefaultLogsCollector.get())).addSDK("MOBSDK", SDK_VERSION_CODE);
        try {
            NLog nLog = NLog.getInstance("MOBSDK");
            nLog.d("===============================", new Object[0]);
            nLog.d("MobCommons name: " + SDK_VERSION_NAME + ", code: " + SDK_VERSION_CODE, new Object[0]);
            nLog.d("===============================", new Object[0]);
        } catch (Throwable unused) {
        }
    }

    public static void canIContinueBusiness(final MobProduct mobProduct, final InternalPolicyUi internalPolicyUi, final OperationCallback<Boolean> operationCallback) {
        if (operationCallback == null) {
            throw new IllegalArgumentException("callback can not be null");
        }
        new Thread(new Runnable() { // from class: com.mob.MobSDK.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (MobProduct.this == null) {
                        operationCallback.onFailure(new PolicyThrowable("MobProduct can not be null"));
                    }
                    a.a().a(MobProduct.this, internalPolicyUi, operationCallback);
                } catch (Throwable th) {
                    MobLog.getInstance().e(th);
                    operationCallback.onFailure(th);
                }
            }
        }).start();
    }

    public static boolean checkForceHttps() {
        e();
        return f;
    }

    public static boolean checkPpNecessary() {
        e();
        return g;
    }

    public static String checkRequestUrl(String str) {
        return j.a(str);
    }

    public static boolean checkV6() {
        e();
        return h;
    }

    public static synchronized void clearUser() {
        synchronized (MobSDK.class) {
            MobUser.a();
        }
    }

    private static boolean d() {
        boolean z = false;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    String className = stackTraceElement.getClassName();
                    if (!"android.app.Instrumentation".equals(className)) {
                        if ("android.app.ActivityThread".equals(className) && "handleBindApplication".equals(stackTraceElement.getMethodName())) {
                            z = true;
                            break;
                        }
                    } else {
                        if ("callApplicationOnCreate".equals(stackTraceElement.getMethodName())) {
                            z = true;
                            break;
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
        if (!z) {
            Log.e("MobSDK", "Please invoke MobSDK.init(context) method in your application onCreate()");
        }
        return z;
    }

    public static String dynamicModifyUrl(String str) {
        return j.b(str);
    }

    private static void e() {
        Bundle bundle;
        if (a == null) {
            Log.e("MobSDK", "Please invoke MobSDK.init(context) method firstly.");
            return;
        }
        if (d) {
            return;
        }
        d = true;
        String str = null;
        try {
            bundle = a.getPackageManager().getPackageInfo(a.getPackageName(), 128).applicationInfo.metaData;
        } catch (Throwable unused) {
            bundle = null;
        }
        if (e == null) {
            if (bundle != null) {
                try {
                    e = InternationalDomain.domainOf(bundle.getString("Domain"));
                } catch (Throwable unused2) {
                    e = InternationalDomain.DEFAULT;
                }
            } else {
                e = InternationalDomain.DEFAULT;
            }
        }
        if (bundle != null) {
            try {
                str = bundle.getString("Mob-Https");
            } catch (Throwable unused3) {
            }
            if (str == null) {
                try {
                    f = bundle.getBoolean("Mob-Https");
                } catch (Throwable unused4) {
                }
            } else {
                f = "yes".equalsIgnoreCase(str);
            }
        }
        if (bundle != null) {
            try {
                g = bundle.getBoolean("Mob-PpNecessary", false);
            } catch (Throwable unused5) {
            }
        }
        if (bundle != null) {
            try {
                h = bundle.getBoolean("Mob-V6", false);
            } catch (Throwable unused6) {
            }
        }
    }

    public static HashMap<String, String> exchangeIds(String[] strArr) {
        return MobUser.a(strArr);
    }

    private static boolean f() {
        return b.ac();
    }

    private static boolean g() {
        return b.b();
    }

    public static String getAppSecret() {
        return c;
    }

    public static String getAppkey() {
        return b;
    }

    public static Context getContext() {
        Context context;
        if (a == null) {
            try {
                Object currentActivityThread = DeviceHelper.currentActivityThread();
                if (currentActivityThread != null && (context = (Context) ReflectHelper.invokeInstanceMethod(currentActivityThread, "getApplication", new Object[0])) != null) {
                    init(context);
                }
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
        return a;
    }

    public static InternationalDomain getDomain() {
        if (e == null) {
            e();
        }
        return e == null ? InternationalDomain.DEFAULT : e;
    }

    public static PrivacyPolicy getPrivacyPolicy(int i) {
        return getPrivacyPolicy(i, null);
    }

    public static PrivacyPolicy getPrivacyPolicy(int i, Locale locale) {
        try {
            return new h().a(i == 1 ? 1 : 2, locale);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public static void getPrivacyPolicyAsync(int i, PrivacyPolicy.OnPolicyListener onPolicyListener) {
        getPrivacyPolicyAsync(i, null, onPolicyListener);
    }

    public static void getPrivacyPolicyAsync(final int i, final Locale locale, final PrivacyPolicy.OnPolicyListener onPolicyListener) {
        if (onPolicyListener != null) {
            new Thread(new Runnable() { // from class: com.mob.MobSDK.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        final PrivacyPolicy a2 = new h().a(i == 1 ? 1 : 2, locale);
                        try {
                            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.MobSDK.2.1
                                @Override // android.os.Handler.Callback
                                public boolean handleMessage(Message message) {
                                    onPolicyListener.onComplete(a2);
                                    return false;
                                }
                            });
                        } catch (Throwable th) {
                            MobLog.getInstance().d(th);
                            onPolicyListener.onComplete(a2);
                        }
                    } catch (Throwable th2) {
                        try {
                            MobLog.getInstance().d(th2);
                            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.MobSDK.2.2
                                @Override // android.os.Handler.Callback
                                public boolean handleMessage(Message message) {
                                    onPolicyListener.onFailure(th2);
                                    return false;
                                }
                            });
                        } catch (Throwable th3) {
                            MobLog.getInstance().d(th3);
                            onPolicyListener.onFailure(th2);
                        }
                    }
                }
            }).start();
        }
    }

    public static synchronized void getUser(final MobUser.OnUserGotListener onUserGotListener) {
        synchronized (MobSDK.class) {
            MobUser.a(new MobUser.OnUserGotListener() { // from class: com.mob.MobSDK.6
                @Override // com.mob.MobUser.OnUserGotListener
                public void onUserGot(MobUser mobUser) {
                    if (MobUser.OnUserGotListener.this != null) {
                        MobUser.OnUserGotListener onUserGotListener2 = MobUser.OnUserGotListener.this;
                        if (mobUser.getMobUserId() == null) {
                            mobUser = null;
                        }
                        onUserGotListener2.onUserGot(mobUser);
                    }
                }
            });
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.mob.MobSDK$1] */
    private static void h() {
        try {
            new Thread() { // from class: com.mob.MobSDK.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        com.mob.commons.a.l();
                        MobSDK.i();
                        MobProductCollector.syncInit();
                        com.mob.commons.a.a();
                        i.I();
                        MobProductCollector.collect();
                        com.mob.commons.authorize.a.a(MobSDK.a);
                        DeviceAuthorizer.authorize(null);
                    } catch (Throwable th) {
                        MobLog.getInstance().w(th);
                    }
                }
            }.start();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void i() {
        if (i.G() == 0) {
            i.j(System.currentTimeMillis());
        }
    }

    public static synchronized void init(Context context) {
        synchronized (MobSDK.class) {
            init(context, null, null);
        }
    }

    public static synchronized void init(Context context, String str) {
        synchronized (MobSDK.class) {
            init(context, str, null);
        }
    }

    public static synchronized void init(Context context, String str, String str2) {
        synchronized (MobSDK.class) {
            if (a == null) {
                a = context.getApplicationContext();
                a(str, str2);
                e();
                c();
                d();
                h();
            } else if (!TextUtils.isEmpty(str)) {
                boolean isEmpty = TextUtils.isEmpty(b);
                b = str;
                c = str2;
                if (isEmpty) {
                    b.as();
                }
            }
        }
    }

    public static final int isAuth() {
        int i;
        boolean c2 = com.mob.commons.a.c();
        MobLog.getInstance().d("isAuth(). ppNece: " + c2, new Object[0]);
        if (c2) {
            Boolean k = com.mob.commons.a.k();
            MobLog.getInstance().d("isAuth(). isAgreePp: " + k, new Object[0]);
            i = k == null ? 0 : k.booleanValue() ? 1 : -1;
        } else {
            i = 2;
        }
        MobLog.getInstance().d("isAuth(). isAuth: " + i + "[2:ppms-off，1:agr，0:unkwn，-1:disagr]", new Object[0]);
        return i;
    }

    public static final boolean isForb() {
        boolean f2;
        boolean c2 = com.mob.commons.a.c();
        MobLog.getInstance().d("isForb(). ppNece: " + c2, new Object[0]);
        if (c2) {
            boolean d2 = com.mob.commons.a.d();
            MobLog.getInstance().d("isForb(). isAgrPp: " + d2, new Object[0]);
            if (d2) {
                f2 = f();
            } else {
                boolean i = com.mob.commons.a.i();
                MobLog.getInstance().d("isForb(). funcStch: " + i, new Object[0]);
                f2 = i ? f() : true;
            }
        } else {
            f2 = f();
        }
        MobLog.getInstance().d("isForb(). isForb: " + f2, new Object[0]);
        return f2;
    }

    public static final Boolean isGpAvailable() {
        return null;
    }

    public static final boolean isGppVer() {
        return false;
    }

    public static final boolean isMob() {
        boolean g2;
        boolean c2 = com.mob.commons.a.c();
        MobLog.getInstance().d("isMob(). ppNece: " + c2, new Object[0]);
        if (c2) {
            boolean d2 = com.mob.commons.a.d();
            MobLog.getInstance().d("isMob(). isAgrPp: " + d2, new Object[0]);
            if (d2) {
                g2 = g();
            } else {
                boolean j = com.mob.commons.a.j();
                MobLog.getInstance().d("isMob(). cltSch: " + j, new Object[0]);
                g2 = j ? g() : false;
            }
        } else {
            g2 = g();
        }
        MobLog.getInstance().d("isMob(). isMob: " + g2, new Object[0]);
        return g2;
    }

    public static synchronized void removeUserWatcher(MobUser.UserWatcher userWatcher) {
        synchronized (MobSDK.class) {
            if (userWatcher != null) {
                MobUser.b(userWatcher);
            }
        }
    }

    @Deprecated
    public static void setAllowDialog(boolean z) {
    }

    public static void setChannel(MobProduct mobProduct, int i) {
        f.a().a(mobProduct, i);
    }

    @Deprecated
    public static void setDomain(InternationalDomain internationalDomain) {
        e = internationalDomain;
    }

    @Deprecated
    public static void setPolicyUi(MobPolicyUi mobPolicyUi) {
    }

    public static synchronized void setUser(String str, String str2, String str3, HashMap<String, Object> hashMap) {
        synchronized (MobSDK.class) {
            setUser(str, str2, str3, hashMap, null);
        }
    }

    public static synchronized void setUser(String str, String str2, String str3, HashMap<String, Object> hashMap, String str4) {
        synchronized (MobSDK.class) {
            MobUser.a(str, str2, str3, hashMap, str4);
        }
    }

    @Deprecated
    public static void submitPermissionGrantResult(boolean z, MobProduct mobProduct, final OperationCallback<Void> operationCallback) {
        if (operationCallback != null) {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.MobSDK.4
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    OperationCallback.this.onComplete(null);
                    return false;
                }
            });
        }
    }

    public static void submitPolicyGrantResult(final boolean z, final OperationCallback<Void> operationCallback) {
        new Thread(new Runnable() { // from class: com.mob.MobSDK.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    com.mob.commons.a.a(z, (OperationCallback<Void>) operationCallback);
                } catch (Throwable th) {
                    MobLog.getInstance().e(th);
                    if (operationCallback != null) {
                        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.MobSDK.3.1
                            @Override // android.os.Handler.Callback
                            public boolean handleMessage(Message message) {
                                operationCallback.onFailure(th);
                                return false;
                            }
                        });
                    }
                }
            }
        }).start();
    }
}
