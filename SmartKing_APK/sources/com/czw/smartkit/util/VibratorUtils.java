package com.czw.smartkit.util;

import android.content.Context;
import android.os.Vibrator;

/* loaded from: classes.dex */
public class VibratorUtils {
    private static VibratorUtils instance;
    private Context context;
    private Vibrator vibrator;

    private VibratorUtils(Context context) {
        this.context = context;
        Context context2 = this.context;
        Context context3 = this.context;
        this.vibrator = (Vibrator) context2.getSystemService("vibrator");
    }

    public static VibratorUtils getInstance(Context context) {
        if (instance == null) {
            instance = new VibratorUtils(context);
        }
        return instance;
    }

    public void cancelVibrator() {
        if (this.vibrator != null) {
            this.vibrator.cancel();
        }
    }

    public void vibrator(long[] jArr, int i) {
        if (this.vibrator == null || !this.vibrator.hasVibrator()) {
            return;
        }
        this.vibrator.vibrate(jArr, i);
    }
}
