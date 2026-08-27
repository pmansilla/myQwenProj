package com.mob;

import android.util.Base64;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.commons.MobProductCollector;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.RxMob;
import com.mob.tools.network.HttpConnection;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.network.StringPart;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.MobRSA;
import com.tencent.bugly.BuglyStrategy;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

/* loaded from: classes.dex */
public final class MobCommunicator implements PublicMemberKeeper {
    private BigInteger modulus;
    private BigInteger publicKey;
    private MobRSA rsa;
    private NetworkHelper.NetworkTimeOut timeout;
    private Random rnd = new Random();
    private Hashon hashon = new Hashon();
    private com.mob.mcl.b.a mcLink = new com.mob.mcl.b.a();

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
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: classes.dex */
    public class a<T> extends RxMob.QuickSubscribe<T> {
        final /* synthetic */ boolean a;
        final /* synthetic */ HashMap b;
        final /* synthetic */ HashMap c;
        final /* synthetic */ String d;
        final /* synthetic */ boolean e;

        a(boolean z, HashMap hashMap, HashMap hashMap2, String str, boolean z2) {
            this.a = z;
            this.b = hashMap;
            this.c = hashMap2;
            this.d = str;
            this.e = z2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.mob.tools.RxMob.QuickSubscribe
        protected void doNext(RxMob.Subscriber<T> subscriber) throws Throwable {
            subscriber.onNext(!DeviceAuthorizer.isFor() ? MobCommunicator.this.requestSynchronized(this.a, this.b, this.c, this.d, this.e) : null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: classes.dex */
    public class b<T> extends RxMob.Subscriber<T> {
        final /* synthetic */ Callback a;

        b(MobCommunicator mobCommunicator, Callback callback) {
            this.a = callback;
        }

        @Override // com.mob.tools.RxMob.Subscriber
        public void onError(Throwable th) {
            this.a.onResultError(th);
        }

        @Override // com.mob.tools.RxMob.Subscriber
        public void onNext(T t) {
            this.a.onResultOk(t);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements HttpResponseCallback {
        final /* synthetic */ String[] a;
        final /* synthetic */ byte[] b;

        c(String[] strArr, byte[] bArr) {
            this.a = strArr;
            this.b = bArr;
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
                HashMap fromJson = MobCommunicator.this.hashon.fromJson(new String(byteArray, "utf-8"));
                fromJson.put("httpStatus", Integer.valueOf(responseCode));
                throw new NetworkError(MobCommunicator.this.hashon.fromHashMap(fromJson));
            }
            long contentLength = MobCommunicator.this.getContentLength(httpConnection);
            if (contentLength == -1 || contentLength != byteArray.length) {
                HashMap hashMap = new HashMap();
                hashMap.put("httpStatus", Integer.valueOf(responseCode));
                hashMap.put("status", -2);
                hashMap.put("error", "Illegal content length");
                throw new NetworkError(MobCommunicator.this.hashon.fromHashMap(hashMap));
            }
            this.a[0] = MobCommunicator.this.decipher(this.b, byteArray);
            this.a[1] = MobCommunicator.this.isTcpResponse(httpConnection);
        }
    }

    public MobCommunicator(int i, String str, String str2) {
        this.rsa = new MobRSA(i);
        this.publicKey = new BigInteger(str, 16);
        this.modulus = new BigInteger(str2, 16);
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        this.timeout = networkTimeOut;
        networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        networkTimeOut.connectionTimeout = 5000;
    }

    private byte[] compress(byte[] bArr) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new GZIPOutputStream(byteArrayOutputStream));
        bufferedOutputStream.write(bArr);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String decipher(byte[] bArr, byte[] bArr2) throws Throwable {
        return new String(Data.AES128Decode(bArr, Base64.decode(bArr2, 2)), "utf-8");
    }

    private String encipher(byte[] bArr, String str, boolean z) throws Throwable {
        byte[] bytes = str.getBytes("utf-8");
        if (z) {
            bytes = compress(bytes);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        byte[] encode = this.rsa.encode(bArr, this.publicKey, this.modulus);
        dataOutputStream.writeInt(encode.length);
        dataOutputStream.write(encode);
        byte[] AES128Encode = Data.AES128Encode(bArr, bytes);
        dataOutputStream.writeInt(AES128Encode.length);
        dataOutputStream.write(AES128Encode);
        dataOutputStream.flush();
        dataOutputStream.close();
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
    }

    private byte[] getAESKey() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        this.rnd.setSeed(System.currentTimeMillis());
        dataOutputStream.writeLong(this.rnd.nextLong());
        dataOutputStream.writeLong(this.rnd.nextLong());
        dataOutputStream.flush();
        dataOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getContentLength(HttpConnection httpConnection) throws Throwable {
        List<String> header = getHeader(httpConnection, "Content-Length");
        if (header == null || header.size() <= 0) {
            return -1L;
        }
        return Long.parseLong(header.get(0));
    }

    private ArrayList<KVPair<String>> getDefaultHeaders(String str, int i) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("sign", Data.MD5(str + MobSDK.getAppSecret())));
        arrayList.add(new KVPair<>("key", MobSDK.getAppkey()));
        arrayList.add(new KVPair<>("Content-Length", String.valueOf(i)));
        arrayList.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
        arrayList.add(new KVPair<>("moid", DeviceAuthorizer.getMString(MobSDK.getContext())));
        return arrayList;
    }

    private List<String> getHeader(HttpConnection httpConnection, String str) throws Throwable {
        Map<String, List<String>> headerFields = httpConnection.getHeaderFields();
        if (headerFields == null || headerFields.isEmpty()) {
            return null;
        }
        for (String str2 : headerFields.keySet()) {
            if (str2 != null && str2.equals(str)) {
                return headerFields.get(str2);
            }
        }
        return null;
    }

    private ArrayList<KVPair<String>> getHeaders(boolean z, HashMap<String, String> hashMap, String str, int i) throws Throwable {
        ArrayList<KVPair<String>> defaultHeaders = z ? getDefaultHeaders(str, i) : null;
        if (defaultHeaders == null) {
            defaultHeaders = new ArrayList<>();
        }
        if (hashMap != null && !hashMap.isEmpty()) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                if (entry != null) {
                    defaultHeaders.add(new KVPair<>(entry.getKey(), entry.getValue()));
                }
            }
        }
        return defaultHeaders;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isTcpResponse(HttpConnection httpConnection) throws Throwable {
        if (!(httpConnection instanceof com.mob.mcl.b.c)) {
            return "http";
        }
        List<String> header = getHeader(httpConnection, "apc");
        return (header == null || header.size() <= 0 || !header.get(0).equals("true")) ? "tcp" : "apc";
    }

    private HttpResponseCallback newCallback(byte[] bArr, String[] strArr) {
        return new c(strArr, bArr);
    }

    private Object processResponse(String str) throws Throwable {
        if (str == null) {
            HashMap hashMap = new HashMap();
            hashMap.put("status", -1);
            hashMap.put("error", "response is empty");
            throw new NetworkError(this.hashon.fromHashMap(hashMap));
        }
        HashMap fromJson = this.hashon.fromJson(str.trim());
        if (!fromJson.isEmpty()) {
            Object obj = fromJson.get("res");
            return obj == null ? fromJson.get(AeUtil.ROOT_DATA_PATH_OLD_NAME) : obj;
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put("status", -1);
        hashMap2.put("error", "response is empty");
        throw new NetworkError(this.hashon.fromHashMap(hashMap2));
    }

    public void addTcpIntercept(String str) {
        this.mcLink.a(str);
    }

    public void removeTcpIntercept(String str) {
        this.mcLink.b(str);
    }

    public <T> void request(HashMap<String, Object> hashMap, String str, boolean z, Callback<T> callback) {
        request(true, null, hashMap, str, z, callback);
    }

    public <T> void request(HashMap<String, String> hashMap, HashMap<String, Object> hashMap2, String str, boolean z, Callback<T> callback) {
        request(true, hashMap, hashMap2, str, z, callback);
    }

    public <T> void request(boolean z, HashMap<String, String> hashMap, HashMap<String, Object> hashMap2, String str, boolean z2, Callback<T> callback) {
        RxMob.create(new a(z, hashMap, hashMap2, str, z2)).subscribeOnNewThreadAndObserveOnUIThread(new b(this, callback));
    }

    public String requestSyncHttpGet(String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2) throws Throwable {
        String arrayList3 = arrayList != null ? arrayList.toString() : "";
        String arrayList4 = arrayList2 != null ? arrayList2.toString() : "";
        com.mob.mcl.d.b.a().a(">>> get request: " + arrayList3 + "\nurl = " + str + "\nheader = " + arrayList4);
        KVPair<String> a2 = this.mcLink.a(false, str, arrayList, arrayList2, this.timeout);
        if (a2 == null) {
            return null;
        }
        com.mob.mcl.d.b.a().a(">>> get " + a2.name + " response :  " + a2.value);
        return a2.value;
    }

    public <T> T requestSynchronized(String str, String str2, boolean z) throws Throwable {
        return (T) requestSynchronized((HashMap<String, String>) null, str, str2, z);
    }

    public <T> T requestSynchronized(HashMap<String, String> hashMap, String str, String str2, boolean z) throws Throwable {
        return (T) requestSynchronized(true, hashMap, str, str2, z);
    }

    public <T> T requestSynchronized(HashMap<String, Object> hashMap, String str, boolean z) throws Throwable {
        return (T) requestSynchronized((HashMap<String, String>) null, hashMap, str, z);
    }

    public <T> T requestSynchronized(HashMap<String, String> hashMap, HashMap<String, Object> hashMap2, String str, boolean z) throws Throwable {
        return (T) requestSynchronized(true, hashMap, hashMap2, str, z);
    }

    public <T> T requestSynchronized(boolean z, HashMap<String, String> hashMap, String str, String str2, boolean z2) throws Throwable {
        byte[] aESKey = getAESKey();
        String encipher = encipher(aESKey, str, z2);
        ArrayList<KVPair<String>> headers = getHeaders(z, hashMap, str, encipher.getBytes("utf-8").length);
        String[] strArr = new String[2];
        HttpResponseCallback newCallback = newCallback(aESKey, strArr);
        StringPart stringPart = new StringPart();
        stringPart.append(encipher);
        com.mob.mcl.d.b.a().a(">>>  request: " + str + "\nurl = " + str2 + "\nheader = " + headers.toString());
        this.mcLink.a(false, str2, headers, stringPart, -1, newCallback, this.timeout);
        if (strArr[0] == null) {
            return null;
        }
        com.mob.mcl.d.b.a().a(">>> " + strArr[1] + " response: " + strArr[0]);
        return (T) processResponse(strArr[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:3:0x000d, code lost:
    
        if (r9.length() == 0) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> T requestSynchronized(boolean r7, java.util.HashMap<java.lang.String, java.lang.String> r8, java.util.HashMap<java.lang.String, java.lang.Object> r9, java.lang.String r10, boolean r11) throws java.lang.Throwable {
        /*
            r6 = this;
            if (r9 != 0) goto L3
            goto Lf
        L3:
            com.mob.tools.utils.Hashon r0 = r6.hashon
            java.lang.String r9 = r0.fromHashMap(r9)
            int r0 = r9.length()
            if (r0 != 0) goto L11
        Lf:
            java.lang.String r9 = "{}"
        L11:
            r3 = r9
            r0 = r6
            r1 = r7
            r2 = r8
            r4 = r10
            r5 = r11
            java.lang.Object r7 = r0.requestSynchronized(r1, r2, r3, r4, r5)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.MobCommunicator.requestSynchronized(boolean, java.util.HashMap, java.util.HashMap, java.lang.String, boolean):java.lang.Object");
    }
}
