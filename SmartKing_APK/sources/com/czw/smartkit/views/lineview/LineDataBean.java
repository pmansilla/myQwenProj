package com.czw.smartkit.views.lineview;

/* loaded from: classes.dex */
public class LineDataBean {
    private String label;
    private int value;

    public LineDataBean(String str, int i) {
        this.label = str;
        this.value = i;
    }

    public String getLabel() {
        return this.label;
    }

    public int getValue() {
        return this.value;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public void setValue(int i) {
        this.value = i;
    }

    public String toString() {
        return "LineDataBean{label='" + this.label + "', value=" + this.value + '}';
    }
}
