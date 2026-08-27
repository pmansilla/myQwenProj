package com.amap.api.mapcore.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.autonavi.amap.mapcore.Inner_3dMap_locationListener;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;

/* compiled from: MapLocClientUtils.java */
/* loaded from: classes.dex */
public final class kd {
    public static Inner_3dMap_location a;
    kb b = null;

    private static AMapLocationClientOption a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        AMapLocationClientOption.AMapLocationMode aMapLocationMode;
        AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setInterval(inner_3dMap_locationOption.getInterval());
        AMapLocationClientOption.AMapLocationMode aMapLocationMode2 = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
        switch (inner_3dMap_locationOption.getLocationMode()) {
            case Battery_Saving:
                aMapLocationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving;
                break;
            case Device_Sensors:
                aMapLocationMode = AMapLocationClientOption.AMapLocationMode.Device_Sensors;
                break;
            case Hight_Accuracy:
            default:
                aMapLocationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
                break;
        }
        aMapLocationClientOption.setLocationMode(aMapLocationMode);
        aMapLocationClientOption.setOnceLocation(inner_3dMap_locationOption.isOnceLocation());
        aMapLocationClientOption.setNeedAddress(inner_3dMap_locationOption.isNeedAddress());
        return aMapLocationClientOption;
    }

    public static Inner_3dMap_location a(AMapLocation aMapLocation) {
        Inner_3dMap_location inner_3dMap_location = new Inner_3dMap_location("");
        if (aMapLocation != null) {
            try {
                inner_3dMap_location.setLatitude(aMapLocation.getLatitude());
                inner_3dMap_location.setLongitude(aMapLocation.getLongitude());
                inner_3dMap_location.setAccuracy(aMapLocation.getAccuracy());
                inner_3dMap_location.setBearing(aMapLocation.getBearing());
                inner_3dMap_location.setAltitude(aMapLocation.getAltitude());
                inner_3dMap_location.setProvider(aMapLocation.getProvider());
                inner_3dMap_location.setSpeed(aMapLocation.getSpeed());
                inner_3dMap_location.setTime(aMapLocation.getTime());
                inner_3dMap_location.setErrorCode(aMapLocation.getErrorCode());
                inner_3dMap_location.setErrorInfo(aMapLocation.getErrorInfo());
                inner_3dMap_location.setLocationType(aMapLocation.getLocationType());
                inner_3dMap_location.setLocationDetail(aMapLocation.getLocationDetail());
                inner_3dMap_location.setProvince(aMapLocation.getProvince());
                inner_3dMap_location.setCity(aMapLocation.getCity());
                inner_3dMap_location.setCityCode(aMapLocation.getCityCode());
                inner_3dMap_location.setCountry(aMapLocation.getCountry());
                inner_3dMap_location.setDistrict(aMapLocation.getDistrict());
                inner_3dMap_location.setAddress(aMapLocation.getAddress());
                inner_3dMap_location.setAdCode(aMapLocation.getAdCode());
                inner_3dMap_location.setExtras(aMapLocation.getExtras());
                inner_3dMap_location.setRoad(aMapLocation.getRoad());
            } catch (Throwable th) {
                kw.a(th, "Util", "converterLocation");
            }
        }
        return inner_3dMap_location;
    }

    public static void a(Object obj, Inner_3dMap_locationOption inner_3dMap_locationOption) {
        if (obj == null) {
            return;
        }
        ((AMapLocationClient) obj).setLocationOption(a(inner_3dMap_locationOption));
    }

    public final void a(Object obj, Inner_3dMap_locationListener inner_3dMap_locationListener) {
        if (this.b == null) {
            this.b = new kb();
        }
        this.b.a(inner_3dMap_locationListener);
        ((AMapLocationClient) obj).setLocationListener(this.b);
    }
}
