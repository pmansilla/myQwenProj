package com.czw.smartkit.statistics.step;

import android.text.TextUtils;
import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.widget.smartking.WeekRectView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.databaseModule.step.StepServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.utils.DateUtil;
import com.czw.utils.LogUtil;
import java.util.HashMap;
import org.apache.commons.lang.time.DateFormatUtils;

/* loaded from: classes.dex */
public class StepWeekPage extends BaseFragment {
    private TextView totalKcal;
    private TextView totalStep;
    private WeekRectView weekRectView1;
    private WeekRectView weekRectView2;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.weekRectView1 = (WeekRectView) $View(R.id.weekRectView1);
        this.weekRectView2 = (WeekRectView) $View(R.id.weekRectView2);
        this.totalStep = (TextView) $View(R.id.totalStep);
        this.totalKcal = (TextView) $View(R.id.totalKcal);
        this.weekRectView1.initCfg(-6040320);
        this.weekRectView2.initCfg(-1);
        updateShow(DateUtil.getWeekdays(0));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_step_week;
    }

    public void updateShow(final long[] jArr) {
        if (getActivity() == null || this.totalKcal == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.step.StepWeekPage.1
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr = new String[7];
                HashMap hashMap = new HashMap();
                HashMap hashMap2 = new HashMap();
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < 7; i3++) {
                    String format = DateFormatUtils.format(jArr[i3], "yyyy-MM-dd");
                    strArr[i3] = format;
                    StepDataTable stepDataByDate = StepServiceImpl.getInstance().getStepDataByDate(UserUtil.getUid(), format);
                    if (stepDataByDate != null) {
                        hashMap.put(format, stepDataByDate.getWalkCounts() + "");
                        hashMap2.put(format, stepDataByDate.getCalorie() + "");
                        i += stepDataByDate.getWalkCounts();
                        i2 += stepDataByDate.getCalorie();
                    } else {
                        hashMap.put(format, AmapLoc.RESULT_TYPE_GPS);
                        hashMap2.put(format, AmapLoc.RESULT_TYPE_GPS);
                    }
                }
                int[] iArr = new int[7];
                int[] iArr2 = new int[7];
                for (int i4 = 0; i4 < 7; i4++) {
                    iArr[i4] = Integer.valueOf(TextUtils.isEmpty((CharSequence) hashMap.get(strArr[i4])) ? AmapLoc.RESULT_TYPE_GPS : (String) hashMap.get(strArr[i4])).intValue();
                    iArr2[i4] = Float.valueOf(TextUtils.isEmpty((CharSequence) hashMap2.get(strArr[i4])) ? AmapLoc.RESULT_TYPE_GPS : (String) hashMap2.get(strArr[i4])).intValue();
                }
                LogUtil.e(iArr.toString());
                LogUtil.e(iArr2.toString());
                StepWeekPage.this.weekRectView1.updateShow(jArr, iArr, null);
                StepWeekPage.this.weekRectView2.updateShow(jArr, iArr2, null);
                StepWeekPage.this.totalStep.setText(StepWeekPage.this.getString(R.string.week_step_total, i + ""));
                StepWeekPage.this.totalKcal.setText(StepWeekPage.this.getString(R.string.week_kcal_total, i2 + ""));
            }
        });
    }
}
