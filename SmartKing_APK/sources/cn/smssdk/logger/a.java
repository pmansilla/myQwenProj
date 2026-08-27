package cn.smssdk.logger;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.f;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;

/* compiled from: BaseInformation.java */
/* loaded from: classes.dex */
public class a {
    private static a c;
    private static int d;
    private TelephonyManager a = (TelephonyManager) MobSDK.getContext().getApplicationContext().getSystemService("phone");
    private String b;

    /* compiled from: BaseInformation.java */
    /* renamed from: cn.smssdk.logger.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class RunnableC0017a implements Runnable {

        /* compiled from: BaseInformation.java */
        /* renamed from: cn.smssdk.logger.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class C0018a extends PhoneStateListener {
            C0018a(RunnableC0017a runnableC0017a) {
            }

            @Override // android.telephony.PhoneStateListener
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                int unused = a.d = signalStrength.getGsmSignalStrength();
                int unused2 = a.d = (a.d * 2) - 113;
            }
        }

        RunnableC0017a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Looper.prepare();
                a.this.a.listen(new C0018a(this), 256);
                Looper.loop();
            } catch (Throwable th) {
                SMSLog.getInstance().d(th, SMSLog.FORMAT_SIMPLE, new Object[0]);
            }
        }
    }

    private a() {
        new Thread(new RunnableC0017a()).start();
    }

    public static a p() {
        if (c == null) {
            c = new a();
        }
        return c;
    }

    public String a() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getDeviceId();
    }

    public void a(String str) {
        this.b = str;
    }

    public String b() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getModel();
    }

    public String c() {
        return Build.BRAND;
    }

    public String d() {
        return this.b;
    }

    public String e() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getMacAddress();
    }

    public String f() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getSignMD5();
    }

    public int g() {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        try {
            if (DeviceHelper.getInstance(MobSDK.getContext()).checkPermission("android.permission.ACCESS_NETWORK_STATE") && (connectivityManager = (ConnectivityManager) MobSDK.getContext().getApplicationContext().getSystemService("connectivity")) != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return d;
            }
            return 0;
        } catch (Throwable th) {
            SMSLog.getInstance().d(th, SMSLog.FORMAT_SIMPLE, new Object[0]);
        }
        return 0;
    }

    public String h() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getDetailNetworkTypeForStatic();
    }

    public int i() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getOSVersionInt();
    }

    public String j() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getPackageName();
    }

    public String k() {
        return DeviceHelper.getInstance(MobSDK.getContext()).getOSVersionName();
    }

    public String l() {
        return "3.8.3";
    }

    public String m() {
        return f.a();
    }

    public int n() {
        WifiInfo connectionInfo;
        try {
            if (DeviceHelper.getInstance(MobSDK.getContext()).checkPermission("android.permission.ACCESS_NETWORK_STATE") && ((ConnectivityManager) MobSDK.getContext().getApplicationContext().getSystemService("connectivity")).getNetworkInfo(1).isConnected() && (connectionInfo = ((WifiManager) MobSDK.getContext().getApplicationContext().getSystemService("wifi")).getConnectionInfo()) != null) {
                return connectionInfo.getRssi();
            }
            return 0;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return 0;
    }
}
