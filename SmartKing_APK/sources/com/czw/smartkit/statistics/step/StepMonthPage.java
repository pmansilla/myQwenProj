package com.czw.smartkit.statistics.step;

import android.text.TextUtils;
import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.widget.smartking.MonthLineView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.databaseModule.step.StepServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.utils.LogUtil;
import java.util.HashMap;

/* loaded from: classes.dex */
public class StepMonthPage extends BaseFragment {
    private MonthLineView monthLine1;
    private MonthLineView monthLine2;
    private TextView totalKcal;
    private TextView totalStep;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.monthLine1 = (MonthLineView) $View(R.id.monthLine1);
        this.monthLine2 = (MonthLineView) $View(R.id.monthLine2);
        this.totalStep = (TextView) $View(R.id.totalStep);
        this.totalKcal = (TextView) $View(R.id.totalKcal);
        this.monthLine1.initCfg(-6040320);
        this.monthLine2.initCfg(-1);
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_step_month;
    }

    public void updateShow(final int i, final int i2, final int i3) {
        if (getActivity() == null || this.totalKcal == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.step.StepMonthPage.1
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr = new String[i3];
                HashMap hashMap = new HashMap();
                HashMap hashMap2 = new HashMap();
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                while (i4 < i3) {
                    int i7 = i4 + 1;
                    String format = String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i7));
                    strArr[i4] = format;
                    StepDataTable stepDataByDate = StepServiceImpl.getInstance().getStepDataByDate(UserUtil.getUid(), format);
                    if (stepDataByDate != null) {
                        hashMap.put(format, stepDataByDate.getWalkCounts() + "");
                        hashMap2.put(format, stepDataByDate.getCalorie() + "");
                        i5 += stepDataByDate.getWalkCounts();
                        i6 += stepDataByDate.getCalorie();
                    } else {
                        hashMap.put(format, AmapLoc.RESULT_TYPE_GPS);
                        hashMap2.put(format, AmapLoc.RESULT_TYPE_GPS);
                    }
                    i4 = i7;
                }
                int[] iArr = new int[i3];
                int[] iArr2 = new int[i3];
                for (int i8 = 0; i8 < i3; i8++) {
                    iArr[i8] = Integer.valueOf(TextUtils.isEmpty((CharSequence) hashMap.get(strArr[i8])) ? AmapLoc.RESULT_TYPE_GPS : (String) hashMap.get(strArr[i8])).intValue();
                    iArr2[i8] = Float.valueOf(TextUtils.isEmpty((CharSequence) hashMap2.get(strArr[i8])) ? AmapLoc.RESULT_TYPE_GPS : (String) hashMap2.get(strArr[i8])).intValue();
                }
                LogUtil.e(iArr.toString());
                LogUtil.e(iArr2.toString());
                StepMonthPage.this.monthLine1.updateShow(iArr);
                StepMonthPage.this.monthLine2.updateShow(iArr2);
                StepMonthPage.this.totalStep.setText(StepMonthPage.this.getString(R.string.month_step_total, i5 + ""));
                StepMonthPage.this.totalKcal.setText(StepMonthPage.this.getString(R.string.month_kcal_total, i6 + ""));
            }
        });
    }
}
