package com.czw.smartkit.bleModule.step;

import java.util.List;

/* loaded from: classes.dex */
public interface DevStepCallback {
    void onStepHistoryDataList(List<DevPartStepBean> list);

    void onStepTotal(DevTotalStepBean devTotalStepBean);
}
