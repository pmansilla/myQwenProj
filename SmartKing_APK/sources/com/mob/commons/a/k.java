package com.mob.commons.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import com.amap.location.common.model.Adjacent;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import org.json.JSONObject;

/* compiled from: DvcClt.java */
/* loaded from: classes.dex */
public class k extends d {
    private Hashon a;
    private Random b;
    private BroadcastReceiver c;
    private BroadcastReceiver d;
    private BroadcastReceiver e;
    private boolean g = false;
    private boolean h = false;
    private boolean i = false;
    private DeviceHelper f = DeviceHelper.getInstance(MobSDK.getContext());

    k() {
    }

    private HashMap<String, Object> a(Location location) {
        if (location == null) {
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
        String ssid = this.f.getSSID();
        String bssid = this.f.getBssid();
        if (!TextUtils.isEmpty(bssid)) {
            hashMap.put("cbsmt", bssid);
        }
        if (!TextUtils.isEmpty(ssid)) {
            hashMap.put("cssmt", ssid);
        }
        return hashMap;
    }

    private void a(Parcelable parcelable) {
        try {
            if (((NetworkInfo) parcelable).isConnected()) {
                TreeMap treeMap = new TreeMap();
                treeMap.put(Dic.SSID, this.f.getSSID());
                treeMap.put(Dic.BSSID, this.f.getBssid());
                String MD5 = Data.MD5(new JSONObject(treeMap).toString());
                String b = com.mob.commons.i.b();
                if ((b == null || !b.equals(MD5)) && com.mob.commons.b.r()) {
                    i();
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private void a(ArrayList<HashMap<String, Object>> arrayList) {
        synchronized (this) {
            try {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("type", "WLMT");
                hashMap.put("list", arrayList);
                hashMap.put("cl", f());
                hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
                com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
    }

    private void a(ArrayList<HashMap<String, Object>> arrayList, int i) {
        HashMap<String, Object> a;
        Location location = i == 1 ? this.f.getLocation(30, 0, true) : this.f.getLocation(0, 15, true);
        if (location == null || (a = a(location)) == null || a.isEmpty()) {
            return;
        }
        a.put("lctpmt", Integer.valueOf(i));
        if (arrayList != null && !arrayList.isEmpty()) {
            a.put("wilmt", arrayList);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "LCMT");
        hashMap.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, a);
        hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
        com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap);
    }

    private void b(ArrayList<HashMap<String, Object>> arrayList) {
        this.h = false;
        if (com.mob.commons.b.p()) {
            try {
                a(arrayList, 2);
                a(arrayList, 1);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
        a(5, com.mob.commons.b.q() * 1000);
    }

    private void c(int i) {
        if (this.d == null) {
            this.d = new BroadcastReceiver() { // from class: com.mob.commons.a.k.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (k.this.i && "android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
                        k.this.i = false;
                        k.this.b(8);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "registerReceiver", new Object[]{this.d, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            } catch (Throwable unused) {
            }
        }
    }

    private void h() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("pmmt", this.f.getBluetoothName());
            hashMap.put("signmd5", this.f.getSignMD5());
            hashMap.put("boardname", Build.BOARD);
            hashMap.put("devicename", Build.DEVICE);
            hashMap.put("displayid", Build.DISPLAY);
            hashMap.put("fingerprint", Build.FINGERPRINT);
            if (Build.VERSION.SDK_INT >= 14) {
                hashMap.put("radiover", Build.getRadioVersion());
            } else {
                hashMap.put("radiover", null);
            }
            hashMap.put("density", Float.valueOf(ResHelper.getDensity(MobSDK.getContext())));
            hashMap.put("densitydpi", Integer.valueOf(ResHelper.getDensityDpi(MobSDK.getContext())));
            hashMap.put(Adjacent.BTM, this.f.getBTMac());
            hashMap.put("btmp", this.f.getBTMacFromProvider());
            hashMap.put("bt", Integer.valueOf(this.f.isBT() ? 1 : 0));
            hashMap.put("cameraResolutions", this.f.getCamResolution());
            hashMap.put("timezone", this.f.getTimezone());
            hashMap.put("cpuType", this.f.getCPUType());
            hashMap.put("flavor", this.f.getFlavor());
            hashMap.put("features", this.f.getSupport());
            hashMap.put("defaultInputMethod", this.f.getDefaultIM());
            hashMap.put("inputMethods", this.f.getIMList());
            hashMap.put("brand", this.f.getBrand());
            hashMap.put("isSimulator", Boolean.valueOf(this.f.isSmlt()));
            hashMap.put("ipInfo", this.f.getLocalIpInfo());
            String MD5 = Data.MD5(v().fromHashMap(hashMap));
            String a = com.mob.commons.i.a();
            boolean z = com.mob.commons.b.a() >= com.mob.commons.i.o();
            if (a == null || !a.equals(MD5) || z) {
                HashMap<String, Object> hashMap2 = new HashMap<>();
                hashMap2.put("type", "DEXTMT");
                hashMap2.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap);
                hashMap2.put("datetime", Long.valueOf(com.mob.commons.b.a()));
                com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap2);
                com.mob.commons.i.a(MD5);
                com.mob.commons.i.e(com.mob.commons.b.a() - 1702967296);
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private void i() {
        ArrayList<HashMap<String, Object>> availableWifiList;
        synchronized (this) {
            HashMap hashMap = new HashMap();
            try {
                String bssid = this.f.getBssid();
                if (!TextUtils.isEmpty(bssid) && (availableWifiList = this.f.getAvailableWifiList()) != null && !availableWifiList.isEmpty()) {
                    Iterator<HashMap<String, Object>> it = availableWifiList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        HashMap<String, Object> next = it.next();
                        Object obj = next.get("BSSID");
                        if (obj != null && String.valueOf(obj).equals(bssid)) {
                            hashMap.putAll(next);
                            break;
                        }
                    }
                    hashMap.remove("BSSID");
                    hashMap.remove("SSID");
                }
                HashMap<String, Object> currentWifiInfo = this.f.getCurrentWifiInfo();
                if (currentWifiInfo != null) {
                    hashMap.putAll(currentWifiInfo);
                }
                String ssid = this.f.getSSID();
                hashMap.put(Dic.SSID, ssid);
                hashMap.put(Dic.BSSID, bssid);
                HashMap<String, Object> hashMap2 = new HashMap<>();
                hashMap2.put("type", "WIMT");
                hashMap2.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap);
                hashMap2.put("cl", f());
                long a = com.mob.commons.b.a();
                hashMap2.put("datetime", Long.valueOf(a));
                com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap2);
                com.mob.commons.i.a(a);
                TreeMap treeMap = new TreeMap();
                treeMap.put(Dic.SSID, ssid);
                treeMap.put(Dic.BSSID, bssid);
                com.mob.commons.i.b(Data.MD5(new JSONObject(treeMap).toString()));
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
            a(2, com.mob.commons.b.s() * 1000);
        }
    }

    private void j() {
        if (this.c == null) {
            this.c = new BroadcastReceiver() { // from class: com.mob.commons.a.k.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (intent == null || !"android.net.wifi.STATE_CHANGE".equals(intent.getAction())) {
                        return;
                    }
                    try {
                        Parcelable parcelableExtra = intent.getParcelableExtra("networkInfo");
                        if (parcelableExtra != null) {
                            Message message = new Message();
                            message.what = 9;
                            message.obj = parcelableExtra;
                            k.this.b(message);
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().w(th);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.STATE_CHANGE");
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "registerReceiver", new Object[]{this.c, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            } catch (Throwable unused) {
            }
        }
    }

    private void k() {
        if (this.c != null) {
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "unregisterReceiver", new Object[]{this.c}, new Class[]{BroadcastReceiver.class});
            } catch (Throwable unused) {
            }
            this.c = null;
        }
    }

    private void l() throws Throwable {
        int i;
        try {
            i = Integer.parseInt(this.f.getCarrier());
        } catch (Throwable unused) {
            i = -1;
        }
        int cellLac = this.f.getCellLac();
        int cellId = this.f.getCellId();
        int psc = this.f.getPsc();
        HashMap hashMap = null;
        if (i != -1 && cellLac != -1 && cellId != -1) {
            hashMap = new HashMap();
            hashMap.put("lac", Integer.valueOf(cellLac));
            hashMap.put("cell", Integer.valueOf(cellId));
            if (psc != -1) {
                hashMap.put("psc", Integer.valueOf(psc));
            }
        }
        int cdmaBid = this.f.getCdmaBid();
        int cdmaSid = this.f.getCdmaSid();
        int cdmaNid = this.f.getCdmaNid();
        int cdmaLat = this.f.getCdmaLat();
        int cdmaLon = this.f.getCdmaLon();
        if (i != -1 && cdmaBid != -1 && cdmaSid != -1 && cdmaNid != -1) {
            if (hashMap == null) {
                hashMap = new HashMap();
            }
            hashMap.put("bid", Integer.valueOf(cdmaBid));
            hashMap.put("sid", Integer.valueOf(cdmaSid));
            hashMap.put("nid", Integer.valueOf(cdmaNid));
            if (cdmaLat != -1) {
                hashMap.put("lat", Integer.valueOf(cdmaLat));
            }
            if (cdmaLon != -1) {
                hashMap.put("lon", Integer.valueOf(cdmaLon));
            }
        }
        if (hashMap != null) {
            hashMap.put("carrier", Integer.valueOf(i));
            hashMap.put("simopname", this.f.getCarrierName());
            ArrayList<HashMap<String, Object>> neighboringCellInfo = this.f.getNeighboringCellInfo();
            if (neighboringCellInfo != null && neighboringCellInfo.size() > 0) {
                hashMap.put("nearby", neighboringCellInfo);
            }
            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("type", "BSIOMT");
            hashMap2.put("cl", f());
            hashMap2.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap);
            hashMap2.put("datetime", Long.valueOf(com.mob.commons.b.a()));
            com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap2);
            com.mob.commons.i.c(Data.MD5(v().fromHashMap(hashMap)));
        }
        com.mob.commons.i.b(com.mob.commons.b.a() + (com.mob.commons.b.o() * 1000));
    }

    private boolean m() throws Throwable {
        int i;
        try {
            i = Integer.parseInt(this.f.getCarrier());
        } catch (Throwable unused) {
            i = -1;
        }
        int cellLac = this.f.getCellLac();
        int cellId = this.f.getCellId();
        if (i == -1 || cellLac == -1 || cellId == -1) {
            return false;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("carrier", Integer.valueOf(i));
        hashMap.put("simopname", this.f.getCarrierName());
        hashMap.put("lac", Integer.valueOf(cellLac));
        hashMap.put("cell", Integer.valueOf(cellId));
        String MD5 = Data.MD5(v().fromHashMap(hashMap));
        String c = com.mob.commons.i.c();
        return c == null || !c.equals(MD5);
    }

    private void n() {
        HashMap<String, Object> a;
        Location location = this.f.getLocation(0, 0, true);
        if (location == null || (a = a(location)) == null || a.isEmpty()) {
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "O_LCMT");
        hashMap.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, a);
        hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
        TreeMap treeMap = new TreeMap();
        treeMap.put("ltdmt", Double.valueOf(location.getLatitude()));
        treeMap.put("lndmt", Double.valueOf(location.getLongitude()));
        String MD5 = Data.MD5(new JSONObject(treeMap).toString());
        String q = com.mob.commons.i.q();
        long r = com.mob.commons.i.r();
        long a2 = com.mob.commons.b.a();
        if (!TextUtils.isEmpty(q) && q.equals(MD5) && a2 < r) {
            MobLog.getInstance().d("o_loc: no", new Object[0]);
            return;
        }
        MobLog.getInstance().d("o_loc: yes", new Object[0]);
        com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap);
        com.mob.commons.i.j(MD5);
        com.mob.commons.i.g(a2 + (com.mob.commons.b.B() * 1000));
    }

    private void o() {
        try {
            ArrayList<HashMap<String, Object>> u = u();
            if (u == null || u.isEmpty()) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<HashMap<String, Object>> it = u.iterator();
            while (it.hasNext()) {
                Object obj = it.next().get("BSSID");
                if (obj != null) {
                    arrayList.add(String.valueOf(obj));
                }
            }
            Collections.sort(arrayList);
            String MD5 = Data.MD5(TextUtils.join("", arrayList));
            String i = com.mob.commons.i.i();
            long p = com.mob.commons.i.p();
            long a = com.mob.commons.b.a();
            MobLog.getInstance().d("wiHashLast: " + i, new Object[0]);
            MobLog.getInstance().d("wiHash: " + MD5, new Object[0]);
            if (i == null || !i.equals(MD5) || p < a) {
                a(u);
                com.mob.commons.i.g(MD5);
                com.mob.commons.i.f(com.mob.commons.b.a() + (com.mob.commons.b.v() * 1000));
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private void p() {
        if (this.d != null) {
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "unregisterReceiver", new Object[]{this.d}, new Class[]{BroadcastReceiver.class});
            } catch (Throwable unused) {
            }
            this.d = null;
        }
    }

    private void q() {
        r();
        this.h = true;
        this.f.scanWifiList();
    }

    private void r() {
        if (this.e == null) {
            this.e = new BroadcastReceiver() { // from class: com.mob.commons.a.k.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (k.this.h && "android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
                        k.this.b(11);
                        k.this.s();
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "registerReceiver", new Object[]{this.e, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        if (this.e != null) {
            try {
                ReflectHelper.invokeInstanceMethod(MobSDK.getContext(), "unregisterReceiver", new Object[]{this.e}, new Class[]{BroadcastReceiver.class});
            } catch (Throwable unused) {
            }
            this.e = null;
        }
    }

    private void t() {
        try {
            a(10);
            if (this.g) {
                return;
            }
            b(u());
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private ArrayList<HashMap<String, Object>> u() {
        ArrayList<HashMap<String, Object>> availableWifiList;
        ArrayList<String> w;
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            availableWifiList = this.f.getAvailableWifiList();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        if (availableWifiList != null && !availableWifiList.isEmpty() && (w = com.mob.commons.b.w()) != null && !w.isEmpty()) {
            String bssid = this.f.getBssid();
            Iterator<HashMap<String, Object>> it = availableWifiList.iterator();
            while (it.hasNext()) {
                HashMap<String, Object> next = it.next();
                Object obj = next.get("BSSID");
                if (obj != null && String.valueOf(obj).equals(bssid)) {
                    next.put("___curConn", true);
                    bssid = null;
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                Iterator<String> it2 = w.iterator();
                while (it2.hasNext()) {
                    String next2 = it2.next();
                    Object obj2 = next.get(next2);
                    if (obj2 != null) {
                        hashMap.put(next2, obj2);
                    }
                }
                arrayList.add(hashMap);
            }
            return arrayList;
        }
        return arrayList;
    }

    private Hashon v() {
        if (this.a == null) {
            this.a = new Hashon();
        }
        return this.a;
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.dic_lock");
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x00e2  */
    @Override // com.mob.commons.a.d
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void a(android.os.Message r8) {
        /*
            Method dump skipped, instructions count: 346
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.a.k.a(android.os.Message):void");
    }

    @Override // com.mob.commons.a.d
    protected void b() {
        k();
        p();
        s();
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(1);
        b(2);
        Message obtain = Message.obtain();
        obtain.arg1 = -1;
        obtain.what = 6;
        b(obtain);
        b(3);
        b(5);
        b(7);
    }
}
