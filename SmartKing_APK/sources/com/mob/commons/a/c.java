package com.mob.commons.a;

import android.os.Message;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Hashon;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* compiled from: AtClt.java */
/* loaded from: classes.dex */
public class c extends d {
    private long a;
    private HashMap<Long, Long> b;

    private void h() {
        if (this.b == null) {
            this.b = new HashMap<>();
        }
        for (Map.Entry<Long, Long> entry : this.b.entrySet()) {
            if (entry != null && entry.getKey().longValue() != this.a) {
                i();
            }
        }
        long a = com.mob.commons.b.a() - this.a;
        this.b.put(Long.valueOf(this.a), Long.valueOf(a));
        com.mob.commons.i.b(this.b);
        MobLog.getInstance().d("[%s] %s", "AtClt", "Cache AT: {\"launchAt: " + this.a + ", \"duration\": " + a + "}");
        long s = com.mob.commons.i.s();
        if (a >= com.mob.commons.b.ab() * 1000 && s <= com.mob.commons.b.a()) {
            i();
        }
        a(1, com.mob.commons.b.Z() * 1000);
    }

    private void i() {
        try {
            HashMap hashMap = new HashMap();
            for (Map.Entry<Long, Long> entry : this.b.entrySet()) {
                if (entry != null) {
                    hashMap.put("launchAt", entry.getKey());
                    hashMap.put("duration", entry.getValue());
                }
            }
            long a = com.mob.commons.b.a();
            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("type", "ARSTAMT");
            hashMap2.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap);
            hashMap2.put("datetime", Long.valueOf(a));
            com.mob.commons.c.a().a(a, hashMap2);
            MobLog.getInstance().d("[%s] %s", "AtClt", "Push AT: " + new Hashon().fromHashMap(hashMap));
            com.mob.commons.i.h(a + (com.mob.commons.b.ab() * 1000));
            if (this.b != null) {
                this.b.clear();
            }
            com.mob.commons.i.b((HashMap<Long, Long>) null);
        } catch (Throwable th) {
            MobLog.getInstance().w(th, "[%s] %s", "AtClt", "Push error");
        }
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.at_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (message.what == 1 && com.mob.commons.b.Z() > 0) {
            h();
        }
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.Z() > 0;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        this.a = com.mob.commons.b.a();
        this.b = com.mob.commons.i.t();
        b(1);
    }
}
