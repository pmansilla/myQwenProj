package cn.smssdk.net;

import android.text.TextUtils;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.SPHelper;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.HashMap;
import me.panpf.sketch.uri.HttpUriModel;

/* compiled from: ServiceApi.java */
/* loaded from: classes.dex */
public class g extends a {
    private int k;
    private long l;
    private SPHelper m = SPHelper.getInstance();

    private String b(HashMap<String, Object> hashMap) {
        StringBuffer stringBuffer = new StringBuffer();
        String str = (String) hashMap.get("host");
        int intValue = ((Integer) hashMap.get("port")).intValue();
        String str2 = (String) hashMap.get("action");
        if (!TextUtils.isEmpty(str) && !str.contains(HttpUriModel.SCHEME)) {
            stringBuffer.append(HttpUriModel.SCHEME);
        }
        stringBuffer.append(str);
        stringBuffer.append(":");
        stringBuffer.append(intValue);
        stringBuffer.append(str2);
        return stringBuffer.toString();
    }

    private void e() {
        if (this.f) {
            this.l = this.m.getLastRequestTimeMillis(this.b);
        }
    }

    private void f() {
        this.m.setLastRequestTimeMillis(this.b, this.l);
    }

    @Override // cn.smssdk.net.a
    protected HashMap<String, Object> a(String str, String str2, HashMap<String, Object> hashMap) throws Throwable {
        ArrayList<String> arrayList = this.h;
        if (arrayList != null && arrayList.size() > 0) {
            return e.a().a(this.a, this.h, str, str2, hashMap);
        }
        SMSLog.getInstance().e(SMSLog.FORMAT, "ServiceApi", "buildParams", "[" + this.b + "]Can not build request params since listParam is null.");
        throw new Throwable("Can not build request params since listParam is null.");
    }

    public void a(HashMap<String, Object> hashMap) throws Throwable {
        this.b = (String) hashMap.get(IMAPStore.ID_NAME);
        if (TextUtils.isEmpty(this.b)) {
            throw new Throwable("GET API NAME ERROR");
        }
        if (this.b.equals("getZoneList")) {
            this.a = 2;
        } else if (this.b.equals("getToken")) {
            this.a = 3;
        } else if (this.b.equals("submitUser")) {
            this.a = 4;
        } else if (this.b.equals("logCollect")) {
            this.a = 7;
        } else if (this.b.equals("logInstall")) {
            this.a = 8;
        } else if (this.b.equals("sendTextSMS")) {
            this.a = 9;
        } else if (this.b.equals("sendVoiceSMS")) {
            this.a = 10;
        } else if (this.b.equals("verifyCode")) {
            this.a = 11;
        } else if (this.b.equals("uploadCollectData")) {
            this.a = 12;
        } else if (this.b.equals("sdkLog")) {
            this.a = 13;
        } else {
            SMSLog.getInstance().w(SMSLog.FORMAT, "ServiceApi", "parseConfig", "Unknown api type. name: " + this.b);
            this.a = 0;
        }
        this.c = b(hashMap);
        this.h = (ArrayList) hashMap.get("params");
        ArrayList<String> arrayList = this.h;
        if (arrayList == null || arrayList.isEmpty()) {
            throw new Throwable("GET API PARAMS ERROR");
        }
        Integer num = (Integer) hashMap.get("zip");
        if (num == null || num.intValue() != 1) {
            this.d = false;
        } else {
            this.d = true;
        }
        Integer num2 = (Integer) hashMap.get("request");
        if (num2 == null || num2.intValue() != 1) {
            this.e = false;
        } else {
            this.e = true;
        }
        this.k = ((Integer) hashMap.get("frequency")).intValue();
        if (this.k != 0) {
            this.f = true;
        }
        e();
    }

    @Override // cn.smssdk.net.a
    public boolean a() throws Throwable {
        if (!this.e) {
            SMSLog.getInstance().w(SMSLog.FORMAT, "ServiceApi", "checkLimit", "[" + this.b + "]No access permission for this api, terminate this request.");
            throw new Throwable("{\"status\":606}");
        }
        if (this.f) {
            long currentTimeMillis = System.currentTimeMillis() - this.l;
            if (currentTimeMillis < this.k) {
                SMSLog.getInstance().w(SMSLog.FORMAT, "ServiceApi", "checkLimit", "[" + this.b + "]Request too frequently, terminate this request. Interval: " + currentTimeMillis + ", frequency: " + this.k);
                throw new Throwable("{\"status\":600}");
            }
            SMSLog.getInstance().d(SMSLog.FORMAT, "ServiceApi", "checkLimit", "[" + this.b + "]interval > frequency.");
        } else {
            SMSLog.getInstance().d(SMSLog.FORMAT, "ServiceApi", "checkLimit", "[" + this.b + "]Not limited for this api.");
        }
        SMSLog.getInstance().d(SMSLog.FORMAT, "ServiceApi", "checkLimit", "[" + this.b + "]Check OK, allow sending request.");
        return false;
    }

    public void d() {
        if (this.f) {
            this.l = System.currentTimeMillis();
            f();
        }
    }
}
