package com.autonavi.ae.gmap.glanimation;

import android.os.SystemClock;

/* loaded from: classes.dex */
public class AdglAnimation2V extends AbstractAdglAnimation {
    private double curValue1;
    private double curValue2;
    private AbstractAdglAnimationParam2V v2Param = null;

    public AdglAnimation2V(int i) {
        reset();
        this.duration = i;
        this.curValue1 = 0.0d;
        this.curValue2 = 0.0d;
    }

    @Override // com.autonavi.ae.gmap.glanimation.AbstractAdglAnimation
    public void doAnimation(Object obj) {
        if (this.isOver) {
            return;
        }
        this.offsetTime = SystemClock.uptimeMillis() - this.startTime;
        float f = ((float) this.offsetTime) / this.duration;
        if (f > 1.0f) {
            this.isOver = true;
            f = 1.0f;
        } else if (f < 0.0f) {
            this.isOver = true;
            return;
        }
        if (this.v2Param != null) {
            this.v2Param.setNormalizedTime(f);
            this.curValue1 = this.v2Param.getCurXValue();
            this.curValue2 = this.v2Param.getCurYValue();
        }
    }

    public double getCurValue(int i) {
        return i == 0 ? this.curValue1 : this.curValue2;
    }

    public double getEndValue(int i) {
        if (i == 0) {
            if (this.v2Param != null) {
                return this.v2Param.getToXValue();
            }
            return 0.0d;
        }
        if (this.v2Param != null) {
            return this.v2Param.getToYValue();
        }
        return 0.0d;
    }

    public double getStartValue(int i) {
        if (i == 0) {
            if (this.v2Param != null) {
                return this.v2Param.getFromXValue();
            }
            return 0.0d;
        }
        if (this.v2Param != null) {
            return this.v2Param.getFromYValue();
        }
        return 0.0d;
    }

    public void reset() {
        this.isOver = false;
        this.duration = 0;
        this.curValue1 = 0.0d;
        this.curValue2 = 0.0d;
        if (this.v2Param != null) {
            this.v2Param.reset();
        }
    }
}
