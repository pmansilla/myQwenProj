package com.czw.smartkit.bleModule.measure;

/* loaded from: classes.dex */
public interface DevMeasureCallback {
    void onMeasure(DevMeasureBean devMeasureBean);

    void onStartMeasure(MeasureType measureType);

    void onStopMeasure(MeasureType measureType);
}
