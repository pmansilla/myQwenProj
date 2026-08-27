package cn.sharesdk.framework.b;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.b.b.g;
import cn.sharesdk.framework.utils.e;
import com.mob.MobSDK;
import com.mob.commons.SHARESDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.FileLocker;
import java.io.File;
import java.util.Calendar;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: StatisticsLogger.java */
/* loaded from: classes.dex */
public class d extends cn.sharesdk.framework.utils.d {
    private static d b;
    private Handler e;
    private boolean f;
    private long g;
    private boolean h;
    private DeviceHelper c = DeviceHelper.getInstance(MobSDK.getContext());
    private a d = a.a();
    private FileLocker j = new FileLocker();
    private File i = new File(MobSDK.getContext().getFilesDir(), ".statistics");

    private d() {
        if (this.i.exists()) {
            return;
        }
        try {
            this.i.createNewFile();
        } catch (Exception e) {
            e.b().d(e);
        }
    }

    public static synchronized d a() {
        d dVar;
        synchronized (d.class) {
            if (b == null) {
                b = new d();
            }
            dVar = b;
        }
        return dVar;
    }

    private void c() {
        boolean d = d();
        if (d) {
            if (this.h) {
                return;
            }
            this.h = d;
            this.g = System.currentTimeMillis();
            a(new g());
            return;
        }
        if (this.h) {
            this.h = d;
            long currentTimeMillis = System.currentTimeMillis() - this.g;
            cn.sharesdk.framework.b.b.e eVar = new cn.sharesdk.framework.b.b.e();
            eVar.a = currentTimeMillis;
            a(eVar);
        }
    }

    private void c(cn.sharesdk.framework.b.b.c cVar) {
        cVar.f = this.c.getDeviceKey();
        cVar.g = this.c.getPackageName();
        cVar.h = this.c.getAppVersion();
        cVar.i = String.valueOf(ShareSDK.SDK_VERSION_CODE);
        cVar.j = this.c.getPlatformCode();
        cVar.k = this.c.getDetailNetworkTypeForStatic();
        if (TextUtils.isEmpty(MobSDK.getAppkey())) {
            Log.w("ShareSDKCore", "Your appKey of ShareSDK is null , this will cause its data won't be count!");
        } else if (!"cn.sharesdk.demo".equals(cVar.g) && ("api20".equals(MobSDK.getAppkey()) || "androidv1101".equals(MobSDK.getAppkey()))) {
            Log.w("ShareSDKCore", "Your app is using the appkey of ShareSDK Demo, this will cause its data won't be count!");
        }
        cVar.l = this.c.getDeviceData();
    }

    private void d(cn.sharesdk.framework.b.b.c cVar) {
        try {
            this.d.a(cVar);
            cVar.h();
        } catch (Throwable th) {
            e.b().d(th);
            e.b().d(cVar.toString(), new Object[0]);
        }
    }

    private boolean d() {
        return DeviceHelper.getInstance(MobSDK.getContext()).amIOnForeground();
    }

    public void a(Handler handler) {
        this.e = handler;
    }

    @Override // cn.sharesdk.framework.utils.d
    protected void a(Message message) {
        if (this.f) {
            return;
        }
        this.f = true;
        try {
            this.j.setLockFile(this.i.getAbsolutePath());
            if (this.j.lock(false)) {
                new Thread(new Runnable() { // from class: cn.sharesdk.framework.b.d.1
                    @Override // java.lang.Runnable
                    public void run() {
                        d.this.d.a(DeviceAuthorizer.authorize(new SHARESDK()));
                    }
                }).start();
                this.d.b();
                this.d.c();
                this.a.sendEmptyMessageDelayed(4, DateUtils.MILLIS_PER_HOUR);
                this.a.sendEmptyMessage(1);
                this.a.sendEmptyMessage(2);
            }
        } catch (Throwable th) {
            e.b().d(th);
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [cn.sharesdk.framework.b.d$2] */
    public void a(final cn.sharesdk.framework.b.b.c cVar) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread() { // from class: cn.sharesdk.framework.b.d.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    d.this.b(cVar);
                }
            }.start();
        } else {
            b(cVar);
        }
    }

    @Override // cn.sharesdk.framework.utils.d
    protected void b(Message message) {
        switch (message.what) {
            case 1:
                c();
                try {
                    this.a.sendEmptyMessageDelayed(1, BootloaderScanner.TIMEOUT);
                    return;
                } catch (Throwable th) {
                    e.b().d(th);
                    return;
                }
            case 2:
                try {
                    this.d.d();
                    return;
                } catch (Throwable th2) {
                    e.b().d(th2);
                    return;
                }
            case 3:
                if (message.obj != null) {
                    d((cn.sharesdk.framework.b.b.c) message.obj);
                    this.a.removeMessages(2);
                    this.a.sendEmptyMessageDelayed(2, 2000L);
                    return;
                }
                return;
            case 4:
                long longValue = cn.sharesdk.framework.b.a.e.a().h().longValue();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(longValue);
                int i = calendar.get(1);
                int i2 = calendar.get(2);
                int i3 = calendar.get(5);
                calendar.setTimeInMillis(System.currentTimeMillis());
                int i4 = calendar.get(1);
                int i5 = calendar.get(2);
                int i6 = calendar.get(5);
                if (i != i4 || i2 != i5 || i3 != i6) {
                    this.d.c();
                }
                this.a.sendEmptyMessageDelayed(4, DateUtils.MILLIS_PER_HOUR);
                return;
            default:
                return;
        }
    }

    public void b(cn.sharesdk.framework.b.b.c cVar) {
        if (MobSDK.isMob() && this.f) {
            c(cVar);
            if (!cVar.g()) {
                e.b().d("Drop event: " + cVar.toString(), new Object[0]);
                return;
            }
            Message message = new Message();
            message.what = 3;
            message.obj = cVar;
            try {
                this.a.sendMessage(message);
            } catch (Throwable th) {
                e.b().d(th);
            }
        }
    }

    @Override // cn.sharesdk.framework.utils.d
    protected void c(Message message) {
        if (this.f) {
            long currentTimeMillis = System.currentTimeMillis() - this.g;
            cn.sharesdk.framework.b.b.e eVar = new cn.sharesdk.framework.b.b.e();
            eVar.a = currentTimeMillis;
            a(eVar);
            this.f = false;
            try {
                this.e.sendEmptyMessage(1);
            } catch (Throwable th) {
                e.b().d(th);
            }
            b = null;
            this.a.getLooper().quit();
        }
    }
}
