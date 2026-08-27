package com.mob.mobapm.internal;

import android.text.TextUtils;
import android.util.Base64;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.commons.MobProductCollector;
import com.mob.mobapm.internal.e;
import com.mob.tools.MobLog;
import com.mob.tools.network.HttpConnection;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.network.KVPair;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.MobRSA;
import com.tencent.bugly.BuglyStrategy;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

/* loaded from: classes.dex */
public final class APMMobCommunicator {
    private BigInteger b;
    private BigInteger c;
    private MobRSA d;
    private e.c g;
    private Random a = new Random();
    private Hashon e = new Hashon();
    private e f = new e();

    /* loaded from: classes.dex */
    public static class Callback<T> implements PublicMemberKeeper {
        public void onResultError(Throwable th) {
        }

        public void onResultOk(T t) {
        }
    }

    /* loaded from: classes.dex */
    public static class NetworkError extends Exception implements PublicMemberKeeper {
        private static final long serialVersionUID = -8447657431687664787L;

        public NetworkError(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements HttpResponseCallback {
        final /* synthetic */ String[] a;

        a(APMMobCommunicator aPMMobCommunicator, String[] strArr) {
            this.a = strArr;
        }

        @Override // com.mob.tools.network.HttpResponseCallback
        public void onResponse(HttpConnection httpConnection) throws Throwable {
            this.a[0] = String.valueOf(httpConnection.getResponseCode());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements HttpResponseCallback {
        final /* synthetic */ String[] a;

        b(String[] strArr) {
            this.a = strArr;
        }

        @Override // com.mob.tools.network.HttpResponseCallback
        public void onResponse(HttpConnection httpConnection) throws Throwable {
            int responseCode = httpConnection.getResponseCode();
            InputStream inputStream = responseCode == 200 ? httpConnection.getInputStream() : httpConnection.getErrorStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            for (int read = inputStream.read(bArr); read != -1; read = inputStream.read(bArr)) {
                byteArrayOutputStream.write(bArr, 0, read);
            }
            inputStream.close();
            byteArrayOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (responseCode != 200) {
                HashMap fromJson = APMMobCommunicator.this.e.fromJson(new String(byteArray, "utf-8"));
                fromJson.put("httpStatus", Integer.valueOf(responseCode));
                throw new NetworkError(APMMobCommunicator.this.e.fromHashMap(fromJson));
            }
            this.a[0] = new String(byteArray, "utf-8");
        }
    }

    public APMMobCommunicator(int i, String str, String str2) {
        this.d = new MobRSA(i);
        this.b = new BigInteger(str, 16);
        this.c = new BigInteger(str2, 16);
        e.c cVar = new e.c();
        this.g = cVar;
        cVar.a = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        cVar.b = 5000;
    }

    private HttpResponseCallback a(byte[] bArr, String[] strArr) {
        return new b(strArr);
    }

    private HttpResponseCallback a(String[] strArr) {
        return new a(this, strArr);
    }

    private String a(byte[] bArr, String str, boolean z) throws Throwable {
        byte[] bytes = str.getBytes("utf-8");
        if (z) {
            bytes = a(bytes);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        byte[] encode = this.d.encode(bArr, this.b, this.c);
        dataOutputStream.writeInt(encode.length);
        dataOutputStream.write(encode);
        byte[] AES128Encode = Data.AES128Encode(bArr, bytes);
        dataOutputStream.writeInt(AES128Encode.length);
        dataOutputStream.write(AES128Encode);
        dataOutputStream.flush();
        dataOutputStream.close();
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
    }

    private ArrayList<KVPair<String>> a(boolean z, HashMap<String, String> hashMap) throws Throwable {
        ArrayList<KVPair<String>> b2 = z ? b() : null;
        if (b2 == null) {
            b2 = new ArrayList<>();
        }
        if (hashMap != null && !hashMap.isEmpty()) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                if (entry != null) {
                    b2.add(new KVPair<>(entry.getKey(), entry.getValue()));
                }
            }
        }
        return b2;
    }

    private byte[] a() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        this.a.setSeed(System.currentTimeMillis());
        dataOutputStream.writeLong(this.a.nextLong());
        dataOutputStream.writeLong(this.a.nextLong());
        dataOutputStream.flush();
        dataOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] a(byte[] bArr) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new GZIPOutputStream(byteArrayOutputStream));
        bufferedOutputStream.write(bArr);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private Object b(String str) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("code", str);
        return hashMap;
    }

