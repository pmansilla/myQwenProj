package ycble.runchinaup.core;

import java.io.Serializable;

/* loaded from: classes2.dex */
public final class TimeOutHelper implements Serializable {
    private int milliSecond;
    public int reSendCount = 0;
    private String strData;

    public TimeOutHelper(String str, int i) {
        this.strData = str;
        this.milliSecond = i;
    }

    public int getMilliSecond() {
        return this.milliSecond;
    }

    public String getStrData() {
        return this.strData;
    }
}
