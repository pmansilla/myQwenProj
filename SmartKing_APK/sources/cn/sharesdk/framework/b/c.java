package cn.sharesdk.framework.b;

import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.b.a.e;
import cn.sharesdk.framework.utils.g;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AeUtil;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.commons.InternationalDomain;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.tencent.bugly.BuglyStrategy;
import io.reactivex.annotations.SchedulerSupport;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import me.panpf.sketch.uri.FileUriModel;

/* compiled from: Protocols.java */
/* loaded from: classes.dex */
public class c {
    private static String a = "";
    private static MobCommunicator j;
    private e b = e.a();
    private DeviceHelper c = DeviceHelper.getInstance(MobSDK.getContext());
    private NetworkHelper d = new NetworkHelper();
    private Hashon e = new Hashon();
    private String f;
    private String g;
    private boolean h;
    private HashMap<String, String> i;

    public c() {
        try {
            this.i = (HashMap) this.b.h("buffered_server_paths");
        } catch (Throwable unused) {
            this.i = new HashMap<>();
        }
        InternationalDomain domain = MobSDK.getDomain();
        if (new g().a(domain) && domain != null) {
            a = domain.getDomain();
        }
        h();
    }

    private String f(String str) throws Throwable {
        boolean c = this.b.c();
        boolean d = this.b.d();
        StringBuilder sb = new StringBuilder();
        sb.append(Data.urlEncode(this.c.getPackageName(), "utf-8"));
        sb.append("|");
        sb.append(Data.urlEncode(this.c.getAppVersionName(), "utf-8"));
        sb.append("|");
        sb.append(Data.urlEncode(String.valueOf(ShareSDK.SDK_VERSION_CODE), "utf-8"));
        sb.append("|");
        sb.append(Data.urlEncode(String.valueOf(this.c.getPlatformCode()), "utf-8"));
        sb.append("|");
        sb.append(Data.urlEncode(this.c.getDetailNetworkTypeForStatic(), "utf-8"));
        sb.append("|");
        if (c) {
            sb.append(Data.urlEncode(String.valueOf(this.c.getOSVersionInt()), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(this.c.getScreenSize(), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(this.c.getManufacturer(), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(this.c.getModel(), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(this.c.getCarrier(), "utf-8"));
            sb.append("|");
        } else {
            sb.append("|||||");
        }
        if (d) {
            sb.append(str);
        } else {
            sb.append(str.split("\\|")[0]);
            sb.append("|||||");
        }
        String sb2 = sb.toString();
        cn.sharesdk.framework.utils.e.b().i("shorLinkMsg ===>>>>", sb2);
        return Base64.encodeToString(Data.AES128Encode(Data.rawMD5(String.format("%s:%s", this.c.getDeviceKey(), MobSDK.getAppkey())), sb2), 2);
    }

    private static synchronized MobCommunicator g() {
        MobCommunicator mobCommunicator;
        synchronized (c.class) {
            if (j == null) {
                j = new MobCommunicator(1024, "bb7addd7e33383b74e82aba9b1d274c73aea6c0c71fcc88730270f630dbe490e1d162004f74e9532f98e17004630fbea9b346de63c23e83a7dfad70dd47cebfd", "288e7c44e01569a905386e6341baabfcde63ec37d0f0835cc662c299a5d0072970808a7fa434f0a51fa581d09d5ec4350ba5d548eafbe1fd956fb3afd678c1fb6134c904668652ec5cceb5d85da337a0f2f13ea457cca74a01b3ba0f4c809ad30d382bba2562ec9b996ae44c3700731c1b914997ef826331759e4084a019a03f");
            }
            mobCommunicator = j;
        }
        return mobCommunicator;
    }

    private void h() {
        this.f = (this.c.getPackageName() + FileUriModel.SCHEME + this.c.getAppVersionName()) + SQLBuilder.BLANK + "ShareSDK/3.4.1" + SQLBuilder.BLANK + ("Android/" + this.c.getOSVersionInt());
        this.g = b("http://api.share.mob.com:80");
        this.h = true;
    }

    private String i() {
        return this.g + "/conn";
    }

    private String j() {
        if (this.i == null || !this.i.containsKey("/date")) {
            return this.g + "/date";
        }
        return this.i.get("/date") + "/date";
    }

    private String k() {
        return this.g + "/conf5";
    }

    private String l() {
        return b("http://up.mob.com/upload/image");
    }

    private String m() {
        if (this.i == null || !this.i.containsKey("/log4")) {
            return this.g + "/log4";
        }
        return this.i.get("/log4") + "/log4";
    }

    private String n() {
        return b("http://l.mob.com/url/shareSdkEncryptMapping.do");
    }

    private String o() {
        if (this.i == null || !this.i.containsKey("/snsconf")) {
            return this.g + "/snsconf";
        }
        return this.i.get("/snsconf") + "/snsconf";
    }

    public HashMap<String, Object> a() throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Identity", cn.sharesdk.framework.a.a.a()));
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        networkTimeOut.connectionTimeout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        String httpPost = this.d.httpPost(i(), arrayList, (KVPair<String>) null, arrayList2, networkTimeOut);
        cn.sharesdk.framework.utils.e.b().i(" isConnectToServer response == %s", httpPost);
        return this.e.fromJson(httpPost);
    }

    public HashMap<String, Object> a(String str, ArrayList<String> arrayList, int i, String str2) throws Throwable {
        if (!this.h) {
            return null;
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new KVPair("key", MobSDK.getAppkey()));
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(new KVPair("urls", arrayList.get(i2).toString()));
        }
        arrayList2.add(new KVPair("deviceid", this.c.getDeviceKey()));
        arrayList2.add(new KVPair("snsplat", String.valueOf(i)));
        String f = f(str2);
        if (TextUtils.isEmpty(f)) {
            return null;
        }
        arrayList2.add(new KVPair("m", f));
        new ArrayList().add(new KVPair("User-Identity", cn.sharesdk.framework.a.a.a()));
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = 5000;
        networkTimeOut.connectionTimeout = 5000;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("key", MobSDK.getAppkey());
        ArrayList arrayList3 = new ArrayList();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            arrayList3.add(URLEncoder.encode(arrayList.get(i3), "UTF-8"));
        }
        hashMap.put("urls", arrayList3);
        hashMap.put("deviceid", this.c.getDeviceKey());
        hashMap.put("snsplat", Integer.valueOf(i));
        if (TextUtils.isEmpty(f)) {
            return null;
        }
        hashMap.put("m", f);
        HashMap<String, Object> hashMap2 = (HashMap) g().requestSynchronized(hashMap, n(), false);
        cn.sharesdk.framework.utils.e.b().i("> SERVER_SHORT_LINK_URL  resp: %s", hashMap2);
        if (hashMap2.size() == 0) {
            this.h = false;
            return null;
        }
        if (hashMap2.get(AeUtil.ROOT_DATA_PATH_OLD_NAME) == null) {
            return null;
        }
        return hashMap2;
    }

    public void a(cn.sharesdk.framework.b.b.c cVar) throws Throwable {
        cn.sharesdk.framework.b.a.d.a(cVar.toString(), cVar.e);
    }

    public void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        cn.sharesdk.framework.utils.e.b().d("duid === " + str, new Object[0]);
        this.f += SQLBuilder.BLANK + str;
    }

