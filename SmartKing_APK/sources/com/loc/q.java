package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.MyLocationStyle;
import com.sun.mail.imap.IMAPStore;
import org.json.JSONObject;

/* compiled from: H5LocationClient.java */
/* loaded from: classes.dex */
public final class q {
    a c;
    private Context d;
    private WebView f;
    Object a = new Object();
    private AMapLocationClient e = null;
    private String g = "AMap.Geolocation.cbk";
    AMapLocationClientOption b = null;
    private volatile boolean h = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: H5LocationClient.java */
    /* loaded from: classes.dex */
    public class a implements AMapLocationListener {
        a() {
        }

        @Override // com.amap.api.location.AMapLocationListener
        public final void onLocationChanged(AMapLocation aMapLocation) {
            if (q.this.h) {
                q.a(q.this, q.b(aMapLocation));
            }
        }
    }

    public q(Context context, WebView webView) {
        this.f = null;
        this.c = null;
        this.d = context.getApplicationContext();
        this.f = webView;
        this.c = new a();
    }

    static /* synthetic */ void a(q qVar, final String str) {
        try {
            if (qVar.f != null) {
                if (Build.VERSION.SDK_INT < 19) {
                    qVar.f.post(new Runnable() { // from class: com.loc.q.2
                        @Override // java.lang.Runnable
                        public final void run() {
                            q.this.f.loadUrl("javascript:" + q.this.g + "('" + str + "')");
                        }
                    });
                    return;
                }
                qVar.f.evaluateJavascript("javascript:" + qVar.g + "('" + str + "')", new ValueCallback<String>() { // from class: com.loc.q.1
                    @Override // android.webkit.ValueCallback
                    public final /* bridge */ /* synthetic */ void onReceiveValue(String str2) {
                    }
                });
            }
        } catch (Throwable th) {
            es.a(th, "H5LocationClient", "callbackJs()");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(AMapLocation aMapLocation) {
        String str;
        Object obj;
        JSONObject jSONObject = new JSONObject();
        if (aMapLocation == null) {
            jSONObject.put(MyLocationStyle.ERROR_CODE, -1);
            str = MyLocationStyle.ERROR_INFO;
            obj = "unknownError";
        } else {
            if (aMapLocation.getErrorCode() != 0) {
                jSONObject.put(MyLocationStyle.ERROR_CODE, aMapLocation.getErrorCode());
                jSONObject.put(MyLocationStyle.ERROR_INFO, aMapLocation.getErrorInfo());
                jSONObject.put("locationDetail", aMapLocation.getLocationDetail());
                return jSONObject.toString();
            }
            jSONObject.put(MyLocationStyle.ERROR_CODE, 0);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("x", aMapLocation.getLongitude());
            jSONObject2.put("y", aMapLocation.getLatitude());
            jSONObject2.put("precision", aMapLocation.getAccuracy());
            jSONObject2.put("type", aMapLocation.getLocationType());
            jSONObject2.put("country", aMapLocation.getCountry());
            jSONObject2.put("province", aMapLocation.getProvince());
            jSONObject2.put("city", aMapLocation.getCity());
            jSONObject2.put("cityCode", aMapLocation.getCityCode());
            jSONObject2.put("district", aMapLocation.getDistrict());
            jSONObject2.put("adCode", aMapLocation.getAdCode());
            jSONObject2.put("street", aMapLocation.getStreet());
            jSONObject2.put("streetNum", aMapLocation.getStreetNum());
            jSONObject2.put("floor", aMapLocation.getFloor());
            jSONObject2.put(IMAPStore.ID_ADDRESS, aMapLocation.getAddress());
            str = "result";
            obj = jSONObject2;
        }
        jSONObject.put(str, obj);
        return jSONObject.toString();
    }

    public final void a() {
        if (this.f == null || this.d == null || Build.VERSION.SDK_INT < 17 || this.h) {
            return;
        }
        try {
            this.f.getSettings().setJavaScriptEnabled(true);
            this.f.addJavascriptInterface(this, "AMapAndroidLoc");
            if (!TextUtils.isEmpty(this.f.getUrl())) {
                this.f.reload();
            }
            if (this.e == null) {
                this.e = new AMapLocationClient(this.d);
                this.e.setLocationListener(this.c);
            }
            this.h = true;
        } catch (Throwable unused) {
        }
    }

    public final void b() {
        synchronized (this.a) {
            this.h = false;
            if (this.e != null) {
                this.e.unRegisterLocationListener(this.c);
                this.e.stopLocation();
                this.e.onDestroy();
                this.e = null;
            }
            this.b = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x005b A[Catch: Throwable -> 0x0079, all -> 0x0090, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:9:0x0009, B:11:0x000d, B:14:0x0018, B:17:0x0023, B:21:0x002f, B:24:0x0038, B:27:0x003e, B:29:0x004b, B:32:0x0054, B:34:0x005b, B:35:0x005f, B:36:0x0068, B:38:0x0071, B:45:0x0063, B:40:0x0079, B:42:0x007d, B:43:0x008e), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0071 A[Catch: Throwable -> 0x0079, all -> 0x0090, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:9:0x0009, B:11:0x000d, B:14:0x0018, B:17:0x0023, B:21:0x002f, B:24:0x0038, B:27:0x003e, B:29:0x004b, B:32:0x0054, B:34:0x005b, B:35:0x005f, B:36:0x0068, B:38:0x0071, B:45:0x0063, B:40:0x0079, B:42:0x007d, B:43:0x008e), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x007d A[Catch: all -> 0x0090, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:9:0x0009, B:11:0x000d, B:14:0x0018, B:17:0x0023, B:21:0x002f, B:24:0x0038, B:27:0x003e, B:29:0x004b, B:32:0x0054, B:34:0x005b, B:35:0x005f, B:36:0x0068, B:38:0x0071, B:45:0x0063, B:40:0x0079, B:42:0x007d, B:43:0x008e), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0063 A[Catch: Throwable -> 0x0079, all -> 0x0090, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:9:0x0009, B:11:0x000d, B:14:0x0018, B:17:0x0023, B:21:0x002f, B:24:0x0038, B:27:0x003e, B:29:0x004b, B:32:0x0054, B:34:0x005b, B:35:0x005f, B:36:0x0068, B:38:0x0071, B:45:0x0063, B:40:0x0079, B:42:0x007d, B:43:0x008e), top: B:3:0x0003 }] */
    @android.webkit.JavascriptInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void getLocation(java.lang.String r9) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.a
            monitor-enter(r0)
            boolean r1 = r8.h     // Catch: java.lang.Throwable -> L90
            if (r1 != 0) goto L9
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L90
            return
        L9:
            com.amap.api.location.AMapLocationClientOption r1 = r8.b     // Catch: java.lang.Throwable -> L90
            if (r1 != 0) goto L14
            com.amap.api.location.AMapLocationClientOption r1 = new com.amap.api.location.AMapLocationClientOption     // Catch: java.lang.Throwable -> L90
            r1.<init>()     // Catch: java.lang.Throwable -> L90
            r8.b = r1     // Catch: java.lang.Throwable -> L90
        L14:
            r1 = 5
            r2 = 30000(0x7530, double:1.4822E-319)
            r4 = 0
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L90
            r5.<init>(r9)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L90
            java.lang.String r9 = "to"
            long r6 = r5.optLong(r9, r2)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L90
            java.lang.String r9 = "useGPS"
            r2 = 1
            int r9 = r5.optInt(r9, r2)     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L90
            if (r9 != r2) goto L2e
            r9 = 1
            goto L2f
        L2e:
            r9 = 0
        L2f:
            java.lang.String r3 = "watch"
            int r3 = r5.optInt(r3, r4)     // Catch: java.lang.Throwable -> L53 java.lang.Throwable -> L90
            if (r3 != r2) goto L38
            r4 = 1
        L38:
            java.lang.String r2 = "interval"
            int r2 = r5.optInt(r2, r1)     // Catch: java.lang.Throwable -> L53 java.lang.Throwable -> L90
            java.lang.String r1 = "callback"
            r3 = 0
            java.lang.String r1 = r5.optString(r1, r3)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L90
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L90
            if (r3 != 0) goto L4e
        L4b:
            r8.g = r1     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L90
            goto L54
        L4e:
            java.lang.String r1 = "AMap.Geolocation.cbk"
            goto L4b
        L51:
            r6 = r2
        L52:
            r9 = 0
        L53:
            r2 = 5
        L54:
            com.amap.api.location.AMapLocationClientOption r1 = r8.b     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            r1.setHttpTimeOut(r6)     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            if (r9 == 0) goto L63
            com.amap.api.location.AMapLocationClientOption r9 = r8.b     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            com.amap.api.location.AMapLocationClientOption$AMapLocationMode r1 = com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
        L5f:
            r9.setLocationMode(r1)     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            goto L68
        L63:
            com.amap.api.location.AMapLocationClientOption r9 = r8.b     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            com.amap.api.location.AMapLocationClientOption$AMapLocationMode r1 = com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Battery_Saving     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            goto L5f
        L68:
            com.amap.api.location.AMapLocationClientOption r9 = r8.b     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            r1 = r4 ^ 1
            r9.setOnceLocation(r1)     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            if (r4 == 0) goto L79
            com.amap.api.location.AMapLocationClientOption r9 = r8.b     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            int r2 = r2 * 1000
            long r1 = (long) r2     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
            r9.setInterval(r1)     // Catch: java.lang.Throwable -> L79 java.lang.Throwable -> L90
        L79:
            com.amap.api.location.AMapLocationClient r9 = r8.e     // Catch: java.lang.Throwable -> L90
            if (r9 == 0) goto L8e
            com.amap.api.location.AMapLocationClient r9 = r8.e     // Catch: java.lang.Throwable -> L90
            com.amap.api.location.AMapLocationClientOption r1 = r8.b     // Catch: java.lang.Throwable -> L90
            r9.setLocationOption(r1)     // Catch: java.lang.Throwable -> L90
            com.amap.api.location.AMapLocationClient r9 = r8.e     // Catch: java.lang.Throwable -> L90
            r9.stopLocation()     // Catch: java.lang.Throwable -> L90
            com.amap.api.location.AMapLocationClient r9 = r8.e     // Catch: java.lang.Throwable -> L90
            r9.startLocation()     // Catch: java.lang.Throwable -> L90
        L8e:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L90
            return
        L90:
            r9 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L90
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.q.getLocation(java.lang.String):void");
    }

    @JavascriptInterface
    public final void stopLocation() {
        if (this.h && this.e != null) {
            this.e.stopLocation();
        }
    }
}
