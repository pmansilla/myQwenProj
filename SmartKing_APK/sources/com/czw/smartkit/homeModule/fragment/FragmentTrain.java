package com.czw.smartkit.homeModule.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
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
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.data.UnitCfg;
import com.czw.smartkit.bleModule.step.DevPartStepBean;
import com.czw.smartkit.bleModule.step.DevStepCallback;
import com.czw.smartkit.bleModule.step.DevStepUtil;
import com.czw.smartkit.bleModule.step.DevTotalStepBean;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.databaseModule.train.TrainServiceImpl;
import com.czw.smartkit.homeModule.syncData.SyncDataType;
import com.czw.smartkit.homeModule.syncData.SyncDataUtil;
import com.czw.smartkit.observerModule.UnitChangeHelper;
import com.czw.smartkit.statistics.TransHistoryActivity;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.util.SkUtils;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import java.util.ArrayList;
import java.util.List;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import org.apache.commons.lang.time.DateFormatUtils;
import ycble.runchinaup.core.BleConnState;
import ycble.runchinaup.core.callback.BleConnCallback;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes.dex */
public class FragmentTrain extends BaseFragment implements View.OnClickListener, AMapLocationListener, SyncDataUtil.SyncDataCallback, BleConnCallback, DevStepCallback, UnitChangeHelper.UnitListener {
    private View infoLayout;
    private AMap mMap;
    private MapView mapView;
    private View maplayout;
    public AMapLocationClient mlocationClient;
    MyLocationStyle myLocationStyle;

    @BindView(R.id.sport_kcal)
    TextView sportCalorieTextView;

