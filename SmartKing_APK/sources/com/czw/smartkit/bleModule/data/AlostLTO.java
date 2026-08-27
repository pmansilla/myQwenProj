package com.czw.smartkit.bleModule.data;

import java.io.Serializable;

/* loaded from: classes.dex */
public class AlostLTO implements Serializable {
    private boolean enable = false;
    private int timelen = 5;

    public int getTimelen() {
        return this.timelen;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean z) {
        this.enable = z;
    }

    public void setTimelen(int i) {
        this.timelen = i;
    }
}
