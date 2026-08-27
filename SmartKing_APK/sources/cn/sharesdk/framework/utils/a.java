package cn.sharesdk.framework.utils;

import android.util.Base64;
import com.mob.tools.network.KVPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.text.Typography;

/* compiled from: Oauth1Signer.java */
/* loaded from: classes.dex */
public class a {
    private b a = new b();
    private cn.sharesdk.framework.utils.b b = new cn.sharesdk.framework.utils.b("-._~", false);

    /* compiled from: Oauth1Signer.java */
    /* renamed from: cn.sharesdk.framework.utils.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public enum EnumC0006a {
        HMAC_SHA1,
        PLAINTEXT
    }

    /* compiled from: Oauth1Signer.java */
    /* loaded from: classes.dex */
    public static class b {
        public String a;
        public String b;
        public String c;
        public String d;
        public String e;
    }

    private ArrayList<KVPair<String>> a(long j, String str) {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("oauth_consumer_key", this.a.a));
        arrayList.add(new KVPair<>("oauth_signature_method", str));
        arrayList.add(new KVPair<>("oauth_timestamp", String.valueOf(j / 1000)));
        arrayList.add(new KVPair<>("oauth_nonce", String.valueOf(j)));
        arrayList.add(new KVPair<>("oauth_version", "1.0"));
        String str2 = this.a.c;
        if (str2 != null && str2.length() > 0) {
            arrayList.add(new KVPair<>("oauth_token", str2));
        }
        return arrayList;
    }

    private ArrayList<KVPair<String>> a(long j, ArrayList<KVPair<String>> arrayList, String str) {
        HashMap hashMap = new HashMap();
        if (arrayList != null) {
            Iterator<KVPair<String>> it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                hashMap.put(a(next.name), a(next.value));
            }
        }
        ArrayList<KVPair<String>> a = a(j, str);
        if (a != null) {
            Iterator<KVPair<String>> it2 = a.iterator();
            while (it2.hasNext()) {
                KVPair<String> next2 = it2.next();
                hashMap.put(a(next2.name), a(next2.value));
            }
        }
        String[] strArr = new String[hashMap.size()];
        Iterator it3 = hashMap.entrySet().iterator();
        int i = 0;
        while (it3.hasNext()) {
            strArr[i] = (String) ((Map.Entry) it3.next()).getKey();
            i++;
        }
        Arrays.sort(strArr);
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        for (String str2 : strArr) {
            arrayList2.add(new KVPair<>(str2, hashMap.get(str2)));
        }
        return arrayList2;
    }

    private ArrayList<KVPair<String>> a(String str, String str2, ArrayList<KVPair<String>> arrayList, EnumC0006a enumC0006a) throws Throwable {
        String trim;
        long currentTimeMillis = System.currentTimeMillis();
        String str3 = null;
        switch (enumC0006a) {
            case HMAC_SHA1:
                str3 = "HMAC-SHA1";
                SecretKeySpec secretKeySpec = new SecretKeySpec((a(this.a.b) + Typography.amp + a(this.a.d)).getBytes("utf-8"), "HMAC-SHA1");
                Mac mac = Mac.getInstance("HMAC-SHA1");
                mac.init(secretKeySpec);
                trim = new String(Base64.encode(mac.doFinal((str2 + Typography.amp + a(str) + Typography.amp + a(b(a(currentTimeMillis, arrayList, "HMAC-SHA1")))).getBytes("utf-8")), 0)).trim();
                break;
            case PLAINTEXT:
                str3 = "PLAINTEXT";
                trim = a(this.a.b) + Typography.amp + a(this.a.d);
                break;
            default:
                trim = null;
                break;
        }
        ArrayList<KVPair<String>> a = a(currentTimeMillis, str3);
        a.add(new KVPair<>("oauth_signature", trim));
        return a;
    }

    private String b(ArrayList<KVPair<String>> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Iterator<KVPair<String>> it = arrayList.iterator();
        while (it.hasNext()) {
            KVPair<String> next = it.next();
            if (i > 0) {
                sb.append(Typography.amp);
            }
            sb.append(next.name);
            sb.append('=');
            sb.append(next.value);
            i++;
        }
        return sb.toString();
    }

    public b a() {
        return this.a;
    }

    public String a(String str) {
        return str == null ? "" : this.b.escape(str);
    }

    public ArrayList<KVPair<String>> a(String str, ArrayList<KVPair<String>> arrayList) throws Throwable {
        return a(str, arrayList, EnumC0006a.HMAC_SHA1);
    }

    public ArrayList<KVPair<String>> a(String str, ArrayList<KVPair<String>> arrayList, EnumC0006a enumC0006a) throws Throwable {
        return a(str, "POST", arrayList, enumC0006a);
    }

    public ArrayList<KVPair<String>> a(ArrayList<KVPair<String>> arrayList) {
        StringBuilder sb = new StringBuilder("OAuth ");
        Iterator<KVPair<String>> it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            KVPair<String> next = it.next();
            if (i > 0) {
                sb.append(',');
            }
            String a = a(next.value);
            sb.append(next.name);
            sb.append("=\"");
            sb.append(a);
            sb.append("\"");
            i++;
        }
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("Authorization", sb.toString()));
        arrayList2.add(new KVPair<>("Content-Type", "application/x-www-form-urlencoded"));
        return arrayList2;
    }

    public void a(String str, String str2) {
        this.a.c = str;
        this.a.d = str2;
    }

    public void a(String str, String str2, String str3) {
        this.a.a = str;
        this.a.b = str2;
        this.a.e = str3;
    }

    public ArrayList<KVPair<String>> b(String str, ArrayList<KVPair<String>> arrayList) throws Throwable {
        return b(str, arrayList, EnumC0006a.HMAC_SHA1);
    }

    public ArrayList<KVPair<String>> b(String str, ArrayList<KVPair<String>> arrayList, EnumC0006a enumC0006a) throws Throwable {
        return a(str, "GET", arrayList, enumC0006a);
    }

    public ArrayList<KVPair<String>> c(String str, ArrayList<KVPair<String>> arrayList, EnumC0006a enumC0006a) throws Throwable {
        return a(str, "PUT", arrayList, enumC0006a);
    }
}
