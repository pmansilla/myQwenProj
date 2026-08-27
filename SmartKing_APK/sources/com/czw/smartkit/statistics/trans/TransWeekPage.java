package com.czw.smartkit.statistics.trans;

import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.modes.widget.smartking.WeekRectView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.train.TrainDataTable;
import com.czw.smartkit.databaseModule.train.TrainServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.util.SkUtils;
import com.czw.utils.DateUtil;
import java.util.HashMap;

/* loaded from: classes.dex */
public class TransWeekPage extends BaseFragment {
    private TextView totalTimeTv;
    private WeekRectView weekRectView1;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.weekRectView1 = (WeekRectView) $View(R.id.weekRectView1);
        this.weekRectView1.initCfg(-1);
        this.weekRectView1.updateShow(new long[7], new int[7], new WeekRectView.FormatInvoke() { // from class: com.czw.smartkit.statistics.trans.TransWeekPage.1
            @Override // com.czw.modes.widget.smartking.WeekRectView.FormatInvoke
            public String format(int i) {
                return SkUtils.getHHMMSSTime(i);
            }
        });
        this.totalTimeTv = (TextView) $View(R.id.totalTimeTv);
        this.totalTimeTv.setText(getString(R.string.format_trans_sport_time, SkUtils.getHHMMSSTime(0)));
        updateShow(DateUtil.getWeekdays(0));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_trans_week;
    }

    public void updateShow(final long[] jArr) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.trans.TransWeekPage.2
            @Override // java.lang.Runnable
            public void run() {
                String[] strArr = new String[7];
                HashMap hashMap = new HashMap();
                int i = 0;
                for (int i2 = 0; i2 < 7; i2++) {
                    String format = DateUtil.yyyyMMdd.format(Long.valueOf(jArr[i2]));
                    strArr[i2] = format;
                    TrainDataTable trainDataTable = TrainServiceImpl.getInstance().totalDayData(UserUtil.getUid(), format);
                    if (trainDataTable != null) {
                        hashMap.put(strArr[i2], trainDataTable.getTimeConsuming());
                        i += Integer.valueOf(trainDataTable.getTimeConsuming()).intValue();
                    } else {
                        hashMap.put(strArr[i2], AmapLoc.RESULT_TYPE_GPS);
                    }
                }
                int[] iArr = new int[7];
                for (int i3 = 0; i3 < 7; i3++) {
                    iArr[i3] = Integer.valueOf((String) hashMap.get(strArr[i3])).intValue();
                }
                TransWeekPage.this.weekRectView1.updateShow(jArr, iArr, new WeekRectView.FormatInvoke() { // from class: com.czw.smartkit.statistics.trans.TransWeekPage.2.1
                    @Override // com.czw.modes.widget.smartking.WeekRectView.FormatInvoke
                    public String format(int i4) {
                        return SkUtils.getHHMMSSTime(i4);
                    }
                });
                TransWeekPage.this.totalTimeTv.setText(TransWeekPage.this.getString(R.string.format_trans_sport_time, SkUtils.getHHMMSSTime(i)));
            }
        });
    }
}
