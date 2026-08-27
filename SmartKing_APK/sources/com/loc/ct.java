package com.loc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.DPoint;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import com.loc.s;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* compiled from: ConnectionServiceManager.java */
/* loaded from: classes.dex */
public class ct {
    private String b;
    private Context c;
    private boolean d = true;
    public boolean a = false;
    private s e = null;
    private ServiceConnection f = null;
    private ServiceConnection g = null;
    private ServiceConnection h = null;
    private Intent i = new Intent();
    private String j = "com.autonavi.minimap";
    private String k = "com.amap.api.service.AMapService";
    private String l = "com.autonavi.minimap.LBSConnectionService";
    private boolean m = false;
    private boolean n = false;
    private boolean o = false;
    private boolean p = false;
    private boolean q = false;
    private boolean r = false;
    private List<Intent> s = new ArrayList();
    private boolean t = false;

    public ct(Context context) {
        this.b = null;
        this.c = null;
        this.c = context;
        try {
            this.b = y.b(eh.a(u.f(context).getBytes("UTF-8"), "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDCEYwdO3V2ANrhApjqyk7X8FH5AEaWly58kP9IDAhMqwtIbmcJrUK9oO9Afh3KZnOlDtjiowy733YqpLRO7WBvdbW/c4Dz/d3dy/m+6HMqxaak+GQQRHw/VPdKciaZ3eIZp4MWOyIQwiFSQvPTAo/Na8hV4SgBZHB3lGFw0yu+BmG+h32eIE6p4Y8EDCn+G+yzekX+taMrWTQIysledrygZSGPv1ukbdFDnH/xZEI0dCr9pZT+AZQl3o9a2aMyuRrHM0oupXKKiYl69Y8fKh1Tyd752rF6LrR5uOb9aOfXt18hb+3YL5P9rQ+ZRYbyHYFaxzBPA2jLq0KUQ+Dmg7YhAgMBAAECggEAL9pj0lF3BUHwtssNKdf42QZJMD0BKuDcdZrLV9ifs0f54EJY5enzKw8j76MpdV8N5QVkNX4/BZR0bs9uJogh31oHFs5EXeWbb7V8P7bRrxpNnSAijGBWwscQsyqymf48YlcL28949ujnjoEz3jQjgWOyYnrCgpVhphrQbCGmB5TcZnTFvHfozt/0tzuMj5na5lRnkD0kYXgr0x/SRZcPoCybSpc3t/B/9MAAboGaV/QQkTotr7VOuJfaPRjvg8rzyPzavo3evxsjXj7vDXbN4w0cbk/Uqn2JtvPQ8HoysmF2HdYvILZibvJmWH1hA58b4sn5s6AqFRjMOL7rHdD+gQKBgQD+IzoofmZK5tTxgO9sWsG71IUeshQP9fe159jKCehk1RfuIqqbRP0UcxJiw4eNjHs4zU0HeRL3iF5XfUs0FQanO/pp6YL1xgVdfQlDdTdk6KFHJ0sUJapnJn1S2k7IKfRKE1+rkofSXMYUTsgHF1fDp+gxy4yUMY+h9O+JlKVKOwKBgQDDfaDIblaSm+B0lyG//wFPynAeGd0Q8wcMZbQQ/LWMJZhMZ7fyUZ+A6eL/jB53a2tgnaw2rXBpMe1qu8uSpym2plU0fkgLAnVugS5+KRhOkUHyorcbpVZbs5azf7GlTydR5dI1PHF3Bncemoa6IsEvumHWgQbVyTTz/O9mlFafUwKBgQCvDebms8KUf5JY1F6XfaCLWGVl8nZdVCmQFKbA7Lg2lI5KS3jHQWsupeEZRORffU/3nXsc1apZ9YY+r6CYvI77rRXd1KqPzxos/o7d96TzjkZhc9CEjTlmmh2jb5rqx/Ns/xFcZq/GGH+cx3ODZvHeZQ9NFY+9GLJ+dfB2DX0ZtwKBgQC+9/lZ8telbpqMqpqwqRaJ8LMn5JIdHZu0E6IcuhFLr+ogMW3zTKMpVtGGXEXi2M/TWRPDchiO2tQX4Q5T2/KW19QCbJ5KCwPWiGF3owN4tNOciDGh0xkSidRc0xAh8bnyejSoBry8zlcNUVztdkgMLOGonvCjZWPSOTNQnPYluwKBgCV+WVftpTk3l+OfAJTaXEPNYdh7+WQjzxZKjUaDzx80Ts7hRo2U+EQT7FBjQQNqmmDnWtujo5p1YmJC0FT3n1CVa7g901pb3b0RcOziYWAoJi0/+kLyeo6XBhuLeZ7h90S70GGh1o0V/j/9N1jb5DCL4xKkvdYePPTSTku0BM+n"));
        } catch (Throwable th) {
            es.a(th, "ConnectionServiceManager", "ConnectionServiceManager");
        }
    }

