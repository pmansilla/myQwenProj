package com.czw.smartkit.statistics.sleep;

import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.widget.smartking.WeekRectView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.sleep.SleepDataTable;
import com.czw.smartkit.databaseModule.sleep.SleepServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.util.SkUtils;
import com.czw.utils.DateUtil;
import java.util.HashMap;
import org.apache.commons.lang.time.DateFormatUtils;

/* loaded from: classes.dex */
public class SleepWeekPage extends BaseFragment {
    private TextView totalTimeTv;
    private WeekRectView weekRectView;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.weekRectView = (WeekRectView) $View(R.id.weekRectView1);
        this.weekRectView.initCfg(-9961277);
        this.totalTimeTv = (TextView) $View(R.id.totalTimeTv);
        updateShow(DateUtil.getWeekdays(0));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_sleep_week;
    }

    public void updateShow(final long[] jArr) {
        if (getActivity() == null || this.weekRectView == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.sleep.SleepWeekPage.1
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr = new String[7];
                HashMap hashMap = new HashMap();
                for (int i = 0; i < 7; i++) {
                    String format = DateFormatUtils.format(jArr[i], "yyyy-MM-dd");
                    strArr[i] = format;
                    SleepDataTable dataByDay = SleepServiceImpl.getInstance().getDataByDay(UserUtil.getUid(), format);
                    if (dataByDay != null) {
                        hashMap.put(strArr[i], (dataByDay.getDeepTime() + dataByDay.getShallowTime()) + "");
                    } else {
                        hashMap.put(strArr[i], AmapLoc.RESULT_TYPE_GPS);
                    }
                }
                int[] iArr = new int[7];
                for (int i2 = 0; i2 < 7; i2++) {
                    iArr[i2] = Integer.valueOf((String) hashMap.get(strArr[i2])).intValue();
                }
                SleepWeekPage.this.totalTimeTv.setText(SleepWeekPage.this.getString(R.string.format_week_sleep, SkUtils.getHHMMSSTime(0)));
                SleepWeekPage.this.weekRectView.updateShow(jArr, iArr, new WeekRectView.FormatInvoke() { // from class: com.czw.smartkit.statistics.sleep.SleepWeekPage.1.1
                    @Override // com.czw.modes.widget.smartking.WeekRectView.FormatInvoke
                    public String format(int i3) {
                        return String.format("%dh %dm", Integer.valueOf(i3 / 60), Integer.valueOf(i3 % 60));
                    }
                });
            }
        });
    }
}
