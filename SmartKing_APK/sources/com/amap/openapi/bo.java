package com.amap.openapi;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import org.json.JSONObject;

/* compiled from: CloudWrapper.java */
/* loaded from: classes.dex */
public class bo {
    private Context a;
    private OfflineConfig b;
    private com.amap.location.offline.a c;
    private b d;
    private f e = new f() { // from class: com.amap.openapi.bo.1
        @Override // com.amap.openapi.f
        public void a() {
        }

        @Override // com.amap.openapi.f
        public void a(com.amap.openapi.a aVar) {
            bo.this.a(aVar.a());
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CloudWrapper.java */
    /* loaded from: classes.dex */
    public static class a implements IOfflineCloudConfig {
        private boolean a;
        private long b;
        private boolean c;
        private int d;
        private int e;
        private String[] f;
        private int g;
        private int h;
        private int i;
        private boolean j;

        a(JSONObject jSONObject) {
            this.a = true;
            this.b = 0L;
            this.c = false;
            this.d = 6;
            this.e = 8;
            this.g = 10;
            this.h = 5;
            this.i = 100;
            this.j = false;
            if (jSONObject != null) {
                this.a = jSONObject.optBoolean("loe", true);
                this.b = jSONObject.optLong("loct", 0L);
                this.c = jSONObject.optBoolean("loca", false);
                this.d = jSONObject.optInt("lott", 6);
                this.e = jSONObject.optInt("lomwn", 8);
                try {
                    this.f = jSONObject.optString("locpl", "").split(",");
                } catch (Exception unused) {
                }
                this.g = jSONObject.optInt("lomrt", 10);
                this.h = jSONObject.optInt("lomnwrt", 5);
                this.i = jSONObject.optInt("lomnpr", 100);
                this.j = jSONObject.optBoolean("lonfd", false);
            }
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public boolean clearAll() {
            return this.c;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public long getConfigTime() {
            return this.b;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public String[] getContentProviderList() {
            return this.f;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public int getMaxNonWifiRequestTimes() {
            return this.h;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public int getMaxNumPerRequest() {
            return this.i;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public int getMaxRequestTimes() {
            return this.g;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public int getMinWifiNum() {
            return this.e;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public boolean getNeedFirstDownload() {
            return this.j;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public int getTrainingThreshold() {
            return this.d;
        }

        @Override // com.amap.location.offline.IOfflineCloudConfig
        public boolean isEnable() {
            return this.a;
        }
    }

    public bo(Context context, OfflineConfig offlineConfig, com.amap.location.offline.a aVar) {
        this.a = context;
        this.b = offlineConfig;
        this.c = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.c.a = new a(jSONObject);
        } catch (Throwable unused) {
        }
    }

    public void a() {
        if (this.b.productId == 4 && this.b.locEnable && this.c.isEnable()) {
            this.d = b.a();
            this.d.a(this.e);
            d dVar = new d();
            dVar.a(this.b.productId);
            dVar.a(this.b.productVersion);
            dVar.c(this.b.license);
            dVar.b(this.b.mapKey);
            dVar.d(this.b.uuid);
            dVar.e(com.amap.location.common.a.c(this.a));
            dVar.a(this.b.httpClient);
            this.d.a(this.a, dVar);
        }
    }

    public void b() {
        if (this.b.productId != 4 || this.d == null) {
            return;
        }
        this.d.b(this.e);
        this.d.b();
    }
}