    public void a(ArrayList<String> arrayList) throws Throwable {
        cn.sharesdk.framework.b.a.d.a(arrayList);
    }

    public void a(HashMap<String, String> hashMap) {
        this.i = hashMap;
        this.b.a("buffered_server_paths", this.i);
    }

    public boolean a(String str, boolean z) {
        try {
            if (!MobSDK.isMob()) {
                return true;
            }
            if (SchedulerSupport.NONE.equals(this.c.getDetailNetworkTypeForStatic())) {
                throw new IllegalStateException("network is disconnected!");
            }
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("m", str));
            arrayList.add(new KVPair<>("t", z ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_GPS));
            ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
            arrayList2.add(new KVPair<>("User-Identity", cn.sharesdk.framework.a.a.a()));
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
            networkTimeOut.connectionTimeout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
            String httpPost = this.d.httpPost(m(), arrayList, (KVPair<String>) null, arrayList2, networkTimeOut);
            cn.sharesdk.framework.utils.e.b().i("> Upload All Log  resp: %s", httpPost);
            return TextUtils.isEmpty(httpPost) || ((Integer) this.e.fromJson(httpPost).get("status")).intValue() == 200;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return false;
        }
    }

    public long b() throws Throwable {
        if (!this.b.i()) {
            return 0L;
        }
        String str = "{}";
        try {
            str = this.d.httpGet(j(), null, null, null);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
        HashMap fromJson = this.e.fromJson(str);
        if (!fromJson.containsKey("timestamp")) {
            return this.b.b();
        }
        try {
            long currentTimeMillis = System.currentTimeMillis() - ResHelper.parseLong(String.valueOf(fromJson.get("timestamp")));
            this.b.a("service_time", Long.valueOf(currentTimeMillis));
            return currentTimeMillis;
        } catch (Throwable th2) {
            cn.sharesdk.framework.utils.e.b().d(th2);
            return this.b.b();
        }
    }

    public String b(String str) {
        if (TextUtils.isEmpty(a) || TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            int indexOf = str.indexOf("://");
            StringBuffer stringBuffer = new StringBuffer();
            int i = indexOf + 3;
            stringBuffer.append(str.substring(0, i));
            stringBuffer.append(a + ".");
            stringBuffer.append(str.substring(i, str.length()));
            String stringBuffer2 = stringBuffer.toString();
            try {
                cn.sharesdk.framework.utils.e.b().d("DomainUrl = " + stringBuffer2, new Object[0]);
                return stringBuffer2;
            } catch (Throwable th) {
                th = th;
                str = stringBuffer2;
                cn.sharesdk.framework.utils.e.b().d(th);
                return str;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void b(HashMap<String, Object> hashMap) throws Throwable {
        this.b.e(this.e.fromHashMap(hashMap));
    }

    public HashMap<String, Object> c() throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
        arrayList.add(new KVPair<>("device", this.c.getDeviceKey()));
        arrayList.add(new KVPair<>("plat", String.valueOf(this.c.getPlatformCode())));
        arrayList.add(new KVPair<>("apppkg", this.c.getPackageName()));
        arrayList.add(new KVPair<>("appver", String.valueOf(this.c.getAppVersion())));
        arrayList.add(new KVPair<>("sdkver", String.valueOf(ShareSDK.SDK_VERSION_CODE)));
        arrayList.add(new KVPair<>("networktype", this.c.getDetailNetworkTypeForStatic()));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Identity", cn.sharesdk.framework.a.a.a()));
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = 10000;
        networkTimeOut.connectionTimeout = 10000;
        String httpPost = this.d.httpPost(k(), arrayList, (KVPair<String>) null, arrayList2, networkTimeOut);
        cn.sharesdk.framework.utils.e.b().i(" get server config response == %s", httpPost);
        return this.e.fromJson(httpPost);
    }

    public void c(String str) {
        this.g = str;
    }

    public HashMap<String, Object> d() throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
        arrayList.add(new KVPair<>("device", this.c.getDeviceKey()));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Identity", cn.sharesdk.framework.a.a.a()));
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = 10000;
        networkTimeOut.connectionTimeout = 10000;
        return this.e.fromJson(this.d.httpPost(o(), arrayList, (KVPair<String>) null, arrayList2, networkTimeOut));
    }

    public HashMap<String, Object> d(String str) throws Throwable {
        KVPair<String> kVPair = new KVPair<>(AmapLoc.TYPE_OFFLINE_CELL, str);
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("User-Identity", cn.sharesdk.framework.a.a.a()));
        String httpPost = this.d.httpPost(l(), (ArrayList<KVPair<String>>) null, kVPair, arrayList, (NetworkHelper.NetworkTimeOut) null);
        cn.sharesdk.framework.utils.e.b().i("upload file response == %s", httpPost);
        return this.e.fromJson(httpPost);
    }

    public ArrayList<cn.sharesdk.framework.b.a.c> e() throws Throwable {
        ArrayList<cn.sharesdk.framework.b.a.c> a2 = cn.sharesdk.framework.b.a.d.a();
        return a2 == null ? new ArrayList<>() : a2;
    }

    public HashMap<String, Object> e(String str) throws Throwable {
        return this.e.fromJson(new String(Data.AES128Decode(Data.rawMD5(MobSDK.getAppkey() + ":" + this.c.getDeviceKey()), Base64.decode(str, 2)), "UTF-8").trim());
    }

    public HashMap<String, Object> f() throws Throwable {
        return this.e.fromJson(this.b.g());
    }
}
