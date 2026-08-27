package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMapException;
import com.tencent.bugly.BuglyStrategy;

/* compiled from: AbstractBasicHandler.java */
/* loaded from: classes.dex */
public abstract class gv<T, V> extends eq {
    protected T d;
    protected int e = 1;
    protected Context f;
    protected String g;

    public gv(Context context, T t) {
        a(context, t);
    }

    private V a(byte[] bArr) throws gu {
        return b(bArr);
    }

    private void a(Context context, T t) {
        this.f = context;
        this.d = t;
        this.e = 1;
        setSoTimeout(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH);
        setConnectionTimeout(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH);
    }

    private V e() throws gu {
        V v = null;
        int i = 0;
        while (i < this.e) {
            try {
                setProxy(hm.a(this.f));
                V a = a(makeHttpRequest());
                try {
                    i = this.e;
                    v = a;
                } catch (gu e) {
                    e = e;
                    v = a;
                    i++;
                    if (i >= this.e) {
                        throw new gu(e.a());
                    }
                } catch (hc e2) {
                    e = e2;
                    v = a;
                    i++;
                    if (i >= this.e) {
                        d();
                        if (AMapException.ERROR_CONNECTION.equals(e.getMessage()) || AMapException.ERROR_SOCKET.equals(e.getMessage()) || AMapException.ERROR_UNKNOWN.equals(e.a()) || AMapException.ERROR_UNKNOW_SERVICE.equals(e.getMessage())) {
                            throw new gu("http或socket连接失败 - ConnectionException");
                        }
                        throw new gu(e.a());
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException unused) {
                        if (AMapException.ERROR_CONNECTION.equals(e.getMessage()) || AMapException.ERROR_SOCKET.equals(e.getMessage()) || AMapException.ERROR_UNKNOW_SERVICE.equals(e.getMessage())) {
                            throw new gu("http或socket连接失败 - ConnectionException");
                        }
                        throw new gu(e.a());
                    }
                }
            } catch (gu e3) {
                e = e3;
            } catch (hc e4) {
                e = e4;
            }
        }
        return v;
    }

    public V a() throws gu {
        if (this.d != null) {
            return e();
        }
        return null;
    }

    protected abstract V b(String str) throws gu;

    protected V b(byte[] bArr) throws gu {
        String str;
        try {
            str = new String(bArr, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
        }
        if (str == null || "".equals(str)) {
            return null;
        }
        gx.a(str, this.g);
        return b(str);
    }

    protected V d() {
        return null;
    }
}
