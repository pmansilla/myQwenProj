package com.czw.smartkit.observerModule;

import android.widget.TextView;
import com.czw.smartkit.bleModule.data.UnitCfg;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class UnitChangeHelper {
    private static final UnitChangeHelper ourInstance = new UnitChangeHelper();
    private HashSet<UnitListener> listenerHashSet = new HashSet<>();

    /* loaded from: classes.dex */
    public interface UnitListener {
        void currentUnit(UnitCfg unitCfg);
    }

    private UnitChangeHelper() {
    }

    public static float cm2InValue(float f) {
        return f * 0.3937008f;
    }

    public static UnitChangeHelper getInstance() {
        return ourInstance;
    }

    public static String km2Mile(double d) {
        return String.format("%.2f", Double.valueOf(d * 0.6213712096214294d));
    }

    public static double km2MileValue(double d) {
        return d * 0.6213712d;
    }

    public static int temperatureC2F(int i) {
        return Float.valueOf((i * 1.8f) + 32.0f).intValue();
    }

    public static void withDistanceUnitShowKMOrMi(String str, UnitCfg unitCfg, TextView textView, TextView textView2) {
    }

    public static void withDistanceUnitShowMOrFoot(String str, UnitCfg unitCfg, TextView textView, TextView textView2) {
    }

    public void notifyUnitChange(UnitCfg unitCfg) {
        Iterator<UnitListener> it = this.listenerHashSet.iterator();
        while (it.hasNext()) {
            it.next().currentUnit(unitCfg);
        }
    }

    public void registerListener(UnitListener unitListener) {
        if (this.listenerHashSet.contains(unitListener)) {
            return;
        }
        this.listenerHashSet.add(unitListener);
    }

    public void unRegisterListener(UnitListener unitListener) {
        if (this.listenerHashSet.contains(unitListener)) {
            this.listenerHashSet.remove(unitListener);
        }
    }
}
