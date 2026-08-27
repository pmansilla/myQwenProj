package com.autonavi.amap.mapcore.animation;

/* loaded from: classes.dex */
public class GLScaleAnimation extends GLAnimation {
    private float mFromX;
    private float mFromY;
    private float mPivotX = 0.0f;
    private float mPivotY = 0.0f;
    private float mToX;
    private float mToY;

    public GLScaleAnimation(float f, float f2, float f3, float f4) {
        this.mFromX = f;
        this.mToX = f2;
        this.mFromY = f3;
        this.mToY = f4;
    }

    @Override // com.autonavi.amap.mapcore.animation.GLAnimation
    protected void applyTransformation(float f, GLTransformation gLTransformation) {
        float f2 = (this.mFromX == 1.0f && this.mToX == 1.0f) ? 1.0f : this.mFromX + ((this.mToX - this.mFromX) * f);
        float f3 = (this.mFromY == 1.0f && this.mToY == 1.0f) ? 1.0f : this.mFromY + ((this.mToY - this.mFromY) * f);
        if (this.mPivotX == 0.0f && this.mPivotY == 0.0f) {
            gLTransformation.scaleX = f2;
            gLTransformation.scaleY = f3;
        } else {
            gLTransformation.scaleX = f2;
            gLTransformation.scaleY = f3;
        }
    }
}
