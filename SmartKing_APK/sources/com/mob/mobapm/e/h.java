package com.mob.mobapm.e;

import android.text.TextUtils;
import android.util.Base64;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.mobapm.bean.SocketTransaction;
import com.mob.mobapm.core.Transaction;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class h {
    private static h c;
    private Hashon a = new Hashon();
    public byte[] b;

    private h() {
        String str;
        String replace = DeviceHelper.getInstance(MobSDK.getContext()).getModel().replace(SQLBuilder.BLANK, "");
        String str2 = "";
        if (replace.length() > 16) {
            str = replace.substring(0, 16);
        } else if (16 % replace.length() == 0) {
            for (int i = 0; i < 16 / replace.length(); i++) {
                str2 = str2 + replace;
            }
            str = str2;
        } else {
            String str3 = "";
            for (int i2 = 0; i2 < 16 / replace.length(); i2++) {
                str3 = str3 + replace;
            }
            str = str3 + replace.substring(0, 16 % replace.length());
        }
        this.b = str.getBytes();
    }

    public static synchronized h a() {
        h hVar;
        synchronized (h.class) {
            if (c == null) {
                c = new h();
            }
            hVar = c;
        }
        return hVar;
    }

    public synchronized <ReturnType> ReturnType a(String str, Class<ReturnType> cls) {
        try {
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: decodeToObject trans error: " + th, new Object[0]);
            return null;
        }
        return (ReturnType) this.a.fromJson(new String(Data.AES128Decode(this.b, Base64.decode(str, 2)), "utf-8"), cls);
    }

    public String a(Object obj) {
        try {
            HashMap fromJson = this.a.fromJson(this.a.fromObject(obj));
            fromJson.remove("transStatus");
            fromJson.remove("transType");
            fromJson.remove("query");
            fromJson.remove("body");
            return Base64.encodeToString(Data.AES128Encode(this.b, this.a.fromHashMap(fromJson)), 2);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: encode trans error: " + th, new Object[0]);
            return null;
        }
    }

    public synchronized String a(HashMap<String, Object> hashMap) {
        try {
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: encode trans error: " + th, new Object[0]);
            return null;
        }
        return Base64.encodeToString(Data.AES128Encode(this.b, this.a.fromHashMap(hashMap)), 2);
    }

    public synchronized HashMap<String, Object> a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return this.a.fromJson(new String(Data.AES128Decode(this.b, Base64.decode(str, 2)), "utf-8"));
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().i("APM: encode trans error: " + th, new Object[0]);
            }
        }
        return null;
    }

    public String b(Object obj) {
        try {
            HashMap fromJson = this.a.fromJson(this.a.fromObject(obj));
            HashMap hashMap = new HashMap();
            if (fromJson.containsKey("networkType")) {
                hashMap.put("networkType", fromJson.get("networkType"));
            }
            if (fromJson.containsKey("dataNetworkType")) {
                hashMap.put("dataNetworkType", fromJson.get("dataNetworkType"));
            }
            if (fromJson.containsKey("duid")) {
                hashMap.put("duid", fromJson.get("duid"));
            }
            if (fromJson.containsKey("host")) {
                hashMap.put("host", fromJson.get("host"));
            }
            if (fromJson.containsKey(FileDownloadModel.PATH)) {
                hashMap.put(FileDownloadModel.PATH, fromJson.get(FileDownloadModel.PATH));
            }
            if (fromJson.containsKey("requestTime")) {
                hashMap.put("requestTime", fromJson.get("requestTime"));
            }
            if (fromJson.containsKey("responseTime")) {
                hashMap.put("responseTime", fromJson.get("responseTime"));
            }
            if (fromJson.containsKey("requestDuration")) {
                hashMap.put("requestDuration", fromJson.get("requestDuration"));
            }
            if (fromJson.containsKey("clientTime")) {
                hashMap.put("clientTime", fromJson.get("clientTime"));
            }
            if (fromJson.containsKey("method")) {
                hashMap.put("method", fromJson.get("method"));
            }
            if (fromJson.containsKey("query")) {
                hashMap.put("query", fromJson.get("query"));
            }
            if (fromJson.containsKey("body")) {
                hashMap.put("body", fromJson.get("body"));
            }
            return Base64.encodeToString(Data.AES128Encode(this.b, this.a.fromHashMap(hashMap)), 2);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: encode trans error: " + th, new Object[0]);
            return null;
        }
    }

    public synchronized List<HashMap<String, Object>> b(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return (List) ((HashMap) this.a.fromJson(new String(Data.AES128Decode(this.b, Base64.decode(str, 2)), "utf-8"), HashMap.class)).get("fakelist");
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().i("APM: encode trans error: " + th, new Object[0]);
            }
        }
        return null;
    }

    public synchronized SocketTransaction c(String str) {
        try {
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: decode socket trans error: " + th, new Object[0]);
            return null;
        }
        return (SocketTransaction) this.a.fromJson(new String(Data.AES128Decode(this.b, Base64.decode(str, 2)), "utf-8"), SocketTransaction.class);
    }

    public String d(String str) {
        try {
            return new String(Data.AES128Decode(this.b, Base64.decode(str, 2)), "utf-8");
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: encode trans error: " + th, new Object[0]);
            return null;
        }
    }

    public synchronized Transaction e(String str) {
        try {
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: decode trans error: " + th, new Object[0]);
            return null;
        }
        return (Transaction) this.a.fromJson(new String(Data.AES128Decode(this.b, Base64.decode(str, 2)), "utf-8"), Transaction.class);
    }

    public String f(String str) {
        try {
            return Base64.encodeToString(Data.AES128Encode(this.b, str), 2);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: encode trans error: " + th, new Object[0]);
            return null;
        }
    }
}
