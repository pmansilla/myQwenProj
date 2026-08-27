package com.mob.tools.gui;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'DEFAULT' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:451)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByField(EnumVisitor.java:372)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByWrappedInsn(EnumVisitor.java:337)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:322)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:262)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes2.dex */
public final class SmoothScroller {
    private static final /* synthetic */ SmoothScroller[] $VALUES;
    public static final SmoothScroller DEFAULT;
    public static final SmoothScroller LINEAR_ACC;
    public static final SmoothScroller LINEAR_DEC;
    public static final SmoothScroller OVER_SCROLL;
    private Interpolator interpolator;

    /* loaded from: classes2.dex */
    private static class DefaultInterpolator implements Interpolator {
        private float[] defaultInt;

        private DefaultInterpolator() {
            this.defaultInt = new float[]{0.0f, 3.0E-4f, 0.0012f, 0.0026f, 0.0047f, 0.0073f, 0.0104f, 0.014f, 0.0182f, 0.0228f, 0.028f, 0.0336f, 0.0397f, 0.0463f, 0.0533f, 0.0608f, 0.0686f, 0.0769f, 0.0855f, 0.0946f, 0.104f, 0.1138f, 0.1239f, 0.1344f, 0.1452f, 0.1563f, 0.1676f, 0.1793f, 0.1913f, 0.2035f, 0.216f, 0.2287f, 0.2417f, 0.2548f, 0.2682f, 0.2817f, 0.2955f, 0.3094f, 0.3235f, 0.3377f, 0.352f, 0.3665f, 0.381f, 0.3957f, 0.4104f, 0.4253f, 0.4401f, 0.4551f, 0.47f, 0.485f, 0.5f, 0.515f, 0.53f, 0.5449f, 0.5599f, 0.5748f, 0.5896f, 0.6043f, 0.619f, 0.6335f, 0.648f, 0.6623f, 0.6765f, 0.6906f, 0.7045f, 0.7183f, 0.7318f, 0.7452f, 0.7583f, 0.7713f, 0.784f, 0.7965f, 0.8087f, 0.8207f, 0.8324f, 0.8438f, 0.8548f, 0.8656f, 0.8761f, 0.8862f, 0.896f, 0.9054f, 0.9145f, 0.9231f, 0.9314f, 0.9392f, 0.9467f, 0.9537f, 0.9603f, 0.9664f, 0.972f, 0.9772f, 0.9818f, 0.986f, 0.9896f, 0.9927f, 0.9953f, 0.9974f, 0.9988f, 0.9997f, 1.0f};
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return this.defaultInt[(int) (f * 100.0f)];
        }
    }

    /* loaded from: classes2.dex */
    private static class LinearACCInterpolator implements Interpolator {
        private LinearACCInterpolator() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return f * f;
        }
    }

    /* loaded from: classes2.dex */
    private static class LinearDECInterpolator implements Interpolator {
        private LinearDECInterpolator() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return (2.0f - f) * f;
        }
    }

    /* loaded from: classes2.dex */
    private static class OverScrollInterpolator implements Interpolator {
        private float[] defaultInt;

        private OverScrollInterpolator() {
            this.defaultInt = new float[]{0.0f, 3.0E-4f, 0.0012f, 0.0026f, 0.0047f, 0.0073f, 0.0104f, 0.014f, 0.0182f, 0.0228f, 0.028f, 0.0336f, 0.0397f, 0.0463f, 0.0533f, 0.0608f, 0.0686f, 0.0769f, 0.0855f, 0.0946f, 0.104f, 0.1138f, 0.1239f, 0.1344f, 0.1452f, 0.1563f, 0.1676f, 0.1793f, 0.1913f, 0.2035f, 0.216f, 0.2287f, 0.2417f, 0.2548f, 0.2682f, 0.2817f, 0.2955f, 0.3094f, 0.3235f, 0.3377f, 0.352f, 0.3665f, 0.381f, 0.3957f, 0.4104f, 0.4253f, 0.4401f, 0.4551f, 0.47f, 0.485f, 0.5f, 0.515f, 0.53f, 0.5449f, 0.5599f, 0.5748f, 0.5896f, 0.6043f, 0.619f, 0.6335f, 0.648f, 0.6623f, 0.6765f, 0.6906f, 0.7045f, 0.7183f, 0.7318f, 0.7452f, 0.7583f, 0.7713f, 0.784f, 0.7965f, 0.8087f, 0.8207f, 0.8324f, 0.8438f, 0.8548f, 0.8656f, 0.8761f, 0.8862f, 0.896f, 0.9054f, 0.9145f, 0.9231f, 0.9314f, 0.9392f, 0.9467f, 0.9537f, 0.9603f, 0.9664f, 0.972f, 0.9772f, 0.9818f, 0.986f, 0.9896f, 0.9927f, 0.9953f, 0.9974f, 0.9988f, 0.9997f, 1.0f};
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return this.defaultInt[(int) (f * 100.0f)];
        }
    }

    static {
        DEFAULT = new SmoothScroller("DEFAULT", 0, new DefaultInterpolator());
        OVER_SCROLL = new SmoothScroller("OVER_SCROLL", 1, new OverScrollInterpolator());
        LINEAR_ACC = new SmoothScroller("LINEAR_ACC", 2, new LinearACCInterpolator());
        LINEAR_DEC = new SmoothScroller("LINEAR_DEC", 3, new LinearDECInterpolator());
        $VALUES = new SmoothScroller[]{DEFAULT, OVER_SCROLL, LINEAR_ACC, LINEAR_DEC};
    }

    private SmoothScroller(String str, int i, Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public static SmoothScroller valueOf(String str) {
        return (SmoothScroller) Enum.valueOf(SmoothScroller.class, str);
    }

    public static SmoothScroller[] values() {
        return (SmoothScroller[]) $VALUES.clone();
    }

    public Scroller getScroller(Context context) {
        return new Scroller(context, this.interpolator);
    }
}
