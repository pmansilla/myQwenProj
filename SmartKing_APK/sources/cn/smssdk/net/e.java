package cn.smssdk.net;

import cn.smssdk.utils.SMSLog;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* compiled from: ParamsBuilder.java */
/* loaded from: classes.dex */
public class e {
    private static boolean b;
    private static DeviceHelper c;
    private static HashMap<String, Object> d;
    private static boolean e;
    private static e f;
    private Hashon a = new Hashon();

    private e() {
    }

    public static e a() {
        if (f == null) {
            synchronized (e.class) {
                f = new e();
            }
        }
        return f;
    }

    public static void b() {
        String str;
        String carrier;
        c = DeviceHelper.getInstance(MobSDK.getContext());
        d = new HashMap<>();
        d.put("plat", Integer.valueOf(c.getPlatformCode()));
        d.put("sdkver", Integer.valueOf(cn.smssdk.utils.f.b()));
        d.put("md5", c.getSignMD5());
        try {
            e = c.checkPermission("android.permission.READ_PHONE_STATE");
        } catch (Throwable th) {
            SMSLog.getInstance().d(th, SMSLog.FORMAT, "ParamsBuilder", "prepare", "Obtain device info error");
        }
        if (e) {
            str = c.getSimSerialNumber();
            carrier = c.getCarrier();
            if (carrier != null && !carrier.equals("-1")) {
                d.put("operator", carrier);
            }
            if (str != null && !str.equals("-1")) {
                d.put("simserial", str);
            }
            d.put("apppkg", c.getPackageName());
            d.put("appver", c.getAppVersionName());
            b = true;
        }
        str = null;
        carrier = c.getCarrier();
        if (carrier != null) {
            d.put("operator", carrier);
        }
        if (str != null) {
            d.put("simserial", str);
        }
        d.put("apppkg", c.getPackageName());
        d.put("appver", c.getAppVersionName());
        b = true;
    }

    public HashMap<String, Object> a(int i, ArrayList<String> arrayList, String str, String str2, HashMap<String, Object> hashMap) throws Throwable {
        if (!b) {
            throw new Throwable("ParamsBuilder need prepare before use");
        }
        if (cn.smssdk.utils.a.c.booleanValue()) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "ParamsBuilder", "buildParams", "Build params. config: " + cn.smssdk.utils.c.a(arrayList));
            String fromHashMap = hashMap != null ? this.a.fromHashMap(hashMap) : null;
            SMSLog.getInstance().d(SMSLog.FORMAT, "ParamsBuilder", "buildParams", "Build params. extParams: " + fromHashMap);
        }
        HashMap<String, Object> hashMap2 = new HashMap<>();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next.equals("appkey")) {
                hashMap2.put("appkey", MobSDK.getAppkey());
            } else if (next.equals("token")) {
                hashMap2.put("token", str2);
            } else if (next.equals("duid")) {
                hashMap2.put("duid", str);
            } else {
                Object obj = d.get(next);
                if (obj == null) {
                    obj = hashMap.get(next);
                }
                hashMap2.put(next, obj);
            }
        }
        return hashMap2;
    }
}
