package cn.smssdk.logger;

import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.f;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobSDK;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

/* compiled from: SMSLogger.java */
/* loaded from: classes.dex */
public class d {
    private static ILoggerEngine b;
    private static HashMap<Integer, Long> c;
    private static d d;
    private Hashon a = new Hashon();

    private d() {
        c = new HashMap<>();
        b = b.a();
    }

    private HashMap<String, Object> a(Object obj, long j, long j2, int i) {
        HashMap<String, Object> c2 = c();
        c2.put("type", d(i));
        c2.put("time", Long.valueOf(j));
        c2.put("costTime", Long.valueOf(j2));
        c2.put("method", Integer.valueOf(i));
        if (obj instanceof Throwable) {
            Throwable th = (Throwable) obj;
            String message = th.getMessage();
            if (!TextUtils.isEmpty(message)) {
                try {
                    JSONObject jSONObject = new JSONObject(message);
                    int optInt = jSONObject.optInt("status");
                    if (optInt == 615 || i == 1) {
                        c2.put("innerDesc", f.a(th));
                        c2.put("innerCode", 615);
                    } else {
                        c2.put("innerCode", Integer.valueOf(optInt));
                        if (message.contains("detail")) {
                            c2.put("innerDesc", jSONObject.optString("detail"));
                        } else {
                            c2.put("innerDesc", "No Message");
                        }
                    }
                } catch (Throwable unused) {
                    SMSLog.getInstance().d(SMSLog.FORMAT, "SMSLogger", "prepareListParams", "data: " + obj);
                }
            }
            c2.put("isError", true);
        } else if (obj instanceof String) {
            String str = (String) obj;
            if (str.contains("sdkMode")) {
                return (HashMap) this.a.fromJson(str, HashMap.class);
            }
            c2.put("innerCode", 200);
            if (str == null || str.equals("")) {
                c2.put("innerDesc", "No message");
            } else {
                c2.put("innerDesc", str);
            }
        } else {
            c2.put("isError", false);
            String valueOf = String.valueOf(obj);
            c2.put("innerCode", 200);
            if (valueOf.equals("null")) {
                c2.put("innerDesc", "No message");
            } else {
                c2.put("innerDesc", valueOf);
            }
        }
        return c2;
    }

    private int c(int i) {
        int i2 = 2;
        if (i != 2) {
            i2 = 3;
            if (i != 3) {
                if (i != 6) {
                    return i != 8 ? -1 : 4;
                }
                return 1;
            }
        }
        return i2;
    }

    private HashMap<String, Object> c() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("appkey", MobSDK.getAppkey());
        hashMap.put("plat", 1);
        hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, a.p().b());
        hashMap.put("deviceId", a.p().a());
        hashMap.put("net", a.p().h());
        hashMap.put("operator", a.p().m());
        hashMap.put("pkg", a.p().j());
        hashMap.put("md5", a.p().f());
        hashMap.put("sdkver", a.p().l());
        hashMap.put("duid", a.p().d());
        hashMap.put(NotificationCompat.CATEGORY_SYSTEM, Integer.valueOf(a.p().i()));
        hashMap.put("romVersion", a.p().k());
        hashMap.put("sdkMode", "NORMAL");
        hashMap.put("dbm", Integer.valueOf(a.p().g()));
        hashMap.put("wifidbm", Integer.valueOf(a.p().n()));
        hashMap.put("mac", a.p().e());
        hashMap.put("deviceName", a.p().c());
        return hashMap;
    }

    public static d d() {
        if (d == null) {
            synchronized (d.class) {
                if (d == null) {
                    d = new d();
                }
            }
        }
        return d;
    }

    private String d(int i) {
        switch (i) {
            case 1:
                return "init";
            case 2:
            case 3:
            case 4:
                return "code";
            default:
                return "token";
        }
    }

    private List<c> e() {
        return b.getLogList();
    }

    public long a(int i) {
        if (c.containsKey(Integer.valueOf(i))) {
            return c.get(Integer.valueOf(i)).longValue();
        }
        return 0L;
    }

    public void a() {
        b.deleteAllLogItems();
    }

    public void a(int i, long j, String str) {
        b.insertOneRequestLog(i, j, str);
    }

    public void a(int i, Object obj) {
        long a = a(i);
        int c2 = c(i);
        if (c2 != -1) {
            long currentTimeMillis = System.currentTimeMillis();
            HashMap<String, Object> a2 = a(obj, currentTimeMillis, currentTimeMillis - a, c2);
            HashMap<String, Object> hashMap = new HashMap<>();
            ArrayList arrayList = new ArrayList();
            arrayList.add(a2);
            hashMap.put("list", arrayList);
            if (cn.smssdk.net.f.d().a(hashMap)) {
                return;
            }
            a(c2, currentTimeMillis, this.a.fromHashMap(a2));
            SMSLog.getInstance().d(SMSLog.FORMAT, "SMSLogger", "uploadOrSave", "Upload SDK LOG Faied,So insert into db");
        }
    }

    public void b() {
        List<c> e = e();
        if (e == null || e.size() == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (c cVar : e) {
            arrayList.add(a(cVar.c(), cVar.a(), cVar.b(), cVar.d()));
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", arrayList);
        if (cn.smssdk.net.f.d().a(hashMap)) {
            a();
            SMSLog.getInstance().d(SMSLog.FORMAT, "SMSLogger", "uploadAllLogs", "Upload SDK LOG Success,delete the LogItem in db");
        }
    }

    public void b(int i) {
        c.put(Integer.valueOf(i), Long.valueOf(System.currentTimeMillis()));
    }
}
