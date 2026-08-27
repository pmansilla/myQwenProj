package com.czw.smartkit.homeModule.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.data.UnitCfg;
import com.czw.smartkit.bleModule.data.util.DevDataUtil;
import com.czw.smartkit.bleModule.step.DevPartStepBean;
import com.czw.smartkit.bleModule.step.DevStepCallback;
import com.czw.smartkit.bleModule.step.DevStepUtil;
import com.czw.smartkit.bleModule.step.DevTotalStepBean;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.databaseModule.step.StepServiceImpl;
import com.czw.smartkit.homeModule.syncData.SyncDataType;
import com.czw.smartkit.homeModule.syncData.SyncDataUtil;
import com.czw.smartkit.netModule.NetManager;
import com.czw.smartkit.netModule.entity.LocationEntity;
import com.czw.smartkit.netModule.entity.WeatherEntity;
import com.czw.smartkit.observerModule.TargetChangeHelper;
import com.czw.smartkit.observerModule.UnitChangeHelper;
import com.czw.smartkit.preferenceModule.SharePreferenceUnit;
import com.czw.smartkit.preferenceModule.SharedPrefereceWeather;
import com.czw.smartkit.statistics.StepHistoryActivity;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.StepTableView;
import com.czw.smartkit.views.StepView;
import com.czw.utils.LogUtil;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.List;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import ycnet.runchinaup.core.ycimpl.data.YCRespData;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;

/* loaded from: classes.dex */
public class FragmentStep extends BaseFragment implements AMapLocationListener, DevStepCallback, SyncDataUtil.SyncDataCallback, UnitChangeHelper.UnitListener, TargetChangeHelper.TargetListener {
    public static WeatherEntity currentWeather;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    @BindView(R.id.status_weather_icon_iv)
    ImageView ivWeatherIcon;
    private TextView stepTargetTextView;
    private StepView stepView;
    private StepTableView steptableView;
    private int todayTotalCalorie;
    private int todayTotalDistanceM;
    private int todayTotalStep;
    private TextView totalCalorieTextView;
    private TextView totalDistanceTextview;
    private TextView totalStepTextView;

    @BindView(R.id.status_weather_info_tv)
    TextView tvInfo;

    @BindView(R.id.status_weather_temp_tv)
    TextView tvTemp;

    @BindView(R.id.status_weather_temp_unit_tv)
    TextView tvTempUnit;

    @BindView(R.id.status_weacher_info_layout)
    View viewWeather;
    private BleManager bleManager = BleManager.getBleManager();
    private int[] arrWeatherIcon = new int[14];
    private LocationEntity locationEntity = null;

    private void initLocation() {
        this.aMapLocationClient = new AMapLocationClient(getActivity());
        this.aMapLocationClientOption = new AMapLocationClientOption();
        this.aMapLocationClient.setLocationListener(this);
        this.aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        this.aMapLocationClientOption.setInterval(DateUtils.MILLIS_PER_MINUTE);
        this.aMapLocationClient.setLocationOption(this.aMapLocationClientOption);
        this.aMapLocationClient.startLocation();
    }

