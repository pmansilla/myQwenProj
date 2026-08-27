package cn.smssdk.net;

import android.text.TextUtils;
import cn.smssdk.utils.SMSLog;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.commons.ForbThrowable;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: BaseApi.java */
/* loaded from: classes.dex */
public abstract class a {
    private static MobCommunicator i;
    private static Hashon j;
    protected int a;
    protected String b;
    protected String c;
    protected boolean d;
    protected boolean e;
    protected boolean f = false;
    protected ReentrantReadWriteLock g;
    protected ArrayList<String> h;

    public a() {
        if (j == null) {
            j = new Hashon();
        }
    }

    protected abstract HashMap<String, Object> a(String str, String str2, HashMap<String, Object> hashMap) throws Throwable;

    public void a(ReentrantReadWriteLock reentrantReadWriteLock) {
        this.g = reentrantReadWriteLock;
    }

    public abstract boolean a() throws Throwable;

    public String b() {
        return this.b;
    }

    public String b(String str, String str2, HashMap<String, Object> hashMap) throws Throwable {
        if (MobSDK.isForb()) {
            throw new ForbThrowable();
        }
        if (a()) {
            SMSLog.getInstance().w(SMSLog.FORMAT, "BaseApi", "request", "[" + this.b + "]Request limited.");
            return "";
        }
        try {
            if (this.g != null) {
                this.g.readLock().lock();
            }
            HashMap<String, Object> a = a(str, str2, hashMap);
            if (a == null) {
                a = new HashMap<>();
            }
            if (!a.containsKey("duid")) {
                a.put("duid", str);
                if (this.b == null || !this.b.equals("getToken")) {
                    a.put("duid", "Api: " + this.b + " duid: " + str + " is added by workaround.");
                } else {
                    a.put("duidinfo_x17zcD", "Api: " + this.b + " duid: " + str + " added by workaround. " + b.f() + " params is : " + cn.smssdk.utils.c.a(this.h) + " cfgsrv: " + cn.smssdk.utils.c.a(b.y) + "cfgsp: " + cn.smssdk.utils.c.a(b.z));
                }
            } else if (TextUtils.isEmpty((String) a.get("duid"))) {
                if (TextUtils.isEmpty(str)) {
                    a.put("duidinfo_x17zcD", "Api: " + this.b + " duid got from CommonsLib is invalid.");
                } else {
                    a.put("duid", str);
                    a.put("duidinfo_x17zcD", "Api: " + this.b + " duid build to params is invalid and added by workaround.");
                }
            }
            if (i == null) {
                Object[] a2 = c.a();
                i = new MobCommunicator(((Integer) a2[2]).intValue(), (String) a2[0], (String) a2[1]);
            }
            return j.fromObject(i.requestSynchronized(a, MobSDK.checkRequestUrl(this.c), this.d));
        } finally {
            ReentrantReadWriteLock reentrantReadWriteLock = this.g;
            if (reentrantReadWriteLock != null) {
                reentrantReadWriteLock.readLock().unlock();
            }
        }
    }

    public int c() {
        return this.a;
    }
}
