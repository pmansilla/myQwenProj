package com.czw.smartkit.bleModule.gpsLocation;

import android.content.Context;
import android.os.Handler;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.netModule.entity.LocationEntity;
import com.czw.smartkit.netModule.entity.WeatherEntity;
import com.czw.utils.LogUtil;
import java.util.HashSet;
import java.util.Iterator;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class GpsLocationHelper implements AMapLocationListener {
    private static final GpsLocationHelper ourInstance = new GpsLocationHelper();
    private static final long timeSpace = 3600000;
    private HashSet<LocationAndWeatherCallback> callbackHashSet = new HashSet<>();
    private Handler handler = new Handler();
    private WeatherEntity lastWeatherEntity = null;
    private AMapLocationClient mlocationClient;

    /* loaded from: classes.dex */
    public interface LocationAndWeatherCallback {
        void onCurrentWeather(WeatherEntity weatherEntity);
    }

    private GpsLocationHelper() {
    }

    public static GpsLocationHelper getInstance() {
        return ourInstance;
    }

    public static byte[] long2ByteArrLR(long j, int i) {
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = (byte) (j >> (((i - 1) - i2) * 8));
        }
        return bArr;
    }

    private void notifyWeather(WeatherEntity weatherEntity) {
        Iterator<LocationAndWeatherCallback> it = this.callbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onCurrentWeather(weatherEntity);
        }
    }

    private void requestCurrentWeather(LocationEntity locationEntity) {
    }

    public void clearWeatherBuffer() {
    }

    public void initGps(Context context) {
        this.mlocationClient = new AMapLocationClient(context);
        AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
        this.mlocationClient.setLocationListener(this);
        aMapLocationClientOption.setGpsFirst(true);
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        aMapLocationClientOption.setInterval(BootloaderScanner.TIMEOUT);
        this.mlocationClient.setLocationOption(aMapLocationClientOption);
    }

    @Override // com.amap.api.location.AMapLocationListener
    public void onLocationChanged(final AMapLocation aMapLocation) {
        LogUtil.e("aMapLocation.getErrorCode()" + aMapLocation.getErrorCode());
        if (aMapLocation == null || aMapLocation.getErrorCode() != 0) {
            return;
        }
        LogUtil.e("debug_location==>" + aMapLocation.toStr());
        double longitude = aMapLocation.getLongitude();
        double latitude = aMapLocation.getLatitude();
        LogUtil.e("longitude:" + longitude + "///latitude:" + latitude);
        long longValue = Double.valueOf(longitude * 1000000.0d).longValue();
        long longValue2 = Double.valueOf(latitude * 1000000.0d).longValue();
        byte[] long2ByteArrLR = long2ByteArrLR(longValue, 4);
        byte[] long2ByteArrLR2 = long2ByteArrLR(longValue2, 4);
        byte[] bArr = new byte[12];
        bArr[0] = 6;
        bArr[1] = 1;
        if (longValue < 0) {
            bArr[2] = 0;
        } else {
            bArr[2] = 1;
        }
        System.arraycopy(long2ByteArrLR, 0, bArr, 3, 4);
        if (longValue2 < 0) {
            bArr[7] = 0;
        } else {
            bArr[7] = 1;
        }
        System.arraycopy(long2ByteArrLR2, 0, bArr, 8, 4);
        LogUtil.e("定位的数据:" + BleUtil.byte2HexStr(bArr));
        BleManager.getBleManager().writeData(bArr);
        this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.bleModule.gpsLocation.GpsLocationHelper.2
            @Override // java.lang.Runnable
            public void run() {
                BleManager.getBleManager().writeMuliteData(DataStruct.createLocation(aMapLocation.getAddress()));
            }
        }, 500L);
        stopLocation();
    }

    public void registerCallback(LocationAndWeatherCallback locationAndWeatherCallback) {
        if (this.callbackHashSet.contains(locationAndWeatherCallback)) {
            return;
        }
        this.callbackHashSet.add(locationAndWeatherCallback);
    }

    public void startLocation(boolean z) {
        if (this.mlocationClient != null) {
            LogUtil.e("是否强制请求定位:" + z);
            if (z) {
                this.mlocationClient.startLocation();
            }
        }
    }

    public void stopLocation() {
        if (this.mlocationClient != null) {
            LogUtil.e("停止定位");
            this.mlocationClient.stopLocation();
            if (this.handler != null) {
                this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.bleModule.gpsLocation.GpsLocationHelper.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LogUtil.e("一小时后刷新一下天气数据");
                        GpsLocationHelper.this.startLocation(true);
                    }
                }, 3600000L);
            }
        }
    }

    public void unRegisterCallback(LocationAndWeatherCallback locationAndWeatherCallback) {
        if (this.callbackHashSet.contains(locationAndWeatherCallback)) {
            this.callbackHashSet.remove(locationAndWeatherCallback);
        }
    }
}
