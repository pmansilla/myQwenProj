package com.czw.smartkit.bleModule.data;

import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import java.io.Serializable;
import java.util.Arrays;

/* loaded from: classes.dex */
public class AlarmClock implements Serializable {
    public String name = MainApplication.getContext().getString(R.string.clock);
    public boolean enable = false;
    public int startHour = 0;
    public int startMinute = 0;
    public boolean[] cycle = new boolean[7];

    public int packCycle() {
        int i = 0;
        for (int i2 = 0; i2 < 7; i2++) {
            i |= (this.cycle[i2] ? 1 : 0) << i2;
        }
        return i;
    }

    public byte[] packData() {
        return new byte[]{this.enable ? (byte) 1 : (byte) 0, (byte) this.startHour, (byte) this.startMinute, (byte) packCycle()};
    }

    public String toString() {
        return "AlarmClock{name='" + this.name + "', enable=" + this.enable + ", startHour=" + this.startHour + ", startMinute=" + this.startMinute + ", cycle=" + Arrays.toString(this.cycle) + '}';
    }
}