    private AMapLocationServer a(Bundle bundle) {
        byte[] bArr;
        if (!bundle.containsKey("key")) {
            return null;
        }
        try {
            bArr = eh.b(y.b(bundle.getString("key")), "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDCEYwdO3V2ANrhApjqyk7X8FH5AEaWly58kP9IDAhMqwtIbmcJrUK9oO9Afh3KZnOlDtjiowy733YqpLRO7WBvdbW/c4Dz/d3dy/m+6HMqxaak+GQQRHw/VPdKciaZ3eIZp4MWOyIQwiFSQvPTAo/Na8hV4SgBZHB3lGFw0yu+BmG+h32eIE6p4Y8EDCn+G+yzekX+taMrWTQIysledrygZSGPv1ukbdFDnH/xZEI0dCr9pZT+AZQl3o9a2aMyuRrHM0oupXKKiYl69Y8fKh1Tyd752rF6LrR5uOb9aOfXt18hb+3YL5P9rQ+ZRYbyHYFaxzBPA2jLq0KUQ+Dmg7YhAgMBAAECggEAL9pj0lF3BUHwtssNKdf42QZJMD0BKuDcdZrLV9ifs0f54EJY5enzKw8j76MpdV8N5QVkNX4/BZR0bs9uJogh31oHFs5EXeWbb7V8P7bRrxpNnSAijGBWwscQsyqymf48YlcL28949ujnjoEz3jQjgWOyYnrCgpVhphrQbCGmB5TcZnTFvHfozt/0tzuMj5na5lRnkD0kYXgr0x/SRZcPoCybSpc3t/B/9MAAboGaV/QQkTotr7VOuJfaPRjvg8rzyPzavo3evxsjXj7vDXbN4w0cbk/Uqn2JtvPQ8HoysmF2HdYvILZibvJmWH1hA58b4sn5s6AqFRjMOL7rHdD+gQKBgQD+IzoofmZK5tTxgO9sWsG71IUeshQP9fe159jKCehk1RfuIqqbRP0UcxJiw4eNjHs4zU0HeRL3iF5XfUs0FQanO/pp6YL1xgVdfQlDdTdk6KFHJ0sUJapnJn1S2k7IKfRKE1+rkofSXMYUTsgHF1fDp+gxy4yUMY+h9O+JlKVKOwKBgQDDfaDIblaSm+B0lyG//wFPynAeGd0Q8wcMZbQQ/LWMJZhMZ7fyUZ+A6eL/jB53a2tgnaw2rXBpMe1qu8uSpym2plU0fkgLAnVugS5+KRhOkUHyorcbpVZbs5azf7GlTydR5dI1PHF3Bncemoa6IsEvumHWgQbVyTTz/O9mlFafUwKBgQCvDebms8KUf5JY1F6XfaCLWGVl8nZdVCmQFKbA7Lg2lI5KS3jHQWsupeEZRORffU/3nXsc1apZ9YY+r6CYvI77rRXd1KqPzxos/o7d96TzjkZhc9CEjTlmmh2jb5rqx/Ns/xFcZq/GGH+cx3ODZvHeZQ9NFY+9GLJ+dfB2DX0ZtwKBgQC+9/lZ8telbpqMqpqwqRaJ8LMn5JIdHZu0E6IcuhFLr+ogMW3zTKMpVtGGXEXi2M/TWRPDchiO2tQX4Q5T2/KW19QCbJ5KCwPWiGF3owN4tNOciDGh0xkSidRc0xAh8bnyejSoBry8zlcNUVztdkgMLOGonvCjZWPSOTNQnPYluwKBgCV+WVftpTk3l+OfAJTaXEPNYdh7+WQjzxZKjUaDzx80Ts7hRo2U+EQT7FBjQQNqmmDnWtujo5p1YmJC0FT3n1CVa7g901pb3b0RcOziYWAoJi0/+kLyeo6XBhuLeZ7h90S70GGh1o0V/j/9N1jb5DCL4xKkvdYePPTSTku0BM+n");
        } catch (Throwable th) {
            es.a(th, "ConnectionServiceManager", "parseData part");
            bArr = null;
        }
        if (bundle.containsKey("result")) {
            try {
                JSONObject jSONObject = new JSONObject(new String(eh.a(bArr, y.b(bundle.getString("result"))), "utf-8"));
                if (jSONObject.has("error")) {
                    String string = jSONObject.getString("error");
                    if ("invaid type".equals(string)) {
                        this.d = false;
                    }
                    if ("empty appkey".equals(string)) {
                        this.d = false;
                    }
                    if ("refused".equals(string)) {
                        this.d = false;
                    }
                    "failed".equals(string);
                    return null;
                }
                AMapLocationServer aMapLocationServer = new AMapLocationServer("");
                aMapLocationServer.b(jSONObject);
                aMapLocationServer.setProvider("lbs");
                aMapLocationServer.setLocationType(7);
                if (AMapLocation.COORD_TYPE_WGS84.equals(aMapLocationServer.e()) && es.a(aMapLocationServer.getLatitude(), aMapLocationServer.getLongitude())) {
                    DPoint a = eu.a(this.c, aMapLocationServer.getLongitude(), aMapLocationServer.getLatitude());
                    aMapLocationServer.setLatitude(a.getLatitude());
                    aMapLocationServer.setLongitude(a.getLongitude());
                }
                return aMapLocationServer;
            } catch (Throwable th2) {
                es.a(th2, ct.class.getName(), "parseData");
            }
        }
        return null;
    }

