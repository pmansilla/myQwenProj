package com.czw.smartkit.statistics.sleep;

import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.widget.smartking.MonthLineView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.sleep.SleepDataTable;
import com.czw.smartkit.databaseModule.sleep.SleepServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.util.SkUtils;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SleepMonthPage extends BaseFragment {
    private int[] month = new int[31];
    private MonthLineView monthLine1;
    private TextView totalTimeTv;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.monthLine1 = (MonthLineView) $View(R.id.monthLine1);
        this.totalTimeTv = (TextView) $View(R.id.totalTimeTv);
        this.monthLine1.initCfg(-9961277);
        this.monthLine1.updateShow(this.month);
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_sleep_month;
    }

    public void updateShow(final int i, final int i2, final int i3) {
        if (getActivity() == null || this.monthLine1 == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.sleep.SleepMonthPage.1
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr = new String[i3];
                HashMap hashMap = new HashMap();
                for (int i4 = 1; i4 <= i3; i4++) {
                    String format = String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i4));
                    int i5 = i4 - 1;
                    strArr[i5] = format;
                    SleepDataTable dataByDay = SleepServiceImpl.getInstance().getDataByDay(UserUtil.getUid(), format);
                    if (dataByDay != null) {
                        hashMap.put(strArr[i5], (dataByDay.getDeepTime() + dataByDay.getShallowTime()) + "");
                    } else {
                        hashMap.put(strArr[i5], AmapLoc.RESULT_TYPE_GPS);
                    }
                }
                int[] iArr = new int[i3];
                for (int i6 = 0; i6 < i3; i6++) {
                    iArr[i6] = Integer.valueOf((String) hashMap.get(strArr[i6])).intValue();
                }
                SleepMonthPage.this.totalTimeTv.setText(SleepMonthPage.this.getString(R.string.format_month_sleep, SkUtils.getHHMMSSTime(0)));
                SleepMonthPage.this.monthLine1.updateShow(iArr);
            }
        });
    }
}