    @BindView(R.id.sport_time)
    TextView sportTimeTextView;
    private TextView sport_distance;
    private ImageView startBtn;
    private TextView stepTv;
    private ImageView stopBtn;
    private boolean isStart = false;
    private ArrayList<double[]> sportPoints = new ArrayList<>();
    private String yyyyMMddHHmmssString = "yyyy-MM-dd HH:mm:ss";
    private BleManager bleManager = BleManager.getBleManager();
    private DevTotalStepBean lastStepInfo = null;
    private DevTotalStepBean currentStepInfo = null;
    private int totalStepCount = 0;
    private int totalKcalCount = 0;
    private int totalDistance = 0;
    private long startTime = 0;
    private Handler handler = new Handler();
    private int timeCounter = 0;
    private Runnable runnable = new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.6
        @Override // java.lang.Runnable
        public void run() {
            FragmentTrain.access$408(FragmentTrain.this);
            FragmentTrain.this.sportTimeTextView.setText(FragmentTrain.this.getTime(FragmentTrain.this.timeCounter));
            FragmentTrain.this.handler.postDelayed(this, 1000L);
        }
    };
    public AMapLocationClientOption mLocationOption = null;

    static /* synthetic */ int access$408(FragmentTrain fragmentTrain) {
        int i = fragmentTrain.timeCounter;
        fragmentTrain.timeCounter = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTime(int i) {
        int i2 = i / 3600;
        return String.format("%02d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf((i - (i2 * 3600)) / 60), Integer.valueOf(i % 60));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reSetDataShow() {
        this.timeCounter = 0;
        this.startTime = 0L;
        this.stopBtn.setVisibility(8);
        this.sportPoints.clear();
        this.startBtn.setImageResource(R.mipmap.ic_start);
        this.stepTv.setText(AmapLoc.RESULT_TYPE_GPS);
        this.sportTimeTextView.setText("00:00:00");
        if (SkUtils.isChinaUnit()) {
            this.sport_distance.setText("0.00 km");
        } else {
            this.sport_distance.setText("0.00 mi");
        }
        this.sportCalorieTextView.setText("0 kcal");
        this.totalStepCount = 0;
        this.totalDistance = 0;
        this.totalKcalCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveData() {
        if (this.startTime != 0) {
            TrainDataTable trainDataTable = new TrainDataTable();
            trainDataTable.setStartTime(DateFormatUtils.format(this.startTime, this.yyyyMMddHHmmssString));
            trainDataTable.setEndTime(DateFormatUtils.format(System.currentTimeMillis(), this.yyyyMMddHHmmssString));
            trainDataTable.setTimeConsuming(SkUtils.getTransTimeLenInt(trainDataTable.getStartTime().substring(11), trainDataTable.getEndTime().substring(11)) + "");
            trainDataTable.setLocationDetail(new Gson().toJson(this.sportPoints));
            trainDataTable.setCalorie(this.totalKcalCount + "");
            trainDataTable.setStrength(this.totalDistance + "");
            trainDataTable.setFasterRate(this.totalStepCount + "");
            trainDataTable.setUid(UserUtil.getUid());
            TrainServiceImpl.getInstance().saveData(trainDataTable);
            SyncDataUtil.getInstance().syncTrainData();
            reSetDataShow();
        }
    }

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
        this.mlocationClient = new AMapLocationClient(getActivity());
        this.mLocationOption = new AMapLocationClientOption();
        this.mlocationClient.setLocationListener(this);
        this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        this.mLocationOption.setInterval(BootloaderScanner.TIMEOUT);
        this.mlocationClient.setLocationOption(this.mLocationOption);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStopDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity()).setMessage(getString(R.string.sure_stop_trans)).addAction(getString(R.string.stop_and_save_data), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.5
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
                FragmentTrain.this.isStart = false;
                FragmentTrain.this.handler.removeCallbacks(FragmentTrain.this.runnable);
                FragmentTrain.this.mlocationClient.stopLocation();
                FragmentTrain.this.saveData();
            }
        }).addAction(0, getString(R.string.stop_and_clean_data), 2, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.4
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
                FragmentTrain.this.isStart = false;
                FragmentTrain.this.handler.removeCallbacks(FragmentTrain.this.runnable);
                FragmentTrain.this.mlocationClient.stopLocation();
                FragmentTrain.this.reSetDataShow();
            }
        }).create(2131755258).show();
    }

    private void updateUIShow() {
        if (this.currentStepInfo == null || this.lastStepInfo == null) {
            return;
        }
        this.totalStepCount += this.currentStepInfo.getStep() - this.lastStepInfo.getStep();
        this.totalKcalCount = (int) (this.totalKcalCount + (this.currentStepInfo.getCalorie() - (this.lastStepInfo.getCalorie() / 1.0f)));
        this.totalDistance += this.currentStepInfo.getDistance() - this.lastStepInfo.getDistance();
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.8
            @Override // java.lang.Runnable
            public void run() {
                FragmentTrain.this.stepTv.setText(FragmentTrain.this.totalStepCount + "");
                FragmentTrain.this.sportCalorieTextView.setText(String.format("%dKcal", Integer.valueOf(FragmentTrain.this.totalKcalCount)));
                boolean isChinaUnit = SkUtils.isChinaUnit();
                double doubleValue = Double.valueOf((double) FragmentTrain.this.totalDistance).doubleValue() / 1000.0d;
                if (isChinaUnit) {
                    FragmentTrain.this.sport_distance.setText(String.format("%.2f km", Double.valueOf(doubleValue)));
                    return;
                }
                FragmentTrain.this.sport_distance.setText(String.format("%s mi", SkUtils.km2Mi(doubleValue + "")));
            }
        });
        this.lastStepInfo = this.currentStepInfo;
    }

    @Override // com.czw.smartkit.observerModule.UnitChangeHelper.UnitListener
    public void currentUnit(UnitCfg unitCfg) {
        if (this.isStart) {
            updateUIShow();
        } else if (SkUtils.isChinaUnit()) {
            this.sport_distance.setText("0.00 km");
        } else {
            this.sport_distance.setText("0.00 mi");
        }
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

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.stepTv = (TextView) $View(R.id.stepTv);
        this.startBtn = (ImageView) $View(R.id.startBtn);
        this.stopBtn = (ImageView) $View(R.id.stopBtn);
        this.sport_distance = (TextView) $View(R.id.sport_distance);
        this.maplayout = $View(R.id.maplayout);
        this.infoLayout = $View(R.id.infoLayout);
        $View(R.id.map_back).setOnClickListener(this);
        $View(R.id.info_btn).setOnClickListener(this);
        $View(R.id.ref_top).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FragmentTrain.this.jumpTo(TransHistoryActivity.class);
            }
        });
        this.startBtn.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!FragmentTrain.this.bleManager.isConn()) {
                    FragmentTrain.this.toast(R.string.not_conn);
                    return;
                }
                FragmentTrain.this.isStart = !FragmentTrain.this.isStart;
                FragmentTrain.this.stopBtn.setVisibility(0);
                if (FragmentTrain.this.isStart) {
                    if (FragmentTrain.this.startTime == 0) {
                        FragmentTrain.this.startTime = System.currentTimeMillis();
                        FragmentTrain.this.timeCounter = 0;
                    }
                    FragmentTrain.this.mlocationClient.startLocation();
                    FragmentTrain.this.handler.post(FragmentTrain.this.runnable);
                    FragmentTrain.this.startBtn.setImageResource(R.mipmap.ic_trans_pause);
                } else {
                    FragmentTrain.this.handler.removeCallbacks(FragmentTrain.this.runnable);
                    FragmentTrain.this.mlocationClient.stopLocation();
                    FragmentTrain.this.startBtn.setImageResource(R.mipmap.ic_start);
                    FragmentTrain.this.currentStepInfo = null;
                    FragmentTrain.this.lastStepInfo = null;
                }
                BleManager.getBleManager().writeData(DataStruct.createAutoReportStep(FragmentTrain.this.isStart));
            }
        });
        this.stopBtn.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FragmentTrain.this.showStopDialog();
            }
        });
        DevStepUtil.gteInstance().registerCallback(this);
        SyncDataUtil.getInstance().registerCallback(this);
        UnitChangeHelper.getInstance().registerListener(this);
        reSetDataShow();
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.fragment_trans;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.info_btn) {
            this.maplayout.setVisibility(0);
            this.infoLayout.setVisibility(8);
        } else {
            if (id != R.id.map_back) {
                return;
            }
            this.maplayout.setVisibility(8);
            this.infoLayout.setVisibility(0);
        }
    }

    @Override // ycble.runchinaup.core.callback.BleConnCallback
    public void onConnState(BleConnState bleConnState) {
        if (getActivity() == null) {
            return;
        }
        saveData();
    }

    @Override // com.czw.modes.fragment.RootFragment
    public void onCreateMap(Bundle bundle) {
        this.mapView = (MapView) this.mView.findViewById(R.id.map);
        this.mapView.onCreate(bundle);
        if (this.mMap == null) {
            this.mMap = this.mapView.getMap();
            this.mMap.moveCamera(CameraUpdateFactory.zoomTo(20.0f));
            setUpMap();
        }
    }

    @Override // com.czw.smartkit.homeModule.syncData.SyncDataUtil.SyncDataCallback
    public void onDataSyncFinish(SyncDataType syncDataType) {
        if (getActivity() == null) {
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
        DevStepUtil.gteInstance().unRegisterCallback(this);
        this.bleManager.unRegisterConnCallback(this);
        if (this.mlocationClient != null) {
            this.mlocationClient.stopLocation();
        }
    }

    @Override // com.amap.api.location.AMapLocationListener
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (getActivity() == null || aMapLocation == null) {
            return;
        }
        ycBleLog.e("train==>" + aMapLocation.toString());
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentTrain.7
            @Override // java.lang.Runnable
            public void run() {
                if (aMapLocation.getErrorCode() == 0) {
                    if (FragmentTrain.this.isStart) {
                        FragmentTrain.this.sportPoints.add(new double[]{aMapLocation.getLatitude(), aMapLocation.getLongitude()});
                        FragmentTrain.this.drawPath(FragmentTrain.this.sportPoints);
                    }
                    aMapLocation.getLocationType();
                    aMapLocation.getAccuracy();
                    return;
                }
                Toast.makeText(FragmentTrain.this.getActivity(), "GPS Exception " + aMapLocation.getErrorCode(), 0).show();
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
        this.bleManager.registerConnCallback(this);
    }

    @Override // com.czw.smartkit.bleModule.step.DevStepCallback
    public void onStepHistoryDataList(List<DevPartStepBean> list) {
    }

    @Override // com.czw.smartkit.bleModule.step.DevStepCallback
    public void onStepTotal(DevTotalStepBean devTotalStepBean) {
        if (this.isStart) {
            LogUtil.e("debug====>实时步数==训练的数据" + new Gson().toJson(devTotalStepBean));
            this.currentStepInfo = devTotalStepBean;
            if (this.lastStepInfo == null) {
                this.lastStepInfo = this.currentStepInfo;
            }
            updateUIShow();
        }
    }
}
