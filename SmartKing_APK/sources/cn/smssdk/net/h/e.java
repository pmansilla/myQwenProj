package cn.smssdk.net.h;

import android.text.TextUtils;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.wrapper.TokenVerifyResult;
import com.mob.MobSDK;
import com.mob.tools.network.HttpConnection;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Hashon;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/* compiled from: LoginCore.java */
/* loaded from: classes.dex */
public class e {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LoginCore.java */
    /* loaded from: classes.dex */
    public class a extends cn.smssdk.utils.d {
        final /* synthetic */ TokenVerifyResult a;
        final /* synthetic */ String b;
        final /* synthetic */ Hashon c;
        final /* synthetic */ d d;

        /* compiled from: LoginCore.java */
        /* renamed from: cn.smssdk.net.h.e$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class C0020a implements HttpResponseCallback {
            C0020a() {
            }

            @Override // com.mob.tools.network.HttpResponseCallback
            public void onResponse(HttpConnection httpConnection) throws Throwable {
                StringBuilder sb = new StringBuilder();
                int responseCode = httpConnection.getResponseCode();
                if (200 != responseCode) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        } else {
                            sb.append(readLine);
                        }
                    }
                    bufferedReader.close();
                    SMSLog.getInstance().d(SMSLog.FORMAT_SIMPLE, "Response: " + sb.toString());
                    HashMap hashMap = new HashMap();
                    hashMap.put("error", sb.toString());
                    hashMap.put("status", Integer.valueOf(responseCode));
                    throw new Throwable(new Hashon().fromHashMap(hashMap));
                }
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                while (true) {
                    String readLine2 = bufferedReader2.readLine();
                    if (readLine2 == null) {
                        break;
                    } else {
                        sb.append(readLine2);
                    }
                }
                bufferedReader2.close();
                String sb2 = sb.toString();
                SMSLog.getInstance().d(SMSLog.FORMAT_SIMPLE, "Response: " + sb2);
                JSONObject jSONObject = new JSONObject(sb2);
                int optInt = jSONObject.optInt("status");
                String string = jSONObject.getString("res");
                String string2 = jSONObject.getString("error");
                if (200 != optInt) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("status", Integer.valueOf(optInt));
                    hashMap2.put("detail", string2);
                    a.this.d.a(new Throwable(a.this.c.fromHashMap(hashMap2)));
                    return;
                }
                String str = new String(c.b(cn.smssdk.net.h.a.a(string.getBytes()), MobSDK.getAppSecret().getBytes()));
                if (new JSONObject(str).optInt("isValid") == 1) {
                    a.this.d.a();
                    return;
                }
                HashMap hashMap3 = new HashMap();
                hashMap3.put("status", Integer.valueOf(optInt));
                hashMap3.put("detail", str);
                a.this.d.a(new Throwable(a.this.c.fromHashMap(hashMap3)));
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(e eVar, String str, TokenVerifyResult tokenVerifyResult, String str2, Hashon hashon, d dVar) {
            super(str);
            this.a = tokenVerifyResult;
            this.b = str2;
            this.c = hashon;
            this.d = dVar;
        }

        @Override // cn.smssdk.utils.d
        protected void a() {
            try {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("appkey", MobSDK.getAppkey());
                hashMap.put("opToken", this.a.getOpToken());
                hashMap.put("token", this.a.getToken());
                hashMap.put("operator", this.a.getOperator());
                hashMap.put("phone", this.b);
                hashMap.put("timestamp", Long.valueOf(System.currentTimeMillis()));
                hashMap.put("sign", g.a(hashMap, MobSDK.getAppSecret()));
                ArrayList<KVPair<String>> arrayList = new ArrayList<>();
                arrayList.add(new KVPair<>("Content-Type", "application/json"));
                NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
                networkTimeOut.readTimout = 5000;
                networkTimeOut.connectionTimeout = 5000;
                SMSLog.getInstance().d("Request: http://identify.verify.mob.com/auth/verify/mobile\nparams: " + this.c.fromHashMap(hashMap), new Object[0]);
                new NetworkHelper().jsonPost("http://identify.verify.mob.com/auth/verify/mobile", hashMap, arrayList, networkTimeOut, new C0020a());
            } catch (Throwable th) {
                SMSLog.getInstance().d(SMSLog.FORMAT_SIMPLE, "login exception: " + th);
                HashMap hashMap2 = new HashMap();
                hashMap2.put("status", 615);
                this.d.a(new Throwable(this.c.fromHashMap(hashMap2)));
            }
        }
    }

    public void a(String str, TokenVerifyResult tokenVerifyResult, d dVar) {
        if (tokenVerifyResult != null && !TextUtils.isEmpty(str)) {
            new a(this, "login", tokenVerifyResult, str, new Hashon(), dVar).start();
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("status", 618);
        dVar.a(new Throwable(new Hashon().fromHashMap(hashMap)));
    }
}
