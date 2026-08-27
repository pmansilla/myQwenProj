package basecamera.module.lib.util;

import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

/* loaded from: classes.dex */
public class AngleUtil {
    public static int getSensorAngle(float f, float f2) {
        return Math.abs(f) > Math.abs(f2) ? f > 4.0f ? SubsamplingScaleImageView.ORIENTATION_270 : f < -4.0f ? 90 : 0 : (f2 <= 7.0f && f2 < -7.0f) ? 180 : 0;
    }
}
