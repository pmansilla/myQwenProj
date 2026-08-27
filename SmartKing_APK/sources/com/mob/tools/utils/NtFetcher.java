package com.mob.tools.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.text.TextUtils;
import com.mob.tools.MobLog;
import io.reactivex.annotations.SchedulerSupport;

/* loaded from: classes2.dex */
public class NtFetcher {
    private static NtFetcher instance;
    private Context context;
    private DeviceHelper device;
    private Integer dtNtType;
    private String ntType;
    private BroadcastReceiver receiver;

    private NtFetcher(Context context) {
        this.context = context;
        this.device = DeviceHelper.getInstance(context);
        if (this.device.isSensitiveDevice()) {
            prepare();
        }
    }

    private int getDataNtType() {
        Object systemServiceSafe = this.device.getSystemServiceSafe("phone");
        if (systemServiceSafe == null) {
            return -1;
        }
        try {
            return (Build.VERSION.SDK_INT < 24 || !this.device.checkPermission("android.permission.READ_PHONE_STATE")) ? ((Integer) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(19), new Object[0])).intValue() : ((Integer) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(101), new Object[0])).intValue();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return -1;
        }
    }

    public static NtFetcher getInstance(Context context) {
        if (instance == null) {
            synchronized (NtFetcher.class) {
                if (instance == null) {
                    instance = new NtFetcher(context);
                }
            }
        }
        return instance;
    }

    private String getNetworkType() {
        Object systemServiceSafe;
        NetworkInfo activeNetworkInfo;
        try {
            if (!this.device.checkPermission("android.permission.ACCESS_NETWORK_STATE") || (systemServiceSafe = this.device.getSystemServiceSafe("connectivity")) == null || (activeNetworkInfo = ((ConnectivityManager) systemServiceSafe).getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable()) {
                return SchedulerSupport.NONE;
            }
            int type = activeNetworkInfo.getType();
            switch (type) {
                case 0:
                    return is5GMobileNetwork() ? "5G" : is4GMobileNetwork() ? "4G" : isFastMobileNetwork() ? "3G" : "2G";
                case 1:
                    return "wifi";
                default:
                    switch (type) {
                        case 6:
                            return "wimax";
                        case 7:
                            return "bluetooth";
                        case 8:
                            return "dummy";
                        case 9:
                            return "ethernet";
                        default:
                            return String.valueOf(type);
                    }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return SchedulerSupport.NONE;
        }
    }

    @TargetApi(21)
    private ConnectivityManager.NetworkCallback initNetworkCallback() {
        return new ConnectivityManager.NetworkCallback() { // from class: com.mob.tools.utils.NtFetcher.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                super.onAvailable(network);
                NtFetcher.this.refreshNet();
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLosing(Network network, int i) {
                super.onLosing(network, i);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                super.onLost(network);
                NtFetcher.this.refreshNet();
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onUnavailable() {
                super.onUnavailable();
            }
        };
    }

    private boolean is4GMobileNetwork() {
        Object systemServiceSafe = this.device.getSystemServiceSafe("phone");
        if (systemServiceSafe == null) {
            return false;
        }
        try {
            return ((Integer) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(19), new Object[0])).intValue() == 13;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return false;
        }
    }

    private boolean is5GCommon() {
        Object systemServiceSafe = this.device.getSystemServiceSafe("phone");
        if (systemServiceSafe == null) {
            return false;
        }
        try {
            return ((Integer) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(19), new Object[0])).intValue() == 20;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return false;
        }
    }

    private boolean is5GHw() {
        Object systemServiceSafe;
        try {
            if (this.device.checkPermission("android.permission.READ_PHONE_STATE")) {
                String manufacturer = this.device.getManufacturer();
                if (!TextUtils.isEmpty(manufacturer) && ((manufacturer.contains("huawei") || manufacturer.contains("Huawei") || manufacturer.contains("HUAWEI")) && (systemServiceSafe = this.device.getSystemServiceSafe("phone")) != null && Build.VERSION.SDK_INT >= 29)) {
                    if (((Integer) ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(129), new Object[0]), Strings.getString(131), new Object[0])).intValue() == 20) {
                        return true;
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return false;
    }

    private boolean is5GMobileNetwork() {
        if (is5GHw() || maybe5G()) {
            return true;
        }
        return is5GCommon();
    }

    private boolean isFastMobileNetwork() {
        Object systemServiceSafe = this.device.getSystemServiceSafe("phone");
        if (systemServiceSafe == null) {
            return false;
        }
        try {
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        switch (((Integer) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(19), new Object[0])).intValue()) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return true;
            case 4:
                return false;
            case 5:
                return true;
            case 6:
                return true;
            case 7:
                return false;
            case 8:
                return true;
            case 9:
                return true;
            case 10:
                return true;
            case 11:
                return false;
            case 12:
                return true;
            case 13:
                return true;
            case 14:
                return true;
            case 15:
                return true;
            default:
                return false;
        }
    }

    private boolean maybe5G() {
        Object systemServiceSafe;
        if (!this.device.checkPermission("android.permission.READ_PHONE_STATE") || Build.VERSION.SDK_INT < 26 || (systemServiceSafe = this.device.getSystemServiceSafe("phone")) == null) {
            return false;
        }
        return ((Integer) ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(129), new Object[0]), Strings.getString(130), new Object[0])).intValue() == 3;
    }

    @SuppressLint({"MissingPermission"})
    private void prepare() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.device.getSystemServiceSafe("connectivity");
            if (Build.VERSION.SDK_INT >= 26 && this.device.checkPermission("android.permission.ACCESS_NETWORK_STATE")) {
                connectivityManager.registerDefaultNetworkCallback(initNetworkCallback());
            } else if (Build.VERSION.SDK_INT < 21 || !this.device.checkPermission("android.permission.ACCESS_NETWORK_STATE")) {
                registerRcv();
            } else {
                connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), initNetworkCallback());
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshNet() {
        this.ntType = getNetworkType();
        this.dtNtType = Integer.valueOf(getDataNtType());
    }

    private void registerRcv() {
        this.receiver = new BroadcastReceiver() { // from class: com.mob.tools.utils.NtFetcher.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")) {
                        NtFetcher.this.refreshNet();
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        try {
            ReflectHelper.invokeInstanceMethod(this.context, "registerReceiver", new Object[]{this.receiver, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
        } catch (Throwable unused) {
        }
    }

    private void unregisterRcv() {
        if (this.receiver != null) {
            try {
                ReflectHelper.invokeInstanceMethod(this.context, "unregisterReceiver", new Object[]{this.receiver}, new Class[]{BroadcastReceiver.class});
            } catch (Throwable unused) {
            }
            this.receiver = null;
        }
    }

    public synchronized int getDtNtType() {
        if (!this.device.isSensitiveDevice() || this.dtNtType == null) {
            this.dtNtType = Integer.valueOf(getDataNtType());
        }
        return this.dtNtType.intValue();
    }

    public synchronized String getNtType() {
        if (!this.device.isSensitiveDevice() || TextUtils.isEmpty(this.ntType)) {
            this.ntType = getNetworkType();
        }
        return this.ntType;
    }

    public void recycle() {
        unregisterRcv();
    }
}