    private void f() {
        ArrayList<String> h;
        if (er.c(this.c)) {
            Intent intent = new Intent();
            intent.putExtra("appkey", this.b);
            intent.setComponent(new ComponentName(this.j, this.l));
            try {
                this.n = this.c.bindService(intent, this.g, 1);
            } catch (Throwable unused) {
            }
            if (!this.n && (h = er.h()) != null) {
                Iterator<String> it = h.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (!next.equals(this.l)) {
                        intent.setComponent(new ComponentName(this.j, next));
                        try {
                            this.n = this.c.bindService(intent, this.g, 1);
                        } catch (Throwable unused2) {
                        }
                        if (this.m) {
                            break;
                        }
                    }
                }
            }
            this.q = true;
        }
    }

    private AMapLocationServer g() {
        try {
        } catch (Throwable th) {
            es.a(th, "ConnectionServiceManager", "sendCommand");
        }
        if (this.d && this.m) {
            Bundle bundle = new Bundle();
            bundle.putString("type", "corse");
            bundle.putString("appkey", this.b);
            bundle.putInt("opensdk", 1);
            if (this.e != null) {
                this.e.a(bundle);
                if (bundle.size() > 0) {
                    return a(bundle);
                }
            }
            return null;
        }
        return null;
    }

    public final void a() {
        try {
            if (this.f != null && this.p) {
                this.c.unbindService(this.f);
            }
        } catch (Throwable th) {
            es.a(th, "ConnectionServiceManager", "unbindService connService");
        }
        try {
            if (this.g != null && this.q) {
                this.c.unbindService(this.g);
            }
        } catch (Throwable th2) {
            es.a(th2, "ConnectionServiceManager", "unbindService pushService");
        }
        try {
            if (this.h != null && this.r) {
                this.c.unbindService(this.h);
            }
        } catch (Throwable th3) {
            es.a(th3, "ConnectionServiceManager", "unbindService otherService");
        }
        if (this.s != null && this.s.size() > 0) {
            Iterator<Intent> it = this.s.iterator();
            while (it.hasNext()) {
                this.c.stopService(it.next());
            }
        }
        this.e = null;
        this.c = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.d = true;
        this.a = false;
        this.m = false;
        this.n = false;
        this.o = false;
        this.t = false;
        this.p = false;
        this.q = false;
        this.r = false;
        this.s.clear();
        this.s = null;
    }

    public final void b() {
        try {
            if (this.f == null) {
                this.f = new ServiceConnection() { // from class: com.loc.ct.1
                    @Override // android.content.ServiceConnection
                    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        ct.this.a = true;
                        ct.this.e = s.a.a(iBinder);
                    }

                    @Override // android.content.ServiceConnection
                    public final void onServiceDisconnected(ComponentName componentName) {
                        ct.this.a = false;
                        ct.this.e = null;
                    }
                };
            }
            if (this.g == null) {
                this.g = new ServiceConnection() { // from class: com.loc.ct.2
                    @Override // android.content.ServiceConnection
                    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    }

                    @Override // android.content.ServiceConnection
                    public final void onServiceDisconnected(ComponentName componentName) {
                    }
                };
            }
            if (this.h == null) {
                this.h = new ServiceConnection() { // from class: com.loc.ct.3
                    @Override // android.content.ServiceConnection
                    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    }

                    @Override // android.content.ServiceConnection
                    public final void onServiceDisconnected(ComponentName componentName) {
                    }
                };
            }
        } catch (Throwable th) {
            es.a(th, "ConnectionServiceManager", "init");
        }
    }

    public final void c() {
        ArrayList<String> g;
        if (this.t) {
            return;
        }
        try {
            if (er.b(this.c)) {
                this.i.putExtra("appkey", this.b);
                this.i.setComponent(new ComponentName(this.j, this.k));
                try {
                    this.m = this.c.bindService(this.i, this.f, 1);
                } catch (Throwable unused) {
                }
                if (!this.m && (g = er.g()) != null) {
                    Iterator<String> it = g.iterator();
                    while (it.hasNext()) {
                        String next = it.next();
                        if (!next.equals(this.k)) {
                            this.i.setComponent(new ComponentName(this.j, next));
                            try {
                                this.m = this.c.bindService(this.i, this.f, 1);
                            } catch (Throwable unused2) {
                            }
                            if (this.m) {
                                break;
                            }
                        }
                    }
                }
                this.p = true;
            }
            f();
            d();
        } catch (Throwable unused3) {
        }
        this.t = true;
    }

    public final void d() {
        if (this.r || this.t) {
            return;
        }
        try {
        } catch (Throwable th) {
            es.a(th, "ConnectionServiceManager", "bindOtherService");
        }
        if (er.f(this.c)) {
            List<ev> p = er.p();
            if (p != null && p.size() > 0) {
                for (ev evVar : p) {
                    if (evVar != null) {
                        try {
                            if (evVar.a()) {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(evVar.b(), evVar.c()));
                                if (!TextUtils.isEmpty(evVar.e())) {
                                    intent.setAction(evVar.e());
                                }
                                List<Map<String, String>> d = evVar.d();
                                if (d != null && d.size() > 0) {
                                    for (int i = 0; i < d.size(); i++) {
                                        Iterator<Map.Entry<String, String>> it = d.get(i).entrySet().iterator();
                                        if (it.hasNext()) {
                                            Map.Entry<String, String> next = it.next();
                                            intent.putExtra(next.getKey().toString(), next.getValue().toString());
                                        }
                                    }
                                }
                                if (evVar.f()) {
                                    this.c.startService(intent);
                                    this.s.add(intent);
                                }
                                if (this.c.bindService(intent, this.h, 1)) {
                                    this.o = true;
                                }
                            }
                        } catch (Throwable th2) {
                            es.a(th2, "ConnectionServiceManager", "bindOtherService 1");
                        }
                    }
                }
            }
            this.r = true;
        }
    }

    public final AMapLocationServer e() {
        AMapLocationServer g;
        if (!er.f()) {
            return null;
        }
        c();
        for (int i = 4; i > 0 && !this.a; i--) {
            SystemClock.sleep(500L);
        }
        if (!this.a || (g = g()) == null) {
            return null;
        }
        return g;
    }
}
