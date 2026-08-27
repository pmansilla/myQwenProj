package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.alibaba.fastjson.asm.Opcodes;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* compiled from: AMapMessageHandler.java */
/* loaded from: classes.dex */
public final class p implements Handler.Callback {
    private Handler b;
    private l d;
    private boolean e;
    private Map<Integer, al> a = new Hashtable();
    private HandlerThread c = new HandlerThread("AMapMessageHandler");

    public p(Context context, l lVar, ae aeVar) {
        this.e = false;
        this.d = lVar;
        this.c.start();
        this.b = new Handler(this.c.getLooper(), this);
        this.e = false;
    }

    public void a() {
        this.e = true;
        if (this.c != null) {
            this.c.quit();
        }
        if (this.b != null) {
            this.b.removeCallbacksAndMessages(null);
        }
    }

    public void a(al alVar) {
        try {
            if (this.e || alVar == null) {
                return;
            }
            int i = alVar.a;
            if (alVar.a == 153) {
                if (this.a == null || this.a.size() <= 0) {
                    return;
                }
                this.b.obtainMessage(Opcodes.IFEQ).sendToTarget();
                return;
            }
            synchronized (this.a) {
                if (i < 33) {
                    try {
                        this.a.put(Integer.valueOf(i), alVar);
                    } finally {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.e || message == null) {
            return false;
        }
        al alVar = (al) message.obj;
        int i = message.what;
        if (i == 1) {
            this.d.t(((Integer) alVar.b).intValue());
        } else if (i == 153) {
            synchronized (this.a) {
                Set<Integer> keySet = this.a.keySet();
                if (keySet.size() > 0) {
                    Iterator<Integer> it = keySet.iterator();
                    while (it.hasNext()) {
                        al remove = this.a.remove(it.next());
                        this.b.obtainMessage(remove.a, remove).sendToTarget();
                    }
                }
            }
        }
        return false;
    }
}
