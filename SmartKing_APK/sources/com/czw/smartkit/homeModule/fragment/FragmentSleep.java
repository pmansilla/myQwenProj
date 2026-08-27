package com.czw.smartkit.homeModule.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.bleModule.sleep.DevSleepCallback;
import com.czw.smartkit.bleModule.sleep.DevSleepUtil;
import com.czw.smartkit.databaseModule.sleep.SleepDataTable;
import com.czw.smartkit.databaseModule.sleep.SleepServiceImpl;
import com.czw.smartkit.homeModule.syncData.SyncDataType;
import com.czw.smartkit.homeModule.syncData.SyncDataUtil;
import com.czw.smartkit.statistics.SleepHistoryActivity;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.SleepTableView;
import com.czw.smartkit.views.SleepView;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.time.DateFormatUtils;

/* loaded from: classes.dex */
public class FragmentSleep extends BaseFragment implements DevSleepCallback, SyncDataUtil.SyncDataCallback {
    private static String yyyyMMddFromatString = "yyyy-MM-dd";

    @BindView(R.id.sleep_awake_hour_textview)
    TextView sleepAwakeHourTextview;

    @BindView(R.id.sleep_awake_minute_textview)
    TextView sleepAwakeMinuteTextview;

    @BindView(R.id.sleep_deep_hour_textview)
    TextView sleepDeepHourTextview;

    @BindView(R.id.sleep_deep_minute_textview)
    TextView sleepDeepMinuteTextview;

    @BindView(R.id.sleep_hour_textview)
    TextView sleepHourTextView;

    @BindView(R.id.sleep_light_hour_textview)
    TextView sleepLightHourTextview;

    @BindView(R.id.sleep_light_minute_textview)
    TextView sleepLightMinuteTextview;

    @BindView(R.id.sleep_minute_textview)
    TextView sleepMinuteTextView;

    @BindView(R.id.sleepTableView)
    SleepTableView sleepTableView;

    @BindView(R.id.sleepView)
    SleepView sleepView;

    /* JADX INFO: Access modifiers changed from: private */
    public void loadTodaySleepData() {
        int i;
        int i2;
        int i3;
        String format = DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFromatString);
        SleepDataTable dataByDay = SleepServiceImpl.getInstance().getDataByDay(UserUtil.getUid(), format);
        LogUtil.e(format + "睡眠数据:" + new Gson().toJson(dataByDay));
        if (dataByDay != null) {
            i = dataByDay.getDeepTime();
            i2 = dataByDay.getShallowTime();
            i3 = dataByDay.getSoberTime();
            if (TextUtils.isEmpty(dataByDay.getRecord())) {
                this.sleepTableView.updateData(null);
            } else {
                this.sleepTableView.updateData(DevSleepUtil.getMinuteSleepDataList(dataByDay.getRecord()));
            }
        } else {
            this.sleepTableView.updateData(null);
            i = 0;
            i2 = 0;
            i3 = 0;
        }
        int i4 = i + i2;
        this.sleepView.updateShow(i4 / 480.0f);
        this.sleepHourTextView.setText(String.format("%d", Integer.valueOf(i4 / 60)));
        this.sleepMinuteTextView.setText(String.format("%d", Integer.valueOf(i4 % 60)));
        this.sleepDeepHourTextview.setText(String.format("%d", Integer.valueOf(i / 60)));
        this.sleepDeepMinuteTextview.setText(String.format("%d", Integer.valueOf(i % 60)));
        this.sleepLightHourTextview.setText(String.format("%d", Integer.valueOf(i2 / 60)));
        this.sleepLightMinuteTextview.setText(String.format("%d", Integer.valueOf(i2 % 60)));
        this.sleepAwakeHourTextview.setText(String.format("%d", Integer.valueOf(i3 / 60)));
        this.sleepAwakeMinuteTextview.setText(String.format("%d", Integer.valueOf(i3 % 60)));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        $View(R.id.sleepView).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.homeModule.fragment.FragmentSleep.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FragmentSleep.this.jumpTo(SleepHistoryActivity.class);
            }
        });
        DevSleepUtil.getInstance().setDevSleepEntityCallback(this);
        SyncDataUtil.getInstance().registerCallback(this);
        loadTodaySleepData();
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.fragment_sleep;
    }

    @Override // com.czw.smartkit.homeModule.syncData.SyncDataUtil.SyncDataCallback
    public void onDataSyncFinish(SyncDataType syncDataType) {
        if (getActivity() == null || this.sleepView == null || syncDataType != SyncDataType.SLEEP) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentSleep.3
            @Override // java.lang.Runnable
            public void run() {
                FragmentSleep.this.loadTodaySleepData();
            }
        });
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        DevSleepUtil.getInstance().setDevSleepEntityCallback(null);
    }

    @Override // com.czw.smartkit.bleModule.sleep.DevSleepCallback
    public void onDevTodaySleepLoad() {
        if (getActivity() == null || this.sleepView == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentSleep.2
            @Override // java.lang.Runnable
            public void run() {
                FragmentSleep.this.loadTodaySleepData();
            }
        });
    }
}
