package com.czw.smartkit.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.View;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.util.SkUtils;
import com.czw.utils.ViewUtil;
import java.util.List;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* loaded from: classes.dex */
public class SportPathActivity extends TitleActivity implements AMapLocationListener {
    private AMap mMap;
    private MapView mapView;
    public AMapLocationClient mlocationClient;
    MyLocationStyle myLocationStyle;
    TrainDataTable transDTO = null;
    public AMapLocationClientOption mLocationOption = null;
    private LatLng last_for_debug = new LatLng(22.224136d, 115.955284d);

    private void setUpMap() {
        this.myLocationStyle = new MyLocationStyle();
        this.myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        this.myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        this.myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        this.mMap.setMyLocationStyle(this.myLocationStyle);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.mMap.setMyLocationEnabled(true);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.mMap.setMyLocationEnabled(true);
        this.mlocationClient = new AMapLocationClient(this);
        this.mLocationOption = new AMapLocationClientOption();
        this.mlocationClient.setLocationListener(this);
        this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        this.mLocationOption.setInterval(BootloaderScanner.TIMEOUT);
        this.mlocationClient.setLocationOption(this.mLocationOption);
        this.mlocationClient.startLocation();
    }

    protected void drawPath(List<double[]> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < list.size(); i++) {
            polylineOptions.add(new LatLng(list.get(i)[0], list.get(i)[1]));
        }
        polylineOptions.width(15.0f);
        polylineOptions.color(SupportMenu.CATEGORY_MASK);
        polylineOptions.zIndex(5.0f);
        this.mMap.addPolyline(polylineOptions);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.path_sport);
        this.titleBar.setRightImage(R.mipmap.ic_share);
        this.transDTO = (TrainDataTable) getIntent().getSerializableExtra("transDTO");
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_sport_path;
    }

    @Override // com.czw.smartkit.base.BaseActivity
    protected void onCreateMap(Bundle bundle) {
        this.mapView = (MapView) $View(R.id.map);
        this.mapView.onCreate(bundle);
        if (this.mMap == null) {
            this.mMap = this.mapView.getMap();
            this.mMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
            setUpMap();
        }
        if (this.transDTO != null) {
            drawPath(this.transDTO.getLocationPointers());
        }
    }

    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    @Override // com.amap.api.location.AMapLocationListener
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();
                aMapLocation.getAccuracy();
                return;
            }
            Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    @Override // com.czw.smartkit.base.TitleActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override // com.czw.smartkit.base.TitleActivity
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        SkUtils.showShare(getUI(), ViewUtil.getCacheBitmapFromView($View(R.id.acv_win)));
    }
}
