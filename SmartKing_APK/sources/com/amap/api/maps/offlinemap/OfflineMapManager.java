package com.amap.api.maps.offlinemap;

import android.content.Context;
import android.os.Handler;
import com.amap.api.mapcore.util.bo;
import com.amap.api.mapcore.util.bp;
import com.amap.api.mapcore.util.bt;
import com.amap.api.mapcore.util.fr;
import com.amap.api.mapcore.util.hk;
import com.amap.api.mapcore.util.ic;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class OfflineMapManager {
    bt a;
    bp b;
    private Context c;
    private OfflineMapDownloadListener d;
    private OfflineLoadedListener e;
    private Handler f;
    private Handler g;

    /* loaded from: classes.dex */
    public interface OfflineLoadedListener {
        void onVerifyComplete();
    }

    /* loaded from: classes.dex */
    public interface OfflineMapDownloadListener {
        void onCheckUpdate(boolean z, String str);

        void onDownload(int i, int i2, String str);

        void onRemove(boolean z, String str, String str2);
    }

    public OfflineMapManager(Context context, OfflineMapDownloadListener offlineMapDownloadListener) {
        this.d = offlineMapDownloadListener;
        this.c = context.getApplicationContext();
        this.f = new Handler(this.c.getMainLooper());
        this.g = new Handler(this.c.getMainLooper());
        a(context);
        hk.a().a(this.c);
    }

    public OfflineMapManager(Context context, OfflineMapDownloadListener offlineMapDownloadListener, AMap aMap) {
        this.d = offlineMapDownloadListener;
        this.c = context.getApplicationContext();
        this.f = new Handler(this.c.getMainLooper());
        this.g = new Handler(this.c.getMainLooper());
        try {
            a(context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a() throws AMapException {
        if (!fr.d(this.c)) {
            throw new AMapException(AMapException.ERROR_CONNECTION);
        }
    }

    private void a(Context context) {
        this.c = context.getApplicationContext();
        bp.b = false;
        this.b = bp.a(this.c);
        this.b.a(new bp.a() { // from class: com.amap.api.maps.offlinemap.OfflineMapManager.1
            @Override // com.amap.api.mapcore.util.bp.a
            public void a() {
                if (OfflineMapManager.this.e != null) {
                    OfflineMapManager.this.f.post(new Runnable() { // from class: com.amap.api.maps.offlinemap.OfflineMapManager.1.4
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                OfflineMapManager.this.e.onVerifyComplete();
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override // com.amap.api.mapcore.util.bp.a
            public void a(final bo boVar) {
                if (OfflineMapManager.this.d == null || boVar == null) {
                    return;
                }
                OfflineMapManager.this.f.post(new Runnable() { // from class: com.amap.api.maps.offlinemap.OfflineMapManager.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            OfflineMapManager.this.d.onDownload(boVar.c().b(), boVar.getcompleteCode(), boVar.getCity());
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }

            @Override // com.amap.api.mapcore.util.bp.a
            public void b(final bo boVar) {
                if (OfflineMapManager.this.d == null || boVar == null) {
                    return;
                }
                OfflineMapManager.this.f.post(new Runnable() { // from class: com.amap.api.maps.offlinemap.OfflineMapManager.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (!boVar.c().equals(boVar.g) && !boVar.c().equals(boVar.a)) {
                                OfflineMapManager.this.d.onCheckUpdate(false, boVar.getCity());
                            }
                            OfflineMapManager.this.d.onCheckUpdate(true, boVar.getCity());
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }

            @Override // com.amap.api.mapcore.util.bp.a
            public void c(final bo boVar) {
                if (OfflineMapManager.this.d == null || boVar == null) {
                    return;
                }
                OfflineMapManager.this.f.post(new Runnable() { // from class: com.amap.api.maps.offlinemap.OfflineMapManager.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (boVar.c().equals(boVar.a)) {
                                OfflineMapManager.this.d.onRemove(true, boVar.getCity(), "");
                            } else {
                                OfflineMapManager.this.d.onRemove(false, boVar.getCity(), "");
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }
        });
        try {
            this.b.a();
            this.a = this.b.f;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(String str, String str2) throws AMapException {
        this.b.a(str);
    }

    private void b() {
        this.d = null;
    }

    public void destroy() {
        try {
            if (this.b != null) {
                this.b.e();
            }
            b();
            if (this.f != null) {
                this.f.removeCallbacksAndMessages(null);
            }
            this.f = null;
            if (this.g != null) {
                this.g.removeCallbacksAndMessages(null);
            }
            this.g = null;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void downloadByCityCode(String str) throws AMapException {
        try {
            this.b.e(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void downloadByCityName(String str) throws AMapException {
        try {
            this.b.d(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void downloadByProvinceName(String str) throws AMapException {
        try {
            a();
            OfflineMapProvince itemByProvinceName = getItemByProvinceName(str);
            if (itemByProvinceName == null) {
                throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
            }
            Iterator<OfflineMapCity> it = itemByProvinceName.getCityList().iterator();
            while (it.hasNext()) {
                final String city = it.next().getCity();
                this.g.post(new Runnable() { // from class: com.amap.api.maps.offlinemap.OfflineMapManager.2
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            OfflineMapManager.this.b.d(city);
                        } catch (AMapException e) {
                            ic.c(e, "OfflineMapManager", "downloadByProvinceName");
                        }
                    }
                });
            }
        } catch (Throwable th) {
            if (th instanceof AMapException) {
                throw ((AMapException) th);
            }
            ic.c(th, "OfflineMapManager", "downloadByProvinceName");
        }
    }

    public ArrayList<OfflineMapCity> getDownloadOfflineMapCityList() {
        return this.a.c();
    }

    public ArrayList<OfflineMapProvince> getDownloadOfflineMapProvinceList() {
        return this.a.d();
    }

    public ArrayList<OfflineMapCity> getDownloadingCityList() {
        return this.a.e();
    }

    public ArrayList<OfflineMapProvince> getDownloadingProvinceList() {
        return this.a.f();
    }

    public OfflineMapCity getItemByCityCode(String str) {
        return this.a.a(str);
    }

    public OfflineMapCity getItemByCityName(String str) {
        return this.a.b(str);
    }

    public OfflineMapProvince getItemByProvinceName(String str) {
        return this.a.c(str);
    }

    public ArrayList<OfflineMapCity> getOfflineMapCityList() {
        return this.a.b();
    }

    public ArrayList<OfflineMapProvince> getOfflineMapProvinceList() {
        return this.a.a();
    }

    public void pause() {
        this.b.d();
    }

    public void remove(String str) {
        try {
            if (this.b.b(str)) {
                this.b.c(str);
                return;
            }
            OfflineMapProvince c = this.a.c(str);
            if (c != null && c.getCityList() != null) {
                Iterator<OfflineMapCity> it = c.getCityList().iterator();
                while (it.hasNext()) {
                    final String city = it.next().getCity();
                    this.g.post(new Runnable() { // from class: com.amap.api.maps.offlinemap.OfflineMapManager.3
                        @Override // java.lang.Runnable
                        public void run() {
                            OfflineMapManager.this.b.c(city);
                        }
                    });
                }
                return;
            }
            if (this.d != null) {
                this.d.onRemove(false, str, "没有该城市");
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void restart() {
    }

    public void setOnOfflineLoadedListener(OfflineLoadedListener offlineLoadedListener) {
        this.e = offlineLoadedListener;
    }

    public void stop() {
        this.b.c();
    }

    public void updateOfflineCityByCode(String str) throws AMapException {
        OfflineMapCity itemByCityCode = getItemByCityCode(str);
        if (itemByCityCode == null || itemByCityCode.getCity() == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        a(itemByCityCode.getCity(), "cityname");
    }

    public void updateOfflineCityByName(String str) throws AMapException {
        a(str, "cityname");
    }

    public void updateOfflineMapProvinceByName(String str) throws AMapException {
        a(str, "cityname");
    }
}