    private ArrayList<KVPair<String>> b() throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("Content-Type", "application/json"));
        arrayList.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
        if (!TextUtils.isEmpty(g.b().a())) {
            arrayList.add(new KVPair<>("moid", g.b().a()));
        }
        return arrayList;
    }

    private Object c(String str) throws Throwable {
        if (str == null) {
            HashMap hashMap = new HashMap();
            hashMap.put("status", -1);
            hashMap.put("error", "response is empty");
            throw new NetworkError(this.e.fromHashMap(hashMap));
        }
        HashMap fromJson = this.e.fromJson(str.trim());
        if (fromJson.isEmpty()) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("status", -1);
            hashMap2.put("error", "response is empty");
            throw new NetworkError(this.e.fromHashMap(hashMap2));
        }
        Object obj = fromJson.get("res");
        if (obj == null) {
            obj = fromJson.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
        }
        return obj == null ? fromJson : obj;
    }

    public <T> T a(String str, String str2, boolean z) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        HashMap<String, T> fromJson = this.e.fromJson(str2);
        for (String str3 : fromJson.keySet()) {
            arrayList.add(new KVPair<>(str3, String.valueOf(fromJson.get(str3))));
        }
        return (T) this.f.a(str, arrayList, z ? a(z, (HashMap<String, String>) null) : null, this.g);
    }

    public <T> T a(String str, boolean z, HashMap<String, String> hashMap, String str2) throws Throwable {
        ArrayList<KVPair<String>> a2 = a(z, hashMap);
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(str2)) {
            HashMap<String, T> fromJson = this.e.fromJson(str2);
            for (String str3 : fromJson.keySet()) {
                arrayList.add(new KVPair<>(str3, String.valueOf(fromJson.get(str3))));
            }
        }
        return (T) this.f.a(str, arrayList, null, a2, this.g);
    }

    public <T> T a(HashMap<String, Object> hashMap, String str, boolean z, boolean z2) throws Throwable {
        return (T) a(null, hashMap, str, z, z2);
    }

    public <T> T a(HashMap<String, String> hashMap, HashMap<String, Object> hashMap2, String str, boolean z, boolean z2) throws Throwable {
        return (T) a(true, z2, hashMap, hashMap2, str, z);
    }

    public <T> T a(boolean z, boolean z2, HashMap<String, String> hashMap, String str, String str2, boolean z3) throws Throwable {
        byte[] a2 = a();
        String a3 = a(a2, str, z3);
        int length = a3.getBytes("utf-8").length;
        ArrayList<KVPair<String>> a4 = a(z, hashMap);
        String[] strArr = new String[1];
        HttpResponseCallback a5 = a(a2, strArr);
        f fVar = new f();
        fVar.a(a3);
        MobLog.getInstance().d(">>>  request: " + str + "\nurl = " + str2 + "\nheader = " + a4.toString(), new Object[0]);
        if (z2) {
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            HashMap<String, T> fromJson = this.e.fromJson(str);
            for (String str3 : fromJson.keySet()) {
                arrayList.add(new KVPair<>(str3, String.valueOf(fromJson.get(str3))));
            }
            return (T) this.f.a(str2, arrayList, a4, this.g);
        }
        this.f.a(str2, a4, fVar, -1, a5, this.g);
        if (strArr[0] == null) {
            return null;
        }
        MobLog.getInstance().d(">>> response: " + strArr[0], new Object[0]);
        return (T) c(strArr[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:3:0x000d, code lost:
    
        if (r11.length() == 0) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> T a(boolean r8, boolean r9, java.util.HashMap<java.lang.String, java.lang.String> r10, java.util.HashMap<java.lang.String, java.lang.Object> r11, java.lang.String r12, boolean r13) throws java.lang.Throwable {
        /*
            r7 = this;
            if (r11 != 0) goto L3
            goto Lf
        L3:
            com.mob.tools.utils.Hashon r0 = r7.e
            java.lang.String r11 = r0.fromHashMap(r11)
            int r0 = r11.length()
            if (r0 != 0) goto L11
        Lf:
            java.lang.String r11 = "{}"
        L11:
            r4 = r11
            r0 = r7
            r1 = r8
            r2 = r9
            r3 = r10
            r5 = r12
            r6 = r13
            java.lang.Object r8 = r0.a(r1, r2, r3, r4, r5, r6)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.mobapm.internal.APMMobCommunicator.a(boolean, boolean, java.util.HashMap, java.util.HashMap, java.lang.String, boolean):java.lang.Object");
    }

    public HashMap<String, Object> a(String str) throws Throwable {
        File file = new File(MobSDK.getContext().getFilesDir(), "temp");
        File file2 = new File(file, "radar");
        if (!file2.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", this.f.a(str, fileOutputStream, this.g) ? "200" : "-1");
        return hashMap;
    }

    public <T> T b(String str, String str2, boolean z) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(str2)) {
            HashMap<String, T> fromJson = this.e.fromJson(str2);
            for (String str3 : fromJson.keySet()) {
                arrayList.add(new KVPair<>(str3, String.valueOf(fromJson.get(str3))));
            }
        }
        return (T) this.f.b(str, arrayList, z ? a(z, (HashMap<String, String>) null) : null, this.g);
    }

    public <T> T b(String str, boolean z, HashMap<String, String> hashMap, String str2) throws Throwable {
        ArrayList<KVPair<String>> a2 = a(z, hashMap);
        f fVar = new f();
        if (!TextUtils.isEmpty(str2)) {
            fVar.a(str2);
        }
        String[] strArr = new String[1];
        this.f.a(str, a2, fVar, -1, a(strArr), this.g);
        if (strArr[0] == null) {
            return null;
        }
        MobLog.getInstance().d(">>> response code: " + strArr[0], new Object[0]);
        return (T) b(strArr[0]);
    }
}
