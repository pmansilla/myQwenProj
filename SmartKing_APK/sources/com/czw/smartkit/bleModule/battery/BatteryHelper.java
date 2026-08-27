package com.czw.smartkit.bleModule.battery;

import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class BatteryHelper {
    private static BatteryHelper batteryHelper = new BatteryHelper();
    private HashSet<BatteryCallback> callbackHashSet = new HashSet<>();
    private int battery = -1;

    /* loaded from: classes.dex */
    public interface BatteryCallback {
        void onGetBattery(int i);
    }

    private BatteryHelper() {
    }

    public static BatteryHelper getInstance() {
        return batteryHelper;
    }

    public int getBattery() {
        return this.battery;
    }

    public void notifyBattery(int i) {
        this.battery = i;
        Iterator<BatteryCallback> it = this.callbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onGetBattery(i);
        }
    }

    public void registerCallback(BatteryCallback batteryCallback) {
        if (this.callbackHashSet.contains(batteryCallback)) {
            return;
        }
        this.callbackHashSet.add(batteryCallback);
    }

    public void unRegisterCallback(BatteryCallback batteryCallback) {
        if (this.callbackHashSet.contains(batteryCallback)) {
            this.callbackHashSet.remove(batteryCallback);
        }
    }
}
