package com.czw.smartkit.statistics.trans;

import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.widget.smartking.MonthLineView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.databaseModule.train.TrainServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.util.SkUtils;
import java.util.HashMap;

/* loaded from: classes.dex */
public class TransMonthPage extends BaseFragment {
    private MonthLineView monthLine1;
    private TextView totalTimeTv;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.monthLine1 = (MonthLineView) $View(R.id.monthLine1);
        this.monthLine1.initCfg(-1);
        this.totalTimeTv = (TextView) $View(R.id.totalTimeTv);
        this.totalTimeTv.setText(getString(R.string.format_trans_sport_time, SkUtils.getHHMMSSTime(0)));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_trans_month;
    }

    public void updateShow(final int i, final int i2, final int i3) {
        if (getActivity() == null || this.monthLine1 == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.trans.TransMonthPage.1
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr = new String[i3];
                HashMap hashMap = new HashMap();
                int i4 = 0;
                int i5 = 0;
                while (i4 < i3) {
                    int i6 = i4 + 1;
                    String format = String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i6));
                    strArr[i4] = format;
                    TrainDataTable trainDataTable = TrainServiceImpl.getInstance().totalDayData(UserUtil.getUid(), format);
                    if (trainDataTable != null) {
                        hashMap.put(format, trainDataTable.getTimeConsuming());
                        i5 += Integer.valueOf(trainDataTable.getTimeConsuming()).intValue();
                    } else {
                        hashMap.put(format, AmapLoc.RESULT_TYPE_GPS);
                    }
                    i4 = i6;
                }
                int[] iArr = new int[i3];
                for (int i7 = 0; i7 < i3; i7++) {
                    iArr[i7] = Integer.valueOf((String) hashMap.get(strArr[i7])).intValue();
                }
                TransMonthPage.this.monthLine1.updateShow(iArr);
                TransMonthPage.this.totalTimeTv.setText(TransMonthPage.this.getString(R.string.format_trans_sport_time, SkUtils.getHHMMSSTime(i5)));
            }
        });
    }
}
