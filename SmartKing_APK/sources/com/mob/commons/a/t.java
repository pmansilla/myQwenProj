package com.mob.commons.a;

import android.content.pm.PackageInfo;
import android.os.Message;
import android.util.Base64;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.SharePrefrenceHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* compiled from: VplClt.java */
/* loaded from: classes.dex */
public class t extends d {
    private SharePrefrenceHelper a;

    private static Object a(HashMap<String, Object> hashMap, String str) throws Throwable {
        if (com.mob.commons.b.aq() && !com.mob.commons.b.L()) {
            return new MobCommunicator(1024, "009cbd92ccef123be840deec0c6ed0547194c1e471d11b6f375e56038458fb18833e5bab2e1206b261495d7e2d1d9e5aa859e6d4b671a8ca5d78efede48e291a3f", "1dfd1d615cb891ce9a76f42d036af7fce5f8b8efaa11b2f42590ecc4ea4cff28f5f6b0726aeb76254ab5b02a58c1d5b486c39d9da1a58fa6ba2f22196493b3a4cbc283dcf749bf63679ee24d185de70c8dfe05605886c9b53e9f569082eabdf98c4fb0dcf07eb9bb3e647903489ff0b5d933bd004af5be4a1022fdda41f347f1").requestSynchronized(hashMap, str, false);
        }
        return null;
    }

    private Object b(HashMap<String, Object> hashMap) {
        try {
            hashMap.put("cplAt", Long.valueOf(com.mob.commons.b.a()));
            return a(hashMap, com.mob.commons.j.a("v.data.mob.com/cpl"));
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    private synchronized void l() {
        if (this.a == null) {
            this.a = new SharePrefrenceHelper(MobSDK.getContext());
            this.a.open("vpl_cache");
        }
    }

    private void m() {
        try {
            DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
            HashMap<String, Object> hashMap = new HashMap<>();
            String appkey = MobSDK.getAppkey();
            String authorize = DeviceAuthorizer.authorize(null);
            hashMap.put("appkey", appkey);
            hashMap.put("apppkg", deviceHelper.getPackageName());
            hashMap.put("appver", Integer.valueOf(deviceHelper.getAppVersion()));
            hashMap.put("duid", authorize);
            hashMap.put("plat", Integer.valueOf(deviceHelper.getPlatformCode()));
            hashMap.put("networktype", deviceHelper.getNetworkType());
            hashMap.put("lastVplAt", Long.valueOf(i()));
            Object encodeToString = Base64.encodeToString((appkey + ":" + authorize).getBytes("utf-8"), 2);
            hashMap.put("lastVplId", encodeToString);
            HashMap hashMap2 = (HashMap) a(hashMap, com.mob.commons.j.a("v.data.mob.com/vpl"));
            if (hashMap2 != null && hashMap2.size() != 0) {
                j();
                List<String> list = (List) hashMap2.get("pkgs");
                if (list == null || list.size() <= 0) {
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (String str : list) {
                    HashMap hashMap3 = new HashMap();
                    hashMap3.put("apppkg", str);
                    try {
                        PackageInfo packageInfo = MobSDK.getContext().getPackageManager().getPackageInfo(str, 0);
                        hashMap3.put("appver", packageInfo.versionName);
                        hashMap3.put("issys", Boolean.valueOf(((packageInfo.applicationInfo.flags & 1) == 1) || ((packageInfo.applicationInfo.flags & 128) == 1)));
                    } catch (Throwable unused) {
                    }
                    arrayList.add(hashMap3);
                }
                hashMap.remove("networktype");
                hashMap.remove("lastVplAt");
                hashMap.remove("lastVplId");
                hashMap.put(Dic.MAC, deviceHelper.getMacAddress());
                hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, deviceHelper.getModel());
                hashMap.put(Dic.IMEI, deviceHelper.getIMEI());
                hashMap.put(Dic.SERIAL_NO, deviceHelper.getSerialno());
                hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
                hashMap.put(ConnectionModel.ID, encodeToString);
                hashMap.put("pkgs", arrayList);
                Object b = b(hashMap);
                if (b == null) {
                    b = b(hashMap);
                }
                if (b == null) {
                    a(hashMap);
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return h();
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (message.what == 0) {
            long G = com.mob.commons.b.G();
            if (G > 0) {
                try {
                    Thread.sleep(G * 1000);
                    HashMap<String, Object> k = k();
                    if (k != null && !k.isEmpty() && b(k) != null) {
                        a((HashMap<String, Object>) null);
                    }
                } catch (Throwable unused) {
                }
                m();
                this.a = null;
                a(0, com.mob.commons.b.H() * 1000);
            }
        }
    }

    public synchronized void a(HashMap<String, Object> hashMap) {
        l();
        if (hashMap == null) {
            this.a.remove("LAST_FAILED_DATA");
        } else {
            this.a.put("LAST_FAILED_DATA", hashMap);
        }
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.G() > 0;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(0);
    }

    public File h() {
        File file = new File(ResHelper.getDataCache(MobSDK.getContext()), ".vpl_lock");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public synchronized long i() {
        l();
        return this.a.getLong("LAST_UPLOAD_TIME");
    }

    public synchronized void j() {
        l();
        this.a.putLong("LAST_UPLOAD_TIME", Long.valueOf(com.mob.commons.b.a()));
    }

    public synchronized HashMap<String, Object> k() {
        l();
        return (HashMap) this.a.get("LAST_FAILED_DATA");
    }
}
