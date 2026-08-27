package com.mob.mobapm.e;

import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import java.util.HashMap;

/* loaded from: classes.dex */
public class g {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a extends Thread {
        a() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            try {
                HashMap hashMap = new HashMap();
                hashMap.put("appKey", MobSDK.getAppkey());
                hashMap.put("appPkg", MobSDK.getContext().getPackageName());
                hashMap.put("appVersion", String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getAppVersionName()));
                hashMap.put("plat", 1);
                hashMap.put("carrier", DeviceHelper.getInstance(MobSDK.getContext()).getCarrier());
                hashMap.put("factory", DeviceHelper.getInstance(MobSDK.getContext()).getManufacturer());
                hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, DeviceHelper.getInstance(MobSDK.getContext()).getModel());
                hashMap.put("systemVersion", DeviceHelper.getInstance(MobSDK.getContext()).getOSVersionName());
                hashMap.put("systemVersionInt", Integer.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getOSVersionInt()));
                hashMap.put("language", DeviceHelper.getInstance(MobSDK.getContext()).getOSLanguage());
                hashMap.put("serialno", DeviceHelper.getInstance(MobSDK.getContext()).getSerialno());
                hashMap.put("deviceId", DeviceHelper.getInstance(MobSDK.getContext()).getDeviceKey());
                com.mob.mobapm.b.a.e(hashMap);
            } catch (Throwable unused) {
            }
        }
    }

    public static void a() {
        new a().start();
    }
}