    private void requestNetWeather() {
        if (this.locationEntity == null) {
            LogUtil.e("debug==定位为空，不请求天气了");
            return;
        }
        if (TextUtils.isEmpty(this.locationEntity.getCity()) && this.locationEntity.getLat() == 0.0d && this.locationEntity.getLon() == 0.0d) {
            LogUtil.e("debug==定位的城市为空，不请求天气了");
        }
        NetManager.getNetManager().getWeather(this.locationEntity, new YCResponseListener<YCRespData<WeatherEntity>>() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.2
            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(YCRespData<WeatherEntity> yCRespData) {
                if (FragmentStep.this.getActivity() == null || FragmentStep.this.tvTemp == null || yCRespData == null || yCRespData.getData() == null || TextUtils.isEmpty(yCRespData.getData().getWeather())) {
                    return;
                }
                FragmentStep.currentWeather = yCRespData.getData();
                if (FragmentStep.currentWeather == null || FragmentStep.this.viewWeather == null || FragmentStep.this.getActivity() == null) {
                    return;
                }
                FragmentStep.this.getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (TextUtils.isEmpty(FragmentStep.currentWeather.getWeatherCode()) || TextUtils.isEmpty(FragmentStep.currentWeather.getTemperature())) {
                            LogUtil.e("debug==天气数据不对，有字段为null" + FragmentStep.currentWeather);
                            FragmentStep.this.viewWeather.setVisibility(8);
                            return;
                        }
                        FragmentStep.this.aMapLocationClient.stopLocation();
                        FragmentStep.this.viewWeather.setVisibility(0);
                        FragmentStep.this.tvInfo.setText(FragmentStep.currentWeather.getWeather() + "");
                        int intValue = Integer.valueOf(FragmentStep.currentWeather.getWeatherCode()).intValue();
                        if (intValue >= FragmentStep.this.arrWeatherIcon.length) {
                            intValue = 0;
                        }
                        FragmentStep.this.ivWeatherIcon.setImageResource(FragmentStep.this.arrWeatherIcon[intValue]);
                        FragmentStep.this.updateUnitData();
                        SharedPrefereceWeather.read();
                        LogUtil.e("debug==写天气>" + FragmentStep.currentWeather);
                        FragmentStep.this.bleManager.writeData(DevDataUtil.createWeather(FragmentStep.currentWeather));
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShowTotalData() {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.4
            @Override // java.lang.Runnable
            public void run() {
                FragmentStep.this.totalStepTextView.setText(FragmentStep.this.todayTotalStep + "");
                FragmentStep.this.totalCalorieTextView.setText(FragmentStep.this.todayTotalCalorie + "Kcal");
                FragmentStep.this.updateUnitData();
                FragmentStep.this.updateTargetAndFinishProgress();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTableShow(final List<DevPartStepBean> list) {
        if (getActivity() == null || this.steptableView == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.3
            @Override // java.lang.Runnable
            public void run() {
                FragmentStep.this.steptableView.updateData(DevStepUtil.stepDTO2ViewData(list));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTargetAndFinishProgress() {
        if (getActivity() == null || this.stepTargetTextView == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.6
            @Override // java.lang.Runnable
            public void run() {
                String str = UserUtil.getTarget() + "";
                FragmentStep.this.stepTargetTextView.setText(FragmentStep.this.getString(R.string.target) + SQLBuilder.BLANK + str);
                FragmentStep.this.stepView.updateShow((((float) FragmentStep.this.todayTotalStep) * 1.0f) / ((float) Integer.valueOf(str).intValue()));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUnitData() {
        if (getActivity() == null || this.totalDistanceTextview == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.5
            @Override // java.lang.Runnable
            public void run() {
                UnitCfg read = SharePreferenceUnit.read();
                if (read == null) {
                    read = new UnitCfg();
                }
                double d = FragmentStep.this.todayTotalDistanceM / 1000.0f;
                if (read.getIndex() == 0) {
                    String format = String.format("%.3f", Double.valueOf(d));
                    FragmentStep.this.totalDistanceTextview.setText(String.format("%s km", format.substring(0, format.indexOf(".") + 3)));
                    if (FragmentStep.currentWeather == null || TextUtils.isEmpty(FragmentStep.currentWeather.getTemperature())) {
                        return;
                    }
                    FragmentStep.this.tvTemp.setText(FragmentStep.currentWeather.getTemperature());
                    FragmentStep.this.tvTempUnit.setText(R.string.unit_temp_c);
                    return;
                }
                FragmentStep.this.totalDistanceTextview.setText(String.format("%.2f mi", Double.valueOf(UnitChangeHelper.km2MileValue(d))));
                if (FragmentStep.currentWeather == null || TextUtils.isEmpty(FragmentStep.currentWeather.getTemperature())) {
                    return;
                }
                int intValue = Integer.valueOf(FragmentStep.currentWeather.getTemperature()).intValue();
                FragmentStep.this.tvTemp.setText(UnitChangeHelper.temperatureC2F(intValue) + "");
                FragmentStep.this.tvTempUnit.setText(R.string.unit_temp_f);
            }
        });
    }

    @Override // com.czw.smartkit.observerModule.TargetChangeHelper.TargetListener
    public void currentTarget(String str) {
        LogUtil.e("debug==当前的步数目标:" + str);
        updateTargetAndFinishProgress();
    }

    @Override // com.czw.smartkit.observerModule.UnitChangeHelper.UnitListener
    public void currentUnit(UnitCfg unitCfg) {
        updateUnitData();
    }

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.arrWeatherIcon[0] = R.mipmap.ico_weather_0;
        this.arrWeatherIcon[1] = R.mipmap.ico_weather_1;
        this.arrWeatherIcon[2] = R.mipmap.ico_weather_2;
        this.arrWeatherIcon[3] = R.mipmap.ico_weather_3;
        this.arrWeatherIcon[4] = R.mipmap.ico_weather_4;
        this.arrWeatherIcon[5] = R.mipmap.ico_weather_5;
        this.arrWeatherIcon[6] = R.mipmap.ico_weather_6;
        this.arrWeatherIcon[7] = R.mipmap.ico_weather_7;
        this.arrWeatherIcon[8] = R.mipmap.ico_weather_8;
        this.arrWeatherIcon[9] = R.mipmap.ico_weather_9;
        this.arrWeatherIcon[10] = R.mipmap.ico_weather_10;
        this.arrWeatherIcon[11] = R.mipmap.ico_weather_11;
        this.arrWeatherIcon[12] = R.mipmap.ico_weather_12;
        this.arrWeatherIcon[13] = R.mipmap.ico_weather_13;
        this.steptableView = (StepTableView) $View(R.id.steptableView);
        this.stepView = (StepView) $View(R.id.stepView);
        this.totalStepTextView = (TextView) $View(R.id.totalStep);
        this.stepTargetTextView = (TextView) $View(R.id.stepTarget);
        this.totalCalorieTextView = (TextView) $View(R.id.kcalTv);
        this.totalDistanceTextview = (TextView) $View(R.id.distanceTv);
        $View(R.id.topLayout).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FragmentStep.this.jumpTo(StepHistoryActivity.class);
            }
        });
        SharedPrefereceWeather.clear();
        initLocation();
        DevStepUtil.gteInstance().registerCallback(this);
        UnitChangeHelper.getInstance().registerListener(this);
        TargetChangeHelper.getInstance().registerListener(this);
        SyncDataUtil.getInstance().registerCallback(this);
        StepDataTable stepDataByDate = StepServiceImpl.getInstance().getStepDataByDate(UserUtil.getUid(), DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
        if (stepDataByDate != null) {
            this.todayTotalCalorie = stepDataByDate.getCalorie();
            this.todayTotalStep = stepDataByDate.getWalkCounts();
            this.todayTotalDistanceM = stepDataByDate.getDistance();
            updateTableShow(DevStepUtil.getMinuteStepData(stepDataByDate.getDetailJson()));
        }
        updateShowTotalData();
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.fragment_step;
    }

    @Override // com.czw.smartkit.homeModule.syncData.SyncDataUtil.SyncDataCallback
    public void onDataSyncFinish(SyncDataType syncDataType) {
        if (getActivity() == null || this.tvTemp == null || syncDataType != SyncDataType.STEP) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentStep.7
            @Override // java.lang.Runnable
            public void run() {
                StepDataTable stepDataByDate = StepServiceImpl.getInstance().getStepDataByDate(UserUtil.getUid(), DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
                if (stepDataByDate != null) {
                    FragmentStep.this.todayTotalCalorie = stepDataByDate.getCalorie();
                    FragmentStep.this.todayTotalStep = stepDataByDate.getWalkCounts();
                    FragmentStep.this.todayTotalDistanceM = stepDataByDate.getDistance();
                    FragmentStep.this.updateTableShow(DevStepUtil.getMinuteStepData(stepDataByDate.getDetailJson()));
                }
                FragmentStep.this.updateShowTotalData();
            }
        });
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        DevStepUtil.gteInstance().unRegisterCallback(this);
        UnitChangeHelper.getInstance().unRegisterListener(this);
        TargetChangeHelper.getInstance().unRegisterListener(this);
    }

    @Override // com.amap.api.location.AMapLocationListener
    public void onLocationChanged(AMapLocation aMapLocation) {
        LogUtil.e("=====aMapLocation==>" + aMapLocation.getAddress());
        if (aMapLocation.getErrorCode() == 0) {
            if (this.locationEntity != null) {
                requestNetWeather();
                return;
            }
            this.locationEntity = new LocationEntity();
            this.locationEntity.setCity(aMapLocation.getCity());
            this.locationEntity.setLat(aMapLocation.getLatitude());
            this.locationEntity.setLon(aMapLocation.getLongitude());
            this.locationEntity.setCityCode(aMapLocation.getCityCode());
            this.locationEntity.setProvince(aMapLocation.getProvince());
            this.locationEntity.setCountry(aMapLocation.getCountry());
            requestNetWeather();
        }
    }

    @Override // com.czw.smartkit.bleModule.step.DevStepCallback
    public void onStepHistoryDataList(List<DevPartStepBean> list) {
        updateTableShow(list);
    }

    @Override // com.czw.smartkit.bleModule.step.DevStepCallback
    public void onStepTotal(DevTotalStepBean devTotalStepBean) {
        this.todayTotalStep = devTotalStepBean.getStep();
        this.todayTotalDistanceM = devTotalStepBean.getDistance();
        this.todayTotalCalorie = devTotalStepBean.getCalorie();
        updateShowTotalData();
    }

    public void requestLocation(boolean z) {
        if (this.aMapLocationClient != null) {
            if (!z) {
                this.aMapLocationClient.stopLocation();
            } else {
                LogUtil.e("开始定位了");
                this.aMapLocationClient.startLocation();
            }
        }
    }
}
