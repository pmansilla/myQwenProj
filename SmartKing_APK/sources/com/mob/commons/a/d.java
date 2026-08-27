package com.mob.commons.a;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import com.mob.MobSDK;
import com.mob.commons.LockAction;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.FileLocker;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import java.io.Closeable;
import java.io.File;
import java.util.HashMap;

/* compiled from: BaseClt.java */
/* loaded from: classes.dex */
public class d implements Handler.Callback {
    private static HashMap<String, d> c = new HashMap<>();
    private static HashMap<String, Object> d = new HashMap<>();
    private MobHandlerThread a;
    private boolean b = false;
    private Handler e;

    public static void a(String str, File file, String str2, String str3) throws Throwable {
        Object obj;
        Object invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), com.mob.commons.k.a(8), new Object[0]);
        ReflectHelper.importClass(com.mob.commons.k.a(9), com.mob.commons.k.a(9));
        File parentFile = file.getParentFile();
        synchronized (d) {
            obj = d.get(str);
            if (obj == null) {
                obj = ReflectHelper.newInstance(com.mob.commons.k.a(9), file.getAbsolutePath(), parentFile.getAbsolutePath(), parentFile.getAbsolutePath(), invokeInstanceMethod);
                d.put(str, obj);
            }
        }
        ResHelper.deleteFileAndFolder(parentFile);
        String authorize = DeviceAuthorizer.authorize(null);
        final Object invokeInstanceMethod2 = ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeInstanceMethod(obj, com.mob.commons.k.a(10), str2), com.mob.commons.k.a(11), str3, String.class);
        HashMap hashMap = new HashMap();
        hashMap.put("duid", authorize);
        hashMap.put("moid", com.mob.commons.b.d.c(MobSDK.getContext()));
        hashMap.put("sdkVersion", Integer.valueOf(MobSDK.SDK_VERSION_CODE));
        hashMap.put("appKey", MobSDK.getAppkey());
        hashMap.put("appSecret", MobSDK.getAppSecret());
        hashMap.put("domain", MobSDK.getDomain().getDomain());
        hashMap.put("forceHttps", Boolean.valueOf(MobSDK.checkForceHttps()));
        final String fromHashMap = new Hashon().fromHashMap(hashMap);
        ReflectHelper.invokeInstanceMethod(invokeInstanceMethod2, com.mob.commons.k.a(12), true);
        com.mob.commons.d.a().a(15);
        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.commons.a.d.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                try {
                    com.mob.commons.d.a().a(16);
                    ReflectHelper.invokeInstanceMethod(invokeInstanceMethod2, com.mob.commons.k.a(13), null, new Object[]{fromHashMap});
                    com.mob.commons.d.a().a(17);
                } catch (Throwable th) {
                    com.mob.commons.d.a().a(7, th);
                }
                return false;
            }
        });
    }

    public static final synchronized void a(Class<? extends d>... clsArr) {
        synchronized (d.class) {
            if (clsArr != null) {
                if (clsArr.length != 0) {
                    for (Class<? extends d> cls : clsArr) {
                        if (cls != null) {
                            String simpleName = cls.getSimpleName();
                            if (c.get(simpleName) == null) {
                                try {
                                    d newInstance = cls.newInstance();
                                    c.put(simpleName, newInstance);
                                    newInstance.h();
                                } catch (Throwable th) {
                                    MobLog.getInstance().d(th);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void h() {
        final File a = a();
        if (a == null) {
            return;
        }
        this.a = new MobHandlerThread() { // from class: com.mob.commons.a.d.1
            /* JADX INFO: Access modifiers changed from: private */
            public void a() {
                super.run();
            }

            @Override // com.mob.tools.MobHandlerThread
            protected void onLooperPrepared(Looper looper) {
                try {
                    d.this.e = new Handler(looper, d.this);
                    d.this.d();
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
            }

            @Override // com.mob.tools.MobHandlerThread, java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    if (com.mob.commons.e.a(a, new LockAction() { // from class: com.mob.commons.a.d.1.1
                        @Override // com.mob.commons.LockAction
                        public boolean run(FileLocker fileLocker) {
                            try {
                                MobLog.getInstance().d("synchronizeProcess success clt: " + d.this.getClass().getSimpleName() + ", file: " + a.getPath() + ", pid: " + Process.myPid() + ", isStop: " + d.this.b, new Object[0]);
                                if (!d.this.b) {
                                    boolean ac = com.mob.commons.b.ac();
                                    boolean b_ = d.this.b_();
                                    MobLog.getInstance().d("Clt entrance. forb: " + ac + ", coll: " + b_, new Object[0]);
                                    if (!ac && b_) {
                                        a();
                                    }
                                }
                            } catch (Throwable th) {
                                MobLog.getInstance().d(th);
                            }
                            return false;
                        }
                    })) {
                        return;
                    }
                    MobLog.getInstance().w("synchronizeProcess failed clt: " + d.this.getClass().getSimpleName() + ", file: " + a.getPath());
                    d.c.put(getClass().getSimpleName(), null);
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
            }
        };
        this.a.start();
    }

    protected File a() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(int i) {
        if (this.e != null) {
            this.e.removeMessages(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(int i, long j) {
        if (this.e != null) {
            this.e.sendEmptyMessageDelayed(i, j);
        }
    }

    protected void a(Message message) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
    }

    protected void b() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(int i) {
        if (this.e != null) {
            this.e.sendEmptyMessage(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(Message message) {
        if (this.e != null) {
            this.e.sendMessage(message);
        }
    }

    protected boolean b_() {
        return true;
    }

    protected void d() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e() {
        try {
            if (this.e != null) {
                this.e.removeCallbacksAndMessages(null);
            }
            if (this.a != null) {
                this.a.quit();
            }
            this.e = null;
            this.a = null;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        b();
        this.b = true;
        c.put(getClass().getSimpleName(), null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HashMap<String, Object> f() {
        Location location;
        if (!com.mob.commons.b.A() || (location = DeviceHelper.getInstance(MobSDK.getContext()).getLocation(0, 0, true)) == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("accmt", Float.valueOf(location.getAccuracy()));
        if (Build.VERSION.SDK_INT >= 26 && location.hasVerticalAccuracy()) {
            hashMap.put("vacmt", Float.valueOf(location.getVerticalAccuracyMeters()));
        }
        hashMap.put("ltdmt", Double.valueOf(location.getLatitude()));
        hashMap.put("lndmt", Double.valueOf(location.getLongitude()));
        hashMap.put("ltime", Long.valueOf(location.getTime()));
        hashMap.put("prvmt", location.getProvider());
        hashMap.put("atdmt", Double.valueOf(location.getAltitude()));
        hashMap.put("brmt", Float.valueOf(location.getBearing()));
        hashMap.put(Dic.SPEED, Float.valueOf(location.getSpeed()));
        return hashMap;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        if (com.mob.commons.b.M()) {
            e();
            return false;
        }
        a(message);
        return false;
    }
}
