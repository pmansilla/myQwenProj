package com.czw.smartkit.statistics.sleep;

import android.text.TextUtils;
import android.widget.TextView;
import butterknife.BindView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.bleModule.sleep.DevSleepUtil;
import com.czw.smartkit.databaseModule.sleep.SleepDataTable;
import com.czw.smartkit.databaseModule.sleep.SleepServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.SleepTableView;
import com.czw.smartkit.views.SleepView;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.time.DateFormatUtils;

/* loaded from: classes.dex */
public class SleepDayPage extends BaseFragment {

    @BindView(R.id.sleep_history_day_awake_hour_textview)
    TextView sleepAwakeHourTextview;

    @BindView(R.id.sleep_history_day_awake_minute_textview)
    TextView sleepAwakeMinuteTextview;

    @BindView(R.id.sleep_history_day_deep_hour_textview)
    TextView sleepDeepHourTextview;

    @BindView(R.id.sleep_history_day_deep_minute_textview)
    TextView sleepDeepMinuteTextview;

    @BindView(R.id.sleep_history_day_hour_textview)
    TextView sleepHourTextView;

    @BindView(R.id.sleep_history_day_light_hour_textview)
    TextView sleepLightHourTextview;

    @BindView(R.id.sleep_history_day_light_minute_textview)
    TextView sleepLightMinuteTextview;

    @BindView(R.id.sleep_history_day_minute_textview)
    TextView sleepMinuteTextView;

    @BindView(R.id.sleep_history_day_sleepTableView)
    SleepTableView sleepTableView;

    @BindView(R.id.sleep_history_day_sleepView)
    SleepView sleepView;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        loadSleepData(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_sleep_day;
    }

    public void loadSleepData(String str) {
        int i;
        int i2;
        int i3;
        if (getActivity() == null || this.sleepView == null) {
            return;
        }
        SleepDataTable dataByDay = SleepServiceImpl.getInstance().getDataByDay(UserUtil.getUid(), str);
        LogUtil.e(str + "睡眠数据:" + new Gson().toJson(dataByDay));
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
}
