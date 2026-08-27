package com.czw.smartkit.bleModule.measure;

/* loaded from: classes.dex */
public enum MeasureType {
    HR(0),
    OX(2),
    BLOOD(1),
    HR_OX_BLOOD(3),
    TRAIN(4);

    private int type;

    MeasureType(int i) {
        this.type = i;
    }

    public static MeasureType getMeasureType(int i) {
        switch (i) {
            case 1:
                return BLOOD;
            case 2:
                return OX;
            case 3:
                return HR_OX_BLOOD;
            case 4:
                return TRAIN;
            default:
                return HR;
        }
    }

    public int getType() {
        return this.type;
    }
}
