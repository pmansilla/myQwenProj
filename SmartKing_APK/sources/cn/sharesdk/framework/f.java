package cn.sharesdk.framework;

import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.commons.eventrecoder.EventRecorder;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* compiled from: ShareSDKCoreThread.java */
/* loaded from: classes.dex */
public class f extends cn.sharesdk.framework.utils.d {
    private a b;
    private boolean k;
    private boolean j = true;
    private HashMap<String, HashMap<String, String>> c = new HashMap<>();
    private ArrayList<Platform> d = new ArrayList<>();
    private HashMap<String, Integer> e = new HashMap<>();
    private HashMap<Integer, String> f = new HashMap<>();
    private HashMap<Integer, CustomPlatform> g = new HashMap<>();
    private HashMap<Integer, HashMap<String, Object>> h = new HashMap<>();
    private HashMap<Integer, Service> i = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ShareSDKCoreThread.java */
    /* loaded from: classes.dex */
    public enum a {
        INITIALIZING,
        READY
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashMap<String, Object> a(cn.sharesdk.framework.b.a aVar, HashMap<String, Object> hashMap) {
        try {
            if (hashMap.containsKey("error")) {
                cn.sharesdk.framework.utils.e.b().i("ShareSDK parse sns config ==>>", new Hashon().fromHashMap(hashMap));
                return null;
            }
            if (!hashMap.containsKey("res")) {
                cn.sharesdk.framework.utils.e.b().d("ShareSDK platform config result ==>>", "SNS configuration is empty");
                return null;
            }
            String str = (String) hashMap.get("res");
            if (str == null) {
                return null;
            }
            return aVar.c(str);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().w(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(HashMap<String, Object> hashMap) {
        synchronized (this.h) {
            HashMap<Integer, HashMap<String, Object>> a2 = e.a(hashMap);
            if (a2 == null || a2.size() <= 0) {
                return false;
            }
            this.h.clear();
            this.h = a2;
            return true;
        }
    }

    private void h() {
        new Thread(new Runnable() { // from class: cn.sharesdk.framework.f.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    cn.sharesdk.framework.authorize.e.c().a(5, 5);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }).start();
    }

    private void i() {
        synchronized (this.c) {
            this.c.clear();
            try {
                XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
                newInstance.setNamespaceAware(true);
                XmlPullParser newPullParser = newInstance.newPullParser();
                InputStream inputStream = null;
                try {
                    inputStream = MobSDK.getContext().getAssets().open("ShareSDK.xml");
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().d(th);
                }
                newPullParser.setInput(inputStream, "utf-8");
                for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                    if (eventType == 2) {
                        String name = newPullParser.getName();
                        HashMap<String, String> hashMap = new HashMap<>();
                        int attributeCount = newPullParser.getAttributeCount();
                        for (int i = 0; i < attributeCount; i++) {
                            hashMap.put(newPullParser.getAttributeName(i), newPullParser.getAttributeValue(i).trim());
                        }
                        this.c.put(name, hashMap);
                    }
                }
                inputStream.close();
            } catch (Throwable th2) {
                cn.sharesdk.framework.utils.e.b().d(th2);
            }
        }
    }

    public Platform a(String str) {
        Platform[] c;
        if (str == null || (c = c()) == null) {
            return null;
        }
        for (Platform platform : c) {
            if (str.equals(platform.getName())) {
                return platform;
            }
        }
        return null;
    }

    public String a(int i, String str) {
        synchronized (this.h) {
            HashMap<String, Object> hashMap = this.h.get(Integer.valueOf(i));
            String str2 = null;
            if (hashMap == null) {
                return null;
            }
            Object obj = hashMap.get(str);
            if (obj != null) {
                str2 = String.valueOf(obj);
            }
            return str2;
        }
    }

    public String a(Bitmap bitmap) {
        if (a.READY != this.b) {
            return null;
        }
        return cn.sharesdk.framework.b.a.a().a(bitmap);
    }

    public String a(String str, boolean z, int i, String str2) {
        return a.READY != this.b ? str : cn.sharesdk.framework.b.a.a().a(str, i, z, str2);
    }

    public void a(int i) {
        NetworkHelper.connectionTimeout = i;
    }

    public void a(int i, int i2) {
        synchronized (this.h) {
            this.h.put(Integer.valueOf(i2), this.h.get(Integer.valueOf(i)));
        }
    }

    public void a(int i, Platform platform) {
        e.a(i, platform);
    }

    /* JADX WARN: Type inference failed for: r1v14, types: [cn.sharesdk.framework.f$2] */
    @Override // cn.sharesdk.framework.utils.d
    protected void a(Message message) {
        HashMap<Integer, Service> hashMap;
        synchronized (this.i) {
            synchronized (this.d) {
                try {
                    try {
                        String checkRecord = EventRecorder.checkRecord(ShareSDK.SDK_TAG);
                        if (!TextUtils.isEmpty(checkRecord)) {
                            cn.sharesdk.framework.b.a.a().a((HashMap<String, Object>) null);
                            cn.sharesdk.framework.utils.e.b().w("EventRecorder checkRecord result ==" + checkRecord);
                            g();
                        }
                        EventRecorder.clear();
                    } catch (Throwable th) {
                        try {
                            cn.sharesdk.framework.utils.e.b().w(th);
                        } catch (Throwable th2) {
                            cn.sharesdk.framework.utils.e.b().w(th2);
                            this.b = a.READY;
                            this.d.notify();
                            hashMap = this.i;
                        }
                    }
                    this.d.clear();
                    ArrayList<Platform> a2 = e.a();
                    if (a2 != null) {
                        this.d.addAll(a2);
                    }
                    Iterator<Platform> it = this.d.iterator();
                    while (it.hasNext()) {
                        Platform next = it.next();
                        this.f.put(Integer.valueOf(next.getPlatformId()), next.getName());
                        this.e.put(next.getName(), Integer.valueOf(next.getPlatformId()));
                    }
                    e.a(this.a);
                    this.b = a.READY;
                    new Thread() { // from class: cn.sharesdk.framework.f.2
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            f.this.f();
                        }
                    }.start();
                    this.b = a.READY;
                    this.d.notify();
                    hashMap = this.i;
                    hashMap.notify();
                } catch (Throwable th3) {
                    this.b = a.READY;
                    this.d.notify();
                    this.i.notify();
                    throw th3;
                }
            }
        }
    }

    public void a(Class<? extends Service> cls) {
        synchronized (this.i) {
            if (this.i.containsKey(Integer.valueOf(cls.hashCode()))) {
                return;
            }
            try {
                Service newInstance = cls.newInstance();
                this.i.put(Integer.valueOf(cls.hashCode()), newInstance);
                newInstance.onBind();
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().w(th);
            }
        }
    }

    public void a(String str, int i) {
        e.a(str, i);
    }

    public void a(String str, String str2) {
        synchronized (this.c) {
            this.c.put(str2, this.c.get(str));
        }
    }

    public void a(String str, HashMap<String, Object> hashMap) {
        synchronized (this.c) {
            HashMap<String, String> hashMap2 = this.c.get(str);
            if (hashMap2 == null) {
                hashMap2 = new HashMap<>();
            }
            synchronized (hashMap2) {
                for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value != null) {
                        hashMap2.put(key, String.valueOf(value));
                    }
                }
            }
            this.c.put(str, hashMap2);
        }
        synchronized (this.d) {
            if (this.b == a.INITIALIZING) {
                try {
                    this.d.wait();
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().w(th);
                }
            }
        }
        Iterator<Platform> it = this.d.iterator();
        while (it.hasNext()) {
            Platform next = it.next();
            if (next != null && next.getName().equals(str)) {
                next.a();
                return;
            }
        }
    }

    public void a(boolean z) {
        e.a(z);
    }

    public boolean a() {
        return e.b();
    }

    public int b(String str) {
        synchronized (this.d) {
            synchronized (this.g) {
                if (!this.e.containsKey(str)) {
                    return 0;
                }
                return this.e.get(str).intValue();
            }
        }
    }

    public String b(String str, String str2) {
        synchronized (this.c) {
            HashMap<String, String> hashMap = this.c.get(str);
            if (hashMap == null) {
                return null;
            }
            return hashMap.get(str2);
        }
    }

    @Override // cn.sharesdk.framework.utils.d
    public void b() {
        this.b = a.INITIALIZING;
        cn.sharesdk.framework.utils.e.a();
        EventRecorder.prepare();
        i();
        h();
        super.b();
    }

    public void b(int i) {
        NetworkHelper.readTimout = i;
    }

    @Override // cn.sharesdk.framework.utils.d
    protected void b(Message message) {
    }

    public void b(Class<? extends Service> cls) {
        synchronized (this.i) {
            int hashCode = cls.hashCode();
            if (this.i.containsKey(Integer.valueOf(hashCode))) {
                this.i.get(Integer.valueOf(hashCode)).onUnbind();
                this.i.remove(Integer.valueOf(hashCode));
            }
        }
    }

    public void b(boolean z) {
        this.k = z;
    }

    public <T extends Service> T c(Class<T> cls) {
        T cast;
        synchronized (this.i) {
            if (this.b == a.INITIALIZING) {
                try {
                    this.i.wait();
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().w(th);
                }
            }
            try {
                cast = cls.cast(this.i.get(Integer.valueOf(cls.hashCode())));
            } catch (Throwable th2) {
                cn.sharesdk.framework.utils.e.b().w(th2);
                return null;
            }
        }
        return cast;
    }

    public String c(int i) {
        String str;
        synchronized (this.d) {
            synchronized (this.g) {
                str = this.f.get(Integer.valueOf(i));
            }
        }
        return str;
    }

    public String c(String str) {
        if (a.READY != this.b) {
            return null;
        }
        return cn.sharesdk.framework.b.a.a().b(str);
    }

    public Platform[] c() {
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.d) {
            if (this.b == a.INITIALIZING) {
                try {
                    this.d.wait();
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().w(th);
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Platform> it = this.d.iterator();
        while (it.hasNext()) {
            Platform next = it.next();
            if (next != null && next.b()) {
                next.a();
                arrayList.add(next);
            }
        }
        e.a((ArrayList<Platform>) arrayList);
        Iterator<Map.Entry<Integer, CustomPlatform>> it2 = this.g.entrySet().iterator();
        while (it2.hasNext()) {
            CustomPlatform value = it2.next().getValue();
            if (value != null && value.b()) {
                arrayList.add(value);
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        Platform[] platformArr = new Platform[arrayList.size()];
        for (int i = 0; i < platformArr.length; i++) {
            platformArr[i] = (Platform) arrayList.get(i);
        }
        cn.sharesdk.framework.utils.e.b().i("sort list use time: %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return platformArr;
    }

    public void d(Class<? extends CustomPlatform> cls) {
        synchronized (this.g) {
            if (this.g.containsKey(Integer.valueOf(cls.hashCode()))) {
                return;
            }
            try {
                CustomPlatform newInstance = cls.newInstance();
                this.g.put(Integer.valueOf(cls.hashCode()), newInstance);
                if (newInstance != null && newInstance.b()) {
                    this.f.put(Integer.valueOf(newInstance.getPlatformId()), newInstance.getName());
                    this.e.put(newInstance.getName(), Integer.valueOf(newInstance.getPlatformId()));
                }
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().w(th);
            }
        }
    }

    public boolean d() {
        return this.k;
    }

    public void e(Class<? extends CustomPlatform> cls) {
        int hashCode = cls.hashCode();
        synchronized (this.g) {
            this.g.remove(Integer.valueOf(hashCode));
        }
    }

    public boolean e() {
        synchronized (this.h) {
            return this.h != null && this.h.size() > 0;
        }
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [cn.sharesdk.framework.f$3] */
    public boolean f() {
        boolean z = false;
        if (a.READY != this.b) {
            cn.sharesdk.framework.utils.e.b().d("Statistics module unopened", new Object[0]);
            return false;
        }
        final cn.sharesdk.framework.b.a a2 = cn.sharesdk.framework.b.a.a();
        HashMap<String, Object> a3 = a(a2, a2.e());
        if (a3 != null && a3.size() > 0) {
            z = a(a3);
        }
        if (z) {
            new Thread() { // from class: cn.sharesdk.framework.f.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        HashMap<String, Object> f = a2.f();
                        HashMap a4 = f.this.a(a2, f);
                        if (a4 == null || a4.size() <= 0 || !f.this.a((HashMap<String, Object>) a4)) {
                            return;
                        }
                        a2.a(f);
                    } catch (Throwable th) {
                        cn.sharesdk.framework.utils.e.b().w(th);
                    }
                }
            }.start();
            return z;
        }
        try {
            HashMap<String, Object> f = a2.f();
            HashMap<String, Object> a4 = a(a2, f);
            if (a4 == null || a4.size() <= 0) {
                return z;
            }
            boolean a5 = a(a4);
            if (a5) {
                try {
                    a2.a(f);
                } catch (Throwable th) {
                    th = th;
                    z = a5;
                    cn.sharesdk.framework.utils.e.b().w(th);
                    return z;
                }
            }
            return a5;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void g() {
        try {
            ResHelper.clearCache(MobSDK.getContext());
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().w(th);
        }
    }
}
