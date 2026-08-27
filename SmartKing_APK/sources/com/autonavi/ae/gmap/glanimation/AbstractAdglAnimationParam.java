package com.autonavi.ae.gmap.glanimation;

/* loaded from: classes.dex */
public abstract class AbstractAdglAnimationParam {
    protected boolean hasCheckedParam;
    protected float mult;
    protected boolean needToCaculate;
    protected float normalizedTime;
    protected int interpolationType = 0;
    protected float factor = 1.0f;
    protected boolean hasFromValue = false;
    protected boolean hasToValue = false;

    public AbstractAdglAnimationParam() {
        this.hasCheckedParam = false;
        this.needToCaculate = false;
        this.hasCheckedParam = false;
        this.needToCaculate = false;
    }

    static float bounce(float f) {
        return f * f * 8.0f;
    }

    public abstract void checkParam();

    public float getCurMult() {
        return this.mult;
    }

    public int getInterpolatorType() {
        return this.interpolationType;
    }

    public boolean needToCaculate() {
        if (!this.hasCheckedParam) {
            checkParam();
        }
        return this.hasCheckedParam && this.needToCaculate;
    }

    public void reset() {
        this.hasCheckedParam = false;
        this.needToCaculate = false;
        this.interpolationType = 0;
        this.factor = 1.0f;
        this.hasCheckedParam = false;
        this.needToCaculate = false;
        this.hasFromValue = false;
        this.hasToValue = false;
    }

    public void setInterpolatorType(int i, float f) {
        this.interpolationType = i;
        this.factor = f;
    }

    public void setNormalizedTime(float f) {
        this.normalizedTime = f;
        switch (this.interpolationType) {
            case 0:
                break;
            case 1:
                f = (float) Math.pow(f, this.factor * 2.0f);
                break;
            case 2:
                if (this.factor != 1.0f) {
                    f = (float) (1.0d - Math.pow(1.0f - f, this.factor * 2.0f));
                    break;
                } else {
                    float f2 = 1.0f - f;
                    f = 1.0f - (f2 * f2);
                    break;
                }
            case 3:
                double d = f + 1.0f;
                Double.isNaN(d);
                f = (float) ((Math.cos(d * 3.141592653589793d) / 2.0d) + 0.5d);
                break;
            case 4:
                float f3 = f * 1.1226f;
                if (f3 >= 0.3535f) {
                    if (f3 >= 0.7408f) {
                        if (f3 >= 0.9644f) {
                            f = bounce(f3 - 1.0435f) + 0.95f;
                            break;
                        } else {
                            f = bounce(f3 - 0.8526f) + 0.9f;
                            break;
                        }
                    } else {
                        f = bounce(f3 - 0.54719f) + 0.7f;
                        break;
                    }
                } else {
                    f = bounce(f3);
                    break;
                }
            case 5:
                float f4 = f - 1.0f;
                f = (f4 * f4 * ((3.0f * f4) + 2.0f)) + 1.0f;
                break;
            case 6:
                if (f >= 0.0f) {
                    if (f >= 0.25f) {
                        if (f >= 0.5f) {
                            if (f >= 0.75f) {
                                if (f <= 1.0f) {
                                    f = 4.0f - (f * 4.0f);
                                    break;
                                }
                            } else {
                                f = (f * 4.0f) - 2.0f;
                                break;
                            }
                        } else {
                            f = 2.0f - (f * 4.0f);
                            break;
                        }
                    } else {
                        f *= 4.0f;
                        break;
                    }
                }
            default:
                f = 0.0f;
                break;
        }
        this.mult = f;
    }
}
